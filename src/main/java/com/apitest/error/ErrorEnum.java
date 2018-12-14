package com.apitest.error;

import lombok.Getter;

/**
 * 错误种类
 * @author huangshayang
 */
public enum ErrorEnum {
    //查询接口返回成功
    API_QUERY_SUCCESS(1, "查询接口成功"),
    API_ADD_SUCCESS(1, "添加接口成功"),
    API_DELETE_SUCCESS(1, "接口删除成功"),
    API_MODIFY_SUCCESS(1, "修改接口成功"),
    RESET_PASSWORD_SUCCESS(1, "重置密码成功"),
    EMAIL_SEND_SUCCESS(1, "邮件发送成功，请查收"),
    LOGIN_SUCCESS(1, "登录成功"),
    REGISTER_SUCCESS(1, "注册成功"),
    MODIFY_CASE_SUCCESS(1, "修改用例成功"),
    CASE_QUERY_SUCCESS(1, "查询用例成功"),
    CASE_DELETE_SUCCESS(1, "删除用例成功"),
    ADD_CASE_SUCCESS(1, "添加用例成功"),
    HTTP_EXEC_SUCCESS(1, "http请求执行成功"),
    LOG_QUERY_SUCCESS(1, "日志查询成功"),
    LOG_DELETE_SUCCESS(1, "删除日志成功"),
    LOGOUT_SUCCESS(1, "退出成功"),
    //接口id不存在
    API_IS_NULL(10001, "接口id不存在"),
    TOKEN_IS_ERROR(10002, "token错误"),
    PARAMETER_ERROR(10003, "参数传递错误"),
    API_URL_IS_EMPTY(10004, "接口地址不能为空"),
    API_COOKIE_IS_EMPTY(10005, "cookie选择不能为空"),
    USERNAME_OR_PASSWORD_IS_EMPTY(10006, "用户名或密码不能为空"),
    USER_OR_PASSWORD_ERROR(10007, "用户名或密码错误"),
    AUTH_FAILED(10008, "未登录"),
    USER_IS_EXIST(10009, "用户已存在，不能重复注册"),
    CASE_IS_NULL(10010, "用例id不存在"),
    API_METHOD_IS_EMPTY(10011, "接口方法不能为空"),
    LOG_IS_NULL(10012, "日志id不存在"),
    API_IS_REPEAT(10013, "接口地址和方法重复"),
    USER_IS_NOT_EXISTS(10014, "用户不存在，请先注册"),
    API_NOTE_IS_EMPTY(10015, "接口名不能为空"),
    PASSWORD_IS_EMPTY(10016, "密码不能为空"),
    EMAIL_IS_ERROR(10017, "邮箱错误，请检查"),
    REQUEST_NUM_FULL(10018, "请求次数过多，请明天再试"),
    CASE_NOTE_IS_EMPTY(10019, "用例名不能为空"),
    CASE_AVAILABLE_IS_EMPTY(10020, "用例是否可用不能为空");

    @Getter
    private int status;

    @Getter
    private String message;

    ErrorEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
