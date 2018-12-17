package com.longshihan.module_dandu.mvp.model.entity;

/**
 * @author longshihan
 * @time 2017/9/4 15:21
 * @des
 */

public class LeftMenuEvent {
    public String msg;
    public int code;

    public LeftMenuEvent(String msg) {
        this.msg = msg;
    }

    public LeftMenuEvent(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }

    @Override
    public String toString() {
        return "msg:"+msg+",code:"+code;
    }
}
