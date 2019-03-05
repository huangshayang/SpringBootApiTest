package com.apitest.api.impl;

import com.apitest.api.EnvService;
import com.apitest.entity.Enviroment;
import com.apitest.error.ErrorEnum;
import com.apitest.mapper.EnvMapper;
import com.apitest.util.ExceptionUtil;
import com.apitest.util.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author huangshayang
 */
@Service
@Log4j2
public class EnvServiceImpl implements EnvService {

    private static ServerResponse serverResponse;

    @Resource
    private EnvMapper envMapper;

    @Override
    public ServerResponse addEnvService(Enviroment env) {
        log.info("参数: " + env);
        try {
            if (isBlank(env.getName())) {
                serverResponse = new ServerResponse(ErrorEnum.ENV_NAME_IS_EMPTY.getStatus(), ErrorEnum.ENV_NAME_IS_EMPTY.getMessage());
            } else if (isBlank(env.getDomain())) {
                serverResponse = new ServerResponse(ErrorEnum.ENV_DOMAIN_IS_EMPTY.getStatus(), ErrorEnum.ENV_DOMAIN_IS_EMPTY.getMessage());
            } else if (isBlank(env.getUsername())) {
                serverResponse = new ServerResponse(ErrorEnum.ENV_USERNAME_IS_EMPTY.getStatus(), ErrorEnum.ENV_USERNAME_IS_EMPTY.getMessage());
            } else if (isBlank(env.getPassword())) {
                serverResponse = new ServerResponse(ErrorEnum.ENV_PASSWORD_IS_EMPTY.getStatus(), ErrorEnum.ENV_PASSWORD_IS_EMPTY.getMessage());
            } else if (envMapper.findByName(env.getName()) != null) {
                serverResponse = new ServerResponse(ErrorEnum.ENV_NAME_IS_EXIST.getStatus(), ErrorEnum.ENV_NAME_IS_EXIST.getMessage());
            } else {
                envMapper.save(env);
                serverResponse = new ServerResponse(ErrorEnum.ENV_ADD_SUCCESS.getStatus(), ErrorEnum.ENV_ADD_SUCCESS.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse modifyEnvService(Enviroment env, int id) {
        log.info("参数: " + env);
        try {
            //根据id获取到Environment对象
            Optional<Enviroment> envOptional = envMapper.findById(id);
            //如果对象存在
            if (envOptional.isPresent()) {
                //判断是否能通过参数里的name查找到对象
                Enviroment envByName = envMapper.findByName(env.getName());
                if (isBlank(env.getName())) {
                    serverResponse = new ServerResponse(ErrorEnum.ENV_NAME_IS_EMPTY.getStatus(), ErrorEnum.ENV_NAME_IS_EMPTY.getMessage());
                } else if (isBlank(env.getDomain())) {
                    serverResponse = new ServerResponse(ErrorEnum.ENV_DOMAIN_IS_EMPTY.getStatus(), ErrorEnum.ENV_DOMAIN_IS_EMPTY.getMessage());
                } else if (isBlank(env.getUsername())) {
                    serverResponse = new ServerResponse(ErrorEnum.ENV_USERNAME_IS_EMPTY.getStatus(), ErrorEnum.ENV_USERNAME_IS_EMPTY.getMessage());
                } else if (isBlank(env.getPassword())) {
                    serverResponse = new ServerResponse(ErrorEnum.ENV_PASSWORD_IS_EMPTY.getStatus(), ErrorEnum.ENV_PASSWORD_IS_EMPTY.getMessage());
                } else if (envByName != null && envByName.getId() != id) {
                    //判断name是否不为空，且如果不为空找到的对象的id是否不等于当前传入的id
                    serverResponse = new ServerResponse(ErrorEnum.ENV_NAME_IS_EXIST.getStatus(), ErrorEnum.ENV_NAME_IS_EXIST.getMessage());
                } else {
                    Enviroment enviroment = envOptional.get();
                    enviroment.setName(env.getName());
                    enviroment.setDomain(env.getDomain());
                    enviroment.setUsername(env.getUsername());
                    enviroment.setPassword(env.getPassword());
                    enviroment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    envMapper.update(enviroment, id);
                    serverResponse = new ServerResponse(ErrorEnum.ENV_MODIFY_SUCCESS.getStatus(), ErrorEnum.ENV_MODIFY_SUCCESS.getMessage());
                }
            } else {
                serverResponse = new ServerResponse(ErrorEnum.ENV_IS_NULL.getStatus(), ErrorEnum.ENV_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse queryOneEnvService(int id) {
        try {
            Optional<Enviroment> enviroment = envMapper.findById(id);
            serverResponse = enviroment.map(enviroment1 -> new ServerResponse<>(ErrorEnum.ENV_QUERY_SUCCESS.getStatus(), ErrorEnum.ENV_QUERY_SUCCESS.getMessage(), enviroment1)).orElseGet(() -> new ServerResponse<>(ErrorEnum.ENV_IS_NULL.getStatus(), ErrorEnum.ENV_IS_NULL.getMessage()));
        }catch (Exception e){
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse queryAllEnvService() {
        try {
            List<Enviroment> enviromentList = envMapper.findAllEnv();
            serverResponse = new ServerResponse<>(ErrorEnum.ENV_QUERY_SUCCESS.getStatus(), ErrorEnum.ENV_QUERY_SUCCESS.getMessage(), enviromentList);
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }

    @Override
    public ServerResponse deleteEnvService(int id) {
        try {
            if (envMapper.findById(id).isPresent()) {
                envMapper.deleteById(id);
                serverResponse = new ServerResponse(ErrorEnum.ENV_DELETE_SUCCESS.getStatus(), ErrorEnum.ENV_DELETE_SUCCESS.getMessage());
            } else {
                serverResponse = new ServerResponse(ErrorEnum.ENV_IS_NULL.getStatus(), ErrorEnum.ENV_IS_NULL.getMessage());
            }
        } catch (Exception e) {
            new ExceptionUtil(e);
            return null;
        }
        log.info("返回结果: " + serverResponse);
        log.info("线程名: " + Thread.currentThread().getName() + ",线程id: " + Thread.currentThread().getId() + ",线程状态: " + Thread.currentThread().getState());
        return serverResponse;
    }
}
