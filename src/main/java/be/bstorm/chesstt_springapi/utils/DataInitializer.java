package be.bstorm.chesstt_springapi.utils;

import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.*;
import be.bstorm.chesstt_springapi.repositories.MatchRepository;
import be.bstorm.chesstt_springapi.repositories.TournamentRepository;
import be.bstorm.chesstt_springapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public void run(String... args) throws Exception {

        String password = bCryptUtils.hash("Test1234=");

        UUID checkmateId = UUID.randomUUID();
        User checkmate = new User(
                checkmateId,
                "Checkmate",
                "checkmate@test.be",
                password,
                LocalDate.of(1991, 3, 27),
                3000,
                UserGender.MALE,
                UserRole.ADMIN
        );
        UUID sebId = UUID.randomUUID();
        User seb = new User(
                sebId,
                "Seb",
                "seb@test.be",
                password,
                LocalDate.of(1991, 3, 27),
                1200,
                UserGender.MALE,
                UserRole.PLAYER
        );
        UUID tournamentId = UUID.randomUUID();
        Tournament testTournament = new Tournament(
                tournamentId,
                "Tournoi des null",
                "Liège",
                10,
                16,
                0,
                3000,
                List.of(TournamentCategory.JUNIOR, TournamentCategory.SENIOR, TournamentCategory.VETERAN),
                TournamentStatus.IN_PROGRESS,
                false,
                0,
                LocalDateTime.of(2024, 1, 1, 13, 30)
        );
        Match testMatch = new Match();
        testMatch.setRound(1);
        testMatch.setResult(MatchResult.NOT_PLAYED);
        testTournament.addPlayer(checkmate);
        testTournament.addPlayer(seb);
        testMatch.setTournament(testTournament);
        testMatch.setWhitePlayer(checkmate);
        testMatch.setBlackPlayer(seb);

        userRepository.save(checkmate);
        userRepository.save(seb);
        tournamentRepository.save(testTournament);
        matchRepository.save(testMatch);

        Set<Tournament> tournaments = tournamentRepository.getTournamentsByPlayerId(checkmateId);
        tournaments.forEach(System.out::println);
        Set<Match> matches = matchRepository.getMatchesByPlayerId(checkmateId);
        matches.forEach(System.out::println);
        matches = matchRepository.getMatchesByTournamentId(tournamentId);
        matches.forEach(System.out::println);
        matches = matchRepository.getMatchesByTournamentIdAndRound(tournamentId, 1);
        matches.forEach(System.out::println);
    }
}
