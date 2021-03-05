package org.example.sec;

import java.util.ArrayList;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/5 10:32
 */
public class ThankHandler extends SmileHandler{

    @Override
    public String parseXml(String value) {
        return "ThankHandler";
    }

    @Override
    public String decoder(String value) {
        return value;
    }

    @Override
    public Object parseXmlToObject(String value) {
        return new ArrayList<>();
    }
}
