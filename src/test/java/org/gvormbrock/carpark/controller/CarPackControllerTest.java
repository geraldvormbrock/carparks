package org.gvormbrock.carpark.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CarPackControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void testListCarParksFromPosition() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/car-parks-from-position").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "nbFreeCarPark": 3,
                            "countryCode": "fr",
                            "birthday": "2000-10-26",
                            "townName": "Poitiers",
                            "geoLocation": {
                                "longitude": 46.58349874703973,
                                "latitude": 0.3450022616476489
                            }
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value("NOTRE DAME"))
                .andExpect((jsonPath("$[1].name").value("PALAIS DE JUSTICE")));
    }
}
