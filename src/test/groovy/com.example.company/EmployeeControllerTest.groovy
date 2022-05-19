package com.example.company

import com.example.company.service.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@WebMvcTest
class EmployeeControllerTest extends Specification{

    EmployeeService employeeService = Mock()

    @Autowired
    private MockMvc mvc

    def "should create employeeService"() {
        expect:
        employeeService
    }

    def "should receive status 200 OK"() {
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/employees/employee"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().response.contentAsString("Employee created successfully.")
    }

}
