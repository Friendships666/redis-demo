package org.example.demo;

import java.util.Map;

public class RunReportInfo {

    /**
     * 拼接请求报文
     * @param infoMap 数据信息Map形式（层级关系拼接信息）
     * @return
     */
    public static String getRequestInfo(Map<String, Object> infoMap){
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<soapenv:Envelope xmlns:soapenv=\"http:////schemas.xmlsoap.org/soap/envelope/\" xmlns:pub=\"http:////xmlns.oracle.com/oxp/service/PublicReportService\">");
        builder.append("<soapenv:Header/>");
        builder.append("<soapenv:Body>");
        batchBuilder(builder, infoMap);
        builder.append("</soapenv:Body>");
        builder.append("</soapenv:Envelope>");
        return builder.toString();
    }

    public static String batchBuilder(StringBuilder builder, Map<String, Object> infoMap){
        if(infoMap != null && infoMap.size() > 0){
            for(String key : infoMap.keySet()){
                builder.append("<pub:").append(key).append(">");
                if(infoMap.get(key) instanceof Map){
                    batchBuilder(builder, (Map<String, Object>)infoMap.get(key));
                }else{
                    // 剩下的不需要判断，直接拼接
                    builder.append(infoMap.get(key));
                }
                builder.append("</pub:").append(key).append(">");
            }
        }
        return builder.toString();
    }
}
