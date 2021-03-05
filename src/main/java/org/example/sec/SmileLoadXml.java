package org.example.sec;

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
 * @Date 2021/3/5 9:21
 */
public class SmileLoadXml {

    private static final String CLASS_PATH = "META-INF/soap.properties";

    final Map<String, Object> loadMappings = new ConcurrentHashMap<>();
    final Map<String, Object> xmlMappings = new ConcurrentHashMap<>();
    final Map<String, Object> handlerMappings = new ConcurrentHashMap<>();


    public String loadXml(String key, LinkedList<String> list) throws Exception {
        doLoadXml();
        String local = (String) xmlMappings.get(key);
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


    public void doLoadXml(){

        if(xmlMappings.size() > 0 && handlerMappings.size() > 0){
            return;
        }

        Properties mappings = null;
        try {
            mappings = PropertiesLoaderUtils.loadAllProperties(CLASS_PATH);
        } catch (IOException e) {
            throw new RuntimeException("加载SOAP请求配置文件失败");
        }
        loadMappings.clear();
        CollectionUtils.mergePropertiesIntoMap(mappings, loadMappings);

        if(loadMappings.size() > 0){
            xmlMappings.clear();
            handlerMappings.clear();
            for(String k : loadMappings.keySet()){
                String value = (String) loadMappings.get(k);
                String[] values = value.split(",");
                xmlMappings.put(k, values[0]);
                handlerMappings.put(k, values[1]);
            }
        }
    }


    /**

     * 读取流中前面的字符，看是否有bom，如果有bom，将bom头先读掉丢弃
     *
     * @param in
     * @return
     * @throws IOException
     */
    public InputStream getInputStream(InputStream in) throws IOException {

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


    public SmileHandler getInstance(String key) throws Exception{
        String value = (String) handlerMappings.get(key);
        Class<?> clz = Class.forName(value);
        return (SmileHandler) clz.newInstance();
    }

}
