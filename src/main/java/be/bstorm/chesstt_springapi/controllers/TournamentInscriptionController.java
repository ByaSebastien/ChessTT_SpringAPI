package be.bstorm.chesstt_springapi.controllers;

import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class TournamentInscriptionController {

    private final TournamentService tournamentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public ResponseEntity<Void> register(
            Authentication authentication,
            @PathVariable UUID id
            ){
        User user = (User) authentication.getPrincipal();
        tournamentService.register(user,id);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unregister(
            Authentication authentication,
            @PathVariable UUID id
    ){
        User user = (User) authentication.getPrincipal();
        tournamentService.unregister(user,id);
        return ResponseEntity.status(201).build();
    }
}
