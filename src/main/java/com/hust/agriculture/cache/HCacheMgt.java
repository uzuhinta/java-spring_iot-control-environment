package com.hust.agriculture.cache;

import java.util.List;
import java.util.Vector;

public class HCacheMgt {
    
    private static final  List<HCache> caches = new Vector();
    
    public synchronized static void register(HCache cache){
        caches.add(cache);
    }
    
    public synchronized static void clear(){
         for (HCache hCache : caches) {
                hCache.clear();
            }
    }

    public synchronized static String statistic() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------- HCache List -------------------------\n");
        for (HCache cache : caches) {
            sb.append(cache.getName()).append("=>")
                    .append(String.valueOf(cache.toString()))
                    .append("\n");
        }
        sb.append("----------------- HCache END -------------------------\n");
        return sb.toString();
    }

    
}
