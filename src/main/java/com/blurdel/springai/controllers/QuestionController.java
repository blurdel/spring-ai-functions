package com.blurdel.springai.controllers;

import com.blurdel.springai.model.Answer;
import com.blurdel.springai.model.Question;
import com.blurdel.springai.services.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final OpenAIService service;

    @PostMapping("/weather")
    public Answer askQuestion(@RequestBody Question question) {
        return service.getAnswer(question);
    }

    @PostMapping("/stock")
    public Answer getStockPrice(@RequestBody Question question) {
        return service.getStockPrice(question);
    }

}
