package com.teejaynorris.aflashcardapp.model;

import java.util.Date;

/**
 * Created by tjnorris on 10/13/15.
 */
public class UserInfo {
    private String _name;
    private Date _birthday;
    private Long _currentPack;
    private Date _lastAppUse;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Date getBirthday() {
        return _birthday;
    }

    public void setBirthday(Date birthday) {
        _birthday = birthday;
    }

    public Long getCurrentPack() {
        return _currentPack;
    }

    public void setCurrentPack(Long currentPack) {
        _currentPack = currentPack;
    }

    public Date getLastAppUse() {
        return _lastAppUse;
    }

    public void setLastAppUse(Date lastAppUse) {
        _lastAppUse = lastAppUse;
    }
}
