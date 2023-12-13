package be.bstorm.chesstt_springapi.utils;

import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.*;
import be.bstorm.chesstt_springapi.repositories.MatchRepository;
import be.bstorm.chesstt_springapi.repositories.TournamentRepository;
import be.bstorm.chesstt_springapi.repositories.UserRepository;
import be.bstorm.chesstt_springapi.services.impl.TournamentServiceImpl;
import be.bstorm.chesstt_springapi.services.specifications.TournamentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final BCryptUtils bCryptUtils;
    private final TournamentServiceImpl tournamentService;

    @Override
    public void run(String... args) throws Exception {

        String password = bCryptUtils.hash("Test1234=");

        UUID checkmateId = UUID.randomUUID();
        User checkmate = new User(
                checkmateId,
                "Checkmate",
                "checkmate@test.be",
                password,
                LocalDate.of(2006, 1, 1),
                3000,
                UserGender.FEMALE,
                UserRole.ADMIN
        );
        UUID sebId = UUID.randomUUID();
        User seb = new User(
                sebId,
                "Seb",
                "byasebastien@hotmail.com",
                password,
                LocalDate.of(1991, 3, 27),
                1200,
                UserGender.MALE,
                UserRole.PLAYER
        );
        UUID rogerId = UUID.randomUUID();
        User roger = new User(
                rogerId,
                "Roger",
                "roger@test.be",
                password,
                LocalDate.of(1991, 3, 27),
                1200,
                UserGender.MALE,
                UserRole.PLAYER
        );
        UUID testTournamentId = UUID.randomUUID();
        Tournament testTournament = new Tournament(
                testTournamentId,
                "Tournoi des null",
                "Liège",
                10,
                16,
                0,
                3000,
                Set.of(TournamentCategory.JUNIOR, TournamentCategory.SENIOR),
                TournamentStatus.CLOSED,
                true,
                1,
                LocalDateTime.of(2024, 1, 1, 13, 30)
        );
        UUID testTournament2Id = UUID.randomUUID();
        Tournament testTournament2 = new Tournament(
                testTournament2Id,
                "Tournoi des null2",
                "Liège",
                2,
                16,
                0,
                3000,
                Set.of(TournamentCategory.JUNIOR,TournamentCategory.SENIOR, TournamentCategory.VETERAN),
                TournamentStatus.WAITING_FOR_PLAYERS,
                true,
                0,
                LocalDateTime.of(2024, 1, 1, 13, 30)
        );
        testTournament.addPlayer(checkmate);
        testTournament.addPlayer(seb);
        Match testMatch = new Match();
        testMatch.setRound(1);
        testMatch.setResult(MatchResult.NOT_PLAYED);
        testMatch.setTournament(testTournament);
        testMatch.setWhitePlayer(checkmate);
        testMatch.setBlackPlayer(seb);

        userRepository.save(checkmate);
        userRepository.save(seb);
        userRepository.save(roger);
        tournamentRepository.save(testTournament);
        tournamentRepository.save(testTournament2);
        matchRepository.save(testMatch);

        testTournament2.addPlayer(checkmate);
        testTournament2.addPlayer(seb);
        testTournament2.addPlayer(roger);
        tournamentRepository.save(testTournament2);

        Set<Tournament> tournaments = tournamentRepository.getTournamentsByPlayerId(checkmateId);
        tournaments.forEach(System.out::println);
        Set<Match> matches = matchRepository.getMatchesByPlayerId(checkmateId);
        matches.forEach(System.out::println);
        matches = matchRepository.getMatchesByTournamentId(testTournamentId);
        matches.forEach(System.out::println);
        matches = matchRepository.getMatchesByTournamentIdAndRound(testTournamentId, 1);
        matches.forEach(System.out::println);


//        Specification<Tournament> spec = TournamentSpecifications.getByIsWomanOnly();
//        tournamentRepository.findAll(spec).forEach(System.out::println);

        tournamentService.findPlayersWhoCanRegister(testTournament);

    }
}
