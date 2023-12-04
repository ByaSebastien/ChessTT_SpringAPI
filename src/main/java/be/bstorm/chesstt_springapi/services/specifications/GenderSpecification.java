package be.bstorm.chesstt_springapi.services.specifications;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.UserGender;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class GenderSpecification implements Specification<User> {

    private final Tournament tournament;

    public GenderSpecification(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(tournament.isWomenOnly()){
            return criteriaBuilder.equal(root.get("gender"), UserGender.FEMALE);
        }
        return criteriaBuilder.conjunction();
    }
}

//public final class UserSpecifications {
//    public static Specification<User> getByGender(UserGender gender) {
//        return (root, cq, cb) -> cb.equal(root.get("gender"), gender);
//    }
//}
