package be.bstorm.chesstt_springapi.services.security;

import be.bstorm.chesstt_springapi.models.entities.User;

public interface AuthenticationService {


    User login(String login, String password);
}
