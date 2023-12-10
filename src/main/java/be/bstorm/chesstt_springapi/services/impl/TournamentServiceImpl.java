package be.bstorm.chesstt_springapi.services.impl;

import be.bstorm.chesstt_springapi.exceptions.tournament.TournamentInProgressException;
import be.bstorm.chesstt_springapi.exceptions.tournament.TournamentNotFoundException;
import be.bstorm.chesstt_springapi.exceptions.tournament.TournamentValidationException;
import be.bstorm.chesstt_springapi.models.business.TournamentResult;
import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;
import be.bstorm.chesstt_springapi.models.enums.UserGender;
import be.bstorm.chesstt_springapi.repositories.MatchRepository;
import be.bstorm.chesstt_springapi.repositories.TournamentRepository;
import be.bstorm.chesstt_springapi.repositories.UserRepository;
import be.bstorm.chesstt_springapi.services.TournamentService;
import be.bstorm.chesstt_springapi.services.specifications.UserSpecifications;
import be.bstorm.chesstt_springapi.utils.MailerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
        if(t.getEndOfRegistrationDate().isBefore(minEndOfRegistrationDate)){
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
        context.setVariable("tournament",createdTournament);
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
        if(t.getStatus().equals(TournamentStatus.IN_PROGRESS)){
            throw new TournamentInProgressException();
        }
        List<String> playersEmails = t.getPlayers()
                .stream()
                .map(User::getEmail)
                .toList();
        if(t.getStatus().equals(TournamentStatus.CLOSED)){
            Set<Match> matches = matchRepository.getMatchesByTournamentId(id);
            matchRepository.deleteAll(matches);
        }

        tournamentRepository.deleteById(id);

        Context context = new Context();
        context.setVariable("tournament",t);
        mailerUtils.send(
                "Tournois supprimé",
                "deleted",
                context,
                playersEmails.toArray(new String[0])
        );
    }

    @Override
    public TournamentResult findTournamentsWithCriteria(String name, Set<TournamentCategory> categories, TournamentStatus status, boolean isWomanOnly, int page) {
        return null;
    }


    //Trouver les joueurs qui peuvent s'inscrire a un tournois fraichement créé avec les specifications
    public List<User> findPlayersWhoCanRegister(Tournament t){
        Specification<User> specs = Specification.where(null);
        if(t.getMinElo() != null && t.getMaxElo() != null){
            specs = specs.and(UserSpecifications.getByElo(t.getMinElo(),t.getMaxElo()));
        }
        if(t.isWomenOnly()){
            specs = specs.and(UserSpecifications.getByGender(UserGender.FEMALE));
        }
        Specification<User> ageSpecs = Specification.where(null);
        if(t.getCategories().contains(TournamentCategory.JUNIOR)){
            ageSpecs = ageSpecs.or(UserSpecifications.getByAge(t.getEndOfRegistrationDate(),0,18));
        }
        if(t.getCategories().contains(TournamentCategory.SENIOR)){
            ageSpecs = ageSpecs.or(UserSpecifications.getByAge(t.getEndOfRegistrationDate(),18,60));
        }
        if(t.getCategories().contains(TournamentCategory.VETERAN)){
            ageSpecs = ageSpecs.or(UserSpecifications.getByAge(t.getEndOfRegistrationDate(),60,200));
        }
        specs = specs.and(ageSpecs);
        return userRepository.findAll(specs);
    }
}
