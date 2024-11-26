package com.reliaquest.api.controller;

import com.reliaquest.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController<MockEmployee, CreateMockEmployeeInput>{

    public static final String baseURL = "http://localhost:8112/api/v1/employee";

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private RestTemplate restTemplate;


    @Override
    @GetMapping()
    public ResponseEntity<List<MockEmployee>> getAllEmployees() {
        ApiFetchResponse response = null;
        try{
            response = restTemplate.getForObject(URI.create(baseURL), ApiFetchResponse.class);
            if (Objects.nonNull(response) && Objects.nonNull(response.getData())){
                logger.info(response.getData().size()+" Record(s) fetched");
                return new ResponseEntity<>(response.getData(), HttpStatus.OK);
            } else{
                logger.error("Error Fetching Employee Records");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex){
            logger.error("Error Fetching Employee Records");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<MockEmployee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        ResponseEntity<List<MockEmployee>> allEmployeesResponse = getAllEmployees();
        if (allEmployeesResponse.getStatusCode()==HttpStatus.OK && Objects.nonNull(allEmployeesResponse.getBody())){
            List<MockEmployee> allEmployees =allEmployeesResponse.getBody();
            logger.info(allEmployees.size()+" Record(s) fetched");
            List<MockEmployee> filteredEmployees = allEmployees
                    .stream()
                    .filter(mockEmployee -> mockEmployee.getEmployee_name().contains(searchString))
                    .toList();
            logger.info(filteredEmployees.size()+" Record(s) filtered by name="+searchString);
            return new ResponseEntity<>(filteredEmployees, HttpStatus.OK);
        } else{
            logger.error("Error Fetching Employee Record by name="+searchString);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MockEmployee> getEmployeeById(@PathVariable String id) {
        try{
            ApiFetchResponseObject response = restTemplate.getForObject(URI.create(baseURL+"/"+id), ApiFetchResponseObject.class);
            if(Objects.nonNull(response) && Objects.nonNull(response.getData())){
                logger.info("Record fetched for id="+id, response.getData().toString());
                return new ResponseEntity<>(response.getData(), HttpStatus.OK);
            } else{
                logger.error("Error Fetching Employee Record by id="+id);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception ex){
            logger.error("Error Fetching Employee Record by id="+id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        ResponseEntity<List<MockEmployee>> allEmployeesResponse = getAllEmployees();
        if (allEmployeesResponse.getStatusCode()==HttpStatus.OK && Objects.nonNull(allEmployeesResponse.getBody())){
            List<MockEmployee> allEmployees =allEmployeesResponse.getBody();
            logger.info(allEmployees.size()+" Record(s) fetched");
            Integer highestSalary = allEmployees
                    .stream()
                    .sorted((e1, e2) -> e2.getEmployee_salary().compareTo(e1.getEmployee_salary()))
//                    .sorted(Comparator.comparing(MockEmployee::getEmployee_salary))
                    .findFirst()
                    .map(MockEmployee::getEmployee_salary)
                    .orElse(-1);
            logger.info("Highest Salary fetched="+highestSalary);
            return new ResponseEntity<>(highestSalary, HttpStatus.OK);
        } else{
            logger.error("Error Fetching Employee Records");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        ResponseEntity<List<MockEmployee>> allEmployeesResponse = getAllEmployees();
        if (allEmployeesResponse.getStatusCode()==HttpStatus.OK && Objects.nonNull(allEmployeesResponse.getBody())){
            List<MockEmployee> allEmployees =allEmployeesResponse.getBody();
            logger.info(allEmployees.size()+" Record(s) fetched");
            List<String> topTenHighestEarningEmployees = allEmployees
                    .stream()
                    .sorted((e1, e2) -> e2.getEmployee_salary().compareTo(e1.getEmployee_salary()))
//                    .sorted(Comparator.comparing(MockEmployee::getEmployee_salary))
                    .limit(10)
                    .map(MockEmployee::getEmployee_name)
                    .toList();
            logger.info("Top 10 highest earning employees list fetched="+topTenHighestEarningEmployees.toString());
            return new ResponseEntity<>(topTenHighestEarningEmployees, HttpStatus.OK);
        } else{
            logger.error("Error Fetching Employee Records");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PostMapping()
    public ResponseEntity<MockEmployee> createEmployee(@RequestBody CreateMockEmployeeInput employeeInput) {
        try {
            HttpEntity<CreateMockEmployeeInput> requestEntity = new HttpEntity<>(employeeInput);
            ResponseEntity<APIActionResponse> createResponse = restTemplate.exchange(baseURL, HttpMethod.POST, requestEntity, APIActionResponse.class);
            if (createResponse.getStatusCode()== HttpStatus.OK && Objects.nonNull(createResponse.getBody()) && Objects.nonNull(createResponse.getBody().getData())){
                LinkedHashMap map = (LinkedHashMap) createResponse.getBody().getData();
                MockEmployee newEmployee = new MockEmployee(
                        UUID.fromString((String)(map.get("id"))),
                        (String) map.get("employee_name"),
                        (Integer) map.get("employee_salary"),
                        (Integer) map.get("employee_age"),
                        (String) map.get("employee_title"),
                        (String) map.get("employee_email")
                );
                logger.info("New Employee created:"+newEmployee.toString());
                return new ResponseEntity<>(newEmployee, HttpStatus.OK);
            } else {
                logger.error("Error Creating Employee Record");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex){
            logger.error("Error Creating Employee Record");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        try{
            ResponseEntity<MockEmployee> employeeResponse = getEmployeeById(id);
            if (Objects.nonNull(employeeResponse) && Objects.nonNull(employeeResponse.getBody())){
                String employeeName = employeeResponse.getBody().getEmployee_name();
                DeleteMockEmployeeInput employeeToBeDeleted = new DeleteMockEmployeeInput();
                employeeToBeDeleted.setName(employeeName);

                HttpEntity<DeleteMockEmployeeInput> requestEntity = new HttpEntity<>(employeeToBeDeleted);
                ResponseEntity<APIActionResponse> deleteResponse = restTemplate.exchange(baseURL, HttpMethod.DELETE, requestEntity, APIActionResponse.class);
                if (deleteResponse.getStatusCode()== HttpStatus.OK && Objects.nonNull(deleteResponse.getBody()) && deleteResponse.getBody().getData().equals(true)){
                    logger.info("Employee deleted:"+employeeName);
                    return new ResponseEntity<>(employeeName, HttpStatus.OK);
                } else {
                    logger.error("Error Deleting Employee Record for id="+id);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                logger.error("Error Deleting Employee Record for id="+id);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex){
            logger.error("Error Deleting Employee Record for id="+id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
