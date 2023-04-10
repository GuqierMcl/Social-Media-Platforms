package com.thirteen.smp.response;

/**
 * 用于封装Ajax响应数据的类
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
public class ResponseBean {

    /**
     * 结果码，成功=1，失败=0
     */
    private int code;
    /**
     * 显示信息，成功则显示"success"，失败则显示失败原因。
     */
    private String msg;
    /**
     * 数据对象，用于储存响应数据对象
     */
    private Object data;

    public ResponseBean() {
    }

    public ResponseBean(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
