package com.example.icasapp;

import android.util.Log;

import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Firebase.FirebaseHelperKotlin;
import com.example.icasapp.User.Credential;
import com.example.icasapp.User.Privilage;
import com.example.icasapp.User.User;
import com.example.icasapp.User.UserType;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void Privilage() { assertEquals(new Privilage(5).getLevel(), 5); }

    @Test
    public void UserType() { assertEquals(new UserType(2).getType(), "teacher"); }

    // UserOps class testing
    private HashMap<String, String> ActualUser;

    @Before
    public void createActualUSer() {
        ActualUser = new HashMap<String, String>();
            ActualUser.put("name", "Pranav");
            ActualUser.put("semester", "1");
            ActualUser.put("stream", "CSE");
    }
}