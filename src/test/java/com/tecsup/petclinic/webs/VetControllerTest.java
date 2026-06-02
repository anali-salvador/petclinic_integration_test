package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VetDTO;
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
public class VetControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllVets() throws Exception {

        final int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    @Test
    public void testFindVetOK() throws Exception {

        String VET_FIRST_NAME = "James";
        String VET_LAST_NAME  = "Carter";

        this.mockMvc.perform(get("/vets/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(VET_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(VET_LAST_NAME)));
    }

    @Test
    public void testFindVetKO() throws Exception {

        mockMvc.perform(get("/vets/666"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateVet() throws Exception {

        String VET_FIRST_NAME = "Ana";
        String VET_LAST_NAME  = "Lopez";

        VetDTO newVetDTO = VetDTO.builder()
                .firstName(VET_FIRST_NAME)
                .lastName(VET_LAST_NAME)
                .build();

        this.mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(VET_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(VET_LAST_NAME)));
    }

    @Test
    public void testDeleteVet() throws Exception {

        String VET_FIRST_NAME = "Carlos";
        String VET_LAST_NAME  = "Ruiz";

        VetDTO newVetDTO = VetDTO.builder()
                .firstName(VET_FIRST_NAME)
                .lastName(VET_LAST_NAME)
                .build();

        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVetKO() throws Exception {

        mockMvc.perform(delete("/vets/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateVet() throws Exception {

        String VET_FIRST_NAME    = "Pedro";
        String VET_LAST_NAME     = "Gomez";
        String UP_VET_FIRST_NAME = "Pedro";
        String UP_VET_LAST_NAME  = "Ramirez";

        VetDTO newVetDTO = VetDTO.builder()
                .firstName(VET_FIRST_NAME)
                .lastName(VET_LAST_NAME)
                .build();

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // UPDATE
        VetDTO upVetDTO = VetDTO.builder()
                .id(id)
                .firstName(UP_VET_FIRST_NAME)
                .lastName(UP_VET_LAST_NAME)
                .build();

        mockMvc.perform(put("/vets/" + id)
                        .content(om.writeValueAsString(upVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/vets/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_VET_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_VET_LAST_NAME)));

        // DELETE
        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }
}