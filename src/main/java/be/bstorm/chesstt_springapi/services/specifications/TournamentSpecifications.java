package be.bstorm.chesstt_springapi.services.specifications;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TournamentSpecifications {

    public static Specification<Tournament> getByName(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%" + name + "%");
    }

    public static Specification<Tournament> getByCategory(Set<TournamentCategory> categories) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate[] predicates = categories.stream()
                    .map(category -> criteriaBuilder.like(root.get("categories").as(String.class),"%" + category.name() + "%"))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }
    public static Specification<Tournament> getByStatus(TournamentStatus status){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status);
    }

    public static Specification<Tournament> getByIsWomanOnly(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isWomenOnly"));
    }
}
