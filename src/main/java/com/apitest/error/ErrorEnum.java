package com.apitest.error;

import lombok.Getter;

/**
 * 错误种类
 * @author huangshayang
 */
public enum ErrorEnum {
    //接口id不存在
    API_IS_NULL(10000, "接口id不存在"),
    //查询接口返回成功
    API_QUERY_SUCCESS(1, "查询接口成功"),
    API_ADD_SUCCESS(1, "添加接口成功"),
    API_URL_IS_EMPTY(10003, "接口地址不能为空"),
    API_COOKIE_IS_EMPTY(10005, "cookie选择不能为空"),
    API_DELETE_SUCCESS(1, "接口删除成功"),
    API_MODIFY_SUCCESS(1, "修改接口成功"),
    API_IS_REPEAT(10013, "接口地址和方法重复"),
    API_NOTE_IS_EMPTY(10017, "接口名不能为空"),
    USERNAME_OR_PASSWORD_IS_EMPTY(10004, "用户名或密码不能为空"),
    USER_IS_EXIST(10009, "用户已存在，不能重复注册"),
    TOKEN_IS_EMPTY(10015, "token不能为空"),
    TOKEN_IS_ERROR(10016, "token错误"),
    RESET_PASSWORD_SUCCESS(1, "重置密码成功"),
    USER_OR_PASSWORD_ERROR(10007, "用户名或密码错误"),
    USER_IS_NOT_EXISTS(10014, "用户不存在，请先注册"),
    EMAIL_IS_ERROR(10018, "邮箱错误，请检查"),
    EMAIL_SEND_SUCCESS(1, "邮件发送成功，请查收"),
    LOGIN_SUCCESS(1, "登录成功"),
    REGISTER_SUCCESS(1, "注册成功"),
    AUTH_FAILED(10008, "未登录"),
    MODIFY_CASE_SUCCESS(1, "修改用例成功"),
    CASE_IS_NULL(10010, "用例id不存在"),
    API_METHOD_IS_EMPTY(10011, "接口方法不能为空"),
    LOGOUT_SUCCESS(1, "退出成功"),
    CASE_QUERY_SUCCESS(1, "查询用例成功"),
    CASE_DELETE_SUCCESS(1, "删除用例成功"),
    ADD_CASE_SUCCESS(1, "添加用例成功"),
    HTTP_EXEC_SUCCESS(1, "http请求执行成功"),
    LOG_QUERY_SUCCESS(1, "日志查询成功"),
    LOG_DELETE_SUCCESS(1, "删除日志成功"),
    LOG_IS_NULL(10012, "日志id不存在"),
    PARAMETER_ERROR(10002, "参数传递错误"),
    CAPTCHA_ERROR(10001, "验证码错误"),
    PASSWORD_IS_EMPTY(10019, "密码不能为空");

    @Getter
    private int status;

    @Getter
    private String message;

    ErrorEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
