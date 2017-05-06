package pes.gogreenapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.util.List;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.Utils.ActualUserData;

/**
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

@RunWith(AndroidJUnit4.class)
public class SQLiteTest {
    private final String username = "UserTest";
    private List<String> usernames;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Rule
    public TestName testName = new TestName();

    /**
     * Setup for the checkDataInsert and checkDeletedUser tests
     */
    @Before
    public void beforeTests() {
        if (testName.getMethodName().equals("checkDataInsert") ||
                testName.getMethodName().equals("checkDeletedUserTest")) {
            try {
                ActualUserData.createUser(username, "", "", 0,
                        myActivityRule.getActivity().getApplicationContext());
                if (testName.getMethodName().equals("checkDeletedUserTest")) {
                    ActualUserData.deleteUser(username,
                            myActivityRule.getActivity().getApplicationContext());
                }
                usernames = ActualUserData.getUsernames(
                        myActivityRule.getActivity().getApplicationContext(), "");
            } catch (NullParametersException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Clean for the checkDataInsert test
     */
    @After
    public void afterTests() {
        if (testName.getMethodName().equals("checkDataInsert")) {
            try {
                ActualUserData.deleteUser(username,
                        myActivityRule.getActivity().getApplicationContext());
            } catch (NullParametersException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void checkDataInsert() {
        Assert.assertEquals(true, usernames.contains(username));
    }

    @Test
    public void checkDeletedUserTest() {
        Assert.assertEquals(false, usernames.contains(username));
    }

    /**
     * Check if it is throwed the NullParametersException on insert data
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void exceptionOnInsertData() throws NullParametersException {
        ActualUserData.createUser(null, null, null, null, null);
    }

    /**
     * Check if it is throwed the NullParametersException getUsernames() method
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void exceptionOnGetUsernames() throws NullParametersException {
        ActualUserData.getUsernames(null, null);
    }

    /**
     * Check if it is throwed the NullParametersException on delete data
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void exceptionOnDeleteUser() throws NullParametersException {
        ActualUserData.deleteUser(null, null);
    }
}
