package com.hust.agriculture.cache;

public class HCacheEntry<V> {
  
    /**
     * The time this entry was created.
     */
    private long cacheCreated = -1;

    /**
     * The time this emtry was last updated.
     */
    private long cacheUpdated = -1;
    
    private final V data;
    
    public HCacheEntry(V data){
        this.data = data;
        cacheCreated = System.currentTimeMillis();
        cacheUpdated = System.currentTimeMillis();
    }
   
    public long getCacheCreated() {
    	return cacheCreated;
    }
    
    /**
    * cap nhat cacechUpdated de khi expire khong bi xoa du lieu, do day la ban
    * ghi lay thuong xuyen
    */
    public V getData(){
        cacheUpdated = System.currentTimeMillis();
        return data;
    }
    
    /**
     * neu chi lay du lieu thoi ma ko cap nhat cacheUpdated, thi 
     * ham expire se xoa du lieu nay di sau thoi gian timeout
     */
    public V getDataOnly(){
        return data;
    }
    
    public long getCacheUpdated(){
        return cacheUpdated;
    }
}
