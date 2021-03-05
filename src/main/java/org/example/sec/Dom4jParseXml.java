package org.example.sec;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.example.demo.SOAPConstans;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/4 13:31
 */
public class Dom4jParseXml {

    private static final Map<String, Object> parses = new ConcurrentHashMap<>();

    /**
     * dom4j进行解析BASE64加密后的xml数据信息
     * @param value BASE64加密后的xml数据信息
     * @throws Exception
     */
    public static void dom4jParse(String value) throws Exception {
        clearParses();
        org.dom4j.Document document = DocumentHelper.parseText(value);
        org.dom4j.Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        List<Map<String, Object>> parsesList = new LinkedList<>();
        for (Iterator<?> its = elements.iterator(); its.hasNext();) {
            org.dom4j.Element ele = (org.dom4j.Element) its.next();
            if(SOAPConstans.NODE_NAME_G_1.equals(ele.getName())){
                // 可以解析了
                Map<String, Object> g1Map = dom4jParseG_1(ele);
                parsesList.add(g1Map);
                parses.put(ele.getName(), parsesList);
            }else{
                parses.put(ele.getName(), ele.getText());
            }
        }

    }


    /**
     * 使用DOM4j对xml数据信息进行解析
     * @param data xml数据信息字符串
     * @return 解析后的map数据信息
     * @throws DocumentException
     */
    public static Map<String, Object> parse(String data) throws DocumentException {
        Document document = DocumentHelper.parseText(data);
        //根节点
        Element root = document.getRootElement();
        //子节点
        List<Element> childElements = root.elements();
        Map<String,Object> mapEle = new HashMap<String, Object>();
        //遍历子节点
        return getAllElements(childElements,mapEle);
    }

    /**
     * 节点下的子节点
     * @param childElements
     * @param mapEle
     * @return
     */
    private static Map<String, Object> getAllElements(List<Element> childElements, Map<String,Object> mapEle) {
        for (Element ele : childElements) {
            mapEle.put(ele.getName(), ele.getText());
            if(ele.elements().size()>0){
                mapEle = getAllElements(ele.elements(), mapEle);
            }
        }
        return mapEle;
    }


    public static Map<String, Object>  dom4jParseG_1(Element element){
        Map<String, Object> g1Map = new HashMap<>();
        List<Map<String, Object>> g2List = new LinkedList<>();
        List<Element> elements = element.elements();
        for (Iterator<?> its = elements.iterator(); its.hasNext();) {
            Element ele = (Element) its.next();
            if(SOAPConstans.NODE_NAME_G_2.equals(ele.getName())){
                // 可以解析了
                Map<String, Object> g2Map = dom4jParseG_2(ele);
                g2List.add(g2Map);
                g1Map.put(ele.getName(), g2List);
            }else{
                g1Map.put(ele.getName(), ele.getText());
            }
        }
        return g1Map;
    }


    /**
     * 开始解析G_2节点信息
     * @param element G_2节点信息
     * @return
     */
    public static Map<String, Object>  dom4jParseG_2(Element element){
        Map<String, Object> g2Map = new HashMap<>();
        List<Element> elements = element.elements();
        for (Iterator<?> its = elements.iterator(); its.hasNext();) {
            Element ele = (Element) its.next();
            g2Map.put(ele.getName(), ele.getText());
        }
        return g2Map;
    }


    public String encode(String value){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(value.getBytes());
    }

    public static String decoder(String value) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return new String(decoder.decodeBuffer(value), StandardCharsets.UTF_8);
    }


    public static Map<String, Object> getParses(){
        return parses;
    }


    public static void clearParses(){
        parses.clear();
    }


}
