package be.bstorm.chesstt_springapi.models.dtos.user;

public record TokenDTO(
        UserDTO user,
        String token
) {
}
