package org.example.demo;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SoapUtil {


    public static Map<String,Object> responseSoap(String requestSoap,String serviceAddress,String charSet, String contentType){
        String responseSoap = "";
        Map<String,Object> resultmap=new HashMap<String,Object>();
        PostMethod postMethod = new PostMethod(serviceAddress);
        byte[] b = new byte[0];
        try {
            b = requestSoap.getBytes(charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InputStream is = new ByteArrayInputStream(b, 0, b.length);
        RequestEntity re = new InputStreamRequestEntity(is, b.length, contentType);
        postMethod.setRequestEntity(re);
        HttpClient httpClient = new HttpClient();
        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == 200) {
                try {
                    responseSoap = postMethod.getResponseBodyAsString();
                    resultmap.put("responseSoap", responseSoap);
                } catch (IOException e) {
                    throw new RuntimeException("获取请求返回报文失败", e);
                }
            } else {
                throw new RuntimeException("请求失败：" + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("执行http请求失败", e);
        }
        return resultmap;
    }


    public static void main(String[] args){
        String address = "https://ekjc-test.fa.ap1.oraclecloud.com/xmlpserver/services/PublicReportService?wsdl";
        String charSet="utf-8";
        String contentType="text/xml; charset=utf-8";

        Map<String, Object> bodyMap = new LinkedHashMap<>();
        Map<String, Object> runReportMap = new LinkedHashMap<>();
        Map<String, Object> reportRequestMap = new LinkedHashMap<>();
        Map<String, Object>  parameterNameValues = new LinkedHashMap<>();
        reportRequestMap.put("attributeLocale", "en-US");
        reportRequestMap.put("attributeTemplate", "Default");
        reportRequestMap.put("attributeFormat", "xml");
        parameterNameValues.put("item", "");
        reportRequestMap.put("parameterNameValues", parameterNameValues);
        reportRequestMap.put("reportAbsolutePath", "/Custom/ISU/AR/AR INTERFACE.xdo");
        runReportMap.put("reportRequest", reportRequestMap);
        runReportMap.put("userID", "FIN_IMPL");
        runReportMap.put("password", "Ibiden!619834");
        bodyMap.put("runReport", runReportMap);
        String value = RunReportInfo.getRequestInfo(runReportMap);
        try {
            Document document = DocumentHelper.parseText(value);
            System.out.println(document.asXML());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
//        responseSoap(value, address, charSet, contentType);
    }
}

