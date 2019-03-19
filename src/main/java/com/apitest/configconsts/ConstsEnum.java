package com.apitest.configconsts;

import lombok.Getter;

public enum ConstsEnum {
    /**
     * 常量名
     */
    RESET_SUBJECT("密码重置邮件"),
    REGESTER_SUBJECT("注册邮件"),
    USERSESSION_KEY("user_session");

    @Getter
    private String consts;

    ConstsEnum(String consts) {
        this.consts = consts;
    }
}
