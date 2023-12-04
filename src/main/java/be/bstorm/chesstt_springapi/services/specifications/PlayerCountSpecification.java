package be.bstorm.chesstt_springapi.services.specifications;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class PlayerCountSpecification implements Specification<User> {

    private final Tournament tournament;

    public PlayerCountSpecification(Tournament tournament) {
        this.tournament = tournament;
    }


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Tournament> subRoot = subquery.from(Tournament.class);
        Join<Tournament, User> join = subRoot.join("players", JoinType.LEFT);

        subquery.select(criteriaBuilder.count(join.get("id")))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(subRoot.get("id"), tournament.getId()),
                        criteriaBuilder.equal(join, root)
                ));

        return criteriaBuilder.lessThan(subquery, Long.valueOf(tournament.getMaxPlayers()));
    }


}
