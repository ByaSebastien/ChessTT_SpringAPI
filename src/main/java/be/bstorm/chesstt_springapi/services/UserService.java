package be.bstorm.chesstt_springapi.services;

import be.bstorm.chesstt_springapi.models.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User create(User u);
}
