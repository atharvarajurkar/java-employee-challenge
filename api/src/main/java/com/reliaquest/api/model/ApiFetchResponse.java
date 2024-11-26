package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiFetchResponse {
    private List<MockEmployee> data;
    private String status;
}
