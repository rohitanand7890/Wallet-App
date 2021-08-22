package com.bitcoin.wallet.controller;

import com.bitcoin.wallet.config.MessageConfig;
import com.bitcoin.wallet.domain.TimeInterval;
import com.bitcoin.wallet.domain.Transaction;
import com.bitcoin.wallet.exception.ValidationException;
import com.bitcoin.wallet.domain.SimpleMessageResponse;
import com.bitcoin.wallet.service.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * WalletController encapsulates the logic to handle requests and send responses.
 * Relevant status codes like 200, 400, 404, 500 are returned based on response type.
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping
public class WalletController {
    private final MessageConfig messageConfig;
    private final WalletService walletService;

    @PostMapping("/wallet/deposit")
    public ResponseEntity<SimpleMessageResponse> creditToWallet(@RequestBody final Transaction transaction) {
        log.debug("Transaction credit request: "+ transaction);
        HttpStatus httpStatus = HttpStatus.OK;
        SimpleMessageResponse.SimpleMessageResponseBuilder responseBuilder = SimpleMessageResponse.builder();
        try {
            walletService.creditToWallet(transaction);
            responseBuilder.message(messageConfig.getMessage("messages.success", "Amount credited successfully into wallet"));
        } catch (ValidationException e){
            httpStatus = HttpStatus.BAD_REQUEST;
            responseBuilder.message(messageConfig.getMessage("messages.fail", "BadRequest: " + e.getMessage()));
        } catch (Exception e){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseBuilder.message(messageConfig.getMessage("messages.fail", e.getMessage()));
        }
        SimpleMessageResponse response = responseBuilder.build();
        log.debug("Transaction credit response: "+ response);
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/wallet/balance")
    public ResponseEntity<Object> walletBalanceWithinTimeIntervalByHour(@RequestBody final TimeInterval timeInterval) {
        log.debug("Wallet balance request: "+ timeInterval);
        HttpStatus httpStatus = HttpStatus.OK;
        SimpleMessageResponse.SimpleMessageResponseBuilder responseBuilder = SimpleMessageResponse.builder();
        List<Transaction> listOfBalances = new ArrayList<>();
        try {
             listOfBalances = walletService.getWalletBalanceByHour(timeInterval);
        } catch (ValidationException e){
            httpStatus = HttpStatus.BAD_REQUEST;
            responseBuilder.message(messageConfig.getMessage("messages.fail", "BadRequest: " + e.getMessage()));
            SimpleMessageResponse response = responseBuilder.build();
            return new ResponseEntity<>(response, httpStatus);
        } catch (Exception e){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseBuilder.message(messageConfig.getMessage("messages.fail", e.getMessage()));
            SimpleMessageResponse response = responseBuilder.build();
            return new ResponseEntity<>(response, httpStatus);
        }
        log.debug("Wallet balance response: "+ listOfBalances);

        if(listOfBalances.isEmpty()) {
            httpStatus = HttpStatus.NOT_FOUND;
            responseBuilder.message(messageConfig.getMessage("messages.fail", "No valid hour between the start and end time range"));
            SimpleMessageResponse response = responseBuilder.build();
            return new ResponseEntity<>(response, httpStatus);
        }
        return new ResponseEntity<>(listOfBalances, httpStatus);
    }
}
