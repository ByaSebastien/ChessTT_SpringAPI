package be.bstorm.chesstt_springapi.services;

import be.bstorm.chesstt_springapi.models.entities.Tournament;

import java.util.UUID;

public interface TournamentService {

    Tournament create(Tournament t);

    void delete(UUID id);
}
