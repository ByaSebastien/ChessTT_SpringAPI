package be.bstorm.chesstt_springapi.services.specifications;

import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.UserGender;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class UserSpecifications {

    public static Specification<User> getByGender(UserGender gender) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gender"), gender);
    }

    public static Specification<User> getByElo(Integer minElo,Integer maxElo){
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("elo"),minElo,maxElo);
    }

    public static Specification<User> getByAge(LocalDateTime endOfRegistration, int minAge, int maxAge){
        return (root, query, criteriaBuilder) -> {
            Expression<LocalDate> birthdate = root.get("birthdate");

            LocalDate startDate = endOfRegistration.toLocalDate().minusYears(maxAge).plusDays(1);
            LocalDate endDate = endOfRegistration.toLocalDate().minusYears(minAge);

            query.where(
                    criteriaBuilder.between(birthdate, startDate, endDate)
            );

            return query.getRestriction();
        };
    }
}
