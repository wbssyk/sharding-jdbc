package com.example.shardingjdbc.entity;

import java.util.Date;

/**
 * @program: sharding-jdbc
 * @description: log
 * @author: yaKun.shi
 * @create: 2019-09-26 10:03
 **/
public class Log {

    private String message;

    private Date createtime;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
