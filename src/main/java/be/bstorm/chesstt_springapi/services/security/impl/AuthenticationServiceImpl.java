package be.bstorm.chesstt_springapi.services.security.impl;

import be.bstorm.chesstt_springapi.exceptions.user.UserException;
import be.bstorm.chesstt_springapi.exceptions.user.UserNotFoundException;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.repositories.UserRepository;
import be.bstorm.chesstt_springapi.services.security.AuthenticationService;
import be.bstorm.chesstt_springapi.utils.BCryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final BCryptUtils bCryptUtils;
    private final UserRepository userRepository;

    @Override
    public User login(String login, String password) {
        User u = userRepository.findUserByEmailOrUsername(login).orElseThrow(UserNotFoundException::new);

        if(!bCryptUtils.verify(password, u.getPassword())){
            throw new UserException("Wrong password");
        }

        return u;
    }
}
