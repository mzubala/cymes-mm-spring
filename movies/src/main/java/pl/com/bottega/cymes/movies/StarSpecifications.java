package pl.com.bottega.cymes.movies;

import org.springframework.data.jpa.domain.Specification;

import static pl.com.bottega.cymes.movies.Star_.firstName;
import static pl.com.bottega.cymes.movies.Star_.lastName;

class StarSpecifications {

    static Specification<Star> byPhrase(String phrase) {
        return byFirstnameLike(phrase).or(byLastnameLike(phrase));
    }

    static Specification<Star> byFirstnameLike(String phrase) {
        return (root, query, criteriaBuilder) -> {
            if(phrase == null) {
                return null;
            }
            var likeExpr = "%" + phrase.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(firstName)), likeExpr);
        };
    }

    static Specification<Star> byLastnameLike(String phrase) {
        return (root, query, criteriaBuilder) -> {
            if(phrase == null) {
                return null;
            }
            var likeExpr = "%" + phrase.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(lastName)), likeExpr);
        };
    }
}
