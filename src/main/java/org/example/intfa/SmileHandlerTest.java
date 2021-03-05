package org.example.intfa;

import org.hamcrest.beans.PropertyUtil;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SmileHandlerTest extends SmileHandlerSupport {

    private final String CLASS_PARSE = "org.example.intfa.SmileNameHandler";
    private final String CLASS_PATH = "META-INF/smile.properties";
    private Map<String, Object>  handlerMappings = null;

    @Test
    public void test3(){
        String[] args = new String[]{"smileScan", "smileContext"};
        try {
            Class cls = Class.forName(CLASS_PARSE);
            SmileHandler smileHandler = (SmileHandler) BeanUtils.instantiateClass(cls);
            smileHandler.init();
            for(String name : args){
                smileHandler.parse(name);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4(){
        try {
            Properties mappings = PropertiesLoaderUtils.loadAllProperties(CLASS_PATH);
            handlerMappings = new ConcurrentHashMap<>(mappings.size());
            CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
            test5("runReport");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    public void test5(String value) throws Exception {
        String local = (String) handlerMappings.get(value);
        //InputStream in = ClassLoader.getSystemResourceAsStream(local);
        //File file = new File(Thread.currentThread().getContextClassLoader().getResource("runReport.xml").getFile());
        ClassPathResource classPathResource = new ClassPathResource(local);
        InputStream inputStream = classPathResource.getInputStream();
        byte[] bytes = new byte[0];
        bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String str = new String(bytes, "UTF-8");
        System.out.println(str);
    }
}
