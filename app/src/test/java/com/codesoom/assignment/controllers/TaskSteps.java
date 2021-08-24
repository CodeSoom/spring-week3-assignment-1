package com.codesoom.assignment.controllers;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class TaskSteps {
    public static final String PATH = "/tasks";

    public static MockHttpServletRequestBuilder postWithConfig(String contents) {
        return addConfig(post(PATH), contents);
    }

    public static MockHttpServletRequestBuilder putWithConfig(Long id, String contents) {
        return addConfig(put(PATH + "/" + id), contents);
    }

    public static MockHttpServletRequestBuilder patchWithConfig(Long id, String contents) {
        return addConfig(patch(PATH + "/" + id), contents);
    }

    private static MockHttpServletRequestBuilder addConfig(MockHttpServletRequestBuilder requestBuilder, String contents) {
        return requestBuilder.content(contents)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}
