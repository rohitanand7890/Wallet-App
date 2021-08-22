package com.bitcoin.wallet.service;

import com.bitcoin.wallet.domain.WalletBalance;
import com.bitcoin.wallet.domain.TimeInterval;
import com.bitcoin.wallet.domain.Transaction;
import com.bitcoin.wallet.exception.ValidationException;
import com.bitcoin.wallet.repository.WalletRepository;
import com.bitcoin.wallet.util.WalletBalanceCalculator;
import com.bitcoin.wallet.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** WalletService forms the service layer or the middleware between the controller and the data access layer. */

@Slf4j
@Service
@AllArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletBalanceCalculator walletBalanceCalculator;

    public void creditToWallet(final Transaction transaction) throws ValidationException {
        walletRepository.creditToWallet(transaction);
    }

    public List<Transaction> getWalletBalanceByHour(TimeInterval timeInterval) throws ValidationException {
        List<WalletBalance> listOfWalletBalance = walletRepository.getWalletBalanceByHour(timeInterval);
        log.debug("List of wallet balances: " + listOfWalletBalance);
        Map<LocalDateTime, Double> getBalancePerHour = walletBalanceCalculator.getWalletBalancePerHour(timeInterval, listOfWalletBalance);

        List<Transaction> transactions = new ArrayList<>();
        getBalancePerHour.entrySet().stream().forEach(entry -> {
            transactions.add(new Transaction(DateTimeUtil.localTimeToString(entry.getKey()), entry.getValue()));
        });

        log.debug("Wallet Balance by time: " + listOfWalletBalance);
        return transactions;
    }
}
