package com.apitest.error;

import lombok.Getter;

/**
 * 错误种类
 *
 * @author huangshayang
 */
public enum ErrorEnum {
    /*
     * 接口返回成功
     */
    API_QUERY_SUCCESS(1, "查询接口成功"),
    API_ADD_SUCCESS(1, "添加接口成功"),
    API_DELETE_SUCCESS(1, "接口删除成功"),
    API_MODIFY_SUCCESS(1, "修改接口成功"),
    RESET_PASSWORD_SUCCESS(1, "重置密码成功"),
    EMAIL_SEND_SUCCESS(1, "邮件发送成功，请查收"),
    LOGIN_SUCCESS(1, "登录成功"),
    REGISTER_SUCCESS(1, "注册成功"),
    CASE_MODIFY_SUCCESS(1, "修改用例成功"),
    CASE_QUERY_SUCCESS(1, "查询用例成功"),
    CASE_DELETE_SUCCESS(1, "删除用例成功"),
    ADD_CASE_SUCCESS(1, "添加用例成功"),
    LOG_QUERY_SUCCESS(1, "日志查询成功"),
    LOG_DELETE_SUCCESS(1, "删除日志成功"),
    LOGOUT_SUCCESS(1, "退出成功"),
    ENV_ADD_SUCCESS(1, "创建环境成功"),
    ENV_QUERY_SUCCESS(1, "查询环境成功"),
    ENV_DELETE_SUCCESS(1, "删除环境成功"),
    ENV_MODIFY_SUCCESS(1, "修改环境成功"),
    TASK_QUERY_SUCCESS(1, "查询任务成功"),
    TASK_ADD_SUCCESS(1, "添加任务成功"),
    TASK_DELETE_SUCCESS(1, "删除任务成功"),
    QUARTZ_START_SUCCESS(1, "调度任务开始成功"),
    QUARTZ_PAUSE_SUCCESS(1, "调度任务暂停成功"),
    QUARTZ_STOP_SUCCESS(1, "调度任务停止成功"),
    QUARTZ_RESUME_SUCCESS(1, "调度任务恢复成功"),

    /*
     * 接口返回失败
     */
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
    CASE_AVAILABLE_IS_EMPTY(10020, "用例是否可用不能为空"),
    ENV_NAME_IS_EMPTY(10021, "环境的名字不能为空"),
    ENV_DOMAIN_IS_EMPTY(10022, "环境的域名参数不能为空"),
    ENV_USERNAME_IS_EMPTY(10023, "环境的用户名参数不能为空"),
    ENV_PASSWORD_IS_EMPTY(10024, "环境的密码参数不能为空"),
    ENV_NAME_IS_EXIST(10025, "环境名已经存在"),
    ENV_IS_NULL(10026, "环境id不存在"),
    TASK_IS_NULL(10027, "任务id不存在"),
    TASK_NAME_IS_EMPTY(10028, "任务名不能为空"),
    TASK_NAME_IS_EXIST(10029, "任务已经存在"),
    TASK_APIS_IS_EMPTY(10032, "任务关联的api不能为空"),
    TASK_TIME_IS_EMPTY(10033, "任务的时间不能为空"),
    TASK_TIME_IS_INVALID(10034, "任务的时间格式不正确"),
    QUARTZ_IS_NONE(10036, "调度任务不存在"),
    QUARTZ_IS_PAUSED(10037, "调度任务已经暂停"),
    QUARTZ_IS_RUNNING(10038, "调度任务已经在运行中"),
    QUARTZ_NEED_PAUSE(10039, "请先暂停调度任务"),
    QUARTZ_UNKNOWN_ERROR(10040, "quartz未知错误"),
    CASE_IS_REPEAT(10041, "用例重复"),
    CASE_RESULT_IS_EMPTY(10042, "用例期望结果不能为空");

    @Getter
    private int status;

    @Getter
    private String message;

    ErrorEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
