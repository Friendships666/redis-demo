package org.example.demo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/4 13:31
 */
public class LoaderSoapXml {

    private static final String CLASS_PATH = "META-INF/smile.properties";

    /**
     * 根据配置文件加载需要加载的xml文件信息
     * @param key 需要加载xml文件的key值
     * @param list 需要按顺序传入的参数信息
     * @return
     * @throws Exception
     */
    public static String loadXml(String key, LinkedList<String> list) throws Exception {
        Properties mappings = PropertiesLoaderUtils.loadAllProperties(CLASS_PATH);
        Map<String, Object> handlerMappings = new ConcurrentHashMap<>(mappings.size());
        CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
        String local = (String) handlerMappings.get(key);
        ClassPathResource classPathResource = new ClassPathResource(local);
        InputStream inputStream = classPathResource.getInputStream();
        inputStream = getInputStream(inputStream);
        byte[] bytes = new byte[0];
        bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String value = new String(bytes, "UTF-8");
        for(int i=0;i<list.size();i++){
            value = value.replace("{"+i+"}", list.get(i));
        }
        return value;
    }


    /**

     * 读取流中前面的字符，看是否有bom，如果有bom，将bom头先读掉丢弃
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(InputStream in) throws IOException {

        PushbackInputStream testin = new PushbackInputStream(in);
        int ch = testin.read();
        if (ch != 0xEF) {
            testin.unread(ch);
        } else if ((ch = testin.read()) != 0xBB) {
            testin.unread(ch);
            testin.unread(0xef);
        } else if ((ch = testin.read()) != 0xBF) {
            throw new IOException("错误的UTF-8格式文件");
        } else {
            // 不需要做，这里是bom头被读完了
            // // System.out.println("still exist bom");

        }
        return testin;
    }



}
