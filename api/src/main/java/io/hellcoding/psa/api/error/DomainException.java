package io.hellcoding.psa.api.error;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
