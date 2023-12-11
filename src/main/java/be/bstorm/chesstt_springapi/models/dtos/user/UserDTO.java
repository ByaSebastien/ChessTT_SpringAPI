package be.bstorm.chesstt_springapi.models.dtos.user;

import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.UserGender;
import be.bstorm.chesstt_springapi.models.enums.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public record UserDTO(

        UUID id,
        String username,
        String email,
        LocalDate birthdate,
        int elo,
        UserGender gender,
        UserRole role
) {

    public static UserDTO fromEntity(User u){
        return new UserDTO(u.getId(),u.getUsername(),u.getEmail(),u.getBirthdate(),u.getElo(),u.getGender(),u.getRole());
    }
}
