package com.bitcoin.wallet.controller;


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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void whenValidInputForWalletDeposit_thenReturns200() throws Exception {
        Transaction transaction = new Transaction("2015-08-04T01:00:00+00:00", 1d);
        System.out.println(mockMvc.getDispatcherServlet());
        System.out.println(objectMapper.clearProblemHandlers());
        this.mockMvc.perform(post("/wallet/deposit", 42L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInValidInputForWalletDeposit_thenReturns400() throws Exception {
        Transaction transaction = new Transaction("2015-08-04T01::00+00:00", 1d);
        System.out.println(mockMvc.getDispatcherServlet());
        System.out.println(objectMapper.clearProblemHandlers());
        this.mockMvc.perform(post("/wallet/deposit", 42L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }


}