package com.example.icasapp;

import android.util.Log;

import com.example.icasapp.User.Credential;
import com.example.icasapp.User.Privilage;
import com.example.icasapp.User.User;
import com.example.icasapp.User.UserType;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void User() {
        assertEquals(new User(Credential.getCredential(), new Privilage(5)).getPrivilage(), 5);
    }
}