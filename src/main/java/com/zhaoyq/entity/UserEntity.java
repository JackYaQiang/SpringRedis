package com.zhaoyq.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String sex;
    private String age;
    private String id;
    private String telephone;

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
