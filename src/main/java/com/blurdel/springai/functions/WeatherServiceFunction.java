package com.blurdel.springai.functions;

import com.blurdel.springai.model.WeatherRequest;
import com.blurdel.springai.model.WeatherResponse;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

public class WeatherServiceFunction implements Function<WeatherRequest, WeatherResponse> {

    public static final String WEATHER_URL = "https://api.api-ninjas.com/v1/weather";

    private final String apiNinjaKey;

    public WeatherServiceFunction(String apiNinjaKey) {
        this.apiNinjaKey = apiNinjaKey;
    }

    @Override
    public WeatherResponse apply(WeatherRequest weatherRequest) {
        RestClient restClient = RestClient.builder()
                .baseUrl(WEATHER_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Key", apiNinjaKey);
                    httpHeaders.set("Accept", "application/json");
                    httpHeaders.set("Content-Type", "application/json");
                }).build();

        // 400 Bad Request: "{"error": "Searching by city parameter requires a premium subscription."}"
        
        return restClient.get().uri(uriBuilder -> {
            System.out.println("Building URI for weather request: " + weatherRequest);

//            uriBuilder.queryParam("latitude", weatherRequest.latitude());
//            uriBuilder.queryParam("longitude", weatherRequest.longitude());
            uriBuilder.queryParam("city", weatherRequest.location());

            if (weatherRequest.state() != null && !weatherRequest.state().isBlank()) {
                uriBuilder.queryParam("state", weatherRequest.state());
            }
            if (weatherRequest.country() != null && !weatherRequest.country().isBlank()) {
                uriBuilder.queryParam("country", weatherRequest.country());
            }

            return uriBuilder.build();
        }).retrieve().body(WeatherResponse.class);
    }

}
