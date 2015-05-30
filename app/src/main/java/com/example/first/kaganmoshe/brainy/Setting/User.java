package com.example.first.kaganmoshe.brainy.Setting;

import android.provider.ContactsContract;

/**
 * Created by kaganmoshe on 5/30/15.
 */
public class User {
    // Data Members
    private int m_Id;
    private String m_UserName;
    private String m_FirstName;
    private String m_LastName;

    // C'tor
    public User(int id, String firstName, String lastName){
        m_Id = id;
        m_FirstName = firstName;
        m_LastName = lastName;
        m_UserName = firstName + lastName;
    }

    // Methods
    //TODO - Implements
}
