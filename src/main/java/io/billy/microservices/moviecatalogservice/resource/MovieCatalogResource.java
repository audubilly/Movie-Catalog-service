package io.billy.microservices.moviecatalogservice.resource;

import io.billy.microservices.moviecatalogservice.models.CatalogDetails;
import io.billy.microservices.moviecatalogservice.models.Movie;
import io.billy.microservices.moviecatalogservice.models.UserRatings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogDetails> getCatalog(@PathVariable("userId") String userId) {

        UserRatings userRatings = restTemplate.getForObject("http://ratings-data-service/ratingData/users/" + userId, UserRatings.class);
//        UserRatings userRatings = webClientBuilder.build()
//                .get()
//                .uri("http://localhost:8083/ratingData/users/\" + userId")
//                .retrieve()
//                .bodyToMono(UserRatings.class)
//                .block();
        log.info("user ratings --{}", userRatings);
        return userRatings.getUserRatings().stream().map(rating -> {

                    Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://movie-info-service/movies/\" + rating.getMovieId()")
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();
//          Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

                    return new CatalogDetails(movie.getName(), "desc", rating.getRatings());
                })
                .collect(Collectors.toList());


    }
}

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/\" + rating.getMovieId()")
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();
