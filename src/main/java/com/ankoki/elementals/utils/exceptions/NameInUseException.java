package com.ankoki.elementals.utils.exceptions;

public class NameInUseException extends Exception {

    public NameInUseException() {
        super("This spell name is already being used by another spell!");
    }
}
