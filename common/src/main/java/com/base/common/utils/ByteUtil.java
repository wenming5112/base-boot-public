package com.base.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.*;

public class ByteUtil {


    public static <T> byte[] arrToBytes(List<T> list) {
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(list, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        return JSON.toJSONBytes(array);
    }

    public static <T> Object bytesToArr(byte[] bytes) {
        return JSONArray.parse(bytes);
    }

    /**
     * 对象转json字节
     */
    public static <T> byte[] objToBytes(T t) {
        return JSON.toJSONBytes(t);
    }

    /**
     * 字节转json
     */
    public static JSONObject bytesToJson(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return JSON.parseObject(JSON.parse(bytes) == null
                ? null : JSON.parse(bytes).toString());
    }

    /**
     * 对象转json
     */
    public static <T> JSONObject objToJson(T t) {
        return bytesToJson(objToBytes(t));
    }

    /**
     * 字节转对象
     */
    public static <T> T bytesToObj(byte[] bytes, Class<T> t) {
        return JSON.toJavaObject(bytesToJson(bytes), t);
    }


    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
        sortMap.putAll(map);

        return sortMap;
    }


    /**
     * 计算出需要多少个线程
     *
     * @param pageSize 每个线程处理的数量
     * @param list     数据源
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        int listSize = list.size();
        int page = (listSize + (pageSize - 1)) / pageSize;
        List<List<T>> listArray = new ArrayList<List<T>>();
        for (int i = 0; i < page; i++) {
            List<T> subList = new ArrayList<T>();
            for (int j = 0; j < listSize; j++) {
                int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
                if (pageIndex == (i + 1)) {
                    subList.add(list.get(j));
                }
                if ((j + 1) == ((j + 1) * pageSize)) {
                    break;
                }
            }
            listArray.add(subList);
        }
        return listArray;
    }

}
