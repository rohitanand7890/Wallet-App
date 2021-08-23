package com.bitcoin.wallet.controller;


import com.bitcoin.wallet.domain.TimeInterval;
import com.bitcoin.wallet.domain.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public final class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private WalletController walletController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenValidInputForTimeInterval_thenReturns200() throws Exception {
        Transaction transaction = new Transaction("2010-08-04T01:00:00+00:00", 1000d);
        this.mockMvc.perform(post("/wallet/deposit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk());

        TimeInterval timeInterval = new TimeInterval("2010-08-04T01:00:00+00:00", "2010-08-04T02:00:00+00:00");
        MvcResult mvcResult = this.mockMvc.perform(post("/wallet/balance")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeInterval)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertEquals(response, "[{\"datetime\":\"2010-08-04T01:00:00Z\",\"amount\":1000.0},{\"datetime\":\"2010-08-04T02:00:00Z\",\"amount\":1000.0}]");
    }

    @Test
    public void whenInValidInputForWalletDeposit_thenReturns400() throws Exception {
        Transaction transaction = new Transaction("2015-08-04T01::00+00:00", 1000d);
        this.mockMvc.perform(post("/wallet/deposit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInValidTimeInterval1_thenReturns400() throws Exception {
        TimeInterval timeInterval = new TimeInterval("2015-08-03T:00:00+00:00", "2015-08-03T02:00:00+00:00");
        this.mockMvc.perform(post("/wallet/balance")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeInterval)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInValidTimeInterval2_thenReturns400() throws Exception {
        TimeInterval timeInterval = new TimeInterval("2015-08-04T01:10:00+00:00", "2015-08-04T01:00:00+00:00");
        this.mockMvc.perform(post("/wallet/balance")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeInterval)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenWithinHourTimeInterval_thenReturns404() throws Exception {
        TimeInterval timeInterval = new TimeInterval("2009-08-04T01:10:00+00:00", "2009-08-04T01:20:00+00:00");
        this.mockMvc.perform(post("/wallet/balance")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeInterval)))
                .andExpect(status().isNotFound());
    }

}