package be.bstorm.chesstt_springapi.services.impl;

import be.bstorm.chesstt_springapi.exceptions.tournament.*;
import be.bstorm.chesstt_springapi.models.business.TournamentDetailsResult;
import be.bstorm.chesstt_springapi.models.business.TournamentResult;
import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.MatchResult;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;
import be.bstorm.chesstt_springapi.models.enums.UserGender;
import be.bstorm.chesstt_springapi.repositories.MatchRepository;
import be.bstorm.chesstt_springapi.repositories.TournamentRepository;
import be.bstorm.chesstt_springapi.repositories.UserRepository;
import be.bstorm.chesstt_springapi.services.TournamentService;
import be.bstorm.chesstt_springapi.services.specifications.TournamentSpecifications;
import be.bstorm.chesstt_springapi.services.specifications.UserSpecifications;
import be.bstorm.chesstt_springapi.utils.MailerUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MailerUtils mailerUtils;

    @Override
    public Tournament create(Tournament t) {

        LocalDateTime minEndOfRegistrationDate = LocalDateTime.now().plusDays(t.getMinPlayers());
        if (t.getEndOfRegistrationDate().isBefore(minEndOfRegistrationDate)) {
            throw new TournamentValidationException("The end of registration must be greater than " + minEndOfRegistrationDate);
        }
        t.setId(UUID.randomUUID());
        t.setStatus(TournamentStatus.WAITING_FOR_PLAYERS);

        Tournament createdTournament = tournamentRepository.save(t);

        List<String> playersEmails = findPlayersWhoCanRegister(createdTournament)
                .stream()
                .map(User::getEmail)
                .toList();

        Context context = new Context();
        context.setVariable("tournament", createdTournament);
        mailerUtils.send(
                "Nouveau tournois ouvert",
                "invitation",
                context,
                playersEmails.toArray(new String[0])
        );
        return createdTournament;
    }

    @Override
    public void delete(UUID id) {

        Tournament t = tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament " + id + " not found"));
        if (t.getStatus().equals(TournamentStatus.IN_PROGRESS)) {
            throw new TournamentInProgressException();
        }
        List<String> playersEmails = t.getPlayers()
                .stream()
                .map(User::getEmail)
                .toList();
        if (t.getStatus().equals(TournamentStatus.CLOSED)) {
            Set<Match> matches = matchRepository.getMatchesByTournamentId(id);
            matchRepository.deleteAll(matches);
        }

        tournamentRepository.deleteById(id);

        Context context = new Context();
        context.setVariable("tournament", t);
        mailerUtils.send(
                "Tournois supprimé",
                "deleted",
                context,
                playersEmails.toArray(new String[0])
        );
    }

    @Override
    public List<TournamentResult> findTournamentsWithCriteria(String name,
                                                              Set<TournamentCategory> categories,
                                                              TournamentStatus status,
                                                              Boolean isWomanOnly,
                                                              User user,
                                                              int page) {

        Specification<Tournament> spec = getTournamentSpecification(
                name,
                categories,
                status,
                isWomanOnly);

        List<Tournament> tournaments = tournamentRepository.findAll(
                        spec,
                        PageRequest.of(page, 10, Sort.by("createdAt").descending()))
                .stream()
                .toList();

        return tournaments.stream()
                .map(t -> {
                    TournamentResult result = new TournamentResult(t);
                    if (user != null) {
                        result.setRegistered(t.getPlayers().contains(user));
                        result.setCanRegister(canRegister(user,t));
                    }
                    return result;
                }).toList();
    }

    @Override
    public long count(
            String name,
            Set<TournamentCategory> categories,
            TournamentStatus status,
            Boolean isWomanOnly) {

        Specification<Tournament> specification = getTournamentSpecification(
                name,
                categories,
                status,
                isWomanOnly
        );
        return tournamentRepository.count(specification);
    }

    @Override
    public TournamentDetailsResult findOne(UUID tournamentId,User user) {
        Tournament t = tournamentRepository.findTournamentWithPlayers(tournamentId).orElseThrow(TournamentNotFoundException::new);
        TournamentDetailsResult result = new TournamentDetailsResult(t);
        result.setMatches(matchRepository.getMatchesByTournamentIdAndRound(tournamentId,t.getCurrentRound()));
        result.setCanStart(canStart(t));
        result.setCanValidateRound(canValidateRound(t,result.getMatches()));
        if(user != null){
            result.setRegistered(t.getPlayers().contains(user));
            result.setCanRegister(canRegister(user,t));
        }
        return result;
    }

    @Override
    public void register(User user, UUID tournamentId) {

        Tournament t = tournamentRepository.findTournamentWithPlayers(tournamentId).orElseThrow(TournamentNotFoundException::new);
        checkCanRegister(user,t);
        t.addPlayer(user);
        tournamentRepository.save(t);
    }

    @Override
    public void unregister(User user, UUID tournamentId) {
        Tournament t = tournamentRepository.findTournamentWithPlayers(tournamentId).orElseThrow(TournamentNotFoundException::new);
        if(!t.getStatus().equals(TournamentStatus.WAITING_FOR_PLAYERS)){
            throw new TournamentException("Cannot unregister when a tournament has already started");
        }
        if(!t.getPlayers().contains(user)){
            throw new TournamentException("This player is not in the tournament");
        }
        t.removePlayer(user);
        tournamentRepository.save(t);
    }

    @Transactional
    @Override
    public void start(UUID tournamentId) {
        Tournament t = tournamentRepository.findTournamentWithPlayers(tournamentId).orElseThrow(TournamentNotFoundException::new);
        checkCanStart(t);
        generateMatches(t);
        t.setStatus(TournamentStatus.IN_PROGRESS);
        t.setCurrentRound(1);
        tournamentRepository.save(t);
    }

    @Override
    public void nextRound(UUID tournamentId) {
        Tournament t = tournamentRepository.findTournamentWithPlayers(tournamentId).orElseThrow(TournamentNotFoundException::new);
        Set<Match> matches = matchRepository.getMatchesByTournamentIdAndRound(tournamentId,t.getCurrentRound());
        checkCanValidateRound(t,matches);
        if(t.getCurrentRound() == (t.getPlayers().size() % 2 == 0 ? (t.getPlayers().size() - 1) * 2  : t.getPlayers().size() * 2)) {
            t.setStatus(TournamentStatus.CLOSED);
        }else{
            t.setCurrentRound(t.getCurrentRound() + 1);
        }
        tournamentRepository.save(t);
    }

    public void generateMatches(Tournament t){
        List<User> players = new ArrayList<>(t.getPlayers());
        Collections.shuffle(players);
        if(players.size() % 2 != 0){
            players.add(null);
        }
        for (int round = 1; round < players.size() * 2 - 1; round++){
            for (int i = 0; i < players.size() / 2; i++){
                if(players.get(i) != null && players.get(players.size() -i - 1) != null ){
                    Match match = new Match();
                    match.setTournament(t);
                    match.setWhitePlayer(round % 2 == 1 ? players.get(i) : players.get(players.size() - i - 1));
                    match.setBlackPlayer(round % 2 == 1 ? players.get(players.size() - i - 1) : players.get(i));
                    match.setRound(round);
                    match.setResult(MatchResult.NOT_PLAYED);
                    matchRepository.save(match);
                }
            }
            players.add(1,players.getLast());
            players.removeLast();
        }
    }

    private boolean canValidateRound(Tournament t,Set<Match> matches) {
        try{
            checkCanValidateRound(t, matches);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void checkCanValidateRound(Tournament t, Set<Match> matches) {

        if(!t.getStatus().equals(TournamentStatus.IN_PROGRESS)){
            throw new TournamentException("Cannot validate a round when the tournament is not in progress");
        }
        if(matches.stream().anyMatch(match -> match.getResult().equals(MatchResult.NOT_PLAYED))){
            throw new TournamentException("Cannot validate a round when the all the matches are not played");
        }
    }

    private boolean canStart(Tournament t) {

        try{
            checkCanStart(t);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void checkCanStart(Tournament t) {

        if(!t.getStatus().equals(TournamentStatus.WAITING_FOR_PLAYERS)){
            throw new TournamentException("Cannot start a tournament that has already started");
        }
        if(t.getPlayers().size() < t.getMinPlayers()){
            throw new TournamentException("Not enough players");
        }
        //TODO décommenter
//        if(t.getEndOfRegistrationDate().isAfter(LocalDateTime.now())){
//            throw new TournamentException("Cannot start a tournament before the end of registration date");
//        }
    }


    private static Specification<Tournament> getTournamentSpecification(String name,
                                                                        Set<TournamentCategory> categories,
                                                                        TournamentStatus status,
                                                                        Boolean isWomanOnly) {
        Specification<Tournament> spec = Specification.where(null);
        if (name != null) {
            spec = spec.and(TournamentSpecifications.getByName(name));
        }
        if (categories != null) {
            spec = spec.and(TournamentSpecifications.getByCategory(categories));
        }
        if (status != null) {
            spec = spec.and(TournamentSpecifications.getByStatus(status));
        }
        if (isWomanOnly != null && isWomanOnly) {
            spec = spec.and(TournamentSpecifications.getByIsWomanOnly());
        }
        return spec;
    }


    //Trouver les joueurs qui peuvent s'inscrire a un tournois fraichement créé avec les specifications
    public List<User> findPlayersWhoCanRegister(Tournament t) {
        Specification<User> specs = Specification.where(null);
        if (t.getMinElo() != null && t.getMaxElo() != null) {
            specs = specs.and(UserSpecifications.getByElo(t.getMinElo(), t.getMaxElo()));
        }
        if (t.isWomenOnly()) {
            specs = specs.and(UserSpecifications.getByGender(UserGender.FEMALE));
        }
        Specification<User> ageSpecs = Specification.where(null);
        if (t.getCategories().contains(TournamentCategory.JUNIOR)) {
            ageSpecs = ageSpecs.or(UserSpecifications.getByAge(t.getEndOfRegistrationDate(), 0, 18));
        }
        if (t.getCategories().contains(TournamentCategory.SENIOR)) {
            ageSpecs = ageSpecs.or(UserSpecifications.getByAge(t.getEndOfRegistrationDate(), 18, 60));
        }
        if (t.getCategories().contains(TournamentCategory.VETERAN)) {
            ageSpecs = ageSpecs.or(UserSpecifications.getByAge(t.getEndOfRegistrationDate(), 60, 200));
        }
        specs = specs.and(ageSpecs);
        return userRepository.findAll(specs);
    }

    public boolean canRegister(User player, Tournament tournament){
        try{
            checkCanRegister(player, tournament);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void checkCanRegister(User player, Tournament tournament) {
        if(!tournament.getStatus().equals(TournamentStatus.WAITING_FOR_PLAYERS)){
            throw new TournamentRegistrationException("Cannot register when a tournament has already started");
        }
        if (tournament.getPlayers().contains(player))
        {
            throw new TournamentRegistrationException("This player is already registered");
        }
        if (!tournament.isWomenOnly() && player.getGender().equals(UserGender.FEMALE))
        {
            throw new TournamentRegistrationException("Must be a women to participate");
        }
        if (tournament.getPlayers().size() >= tournament.getMaxPlayers())
        {
            throw new TournamentRegistrationException("This tournament is already full");
        }
        if (tournament.getEndOfRegistrationDate().isBefore(LocalDateTime.now()))
        {
            throw new TournamentRegistrationException("The registration date has expired");
        }
        checkCategories(tournament, player);
        checkElo(tournament, player);
    }

    private void checkElo(Tournament tournament, User player) {
        if(tournament.getMinElo() != null && player.getElo() < tournament.getMinElo()){
            throw new TournamentRegistrationException("This player doesn't have the required elo");
        }
        if(tournament.getMaxElo() != null && player.getElo() > tournament.getMaxElo()){
            throw new TournamentRegistrationException("This player doesn't have the required elo");
        }
    }

    private void checkCategories(Tournament tournament, User player){
        boolean flag = false;
        int age = calculateAge(tournament, player);
        if(tournament.getCategories().contains(TournamentCategory.JUNIOR) && age <= 18)
        {
            flag = true;
        }
        if (tournament.getCategories().contains(TournamentCategory.SENIOR) && age > 18 && age <= 60)
        {
            flag = true;
        }
        if (tournament.getCategories().contains(TournamentCategory.VETERAN) && age > 60)
        {
            flag = true;
        }
        if (!flag)
        {
            throw new TournamentRegistrationException("This player doesn't have the required age");
        }
    }

    private int calculateAge(Tournament tournament, User player) {
        int age = tournament.getEndOfRegistrationDate().getYear() - player.getBirthdate().getYear();

        if(player.getBirthdate().isAfter(tournament.getEndOfRegistrationDate().toLocalDate().minusYears(age))){
            age--;
        }
        return age;
    }
}
