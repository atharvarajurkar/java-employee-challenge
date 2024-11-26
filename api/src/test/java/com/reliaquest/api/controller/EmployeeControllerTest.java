package com.reliaquest.api.controller;

import com.reliaquest.api.DummyDataProvider;
import com.reliaquest.api.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmployeeControllerTest {

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private RestTemplate restTemplate;
    ApiFetchResponse apiFetchResponse;


    @Test
    void testBaseURL() {
        assertEquals("http://localhost:8112/api/v1/employee",EmployeeController.baseURL);
    }

    @Test
    void testGetAllEmployees_APIResponseNull() {
        when(restTemplate.getForObject(any(), any()))
                .thenReturn(null);
    }

    @Test
    public void testGetAllEmployees_Success() {
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponse mockResponse = new ApiFetchResponse(employeeList.subList(0,2), "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(mockResponse);

        ResponseEntity<List<MockEmployee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        assertEquals("Batman", response.getBody().get(0).getEmployee_name());
        assertEquals(50000, response.getBody().get(0).getEmployee_salary());
        assertEquals(30, response.getBody().get(0).getEmployee_age());
        assertEquals("Developer", response.getBody().get(0).getEmployee_title());
        assertEquals("bat.man@company.com", response.getBody().get(0).getEmployee_email());

        assertEquals("Spiderman", response.getBody().get(1).getEmployee_name());
        assertEquals(60000, response.getBody().get(1).getEmployee_salary());
        assertEquals(25, response.getBody().get(1).getEmployee_age());
        assertEquals("Designer", response.getBody().get(1).getEmployee_title());
        assertEquals("spider.man@company.com", response.getBody().get(1).getEmployee_email());
    }

    @Test
    public void testGetAllEmployees_SuccessButEmptyRecordList() {

        ApiFetchResponse mockResponse = new ApiFetchResponse(null, "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(mockResponse);

        ResponseEntity<List<MockEmployee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAllEmployees_Error() {
        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(null);

        ResponseEntity<List<MockEmployee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAllEmployees_APICallException() {
        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenThrow(UncheckedIOException.class);

        ResponseEntity<List<MockEmployee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testEmployeesByName_Success_ValidName() {

        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponse mockResponse = new ApiFetchResponse(employeeList.subList(0,2), "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(mockResponse);

        ResponseEntity<List<MockEmployee>> response = employeeController.getEmployeesByNameSearch("Bat");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        assertEquals("Batman", response.getBody().get(0).getEmployee_name());
        assertEquals(50000, response.getBody().get(0).getEmployee_salary());
        assertEquals(30, response.getBody().get(0).getEmployee_age());
        assertEquals("Developer", response.getBody().get(0).getEmployee_title());
        assertEquals("bat.man@company.com", response.getBody().get(0).getEmployee_email());
    }

    @Test
    public void testEmployeesByName_Success_InvalidName() {

        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponse mockResponse = new ApiFetchResponse(employeeList, "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(mockResponse);

        ResponseEntity<List<MockEmployee>> response = employeeController.getEmployeesByNameSearch("Super");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testEmployeesByName_Failure() {
        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(null);

        ResponseEntity<List<MockEmployee>> response = employeeController.getEmployeesByNameSearch("Super");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testEmployeesById_Success() {
        String id = "123";
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponseObject mockResponse = new ApiFetchResponseObject(employeeList.get(0), "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenReturn(mockResponse);

        ResponseEntity<MockEmployee> response = employeeController.getEmployeeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Batman", response.getBody().getEmployee_name());
        assertEquals(50000, response.getBody().getEmployee_salary());
        assertEquals(30, response.getBody().getEmployee_age());
        assertEquals("Developer", response.getBody().getEmployee_title());
        assertEquals("bat.man@company.com", response.getBody().getEmployee_email());
    }

    @Test
    public void testEmployeesById_Failure() {
        String id = "123";
        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenReturn(null);

        ResponseEntity<MockEmployee> response = employeeController.getEmployeeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testEmployeesById_ThrowsException() {
        String id = "123";
        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenThrow(UncheckedIOException.class);

        ResponseEntity<MockEmployee> response = employeeController.getEmployeeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testHighestSalary_Success() {
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponse mockResponse = new ApiFetchResponse(employeeList, "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(mockResponse);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(69000, response.getBody());
    }

    @Test
    public void testHighestSalary_Failure() {
        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(null);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testTop10HighestSalaryNames_Success() {
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponse mockResponse = new ApiFetchResponse(employeeList, "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(mockResponse);

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        List<String> expectedResult = Arrays.asList("Zeus","Green Lantern","Spiderman","Wonder Woman","Batman","Joker","Dr. Fate","Flash","Harley Quinn","Cyborg");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void testTop10HighestSalaryNames_Failure() {
        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL), ApiFetchResponse.class))
                .thenReturn(null);

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateEmployee_Success() {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        map.put("id", UUID.randomUUID().toString());
        map.put("employee_name","Sentry");
        map.put("employee_salary",12345);
        map.put("employee_age",28);
        map.put("employee_title","Lead");
        map.put("employee_email","sen.try@company.com");

        APIActionResponse apiActionResponse = new APIActionResponse(map, APIActionResponse.Status.HANDLED, null);
        ResponseEntity<APIActionResponse> createResponse = new ResponseEntity<>(apiActionResponse, HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class),any(HttpEntity.class), any(Class.class)))
                .thenReturn(createResponse);

        CreateMockEmployeeInput input = new CreateMockEmployeeInput("Sentry",12345,28,"Lead");
        ResponseEntity<MockEmployee> response = employeeController.createEmployee(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sentry", response.getBody().getEmployee_name());
        assertEquals(12345, response.getBody().getEmployee_salary());
        assertEquals(28, response.getBody().getEmployee_age());
        assertEquals("Lead", response.getBody().getEmployee_title());
        assertEquals("sen.try@company.com", response.getBody().getEmployee_email());
    }

    @Test
    public void testCreateEmployee_Failure() {
        ResponseEntity<APIActionResponse> createResponse = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class),any(HttpEntity.class), any(Class.class)))
                .thenReturn(createResponse);

        CreateMockEmployeeInput input = new CreateMockEmployeeInput("Sentry",12345,28,"Lead");
        ResponseEntity<MockEmployee> response = employeeController.createEmployee(input);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateEmployee_ThrowsException() {
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class),any(HttpEntity.class), any(Class.class)))
                .thenThrow(UncheckedIOException.class);

        CreateMockEmployeeInput input = new CreateMockEmployeeInput("Sentry",12345,28,"Lead");
        ResponseEntity<MockEmployee> response = employeeController.createEmployee(input);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteEmployee_Success() {
        String id = "123";
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponseObject mockResponse = new ApiFetchResponseObject(employeeList.get(0), "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenReturn(mockResponse);

        APIActionResponse apiActionResponse = new APIActionResponse(true, APIActionResponse.Status.HANDLED, null);
        ResponseEntity<APIActionResponse> deleteResponse = new ResponseEntity<>(apiActionResponse, HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class),any(HttpEntity.class), any(Class.class)))
                .thenReturn(deleteResponse);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Batman", response.getBody());
    }

    @Test
    public void testDeleteEmployee_Failure() {
        String id = "123";
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponseObject mockResponse = new ApiFetchResponseObject(employeeList.get(0), "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenReturn(mockResponse);

        APIActionResponse apiActionResponse = new APIActionResponse(false, APIActionResponse.Status.ERROR, null);
        ResponseEntity<APIActionResponse> deleteResponse = new ResponseEntity<>(apiActionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class),any(HttpEntity.class), any(Class.class)))
                .thenReturn(deleteResponse);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteEmployee_Failure_NullResponseBody() {
        String id = "123";
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponseObject mockResponse = new ApiFetchResponseObject(employeeList.get(0), "Successfully fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenReturn(mockResponse);

        APIActionResponse apiActionResponse = new APIActionResponse(null, APIActionResponse.Status.ERROR, null);
        ResponseEntity<APIActionResponse> deleteResponse = new ResponseEntity<>(apiActionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class),any(HttpEntity.class), any(Class.class)))
                .thenReturn(deleteResponse);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteEmployee_EmployeeDoesNotExist() {
        String id = "123";
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponseObject mockResponse = new ApiFetchResponseObject(null, "Error");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenReturn(mockResponse);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteEmployee_ThrowsException() {
        String id = "123";
        List<MockEmployee> employeeList = DummyDataProvider.getDummyEmployees();

        ApiFetchResponseObject mockResponse = new ApiFetchResponseObject(employeeList.get(0), "Successfully Fetched");

        when(restTemplate.getForObject(URI.create(EmployeeController.baseURL+"/"+id), ApiFetchResponseObject.class))
                .thenReturn(mockResponse);

        when(restTemplate.exchange(any(String.class), any(HttpMethod.class),any(HttpEntity.class), any(Class.class)))
                .thenThrow(UncheckedIOException.class);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}