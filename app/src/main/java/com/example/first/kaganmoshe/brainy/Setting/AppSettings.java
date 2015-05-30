package com.example.first.kaganmoshe.brainy.Setting;

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

    public EHeadSetType getHeadSetType(){
        return m_HeadSetType;
    }
}
