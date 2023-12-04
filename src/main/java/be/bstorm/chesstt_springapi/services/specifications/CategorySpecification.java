//package be.bstorm.chesstt_springapi.services.specifications;
//
//public class CategorySpecification implements Specification<User> {
//
//    private final Tournament tournament;
//    private final User player;
//
//    public CategorySpecification(Tournament tournament, User player) {
//        this.tournament = tournament;
//        this.player = player;
//    }
//
//    @Override
//    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//        int age = calculateAge(tournament, player);
//
//        return criteriaBuilder.or(
//            criteriaBuilder.and(
//                tournament.get("categories").in(TournamentCategory.Junior),
//                criteriaBuilder.lessThanOrEqualTo(age, 18)
//            ),
//            criteriaBuilder.and(
//                tournament.get("categories").in(TournamentCategory.Senior),
//                criteriaBuilder.between(age, 18, 60)
//            ),
//            criteriaBuilder.and(
//                tournament.get("categories").in(TournamentCategory.Veteran),
//                criteriaBuilder.greaterThan(age, 60)
//            )
//        );
//    }
//
//    private int calculateAge(Tournament tournament, User player) {
//        // Votre logique pour calculer l'âge
//        // ...
//    }
//}
