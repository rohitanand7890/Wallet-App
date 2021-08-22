package com.bitcoin.wallet.util;

import com.bitcoin.wallet.domain.WalletBalance;
import com.bitcoin.wallet.domain.TimeInterval;
import com.bitcoin.wallet.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class WalletBalanceCalculator {

    /**
     * Utility to fetch the wallet balance every hour between a time interval
     *
     * @param timeInterval  encapsulating start and end time
     * @param balanceByTime the wallet balance recorded at the transactions before the end time
     * @return the wallet balance at every hour between start and end time
     * @throws ValidationException
     */
    public Map<LocalDateTime, Double> getWalletBalancePerHour(TimeInterval timeInterval, List<WalletBalance> balanceByTime) throws ValidationException {
        LocalDateTime startDateTime = DateTimeUtil.toLocalDateTime(timeInterval.getStartDatetime());
        LocalDateTime endDateTime = DateTimeUtil.toLocalDateTime(timeInterval.getEndDatetime());

        Map<LocalDateTime, Double> walletBalanceByTime = new HashMap<>();
        HashSet<LocalDateTime> buckets = new HashSet<>();
        while (!startDateTime.isAfter(endDateTime)) {
            LocalDateTime currentHour = startDateTime.truncatedTo(ChronoUnit.HOURS);
            if (currentHour.equals(startDateTime)) {
                buckets.add(currentHour);
            } else {
                LocalDateTime plusOneHour = currentHour.plusHours(1);
                if (!plusOneHour.isAfter(endDateTime)) {
                    buckets.add(plusOneHour);
                }
            }
            startDateTime = startDateTime.plusHours(1);
        }

        log.debug("Time buckets:" + buckets);

        buckets.stream().forEach(bucket -> {
            Optional<Double> max = balanceByTime.stream().filter(b -> !DateTimeUtil.toLocalTimeFromDB(b.getDateTime()).isAfter(bucket)).map(b -> b.getBalance()).max(Comparator.comparingDouble(Double::valueOf));
            walletBalanceByTime.put(bucket, max.orElse(0d));
        });

        log.debug("Wallet balance per hour: " + walletBalanceByTime);
        return walletBalanceByTime;
    }
}