/*
  All right reserverd to GoBros Devevelopers team.
  This code is free software; you can redistribute it and/or modify itunder the terms of
  the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Exceptions;

/**
 * @author Albert
 */
public class UserNotExistException extends Exception {

    /**
     * Empty Constructor
     */
    public UserNotExistException() {

    }

    /**
     * Creates a new User Not Exist Exception with a message
     *
     * @param message message of the Exception
     */
    public UserNotExistException(String message) {

        super(message);
    }
}