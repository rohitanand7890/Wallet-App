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
public class TimeInterval {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("startDatetime")
    private String startDatetime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("endDatetime")
    private String endDatetime;

}