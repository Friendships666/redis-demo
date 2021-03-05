package org.example.demo;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import sun.misc.IOUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestUtils {

    private static final String RUN_EXPORT_HTTP = "https://ekjc-test.fa.ap1.oraclecloud.com/xmlpserver/services/PublicReportService?wsdl=";

    public static String getRequestInfo(Map<String, Object> infoMap){
        StringBuilder builder = new StringBuilder();
        builder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pub=\"http://xmlns.oracle.com/oxp/service/PublicReportService\">");
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

    public static void main(String[] args) throws UnsupportedEncodingException, MalformedURLException {
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
        String value = getRequestInfo(runReportMap);
        System.out.println(value);


        // 创建httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        // 创建get请求
//        value = URLEncoder.encode(value, "UTF-8");
//        HttpGet httpGet = new HttpGet(RUN_EXPORT_HTTP + value);
//        //接收返回值信息
//        HttpResponse response = null;
//        try{
//            response = doGet(RUN_EXPORT_HTTP, value);
//            if(response != null){
//                if(response.getStatusLine().getStatusCode() == 200){
//                    //返回数据成功
//                    value = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//                    System.out.println("httpClient-get请求不带参数：" + value);
//                }
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//
//        }
    }


    public static HttpResponse doGet(String host, String value)
            throws Exception {
        HttpClient httpClient = wrapClient(host);
        value = URLEncoder.encode(value, "UTF-8");
        HttpGet request = new HttpGet(host + value);
        return httpClient.execute(request);
    }


    /**
     * 获取 HttpClient
     * @param host
     * @return
     */
    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (host != null && host.startsWith("https://")) {
            return sslClient();
        }else if (StringUtils.isBlank(host)) {
            return sslClient();
        }
        return httpClient;
    }

    /**
     * 在调用SSL之前需要重写验证方法，取消检测SSL
     * 创建ConnectionManager，添加Connection配置信息
     * @return HttpClient 支持https
     */
    private static HttpClient sslClient() {
        try {
            // 在调用SSL之前需要重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
                @Override public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override public void checkClientTrusted(X509Certificate[] xcs, String str) {}
                @Override public void checkServerTrusted(X509Certificate[] xcs, String str) {}
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[] { trustManager }, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            // 创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https",socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig).build();
            return closeableHttpClient;
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 将结果转换成JSONObject
     * @param httpResponse
     * @return
     * @throws IOException
     */
    public static String get(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        String resp = EntityUtils.toString(entity, "UTF-8");
        EntityUtils.consume(entity);
        return resp;
    }


}
