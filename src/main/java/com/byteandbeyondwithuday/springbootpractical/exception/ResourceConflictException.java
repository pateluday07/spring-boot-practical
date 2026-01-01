package com.byteandbeyondwithuday.springbootpractical.exception;

public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }

}
