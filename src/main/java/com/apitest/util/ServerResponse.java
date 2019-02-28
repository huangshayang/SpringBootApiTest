package com.apitest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author huangshayang
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ServerResponse<T> implements Serializable {

    private int status;
    private String message;
    private T data;

    public ServerResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServerResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
