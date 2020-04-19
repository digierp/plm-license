package com.digiwin.plm.plmlicense.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liuhao
 * @date 2020/4/18
 */
public class RegisterInfo {

    @NotBlank(message = "MAC地址不能为空")
    private String mac;

    @NotBlank(message = "开始时间不能为空")
    private String startDate;

    @NotBlank(message = "到期时间不能为空")
    private String endDate;

    @NotBlank(message = "用户数量不能为空")
    @Length(max = 4, min = 4, message = "用户长度不对，需要4为的十六进制数，如：0003")
    private String users;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "mac='" + mac + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", users='" + users + '\'' +
                '}';
    }
}
