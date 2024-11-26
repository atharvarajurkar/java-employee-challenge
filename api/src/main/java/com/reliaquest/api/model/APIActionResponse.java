package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class APIActionResponse<T>  {

    T data;
    Status status;
    String error;

    public enum Status {
        HANDLED("Successfully processed request."),
        ERROR("Failed to process request.");

        @JsonValue
        @Getter
        private final String value;
        Status(String value) {
            this.value = value;
        }
    }
}
