package be.bstorm.chesstt_springapi.services.specifications;


import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RegistrationDateSpecification implements Specification<User> {

    private final Tournament tournament;

    public RegistrationDateSpecification(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(LocalDateTime.now().isBefore(tournament.getEndOfRegistrationDate())){
            return criteriaBuilder.conjunction();
        }
        return criteriaBuilder.disjunction();
    }
}
