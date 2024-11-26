package com.reliaquest.api.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class APIActionResponseTest {

    @Test
    void assignDataOfTypeString() {
        APIActionResponse<String> response = new APIActionResponse<>("Sample Data", APIActionResponse.Status.HANDLED, null);

        Assertions.assertEquals(response.getData().getClass(), String.class);
        Assertions.assertEquals("Successfully processed request.", response.getStatus().getValue());
    }

    @Test
    void assignDataOfTypeInteger() {
        APIActionResponse<Integer> response = new APIActionResponse<>(44, APIActionResponse.Status.ERROR, "dummy error");

        Assertions.assertEquals(response.getData().getClass(), Integer.class);
        Assertions.assertEquals("Failed to process request.", response.getStatus().getValue());

    }
}