package com.blurdel.springai.services;

import com.blurdel.springai.functions.StockQuoteFunction;
import com.blurdel.springai.functions.WeatherServiceFunction;
import com.blurdel.springai.model.Answer;
import com.blurdel.springai.model.Question;
import com.blurdel.springai.model.StockPriceRequest;
import com.blurdel.springai.model.StockPriceResponse;
import com.blurdel.springai.model.WeatherRequest;
import com.blurdel.springai.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${sfg.aiapp.apiNinjasKey}")
    private String apiNinjasKey;

    private final OpenAiChatModel chatModel;


    @Override
    public Answer getStockPrice(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("CurrentStockPrice", new StockQuoteFunction(apiNinjasKey))
                        .description("Get the current stock price for a stock symbol")
                        .inputType(StockPriceRequest.class)
                        .responseConverter(resp -> {
                            String schema = ModelOptionsUtils.getJsonSchema(StockPriceResponse.class, false);
                            String json = ModelOptionsUtils.toJsonString(resp);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        Message userMsg = new PromptTemplate(question.question()).createMessage();
        Message systemMsg = new SystemPromptTemplate("You are an agent which returns back a stock price for the given stock symbol (or ticker)").createMessage();

        var response = chatModel.call(new Prompt(List.of(userMsg, systemMsg), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getAnswer(Question question) {

        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("CurrentWeather", new WeatherServiceFunction(apiNinjasKey))
                        .description("Get the current weather for a location")
                                .responseConverter(resp -> {
                                    String schema = ModelOptionsUtils.getJsonSchema(WeatherResponse.class, false);
                                    String json = ModelOptionsUtils.toJsonString(resp);
                                    return schema + "\n" + json;
                                })
                        .inputType(WeatherRequest.class)
                        .build()))
                .build();

        Message userMsg = new PromptTemplate(question.question()).createMessage();

        Message systemMsg = new SystemPromptTemplate("You are a weather service. You receive weather information from a service which gives you the information based on the metrics system." +
                " When answering the weather in an imperial system country, you should convert the temperature to Fahrenheit and the wind speed to miles per hour. ").createMessage();

        var response = chatModel.call(new Prompt(List.of(userMsg, systemMsg), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }

}
