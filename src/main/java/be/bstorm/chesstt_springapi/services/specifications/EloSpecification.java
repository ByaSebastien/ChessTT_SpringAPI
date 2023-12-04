//package be.bstorm.chesstt_springapi.services.specifications;
//
//public class EloSpecification implements Specification<User> {
//
//    private final Tournament tournament;
//    private final User player;
//
//    public EloSpecification(Tournament tournament, User player) {
//        this.tournament = tournament;
//        this.player = player;
//    }
//
//    @Override
//    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//        return criteriaBuilder.and(
//            criteriaBuilder.or(
//                tournament.get("eloMin").isNull(),
//                criteriaBuilder.greaterThanOrEqualTo(player.get("elo"), tournament.get("eloMin"))
//            ),
//            criteriaBuilder.and(
//                tournament.get("eloMax").isNull(),
//                criteriaBuilder.lessThanOrEqualTo(player.get("elo"), tournament.get("eloMax"))
//            )
//        );
//    }
//}
