package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiFetchResponseObject {
    private MockEmployee data;
    private String status;
}
