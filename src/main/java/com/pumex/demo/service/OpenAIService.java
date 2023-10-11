package com.pumex.demo.service;

import com.pumex.demo.models.OpenAIRequest;
import com.pumex.demo.models.OpenAIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.pumex.demo.models.OpenAIConstants.*;

@Service
@RequiredArgsConstructor
public class OpenAIService {

  @Value("${openai.api.token}")
  private String token;

  private final RestTemplate restTemplate;

  public OpenAIResponse correctSentence(String textInput) {
    ResponseEntity<OpenAIResponse> openAIResponseResponseEntity =
        restTemplate.exchange(
            OPEN_API_URL, HttpMethod.POST, getHttpRequestEntity(textInput), OpenAIResponse.class);
    return openAIResponseResponseEntity.getBody();
  }

  public OpenAIResponse translatedSentence(String textInput) {
    ResponseEntity<OpenAIResponse> openAIResponseResponseEntity =
        restTemplate.exchange(
            OPEN_API_URL,
            HttpMethod.POST,
            getHttpRequestEntityForTranslation(textInput),
            OpenAIResponse.class);
    return openAIResponseResponseEntity.getBody();
  }

  private HttpEntity<OpenAIRequest> getHttpRequestEntity(String text) {
    OpenAIRequest openAiRequest = getARequest();
    openAiRequest.setPrompt(SPELLING_GRAMMAR_CORRECTION + text);
    return new HttpEntity<>(openAiRequest, getHeaders());
  }

  private HttpEntity<OpenAIRequest> getHttpRequestEntityForTranslation(String text) {
    OpenAIRequest openAiRequest = getARequest();
    openAiRequest.setPrompt(TRANSLATE_TO_ENGLISH + text);
    return new HttpEntity<>(openAiRequest, getHeaders());
  }

  private OpenAIRequest getARequest() {
    OpenAIRequest openAiRequest = new OpenAIRequest();
    openAiRequest.setModel(TEXT_DAVINCI_003);
    openAiRequest.setFrequency_penalty(0);
    openAiRequest.setPresence_penalty(0);
    return openAiRequest;
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    return headers;
  }
}
