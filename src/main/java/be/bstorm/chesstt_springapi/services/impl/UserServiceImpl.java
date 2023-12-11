package be.bstorm.chesstt_springapi.services.impl;

import be.bstorm.chesstt_springapi.exceptions.user.UserAlreadyExistException;
import be.bstorm.chesstt_springapi.exceptions.user.UserException;
import be.bstorm.chesstt_springapi.exceptions.user.UserNotFoundException;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.repositories.UserRepository;
import be.bstorm.chesstt_springapi.services.UserService;
import be.bstorm.chesstt_springapi.utils.BCryptUtils;
import be.bstorm.chesstt_springapi.utils.MailerUtils;
import be.bstorm.chesstt_springapi.utils.PasswordGeneratorUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordGeneratorUtils passwordGenerator;
    private final BCryptUtils bCryptUtils;
    private final MailerUtils mailer;

    @Transactional
    public User create(User u){
        if(userRepository.existsByUsername(u.getUsername())){
            throw new UserAlreadyExistException("This username already exist");
        }
        if(userRepository.existsByEmail(u.getEmail())){
            throw new UserAlreadyExistException("This email already exist");
        }
        String password = passwordGenerator.generate(8);
        u.setId(UUID.randomUUID());
        u.setPassword(bCryptUtils.hash(password));
        u.setElo(u.getElo() == null ? 1200 : u.getElo());

        User createdUser = userRepository.save(u);
        Context context = new Context();
        context.setVariable("username",u.getUsername());
        context.setVariable("password",password);
        mailer.send("Bienvenu sur ChessTT","welcome",context,u.getEmail());
        return createdUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}