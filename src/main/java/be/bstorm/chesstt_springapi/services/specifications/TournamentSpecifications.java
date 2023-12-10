package be.bstorm.chesstt_springapi.services.specifications;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TournamentSpecifications {

    public static Specification<Tournament> getByName(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),name);
    }

    public static Specification<Tournament> getByCategory(Set<TournamentCategory> categories) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate[] predicates = categories.stream()
                    .map(category -> criteriaBuilder.isMember(category, root.get("categories")))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }
}
