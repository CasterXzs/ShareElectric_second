package com.xzs.shareelectric_second.entity;

/**
 * Created by Lenovo on 2018/1/19.
 */

public class UserRecordEntity extends BaseEntity {

    private String phone;
    private String usedtime;
    private String usedmoney;
    private String machinenumber;

    public UserRecordEntity(){}

    public UserRecordEntity(String usedtime,String usedmoney,String machinenumber){
        this.usedtime=usedtime;
        this.usedmoney=usedmoney;
        this.machinenumber=machinenumber;
    }

    public UserRecordEntity(String phone,String usedtime,String usedmoney,String machinenumber){
        this.phone=phone;
        this.usedtime=usedtime;
        this.usedmoney=usedmoney;
        this.machinenumber=machinenumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsedtime() {
        return usedtime;
    }

    public void setUsedtime(String usedtime) {
        this.usedtime = usedtime;
    }

    public String getUsedmoney() {
        return usedmoney;
    }

    public void setUsedmoney(String usedmoney) {
        this.usedmoney = usedmoney;
    }

    public String getMachinenumber() {
        return machinenumber;
    }

    public void setMachinenumber(String machinenumber) {
        this.machinenumber = machinenumber;
    }


}
