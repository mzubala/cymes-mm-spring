package pl.com.bottega.cymes.movies;

import org.springframework.data.jpa.domain.Specification;

class MovieSpecifications {
    static Specification<Movie> byPhrase(String phrase) {
        return (root, query, criteriaBuilder) -> {
            if(phrase == null) {
                return null;
            } else {
                var likeExpr = "%" + phrase.toLowerCase() + "%";
                return criteriaBuilder.like(root.get(Movie_.title), likeExpr);
            }
        };
    }
}
