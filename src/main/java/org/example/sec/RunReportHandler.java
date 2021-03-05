package org.example.sec;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.example.demo.SOAPConstans;
import org.example.demo.SmileUtlis;
import org.example.pojo.RequisitionImportHeadDTO;
import org.example.pojo.RequisitionImportLineDTO;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/5 9:14
 */
public class RunReportHandler extends SmileHandler {

    @Override
    public String parseXml(String value) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(value);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //根节点
        Element root = document.getRootElement();
        //子节点
        List<Element> childElements = root.elements();
        Map<String,Object> mapEle = new HashMap<String, Object>();
        //遍历子节点
        mapEle = getAllElements(childElements, mapEle);
        value = (String) mapEle.get("reportBytes");
        return value;
    }

    /**
     * 节点下的子节点
     * @param childElements
     * @param mapEle
     * @return
     */
    private static Map<String, Object> getAllElements(List<Element> childElements,Map<String,Object> mapEle) {
        for (Element ele : childElements) {
            mapEle.put(ele.getName(), ele.getText());
            if(ele.elements().size()>0){
                mapEle = getAllElements(ele.elements(), mapEle);
            }
        }
        return mapEle;
    }

    @Override
    public String decoder(String value) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = new byte[1024];
        try {
            bytes = decoder.decodeBuffer(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public Object parseXmlToObject(String value) {
        try {
            Dom4jParseXml.dom4jParse(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> parsesMap = Dom4jParseXml.getParses();
        List<RequisitionImportHeadDTO> allList = null;
        if(parsesMap != null && parsesMap.size() > 0) {
            // 获取G_1节点下的数据信息
            List<Map<String, Object>> g1List = (List<Map<String, Object>>) parsesMap.get(SOAPConstans.NODE_NAME_G_1);
            if(g1List != null && g1List.size() > 0){
                allList = new ArrayList<>();
                SmileUtlis<RequisitionImportHeadDTO> headUtil = new SmileUtlis<RequisitionImportHeadDTO>();
                for(Map<String, Object> g1Map : g1List){
                    if (g1Map != null) {
                        // 获取到的数据信息字段都是大写，需要进行反射进行赋值，不同的数据类型需要特殊处理
                        // 获取G_2节点下的数据信息
                        List<RequisitionImportLineDTO> lineList = null;
                        List<Map<String, Object>> g2List = (List<Map<String, Object>>) g1Map.get(SOAPConstans.NODE_NAME_G_2);
                        if(g2List != null && g2List.size() > 0){
                            SmileUtlis<RequisitionImportLineDTO> lineUtil = new SmileUtlis<RequisitionImportLineDTO>();
                            lineList = new ArrayList<>(g2List.size());
                            for(Map<String, Object> g2Map : g2List){
                                if (g2Map != null) {
                                    RequisitionImportLineDTO lineDTO = null;
                                    try {
                                        lineDTO = lineUtil.mapToObject(false, RequisitionImportLineDTO.class, g2Map);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    if(lineDTO != null){
                                        lineList.add(lineDTO);
                                    }
                                }
                            }
                        }

                        //先进行赋值G2的数据信息，在进行赋值G_1的数据信息
                        RequisitionImportHeadDTO headDTO = null;
                        try {
                            headDTO = headUtil.mapToObject(false, RequisitionImportHeadDTO.class, g1Map);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        headDTO.setLineDTOS(lineList);
                        allList.add(headDTO);
                    }
                }
            }
        }
        return allList;
    }
}
