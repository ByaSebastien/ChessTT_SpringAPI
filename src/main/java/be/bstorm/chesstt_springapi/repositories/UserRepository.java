package be.bstorm.chesstt_springapi.repositories;

import be.bstorm.chesstt_springapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("select u " +
            "from User u " +
            "where u.email ilike :email")
    Optional<User> findUserByEmail(String email);

    @Query("select count(u) > 0 " +
            "from User u " +
            "where u.username ilike :username")
    boolean existsByUsername(String username);

    @Query("select count(u) > 0 " +
            "from User u " +
            "where u.email ilike :email")
    boolean existsByEmail(String email);
}
