package org.example.demo;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author liutao
 * @Date 2021/3/1 19:34
 * @Descript
 */
public class Dom4jParseXml {

    private static final String NODE_NAME_G_1 = "G_1";
    private static final String NODE_NAME_G_2 = "G_2";

    private static final Map<String, Object> parses = new ConcurrentHashMap<>();

    public static void parse(String data) throws Exception {
        String value = decoder(data);
        System.out.println("解密后的数据信息：" + value);
        clearParses();
        InputStream inputStream = new ByteArrayInputStream(value.getBytes("UTF-8"));
        InputSource inputSource = new InputSource(inputStream);
        DocumentLoader documentLoader = new DefaultDocumentLoader();
        Document document = documentLoader.loadDocument(inputSource, null, null,1, true);
        Element element = document.getDocumentElement();
        NodeList nl = element.getChildNodes();
        List<Map<String, Object>> parsesList = new LinkedList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if(node instanceof  Element){
                Element ele = (Element) node;
                // 开始解析G_1节点
                if(NODE_NAME_G_1.equals(ele.getNodeName())){
                    Map<String, Object> g1Map = parseG_1(ele);
                    parsesList.add(g1Map);
                    parses.put(ele.getNodeName(), parsesList);
                }else{
                    parses.put(ele.getNodeName(), ele.getTextContent());
                }
            }
        }
    }


    public static void dom4jParse(String data) throws Exception {
        String value = decoder(data);
        System.out.println("解密后的数据信息：" + value);
        clearParses();
        org.dom4j.Document document = DocumentHelper.parseText(value);
        org.dom4j.Element rootElement = document.getRootElement();
        List<org.dom4j.Element> elements = rootElement.elements();
        List<Map<String, Object>> parsesList = new LinkedList<>();
        for (Iterator<?> its = elements.iterator(); its.hasNext();) {
            org.dom4j.Element ele = (org.dom4j.Element) its.next();
            if("G_1".equals(ele.getName())){
                // 可以解析了
                Map<String, Object> g1Map = dom4jParseG_1(ele);
                parsesList.add(g1Map);
                parses.put(ele.getName(), parsesList);
            }else{
                parses.put(ele.getName(), ele.getText());
            }
        }

    }

    public static Map<String, Object>  dom4jParseG_1(org.dom4j.Element element){
        Map<String, Object> g1Map = new HashMap<>();
        List<Map<String, Object>> g2List = new LinkedList<>();
        List<org.dom4j.Element> elements = element.elements();
        for (Iterator<?> its = elements.iterator(); its.hasNext();) {
            org.dom4j.Element ele = (org.dom4j.Element) its.next();
            if("G_2".equals(ele.getName())){
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
    public static Map<String, Object>  dom4jParseG_2(org.dom4j.Element element){
        Map<String, Object> g2Map = new HashMap<>();
        List<org.dom4j.Element> elements = element.elements();
        for (Iterator<?> its = elements.iterator(); its.hasNext();) {
            org.dom4j.Element ele = (org.dom4j.Element) its.next();
            g2Map.put(ele.getName(), ele.getText());
        }
        return g2Map;
    }




    /**
     * 开始解析G_1节点信息
     * @param element G_1节点
     * @return
     */
    public static Map<String, Object>  parseG_1(Element element){
        Map<String, Object> g1Map = new HashMap<>();
        List<Map<String, Object>> g2List = new LinkedList<>();
        NodeList childNodes = element.getChildNodes();
        if(childNodes != null && childNodes.getLength() > 0){
            for(int i = 0; i < childNodes.getLength(); i++){
                Node g1Node = childNodes.item(i);
                if(g1Node instanceof  Element){
                    Element ele = (Element) g1Node;
                    // 开始解析G_2节点
                    if(NODE_NAME_G_2.equals(ele.getNodeName())){
                        Map<String, Object> g2Map = parseG_2(ele);
                        g2List.add(g2Map);
                        g1Map.put(ele.getNodeName(), g2List);
                    }else{
                        g1Map.put(ele.getNodeName(), ele.getTextContent());
                    }
                }
            }
        }

        return g1Map;
    }

    /**
     * 开始解析G_2节点信息
     * @param element G_2节点信息
     * @return
     */
    public static Map<String, Object>  parseG_2(Element element){
        Map<String, Object> g2Map = new HashMap<>();
        NodeList childNodes = element.getChildNodes();
        if(childNodes != null && childNodes.getLength() > 0){
            for(int i = 0; i < childNodes.getLength(); i++){
                Node g1Node = childNodes.item(i);
                if(g1Node instanceof  Element){
                    Element ele = (Element) g1Node;
                    g2Map.put(ele.getNodeName(), ele.getTextContent());
                }
            }
        }
        return g2Map;
    }



    /**
     * 解析xml
     * @param node 节点
     * @param nodeMap  父节点数据
     */
    public static void  parse(Node node, Map<String, Object> nodeMap){
        NodeList childNodes = node.getChildNodes();
        // 判断父类是否存在，若是不存在则进行新增
        if(node.getParentNode() != null){
            if(nodeMap == null){
                nodeMap = new ConcurrentHashMap<>();
                parses.put(node.getParentNode().getNodeName(), nodeMap);
                nodeMap = (Map<String, Object>) parses.get(node.getParentNode().getNodeName());
            }
        }

        Map<String, Object> obj = (Map<String, Object>) nodeMap.get(node.getNodeName());
        if(obj == null){
            obj = new ConcurrentHashMap<>();
            nodeMap.put(node.getNodeName(), obj);
            obj = (Map<String, Object>) nodeMap.get(node.getNodeName());
        }

        for (int j = 0; j < childNodes.getLength(); j++) {
            Node g1Node = childNodes.item(j);
            if("G_2".equals(g1Node.getNodeName())){
                //开始解析
                if(g1Node instanceof Element){
                    parse(g1Node, nodeMap);
                }
            }else{
                if(g1Node instanceof Element){
                    obj.put(g1Node.getNodeName(), g1Node.getTextContent());
                }
            }
        }
    }

    public static Map<String, Object> getParses(){
        return parses;
    }


    public static void clearParses(){
        parses.clear();
    }


    public String encode(String value){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(value.getBytes());
    }

    public static String decoder(String value) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return new String(decoder.decodeBuffer(value), StandardCharsets.UTF_8);
    }



}
