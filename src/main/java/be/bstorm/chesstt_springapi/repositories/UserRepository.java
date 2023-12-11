package be.bstorm.chesstt_springapi.repositories;

import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

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

    @Query("select u " +
            "from User u " +
            "where u.email ilike :login or u.username like :login")
    Optional<User> findUserByEmailOrUsername(String login);

    //Trouver les joueurs qui peuvent s'inscrire a un tournois fraichement créé via query jpql
    @Query("SELECT u.email FROM User u " +
            "WHERE NOT (:isWomanOnly = true AND u.gender != 'FEMALE') " +
            "AND (:minElo IS NULL OR :maxElo IS NULL OR u.elo BETWEEN :minElo AND :maxElo) " +
            "AND (" +
            "   ('JUNIOR' IN :categories AND " +
            "       (EXTRACT(YEAR FROM CAST(:endOfRegistration AS date)) - EXTRACT(YEAR FROM u.birthdate) - " +
            "           CASE WHEN EXTRACT(MONTH FROM u.birthdate) <= EXTRACT(MONTH FROM CAST(:endOfRegistration AS date)) AND " +
            "                     EXTRACT(DAY FROM u.birthdate) < EXTRACT(DAY FROM CAST(:endOfRegistration AS date)) " +
            "                THEN 1 ELSE 0 END) < 18) " +
            "   OR " +
            "   ('SENIOR' IN :categories AND " +
            "       (EXTRACT(YEAR FROM CAST(:endOfRegistration AS date)) - EXTRACT(YEAR FROM u.birthdate) - " +
            "           CASE WHEN EXTRACT(MONTH FROM u.birthdate) <= EXTRACT(MONTH FROM CAST(:endOfRegistration AS date)) AND " +
            "                     EXTRACT(DAY FROM u.birthdate) < EXTRACT(DAY FROM CAST(:endOfRegistration AS date)) " +
            "                THEN 1 ELSE 0 END) BETWEEN 18 AND 59) " +
            "   OR " +
            "   ('VETERAN' IN :categories AND " +
            "       (EXTRACT(YEAR FROM CAST(:endOfRegistration AS date)) - EXTRACT(YEAR FROM u.birthdate) - " +
            "           CASE WHEN EXTRACT(MONTH FROM u.birthdate) <= EXTRACT(MONTH FROM CAST(:endOfRegistration AS date)) AND " +
            "                     EXTRACT(DAY FROM u.birthdate) < EXTRACT(DAY FROM CAST(:endOfRegistration AS date)) " +
            "                THEN 1 ELSE 0 END) >= 60) " +
            ")")
    List<String> findUsersWhoCanRegister(
            @Param("minElo") Integer minElo,
            @Param("maxElo") Integer maxElo,
            @Param("isWomanOnly") boolean isWomanOnly,
            @Param("categories") List<String> categories,
            @Param("endOfRegistration") LocalDateTime endOfRegistration
    );


}
