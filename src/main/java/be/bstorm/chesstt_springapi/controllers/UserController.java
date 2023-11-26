package be.bstorm.chesstt_springapi.controllers;

import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.forms.users.RegisterForm;
import be.bstorm.chesstt_springapi.services.UserService;
import be.bstorm.chesstt_springapi.utils.JwtUtils;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid RegisterForm form,
            UriComponentsBuilder uriBuilder
            ){
        System.out.println(form);
        User createdUser = userService.create(form.toEntity());
        URI location = uriBuilder.path("user/{id}").buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
