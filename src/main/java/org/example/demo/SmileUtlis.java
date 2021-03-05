package org.example.demo;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

public class SmileUtlis<T> {


    /**
     * map数据信息转Object
     * @param clz
     * @param infoMap
     * @return
     */
    public T mapToObject(Class<?> clz, Map<String, Object> infoMap) throws IllegalAccessException {
        return mapToObject(true, clz, infoMap);
    }

    /**
     * map数据信息转Object
     * @param flag 默认使用驼峰命名方式
     * @param clz
     * @param infoMap
     * @return
     */
    public T mapToObject(Boolean flag, Class<?> clz, Map<String, Object> infoMap) throws IllegalAccessException {

        if(infoMap == null || infoMap.size() == 0){
            return null;
        }

        T entity = null;
        try {
            entity = (T) clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        Field[] fields = clz.getDeclaredFields();
        String name = "";
        for(Field field : fields){
            field.setAccessible(true);
            // 默认使用驼峰命名的方式
            if(flag){
                name = field.getName();
            }else{
                name = field.getName().toUpperCase();
            }
            Object value = infoMap.get(name);
            if(value != null){
                Class<?> type = field.getType();
                if(type == Integer.class){
                    value = Integer.valueOf((String) value);
                }else if(type == Long.class){
                    value = Long.valueOf((String) value);
                }else if(type == BigDecimal.class){
                    value = new BigDecimal((String) value);
                }
                field.set(entity, value);
            }
        }
        return entity;
    }

}
