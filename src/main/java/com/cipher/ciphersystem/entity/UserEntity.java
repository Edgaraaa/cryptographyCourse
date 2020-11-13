package com.cipher.ciphersystem.entity;

import java.io.Serializable;
import java.util.Date;

public class UserEntity implements Serializable {
    private Long id;
    private String guid;
    private String name;
    private String age;
    private Date createTime;

    public void setId(Long valueOf) {
        this.id=valueOf;
    }

    public void setGuid(String valueOf) {
        this.guid=valueOf;
    }

    public void setName(String zhangsan) {
        this.name=zhangsan;
    }

    public void setAge(String valueOf) {
        this.age=valueOf;
    }

    public void setCreateTime(Date date) {
        this.createTime=date;
    }
}
