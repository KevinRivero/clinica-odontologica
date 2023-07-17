package com.Integrador.controller;

import com.Integrador.service.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class TurnoControllerTest {
    private static final Logger log = Logger.getLogger(TurnoControllerTest.class);

    @Autowired
    private MockMvc mock;

    @MockBean
    private TurnoService turnoService;

    @Autowired
    private ObjectMapper objectMapper;
}
