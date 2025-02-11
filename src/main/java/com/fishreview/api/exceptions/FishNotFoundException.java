package com.fishreview.api.exceptions;

public class FishNotFoundException extends RuntimeException {
    private static final long serialVerisionUID = 1;

    public FishNotFoundException(String message) {
        super(message);
    }
}
