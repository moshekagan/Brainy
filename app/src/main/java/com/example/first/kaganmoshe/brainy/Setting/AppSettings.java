package com.example.first.kaganmoshe.brainy.Setting;

import android.util.Log;

import EEG.EHeadSetType;

/**
 * Created by kaganmoshe on 5/30/15.
 */
public class AppSettings {
    private final int GustID = -1;

    // Data Members
    private User m_CurrentUser = new User(GustID, "Gust", "Gust"); // Default gust user;
    private ELanguage m_Language = ELanguage.ENG; // English is default
    private EHeadSetType m_HeadSetType = EHeadSetType.MindWave; // MindWaveMobile is default
    private boolean m_UseingHeadSet = true; // Default is to use headset!!
    private boolean m_IsChangedHeadSetType = false;
    private boolean m_FirstConnection = true;

    // C'tor
    public AppSettings(){
    }

    // Methods
    public boolean isUsingHeadSet(){
        return m_UseingHeadSet;
    }

    public void setUsingHeadSet(boolean usingHeadSetVal){
        m_UseingHeadSet = usingHeadSetVal;
    }

    public void setHeadSetType(EHeadSetType type){
        if (type != m_HeadSetType)
            m_IsChangedHeadSetType = true;
        else
           m_IsChangedHeadSetType = false;

        if (m_FirstConnection){
            m_FirstConnection = false;
            m_IsChangedHeadSetType = true;
        }

        m_HeadSetType = type;
        Log.i("HEADSET_SET", type.toString());
    }

    public EHeadSetType getHeadSetType(){
        return m_HeadSetType;
    }

    public boolean isChangedHeadSetType() { return m_IsChangedHeadSetType; }
}
