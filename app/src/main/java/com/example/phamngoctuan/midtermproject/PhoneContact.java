package com.example.phamngoctuan.midtermproject;

/**
 * Created by phamngoctuan on 26/04/2016.
 */
public class PhoneContact {
    String name;
    String number;
    PhoneContactCallback callback;

    PhoneContact(String na, String nu, PhoneContactCallback cb)
    {
        name = na;
        number = nu;
         callback = cb;
    }

    public void Call()
    {
        callback.OnPhoneContactCallback(number);
    }
}
