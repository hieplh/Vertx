package com.example.hiep.user;

import com.example.hiep.helper.Utils;
import java.io.Serializable;
import java.util.Objects;

public class Info implements Serializable {

    private String index;
    private String fullname;
    private String shortname;
    private String birthday;
    private String role;
    private String experience;
    private String avatar;
    private String cv;

    public Info() {
    }

    public Info(String index, String fullname, String birthday,
            String role, String experience, String avatar, String cv) {
        this.index = index;
        this.fullname = fullname;
        this.birthday = birthday;
        this.role = role;
        this.experience = experience;
        this.avatar = avatar;
        this.cv = cv;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String fullname) {
        this.shortname = Utils.getShortenName(fullname, false);
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExperience() {
        return experience;
    }

    public String getCv() {
        return cv;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    @Override
    public String toString() {
        return "Info{" + "index=" + index + ", fullname=" + fullname + ", birthday=" + birthday + ", role=" + role + ", experience=" + experience + ", avatar=" + avatar + ", cv=" + cv + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Info other = (Info) obj;
        return Objects.equals(this.index, other.index);
    }
}
