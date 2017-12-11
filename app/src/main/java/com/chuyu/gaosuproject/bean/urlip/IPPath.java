package com.chuyu.gaosuproject.bean.urlip;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 杨仕俊
 * @description 用于保存ip地址，防止app闪退后 shareprefrence  清空
 * Created by wo on 2017/12/11.
 */
@Entity
public class IPPath {
    /**
     * 主键自增
     */
    @Id(autoincrement = true)
    Long id;
    @NotNull
    String ip;
    @NotNull
    String port;

    @Generated(hash = 69419640)
    public IPPath(Long id, @NotNull String ip, @NotNull String port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    @Generated(hash = 1108445592)
    public IPPath() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
