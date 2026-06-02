package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.OwnerDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class OwnerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllOwners() throws Exception {

        final long ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD), Long.class));
    }

    @Test
    public void testFindOwnerOK() throws Exception {

        String OWNER_FIRST_NAME = "George";
        String OWNER_LAST_NAME  = "Franklin";

        this.mockMvc.perform(get("/owners/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.firstName", is(OWNER_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(OWNER_LAST_NAME)));
    }

    @Test
    public void testFindOwnerKO() throws Exception {

        mockMvc.perform(get("/owners/666"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOwner() throws Exception {

        String OWNER_FIRST_NAME = "Maria";
        String OWNER_LAST_NAME  = "Torres";
        String OWNER_ADDRESS    = "Av. Lima 123";
        String OWNER_CITY       = "Lima";
        String OWNER_TELEPHONE  = "987654321";

        OwnerDTO newOwnerDTO = OwnerDTO.builder()
                .firstName(OWNER_FIRST_NAME)
                .lastName(OWNER_LAST_NAME)
                .address(OWNER_ADDRESS)
                .city(OWNER_CITY)
                .telephone(OWNER_TELEPHONE)
                .build();

        this.mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(OWNER_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(OWNER_LAST_NAME)))
                .andExpect(jsonPath("$.address", is(OWNER_ADDRESS)))
                .andExpect(jsonPath("$.city", is(OWNER_CITY)))
                .andExpect(jsonPath("$.telephone", is(OWNER_TELEPHONE)));
    }

    @Test
    public void testDeleteOwner() throws Exception {

        OwnerDTO newOwnerDTO = OwnerDTO.builder()
                .firstName("Luis")
                .lastName("Perez")
                .address("Calle 456")
                .city("Arequipa")
                .telephone("123456789")
                .build();

        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = ((Number) JsonPath.parse(response).read("$.id")).longValue();

        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOwnerKO() throws Exception {

        mockMvc.perform(delete("/owners/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateOwner() throws Exception {

        OwnerDTO newOwnerDTO = OwnerDTO.builder()
                .firstName("Carlos")
                .lastName("Quispe")
                .address("Jr. Huallaga 789")
                .city("Cusco")
                .telephone("111222333")
                .build();

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = ((Number) JsonPath.parse(response).read("$.id")).longValue();

        // UPDATE
        OwnerDTO upOwnerDTO = OwnerDTO.builder()
                .id(id)
                .firstName("Carlos")
                .lastName("Mamani")
                .address("Jr. Huallaga 789")
                .city("Cusco")
                .telephone("111222333")
                .build();

        mockMvc.perform(put("/owners/" + id)
                        .content(om.writeValueAsString(upOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/owners/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Carlos")))
                .andExpect(jsonPath("$.lastName", is("Mamani")));

        // DELETE
        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());
    }
}