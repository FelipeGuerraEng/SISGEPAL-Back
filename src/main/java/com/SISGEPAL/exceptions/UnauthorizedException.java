package com.SISGEPAL.exceptions;

import lombok.Data;

@Data
public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message) {
        super(message);
    }
}
