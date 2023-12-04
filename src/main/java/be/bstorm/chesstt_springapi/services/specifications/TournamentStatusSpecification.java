package be.bstorm.chesstt_springapi.services.specifications;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TournamentStatusSpecification implements Specification<User> {

    private final Tournament tournament;

    public TournamentStatusSpecification(Tournament tournament) {
        this.tournament = tournament;
    }


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(this.tournament.getStatus().equals(TournamentStatus.WAITING_FOR_PLAYERS)){
            return criteriaBuilder.conjunction();
        }
        return criteriaBuilder.disjunction();
    }
}
