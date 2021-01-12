package com.ankoki.elementals.utils.exceptions;

public class IdInUseException extends Exception {

    public IdInUseException() {
        super("This ID is already being used by another spell!");
    }
}
