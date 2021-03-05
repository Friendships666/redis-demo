package org.example.sec;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/5 9:03
 */
public interface ParseXmlHandler {

    // 返回数据进行xml解析，获取到需要处理的数据信息
    String parseXml(String value);

    // 获取到的数据信息是否需要解密
    String decoder(String value);

    // 获取到的xml数据进行解析
    Object parseXmlToObject(String value);

}
