package com.xzs.shareelectric_second.entity;

/**
 * Created by Lenovo on 2018/1/18.
 */

public class UserEntity extends BaseEntity{

    private String uid;
    private String username;
    private String phone;
    private String password;
    private String nickname;
    private String birthday;
    private String hongbao;
    private String youhuiquan;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHongbao() {
        return hongbao;
    }

    public void setHongbao(String hongbao) {
        this.hongbao = hongbao;
    }

    public String getYouhuiquan() {
        return youhuiquan;
    }

    public void setYouhuiquan(String youhuiquan) {
        this.youhuiquan = youhuiquan;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
