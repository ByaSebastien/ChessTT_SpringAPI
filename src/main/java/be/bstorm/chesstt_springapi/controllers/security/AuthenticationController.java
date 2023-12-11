package be.bstorm.chesstt_springapi.controllers.security;

import be.bstorm.chesstt_springapi.models.dtos.user.TokenDTO;
import be.bstorm.chesstt_springapi.models.dtos.user.UserDTO;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.forms.user.LoginForm;
import be.bstorm.chesstt_springapi.services.security.AuthenticationService;
import be.bstorm.chesstt_springapi.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(
            @RequestBody LoginForm form
            ){

        User connectedUser = authenticationService.login(form.login(), form.password());
        String token = jwtUtils.generateToken(connectedUser);
        TokenDTO result = new TokenDTO(UserDTO.fromEntity(connectedUser),token);
        return ResponseEntity.ok(result);
    }
}
