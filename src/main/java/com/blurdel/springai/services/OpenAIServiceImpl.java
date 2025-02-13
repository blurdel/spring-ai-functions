package com.blurdel.springai.services;

import com.blurdel.springai.model.Answer;
import com.blurdel.springai.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    final ChatModel chatModel;

//    @Value("classpath:/templates/rag-prompt-template.st")
//    private Resource ragPromptTemplate;

//    @Value("classpath:/templates/system-message.st")
//    private Resource systemMessageTemplate;

    @Override
    public Answer getAnswer(Question question) {
        return new Answer("to-do implement me!");
    }

//    @Override
//    public Answer getAnswer(Question question) {
//        PromptTemplate sysMsgPT = new SystemPromptTemplate(systemMessageTemplate);
//        Message sysMsg = sysMsgPT.createMessage();
//
//        List<Document> documents = vectorStore.similaritySearch(
//                SearchRequest.builder().query(question.question()).topK(6).build());
//        List<String> contentList = documents.stream().map(Document::getContent).toList();
//
//        PromptTemplate pt = new PromptTemplate(ragPromptTemplate);
//        Message userMsg = pt.createMessage(Map.of(
//                "input", question.question(),
//                "documents", String.join("\n", contentList)
//            ));
//
//        ChatResponse resp = chatModel.call(new Prompt(List.of(sysMsg, userMsg)));
//
//        return new Answer(resp.getResult().getOutput().getContent());
//    }

}
