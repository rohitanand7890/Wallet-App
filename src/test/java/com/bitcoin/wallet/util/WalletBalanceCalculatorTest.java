package com.bitcoin.wallet.util;

import com.bitcoin.wallet.domain.WalletBalance;
import com.bitcoin.wallet.domain.TimeInterval;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.*;

public final class WalletBalanceCalculatorTest {

    List<WalletBalance> walletBalances = new ArrayList<>(
            Arrays.asList(
                    WalletBalance.builder()
                            .balance(11.0)
                            .dateTime("2015-08-03 22:00:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(12.0)
                            .dateTime("2015-08-04 01:00:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(13.0)
                            .dateTime("2015-08-04 01:30:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(14.0)
                            .dateTime("2015-08-04 01:40:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(15.0)
                            .dateTime("2015-08-04 01:45:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(16.0)
                            .dateTime("2015-08-04 02:00:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(17.0)
                            .dateTime("2015-08-04 04:00:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(18.0)
                            .dateTime("2015-08-04 04:30:00.0")
                            .build(),
                    WalletBalance.builder()
                            .balance(19.0)
                            .dateTime("2015-08-04 05:30:00.0")
                            .build()));

    @Test
    public void testWalletBalanceForDifferentScenarios() throws Exception {
        WalletBalanceCalculator walletBalanceCalculator = new WalletBalanceCalculator();

        Map<LocalDateTime, Double> balancePerHour = walletBalanceCalculator.getWalletBalancePerHour(new TimeInterval("2015-08-03T20:30:00Z", "2015-08-03T23:00:00Z"), walletBalances);
        assertEquals(balancePerHour.size(), 3);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-03T21:00:00Z")), 0.0, 0);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-03T22:00:00Z")), 11.0, 0);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-03T23:00:00Z")), 11.0, 0);

        balancePerHour = walletBalanceCalculator.getWalletBalancePerHour(new TimeInterval("2015-08-04T01:00:00Z", "2015-08-04T02:00:00Z"), walletBalances);
        assertEquals(balancePerHour.size(), 2);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-04T01:00:00Z")), 12.0, 0);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-04T02:00:00Z")), 16.0, 0);

        balancePerHour = walletBalanceCalculator.getWalletBalancePerHour(new TimeInterval("2015-08-04T01:30:00Z", "2015-08-04T01:45:00Z"), walletBalances);
        assertEquals(balancePerHour.size(), 0);

        balancePerHour = walletBalanceCalculator.getWalletBalancePerHour(new TimeInterval("2015-08-04T01:30:00Z", "2015-08-04T02:00:00Z"), walletBalances);
        assertEquals(balancePerHour.size(), 1);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-04T02:00:00Z")), 16.0, 0);


        balancePerHour = walletBalanceCalculator.getWalletBalancePerHour(new TimeInterval("2015-08-04T01:00:00Z", "2015-08-04T01:45:00Z"), walletBalances);
        assertEquals(balancePerHour.size(), 1);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-04T01:00:00Z")), 12.0, 0);

        balancePerHour = walletBalanceCalculator.getWalletBalancePerHour(new TimeInterval("2015-08-04T01:30:00Z", "2015-08-04T04:30:00Z"), walletBalances);
        assertEquals(balancePerHour.size(), 3);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-04T02:00:00Z")), 16.0, 0);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-04T03:00:00Z")), 16.0, 0);
        assertEquals(balancePerHour.get(DateTimeUtil.toLocalDateTime("2015-08-04T04:00:00Z")), 17.0, 0);
    }
}
