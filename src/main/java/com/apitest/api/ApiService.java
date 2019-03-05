package com.apitest.api;

import com.apitest.entity.Apis;
import com.apitest.util.ServerResponse;

public interface ApiService {
    ServerResponse addApiService(Apis api);

    ServerResponse queryAllApiService(int page, int size);

    ServerResponse queryOneApiService(int id);

    ServerResponse modifyApiService(int id, Apis api);

    ServerResponse deleteApiService(int id);
}
