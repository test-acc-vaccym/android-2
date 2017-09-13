package com.example.yasin.ndklearn;

/**
 * Created by Yasin on 2016/3/2.
 */
public class Person {

    String name,add;
    int phone;

    public Person(String name,String add,int phone){
        this.name = name ;
        this.add = add;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }


}

