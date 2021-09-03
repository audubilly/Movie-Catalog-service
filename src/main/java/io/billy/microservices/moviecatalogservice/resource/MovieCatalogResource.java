package io.billy.microservices.moviecatalogservice.resource;

import io.billy.microservices.moviecatalogservice.models.CatalogDetails;
import io.billy.microservices.moviecatalogservice.models.Movie;
import io.billy.microservices.moviecatalogservice.models.Rating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/{userId}")
    public List<CatalogDetails> getCatalog(@PathVariable("userId") String userId){
        List <Rating> ratings = Arrays.asList(
                new Rating("123",4),
                new Rating("124", 5)
        );

        return ratings.stream().map(rating -> {
           Movie movie = restTemplate.getForObject("https://localhost:8082/movies/" + rating.getRating(), Movie.class);
             return new CatalogDetails(movie.getName(), "desc", rating.getRating());
        })

                .collect(Collectors.toList());


    }
}
