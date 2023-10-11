package com.pumex.demo.controller;

import com.pumex.demo.models.OpenAIResponse;
import com.pumex.demo.service.OpenAIService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/demo")
@RequiredArgsConstructor
public class SentenceController {

  private final OpenAIService openAIService;

  @GetMapping("/text/correctedsentences")
  public List<String> correctSentence(@RequestParam String input) {
    OpenAIResponse response = openAIService.correctSentence(input);
    return response.getChoices().stream().map(choices -> choices.getText().strip()).toList();
  }

  @GetMapping("/text/translatedsentences")
  public List<String> translatedSentence(@RequestParam String input) {
    OpenAIResponse response = openAIService.translatedSentence(input);
    return response.getChoices().stream().map(choice -> choice.getText().strip()).toList();
  }
}
