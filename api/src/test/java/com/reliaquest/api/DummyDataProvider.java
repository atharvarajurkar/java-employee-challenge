package com.reliaquest.api;

import com.reliaquest.api.model.MockEmployee;

import java.util.List;
import java.util.UUID;

public class DummyDataProvider {

    public static List<MockEmployee> getDummyEmployees(){
        // Mock data
        MockEmployee mockEmployee1 = new MockEmployee(UUID.randomUUID(), "Batman", 50000, 30, "Developer", "bat.man@company.com");
        MockEmployee mockEmployee2 = new MockEmployee(UUID.randomUUID(), "Spiderman", 60000, 25, "Designer", "spider.man@company.com");
        MockEmployee mockEmployee3 = new MockEmployee(UUID.randomUUID(), "Wonder Woman", 55000, 20, "Developer", "wonder.woman@company.com");
        MockEmployee mockEmployee4 = new MockEmployee(UUID.randomUUID(), "Black Adam", 10000, 15, "Designer", "black.adam@company.com");
        MockEmployee mockEmployee5 = new MockEmployee(UUID.randomUUID(), "Flash", 33000, 29, "Developer", "fl.ash@company.com");
        MockEmployee mockEmployee6 = new MockEmployee(UUID.randomUUID(), "Green Lantern", 66000, 24, "Designer", "green.lantern@company.com");
        MockEmployee mockEmployee7 = new MockEmployee(UUID.randomUUID(), "Cyborg", 29000, 19, "Developer", "cy.borg@company.com");
        MockEmployee mockEmployee8 = new MockEmployee(UUID.randomUUID(), "Zeus", 69000, 14, "Designer", "ze.us@company.com");
        MockEmployee mockEmployee9 = new MockEmployee(UUID.randomUUID(), "Hercules", 14000, 30, "Developer", "her.cules@company.com");
        MockEmployee mockEmployee10 = new MockEmployee(UUID.randomUUID(), "Dr. Fate", 41000, 25, "Designer", "dr.fate@company.com");
        MockEmployee mockEmployee11 = new MockEmployee(UUID.randomUUID(), "Harley Quinn", 33000, 30, "Developer", "harley.quinn@company.com");
        MockEmployee mockEmployee12 = new MockEmployee(UUID.randomUUID(), "Joker", 44000, 25, "Designer", "jo.ker@company.com");

        List<MockEmployee> employeeList =
                List.of(mockEmployee1, mockEmployee2,mockEmployee3,mockEmployee4,mockEmployee5,mockEmployee6,mockEmployee7,mockEmployee8,mockEmployee9,mockEmployee10,mockEmployee11,mockEmployee12);
        return employeeList;
    }
}
