package com.example.backend;

import com.example.backend.model.Expense;
import com.example.backend.model.Person;
import com.example.backend.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HttpRequestTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAll() throws Exception{
        mockMvc.perform(get("/persons")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("[{\"id\":1,\"name\":\"Gabi\",\"status\":\"alive\",\"revolutId\":\"123456\"},{\"id\":2,\"name\":\"Berni\",\"status\":\"alive\",\"revolutId\":\"1234567\"},{\"id\":3,\"name\":\"Boti\",\"status\":\"alive\",\"revolutId\":\"1234568\"}]")));
       // mockMvc.perform(get("/expenses")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("{\"id\":1,\"name\":\"Lidl\",\"price\":10.0,\"paid\":\"Gabi\",\"description\":\"Alma\",\"date\":\"2024-02-18\",\"currency\":\"LEI\"}")));
    }

    @Test
    void testGetOne() throws Exception{
        mockMvc.perform(get("/expenses/1")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("{\"id\":1,\"name\":\"Gabi\",\"status\":\"alive\",\"revolutId\":\"123456\"}")));
        mockMvc.perform(get("/expenses/1")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("{\"id\":1,\"name\":\"Lidl\",\"price\":10.0,\"paid\":{\"id\":1,\"name\":\"Gabi\",\"status\":\"alive\",\"revolutId\":\"123456\"},\"description\":\"Alma\",\"date\":\"2024-02-18\",\"currency\":\"LEI\"}")));
    }

    @Test
    void testAdd() throws Exception{
        Expense expense=new Expense("Boti",10.0,personRepository.findByName("Boti"),"a","2023-12-05","LEI");
        mockMvc.perform(post("/expenses").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(expense))).andDo(print()).andExpect(status().is(201)).andExpect(content().string(containsString("Boti")));
        Person person=new Person("Gunti","out of group","12345");
        mockMvc.perform(post("/persons").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(person))).andDo(print()).andExpect(status().is(201)).andExpect(content().string(containsString("{\"id\":4,\"name\":\"Gunti\",\"status\":\"out of group\",\"revolutId\":\"12345\"}")));
    }

    @Test
    void testUpdate() throws  Exception{
        Expense newExpense=new Expense("Lidl",10.0,personRepository.findByName("Gabi"),"Alma","2024-02-19","LEI");
       // mockMvc.perform(put("/expenses/5").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newExpense))).andDo(print()).andExpect(status().isOk());
        mockMvc.perform(put("/expenses/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newExpense))).andDo(print()).andExpect(status().isOk());
        //mockMvc.perform(get("/expenses")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("1"))).andExpect(content().string(containsString("2")));
        Person person=new Person("Gabi","out of group","123456");
        mockMvc.perform(put("/persons/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(person))).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("{\"id\":1,\"name\":\"Gabi\",\"status\":\"out of group\",\"revolutId\":\"123456\"}")));
    }

    @Test
    void testDelete() throws  Exception{
        mockMvc.perform(delete("/expenses/1")).andExpect(status().is(204));
       // mockMvc.perform(get("/expenses")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("[]")));

    }
}
