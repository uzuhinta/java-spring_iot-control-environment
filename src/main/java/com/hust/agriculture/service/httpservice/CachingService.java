package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.model.AppParam;
import com.hust.agriculture.payload.request.DataMQTT;

public interface CachingService {
    
    void cacheData(DataMQTT dataMQTT);
    void clearCacheData();
    AppParam getParam(String key);
    void clearCacheParam();
}
