package pes.gogreenapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.util.List;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.Exceptions.UserNotExistException;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.Utils.MySQLiteHelper;
import pes.gogreenapp.Utils.UserData;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

@RunWith(AndroidJUnit4.class)
public class SQLiteTest {

    private final String username = "user";
    private List<String> usernames;
    private List<Integer> ids;
    private User user;
    private Integer id;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Rule
    public TestName testName = new TestName();

    /**
     * Setup the database to test mode and insert a user
     */
    @Before
    public void setUp() {

        MySQLiteHelper.changeToTestDatabase(myActivityRule.getActivity().getApplicationContext());

        try {
            UserData.createUser(username, "token", 0, "role",0, myActivityRule.getActivity().getApplicationContext());

            if (testName.getMethodName().equals("checkGetUsernames") ||
                    testName.getMethodName().equals("checkGetIds")) {
                UserData.createUser(username + "X", "token", 0, "role",0,
                        myActivityRule.getActivity().getApplicationContext());
                UserData.createUser(username + "Y", "token", 0, "role",0,
                        myActivityRule.getActivity().getApplicationContext());
                UserData.createUser(username + "Z", "token", 0, "role",0,
                        myActivityRule.getActivity().getApplicationContext());
            }
        } catch (NullParametersException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Clean the insert done on setUp method
     */
    @After
    public void clean() {

        MySQLiteHelper.changeToDevelopmentDatabase(myActivityRule.getActivity().getApplicationContext());
    }

    /**
     * Check if the data inserted on the setUp method exists
     */
    @Test
    public void checkCreateUser() {

        try {
            user = UserData.getUserByUsername(username, myActivityRule.getActivity().getApplicationContext());
        } catch (NullParametersException | UserNotExistException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(username, user.getUsername());
    }

    /**
     * Check if the user deleted on the setUp method doesn't exists
     */
    @Test(expected = UserNotExistException.class)
    public void checkDeleteUser() throws NullParametersException, UserNotExistException {

        UserData.deleteUser(username, myActivityRule.getActivity().getApplicationContext());
        UserData.getUserByUsername(username, myActivityRule.getActivity().getApplicationContext());
    }

    /**
     * Check the getter method by username that returns a user instance of the user inserted on the setUp method
     */
    @Test
    public void checkGetUserByUsername() {

        try {
            user = UserData.getUserByUsername(username, myActivityRule.getActivity().getApplicationContext());
        } catch (NullParametersException | UserNotExistException e) {
            System.out.println(e.getMessage());
        }

        assertNotNull(user);
    }

    /**
     * Check the getter method by username that returns a user id of the user inserted on the setUp method
     */
    @Test
    public void checkGetUserIdByUsername() {

        try {
            id = UserData.getUserIdByUsername(username, myActivityRule.getActivity().getApplicationContext());
        } catch (NullParametersException | UserNotExistException e) {
            System.out.println(e.getMessage());
        }

        Integer userId = 1;
        assertEquals(id, userId);
    }

    /**
     * Check the getter method getUsernames returns a usernames list that has the size expected
     */
    @Test
    public void checkGetUsernames() {

        try {
            usernames = UserData.getUsernames(myActivityRule.getActivity().getApplicationContext(), username);
        } catch (NullParametersException e) {
            System.out.println(e.getMessage());
        }

        int usernamesSize = 3;
        assertEquals(usernamesSize, usernames.size());


    }

    /**
     * Check the getter method getIds returns a ids list that has the size expected
     */
    @Test
    public void checkGetIds() {

        try {
            ids = UserData.getIds(myActivityRule.getActivity().getApplicationContext(), username);
        } catch (NullParametersException e) {
            System.out.println(e.getMessage());
        }

        int idsSize = 3;
        assertEquals(idsSize, ids.size());
    }

    /**
     * Check if throws the UserNotExistException when the getUserByUsername() method is called with a inexistet username
     * on the database
     *
     * @throws UserNotExistException exception for test
     */
    @Test(expected = UserNotExistException.class)
    public void userNotExistsOnGetUserByUsername() throws NullParametersException, UserNotExistException {

        UserData.getUserByUsername("notexists", myActivityRule.getActivity().getApplicationContext());
    }

    /**
     * Check if throws the UserNotExistException when the getUserIdByUsername() method is called with a inexistet
     * username on the database
     *
     * @throws UserNotExistException exception for test
     */
    @Test(expected = UserNotExistException.class)
    public void userNotExistsOnGetUserIdByUsername() throws NullParametersException, UserNotExistException {

        UserData.getUserIdByUsername("notexists", myActivityRule.getActivity().getApplicationContext());
    }

    /**
     * Check if throws the NullParametersException when the insertUser() method is called with null parameters
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void nullParametersOnInsertData() throws NullParametersException {

        UserData.createUser(null, null, null, null, null, null);
    }

    /**
     * Check if throws the NullParametersException when the getUsernames() method is called with null parameters
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void nullParametersOnGetUsernames() throws NullParametersException, UserNotExistException {

        UserData.getUsernames(null, null);
    }

    /**
     * Check if throws the NullParametersException when deleteUser() method is called with null parameters
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void nullParametersOnDeleteUser() throws NullParametersException {

        UserData.deleteUser(null, null);
    }

    /**
     * Check if throws the NullParametersException when the getIds() method is called with null parameters
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void nullParametersOnGetIds() throws NullParametersException, UserNotExistException {

        UserData.getIds(null, null);
    }

    /**
     * Check if throws the NullParametersException when the getUserByUsername() method is called with null parameters
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void nullParametersOnGetUserByUsername() throws NullParametersException, UserNotExistException {

        UserData.getUserByUsername(null, null);
    }

    /**
     * Check if throws the NullParametersException when the getUserIdByUsername() method is called with null parameters
     *
     * @throws NullParametersException exception for test
     */
    @Test(expected = NullParametersException.class)
    public void nullParametersOnGetUserIdByUsername() throws NullParametersException, UserNotExistException {

        UserData.getUserIdByUsername(null, null);
    }

}
