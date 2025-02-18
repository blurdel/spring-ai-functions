package com.blurdel.springai.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.blurdel.springai.model.StockPriceRequest;
import com.blurdel.springai.model.StockPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

@RequiredArgsConstructor
public class StockQuoteFunction implements Function<StockPriceRequest, StockPriceResponse> {

    public final String STOCK_URL = "https://api.api-ninjas.com/v1/stockprice";

    private final String apiNinjaKey;

    @Override
    public StockPriceResponse apply(StockPriceRequest stockPriceRequest) {
        RestClient restClient = RestClient.builder()
                .baseUrl(STOCK_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Key", apiNinjaKey);
                    httpHeaders.set("Accept", "application/json");
                    httpHeaders.set("Content-Type", "application/json");
                }).build();

        JsonNode jsonNode = restClient.get().uri(uriBuilder -> uriBuilder.queryParam("ticker", stockPriceRequest.ticker()).build())
                .retrieve().body(JsonNode.class);

        if (jsonNode.isEmpty()) {
            return new StockPriceResponse(null, null, null, null, null, null);
        } else {
            return new ObjectMapper().convertValue(jsonNode, StockPriceResponse.class);
        }
    }

}
