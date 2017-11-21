package com.chuyu.gaosuproject.bean;

import java.io.Serializable;

/**
 * Created by wo on 2017/7/20.
 */

public class QrCodeBean implements Serializable{
    private String D;

    private String P;

    private String T;

    public String getT() {
        return T;
    }

    public void setT(String t) {
        T = t;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getP() {
        return P;
    }

    public void setP(String p) {
        P = p;
    }

    @Override
    public String toString() {
        return "QrCodeBean{" +
                "D='" + D + '\'' +
                ", P='" + P + '\'' +
                ", T='" + T + '\'' +
                '}';
    }
}
