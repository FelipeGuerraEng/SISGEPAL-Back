package com.SISGEPAL.DTO.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
}
