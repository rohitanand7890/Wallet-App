package com.bitcoin.wallet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;


@Validated
@ToString
@Builder
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("datetime")
    private String dateTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("amount")
    private Double amount;
}
