package org.example.demo;

import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/4 13:31
 */
public class BasicsParseXml {

    private static final Map<String, Object> parses = new ConcurrentHashMap<>();

    /**
     * 特定解析xml解析(使用原生的DocumentLoader解析)
     * <DATA_DS>
     *  <G_1>
     *   <G_2>
     *   </G_2>
     *  </G_1>
     * </DATA_DS>
     * @param data BASE64加密xml数据信息
     * @throws Exception
     */
    public static void parseInfo(String data) throws Exception {
        String value = decoder(data);
        InputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        inputStream = LoaderSoapXml.getInputStream(inputStream);
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
                if(SOAPConstans.NODE_NAME_G_1.equals(ele.getNodeName())){
                    Map<String, Object> g1Map = parseG_1(ele);
                    parsesList.add(g1Map);
                    parses.put(ele.getNodeName(), parsesList);
                }else{
                    parses.put(ele.getNodeName(), ele.getTextContent());
                }
            }
        }
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
                    if(SOAPConstans.NODE_NAME_G_2.equals(ele.getNodeName())){
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


    public static Map<String, Object> getParses(){
        return parses;
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
