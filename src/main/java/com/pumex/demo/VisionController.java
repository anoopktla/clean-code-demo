package com.pumex.demo;


import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;

import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.pumex.demo.TranslateText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
/**
 * Code sample demonstrating Cloud Vision usage within the context of Spring Framework using Spring
 * Cloud GCP libraries. The sample is written as a Spring Boot application to demonstrate a
 * practical application of this usage.
 */
@RestController
public class VisionController {

    @Autowired private ResourceLoader resourceLoader;

      /*@Autowired*/ private CloudVisionTemplate cloudVisionTemplate;

    /**
     * This method downloads an image from a URL and sends its contents to the Vision API for label
     * detection.
     *
     * @param imageUrl the URL of the image
     * @param map the model map to use
     * @return a string with the list of labels and percentage of certainty
     */
    @GetMapping("/extractLabels")
    public ModelAndView extractLabels(String imageUrl, ModelMap map) {
        AnnotateImageResponse response =
                this.cloudVisionTemplate.analyzeImage(
                        this.resourceLoader.getResource(imageUrl), Type.LABEL_DETECTION);

        Map<String, Float> imageLabels =
                response.getLabelAnnotationsList().stream()
                        .collect(
                                Collectors.toMap(
                                        EntityAnnotation::getDescription,
                                        EntityAnnotation::getScore,
                                        (u, v) -> {
                                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                                        },
                                        LinkedHashMap::new));

        map.addAttribute("annotations", imageLabels);
        map.addAttribute("imageUrl", imageUrl);
        return new ModelAndView("result", map);
    }

    @GetMapping("/extractText")
    public String extractText(String imageUrl) throws IOException {
        String textFromImage =
                this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));

        TranslateText translateText = new TranslateText();
        String result = translateText.translateText(textFromImage);
        return "Text from image translated: " + result;
    }
}