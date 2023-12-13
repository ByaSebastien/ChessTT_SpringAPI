package be.bstorm.chesstt_springapi.controllers;

import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.forms.user.RegisterForm;
import be.bstorm.chesstt_springapi.services.UserService;
import be.bstorm.chesstt_springapi.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid RegisterForm form
            ){
        System.out.println(form);
        User createdUser = userService.create(form.toEntity());
        return ResponseEntity.status(201).build();
    }
}
