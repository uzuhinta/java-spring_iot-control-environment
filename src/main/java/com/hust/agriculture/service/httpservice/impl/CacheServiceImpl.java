package com.hust.agriculture.service.httpservice.impl;

import com.hust.agriculture.cache.HCache;
import com.hust.agriculture.model.AppParam;
import com.hust.agriculture.payload.request.DataMQTT;
import com.hust.agriculture.repository.AppParamRepository;
import com.hust.agriculture.service.httpservice.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CachingService {

    private static final HCache<String, AppParam> CACHE_APP_PARAM =
            new HCache<String, AppParam>("CACHE_APP_PARAM", 100, HCache.NO_MAX_CAPACITY, HCache.NO_TIMEOUT);
    private static final Object LOCK_APP_PARAM = new Object();

    private static final HCache<String, DataMQTT> CACHE_DATA_DEVICE =
            new HCache<String, DataMQTT>("CACHE_DATA_DEVICE", 100, HCache.NO_MAX_CAPACITY, HCache.NO_TIMEOUT);
    private static final Object LOCK_DATA_DEVICE = new Object();

    @Autowired
    AppParamRepository appParamRepository;

    @Override
    public void cacheData(DataMQTT dataMQTT) {
        synchronized (LOCK_DATA_DEVICE){
            CACHE_DATA_DEVICE.put(dataMQTT.getToken(), dataMQTT);
        }
    }

    @Override
    public void clearCacheData() {
        synchronized (LOCK_DATA_DEVICE){
            CACHE_DATA_DEVICE.clear();
        }
    }

    @Override
    public AppParam getParam(String key) {
        synchronized (LOCK_APP_PARAM) {
            AppParam appParam = CACHE_APP_PARAM.get(key);
            if (appParam != null) {
                return appParam;
            }

            appParam = appParamRepository.findByCode(key);

            if(appParam != null){
                CACHE_APP_PARAM.put(appParam.getCode(), appParam);
            }

            return appParam;
        }
    }

    @Override
    public void clearCacheParam() {
        synchronized (LOCK_APP_PARAM){
            CACHE_APP_PARAM.clear();
        }
    }
}
