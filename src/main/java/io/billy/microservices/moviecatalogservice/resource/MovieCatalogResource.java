package io.billy.microservices.moviecatalogservice.resource;

import io.billy.microservices.moviecatalogservice.models.CatalogDetails;
import io.billy.microservices.moviecatalogservice.models.Movie;
import io.billy.microservices.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogDetails> getCatalog(@PathVariable("userId") String userId){
        List <Rating> ratings = Arrays.asList(
                new Rating("123",4),
                new Rating("567", 5)
        );

        return ratings.stream().map(rating -> {
//           Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/\" + rating.getMovieId()")
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
             return new CatalogDetails(movie.getName(), "desc", rating.getRating());
        })

        .collect(Collectors.toList());


    }
}
