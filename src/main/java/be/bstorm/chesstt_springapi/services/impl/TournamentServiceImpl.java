package be.bstorm.chesstt_springapi.services.impl;

import be.bstorm.chesstt_springapi.exceptions.tournament.TournamentValidationException;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;
import be.bstorm.chesstt_springapi.repositories.TournamentRepository;
import be.bstorm.chesstt_springapi.repositories.UserRepository;
import be.bstorm.chesstt_springapi.services.TournamentService;
import be.bstorm.chesstt_springapi.utils.MailerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
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

        List<String> playersEmail = userRepository.findUsersWhoCanRegister(createdTournament.getMinElo(),
                createdTournament.getMaxElo(),
                createdTournament.isWomenOnly(),
                createdTournament.getCategories().stream().map(Enum::toString).toList(),
                createdTournament.getEndOfRegistrationDate());

        Context context = new Context();
        context.setVariable("tournament",createdTournament);
            mailerUtils.send(
                    "Nouveau tournois ouvert",
                    "invitation",
                    context,
                    playersEmail.toArray(new String[0])
                    );
        return createdTournament;
    }
}
