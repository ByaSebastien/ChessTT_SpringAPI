package be.bstorm.chesstt_springapi.controllers;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.forms.tournament.TournamentForm;
import be.bstorm.chesstt_springapi.services.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid TournamentForm form,
            UriComponentsBuilder uriBuilder
            ){

        Tournament createdTournament = tournamentService.create(form.toEntity());
        URI location = uriBuilder.path("tournament/{id}").buildAndExpand(createdTournament.getId()).toUri();
        return ResponseEntity.created(location).body(createdTournament);
    }
}
