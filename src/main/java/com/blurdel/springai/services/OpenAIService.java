package com.blurdel.springai.services;

import com.blurdel.springai.model.Answer;
import com.blurdel.springai.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);

}
