/*
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Exceptions;

/**
 * @author Albert
 */

public class NullParametersException extends Exception {

    /**
     * Empty Constructor
     */
    public NullParametersException() {

    }

    /**
     * Creates a new NullParameter Exception with a message
     *
     * @param message message of the Exception
     */
    public NullParametersException(String message) {
        super(message);
    }
}
