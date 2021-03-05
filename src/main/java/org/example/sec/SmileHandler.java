package org.example.sec;

import org.apache.commons.lang3.StringUtils;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/5 9:09
 */
public abstract class SmileHandler implements ParseXmlHandler{


    public Object parse(String value){

        String context = parseXml(value);
        if(StringUtils.isBlank(context)){
            throw new RuntimeException("没有获取请求返回的xml数据");
        }

        // 查看是否需要数据解密处理
        context = decoder(context);

        return parseXmlToObject(context);
    }
    
    
}
