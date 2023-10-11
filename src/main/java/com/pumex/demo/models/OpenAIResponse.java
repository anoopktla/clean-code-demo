package com.pumex.demo.models;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class OpenAIResponse {
    private String id;
    private String object;
    private Date creation;
    private String model;
    private List<Choices> choices;
    private Usage usage;

}
