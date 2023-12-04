//package be.bstorm.chesstt_springapi.services.specifications;
//
//import be.bstorm.chesstt_springapi.models.entities.Tournament;
//import be.bstorm.chesstt_springapi.models.entities.User;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CanRegisterSpecification implements Specification<User> {
//
//    private final Tournament tournament;
//
//    public CanRegisterSpecification(Tournament tournament) {
//        this.tournament = tournament;
//    }
//
//
//    @Override
//    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//        List<Predicate> predicates = new ArrayList<>();
//
//        predicates.add(new TournamentStatusSpecification(tournament).toPredicate(root, query, criteriaBuilder));
//        predicates.add(new PlayerAbsenceSpecification(tournament).toPredicate(root, query, criteriaBuilder));
//        predicates.add(new PlayerCountSpecification(tournament).toPredicate(root, query, criteriaBuilder));
//        predicates.add(new UserSpecifications(tournament).toPredicate(root, query, criteriaBuilder));
//        predicates.add(new RegistrationDateSpecification(tournament).toPredicate(root, query, criteriaBuilder));
//
//        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//    }
//}
