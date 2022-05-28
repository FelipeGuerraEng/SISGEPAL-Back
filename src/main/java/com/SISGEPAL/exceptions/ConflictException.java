package com.SISGEPAL.exceptions;

import lombok.Data;

@Data
public class ConflictException extends Exception{
    public ConflictException(String message) {
        super(message);
    }
}
