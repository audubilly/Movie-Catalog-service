package io.billy.microservices.moviecatalogservice.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogDetails {

    private String name;
    private String desc;
    private int rating;

}
