package be.bstorm.chesstt_springapi.services.specifications;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class PlayerAbsenceSpecification implements Specification<User> {

    private final Tournament tournament;

    public PlayerAbsenceSpecification(Tournament tournament) {
        this.tournament = tournament;
    }


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Subquery<User> subquery = query.subquery(User.class);
        Root<Tournament> subRoot = subquery.from(Tournament.class);

        subquery.select(subRoot.join("players"))
                .where(
                        criteriaBuilder.equal(subRoot.get("id"), tournament.getId())
                );

        return criteriaBuilder.not(root.in(subquery));
    }
}
