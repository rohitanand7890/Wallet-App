package com.bitcoin.wallet.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@ToString
@Builder
@EqualsAndHashCode
@Getter
@Setter
public class SimpleMessageResponse {
    @JsonProperty("message")
    private String message;

}
