package com.bitcoin.wallet.repository;

import com.bitcoin.wallet.domain.WalletBalance;
import com.bitcoin.wallet.domain.TimeInterval;
import com.bitcoin.wallet.domain.Transaction;
import com.bitcoin.wallet.exception.ValidationException;
import com.bitcoin.wallet.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

/** WalletRepository contains the necessary logic to interact with the Postgres */

@Slf4j
@Repository
@AllArgsConstructor
public class WalletRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    private final String INSERT_AMOUNT =
            new StringBuilder()
                    .append("INSERT INTO ")
                    .append("transaction ")
                    .append("( ")
                    .append("  amount, ")
                    .append("  datetime, ")
                    .append("  current_balance ")
                    .append(") ")
                    .append("VALUES ")
                    .append("( ")
                    .append("  :amount, ")
                    .append("  :datetime, ")
                    .append("(SELECT (CASE WHEN max(current_balance) IS NULL THEN 0 ELSE max(current_balance) END) FROM transaction) + :amount")
                    .append(") ")
                    .toString();

    private final String SELECT_ALL_ACCOUNT_BALANCES =
            new StringBuilder()
                    .append("SELECT ")
                    .append("  current_balance, ")
                    .append("  datetime ")
                    .append("FROM ")
                    .append("transaction ")
                    .append("WHERE ")
                    .append("datetime <= :datetime ")
                    .append("ORDER ")
                    .append("BY ")
                    .append(" datetime ")
                    .toString();



    public void creditToWallet(final Transaction transaction) throws ValidationException {
        SqlParameterSource transactionParam = new MapSqlParameterSource()
                        .addValue("amount", transaction.getAmount())
                        .addValue("datetime", DateTimeUtil.toLocalDateTime(transaction.getDateTime()));
        jdbcTemplate.update(INSERT_AMOUNT, transactionParam);
    }

    public List<WalletBalance> getWalletBalanceByHour(TimeInterval timeInterval) throws ValidationException {
        LocalDateTime startDateTime = DateTimeUtil.toLocalDateTime(timeInterval.getStartDatetime());
        LocalDateTime endDateTime = DateTimeUtil.toLocalDateTime(timeInterval.getEndDatetime());

        if(startDateTime.isAfter(endDateTime)) {
            throw new ValidationException("startDatetime must not be after endDatetime");
        }

        List<WalletBalance> walletBalanceByTime = new ArrayList<>();
        log.debug("End date time: " + endDateTime);

        SqlParameterSource transactionParam = new MapSqlParameterSource().addValue("datetime", endDateTime);
        SqlRowSet transactionRowSet = jdbcTemplate.queryForRowSet(SELECT_ALL_ACCOUNT_BALANCES, transactionParam);

        while (transactionRowSet.next()) {
            WalletBalance walletBalance = WalletBalance.builder()
                            .balance(transactionRowSet.getDouble("current_balance"))
                            .dateTime(transactionRowSet.getString("datetime"))
                            .build();
            walletBalanceByTime.add(walletBalance);

        }
        log.debug("Wallet balance by time: " + walletBalanceByTime);
        return walletBalanceByTime;
    }
}
