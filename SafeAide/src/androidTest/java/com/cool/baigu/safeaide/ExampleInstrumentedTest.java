package com.cool.baigu.safeaide;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.cool.baigu.safeaide.beans.BlackContacts;
import com.cool.baigu.safeaide.dao.Impl.BlackListDAO;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.cool.baigu.safeaide", appContext.getPackageName());
    }

    @Test
    public void kaka() {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest("123456".getBytes());
            String result = "";
            for (byte b : bytes) {
//                String temp = Integer.toHexString(b & 0xff);
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            Log.d(TAG, "kaka: " + result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dbTest() {

        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackListDAO dao = new BlackListDAO(appContext);
//        dao.remove(new BlackContacts(0, "10086", "0"));
        dao.add(new BlackContacts(0, "18961856065", "0"));
        dao.add(new BlackContacts(0, "17685324587", "1"));
        dao.add(new BlackContacts(0, "13337852649", "2"));
//        String s = dao.get(new BlackContacts(0, "100861", "1"));



    }

}
