package com.snow.xiaoyi.config.init;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.*;

public class MapValueComparator implements Comparator<Map.Entry<RequestMappingInfo, HandlerMethod>> {

    /**
     * 使用 Map按value进行排序
     * @param oriMap
     * @return
     */
    public static Map<RequestMappingInfo, HandlerMethod> sortMapByValue(Map<RequestMappingInfo, HandlerMethod> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<RequestMappingInfo, HandlerMethod> sortedMap = new LinkedHashMap<RequestMappingInfo, HandlerMethod>();
        List<Map.Entry<RequestMappingInfo, HandlerMethod>> entryList = new ArrayList<Map.Entry<RequestMappingInfo, HandlerMethod>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> iter = entryList.iterator();
        Map.Entry<RequestMappingInfo, HandlerMethod> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    @Override
    public int compare(Map.Entry<RequestMappingInfo, HandlerMethod> me1, Map.Entry<RequestMappingInfo, HandlerMethod> me2) {
        return me1.getValue().getMethod().getName().compareTo(me2.getValue().getMethod().getName());
    }
}