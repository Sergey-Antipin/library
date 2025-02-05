package com.antipin.library.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super("Not found entity with id: " + id);
    }
}

