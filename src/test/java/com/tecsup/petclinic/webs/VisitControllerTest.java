package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VisitDTO;
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
public class VisitControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllVisits() throws Exception {

        this.mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testFindVisitOK() throws Exception {

        this.mockMvc.perform(get("/visits/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    public void testFindVisitKO() throws Exception {

        mockMvc.perform(get("/visits/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateVisit() throws Exception {

        String VISIT_DATE        = "2024-01-15";
        String VISIT_DESCRIPTION = "Consulta general";
        int    PET_ID            = 1;

        VisitDTO newVisitDTO = VisitDTO.builder()
                .visitDate(VISIT_DATE)
                .description(VISIT_DESCRIPTION)
                .petId(PET_ID)
                .build();

        this.mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitDate", is(VISIT_DATE)))
                .andExpect(jsonPath("$.description", is(VISIT_DESCRIPTION)))
                .andExpect(jsonPath("$.petId", is(PET_ID)));
    }

    @Test
    public void testDeleteVisit() throws Exception {

        VisitDTO newVisitDTO = VisitDTO.builder()
                .visitDate("2024-02-20")
                .description("Vacunacion")
                .petId(1)
                .build();

        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = ((Number) JsonPath.parse(response).read("$.id")).longValue();

        mockMvc.perform(delete("/visits/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVisitKO() throws Exception {

        mockMvc.perform(delete("/visits/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateVisit() throws Exception {

        VisitDTO newVisitDTO = VisitDTO.builder()
                .visitDate("2024-03-10")
                .description("Revision dental")
                .petId(1)
                .build();

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = ((Number) JsonPath.parse(response).read("$.id")).longValue();

        // UPDATE
        VisitDTO upVisitDTO = VisitDTO.builder()
                .id(id)
                .visitDate("2024-03-15")
                .description("Revision dental actualizada")
                .petId(1)
                .build();

        mockMvc.perform(put("/visits/" + id)
                        .content(om.writeValueAsString(upVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/visits/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Revision dental actualizada")));

        // DELETE
        mockMvc.perform(delete("/visits/" + id))
                .andExpect(status().isOk());
    }
}