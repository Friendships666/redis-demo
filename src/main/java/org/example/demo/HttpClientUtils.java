package org.example.demo;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.example.pojo.RequisitionImportHeadDTO;
import org.example.pojo.RequisitionImportLineDTO;
import org.example.pojo.RunReportDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * httpclient工具的使用方式
 */
public class HttpClientUtils {

    private static final String RUN_REPORT_HTTP = "https://ekjc-test.fa.ap1.oraclecloud.com/xmlpserver/services/PublicReportService/runReport/";
    private static final String CLASS_PATH = "META-INF/smile.properties";

    private static final String DECODER_DATA = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCEtLUdlbmVyYXRlZCBieSBPcmFjbGUgQkkgUHVibGlzaGVyIC1EYXRhZW5naW5lLCBkYXRhbW9kZWw6X0N1c3RvbV9JU1VfQVJfREFUQV9NT0RFTF9BUl9JTlRFUkZBQ0VfeGRtIC0tPgo8REFUQV9EUz48UF9MQVNUX1VQREFURV9EQVRFX0Y+MjAyMS0wMi0yOFQyMTowNjo1MS4wMDArMDk6MDA8L1BfTEFTVF9VUERBVEVfREFURV9GPjxQX0xBU1RfVVBEQVRFX0RBVEVfVD4yMDIxLTAzLTAzVDIwOjU0OjEyLjAwMCswOTowMDwvUF9MQVNUX1VQREFURV9EQVRFX1Q+CjxHXzE+CjxPUkdBTklaQVRJT05JRD4zMDAwMDAwMDU3MzMyMjE8L09SR0FOSVpBVElPTklEPjxDT01QQU5ZQ09ERT5JQklERU4gU1VaSE9VPC9DT01QQU5ZQ09ERT48RU1QTE9ZRUVOVU0+SEFORDAyPC9FTVBMT1lFRU5VTT48SEVBRFNFUVVFTkNFPjMwMDAwMDAwNjMwODUxODwvSEVBRFNFUVVFTkNFPjxSRVFVRVNUVFlQRT5TQUxFU19JTlZPSUNFPC9SRVFVRVNUVFlQRT48RVhUTlVNQkVSPjEyMzQ1Njc4PC9FWFROVU1CRVI+PFJFQ0VJUFROQU1FPuWuieW+veWui+axn+W6n+aXp+eJqei1hOWbnuaUtuaciemZkOWFrOWPuDwvUkVDRUlQVE5BTUU+PFJFQ0VJUFRUQVhOTz45MTM0MDEwME1BMk4zNE1COTg8L1JFQ0VJUFRUQVhOTz48UkVDRUlQVEFERFJFU1NQSE9ORT7lkIjogqXluILnkbbmtbfljLrlkIjoo5Xot6/lpKflhbTplYfmlL/lupzljZfot6/kvqc05bmiNTA4IDwvUkVDRUlQVEFERFJFU1NQSE9ORT48UkVDRUlQVFRZUEU+MDE8L1JFQ0VJUFRUWVBFPjxJTlZPSUNFVFlQRT4wPC9JTlZPSUNFVFlQRT48QklMTEZMQUc+OTwvQklMTEZMQUc+PFNPVVJDRU5VTUJFUj40PC9TT1VSQ0VOVU1CRVI+CjxHXzI+CjxMSU5FU0VRVUVOQ0U+MTA8L0xJTkVTRVFVRU5DRT48UFJPSkVDVE5VTUJFUj7ph5HnqI7mtYvor5VfMjAyMTAzMDNfMTY1NDwvUFJPSkVDVE5VTUJFUj48UVVBTlRJVFk+MjA8L1FVQU5USVRZPjxQUklDRT4zLjU8L1BSSUNFPjxBTU9VTlQ+NzA8L0FNT1VOVD48VEFYSU5DTFVERURGTEFHPjA8L1RBWElOQ0xVREVERkxBRz48VEFYUkFURT4xMzwvVEFYUkFURT48VEFYQU1PVU5UPjkuMTwvVEFYQU1PVU5UPjxVTklUPmtnPC9VTklUPjxDVVNUT01FUl9UUlhfSUQ+MzAwMDAwMDA2MzA4NTE4PC9DVVNUT01FUl9UUlhfSUQ+CjwvR18yPgo8R18yPgo8TElORVNFUVVFTkNFPjIwPC9MSU5FU0VRVUVOQ0U+PFBST0pFQ1ROVU1CRVI+6YeR56iO5rWL6K+VXzIwMjEwMzAzXzE2NTU8L1BST0pFQ1ROVU1CRVI+PFFVQU5USVRZPjMuNTwvUVVBTlRJVFk+PFBSSUNFPjIwPC9QUklDRT48QU1PVU5UPjcwPC9BTU9VTlQ+PFRBWElOQ0xVREVERkxBRz4wPC9UQVhJTkNMVURFREZMQUc+PFRBWFJBVEU+OTwvVEFYUkFURT48VEFYQU1PVU5UPjYuMzwvVEFYQU1PVU5UPjxVTklUPnNldDwvVU5JVD48Q1VTVE9NRVJfVFJYX0lEPjMwMDAwMDAwNjMwODUxODwvQ1VTVE9NRVJfVFJYX0lEPgo8L0dfMj4KPEdfMj4KPExJTkVTRVFVRU5DRT4zMDwvTElORVNFUVVFTkNFPjxQUk9KRUNUTlVNQkVSPumHkeeojua1i+ivlV8yMDIxMDMwM18xNzAwPC9QUk9KRUNUTlVNQkVSPjxRVUFOVElUWT40MDwvUVVBTlRJVFk+PFBSSUNFPjU8L1BSSUNFPjxBTU9VTlQ+MjAwPC9BTU9VTlQ+PFRBWElOQ0xVREVERkxBRz4wPC9UQVhJTkNMVURFREZMQUc+PFRBWFJBVEU+MDwvVEFYUkFURT48WkVST1RBWFJBVEVGTEFHPjA8L1pFUk9UQVhSQVRFRkxBRz48VEFYQU1PVU5UPjA8L1RBWEFNT1VOVD48VU5JVD5tPC9VTklUPjxDVVNUT01FUl9UUlhfSUQ+MzAwMDAwMDA2MzA4NTE4PC9DVVNUT01FUl9UUlhfSUQ+CjwvR18yPgo8L0dfMT4KPEdfMT4KPE9SR0FOSVpBVElPTklEPjMwMDAwMDAwNTczMzIyMTwvT1JHQU5JWkFUSU9OSUQ+PENPTVBBTllDT0RFPklCSURFTiBTVVpIT1U8L0NPTVBBTllDT0RFPjxFTVBMT1lFRU5VTT5IQU5EMDI8L0VNUExPWUVFTlVNPjxIRUFEU0VRVUVOQ0U+MzAwMDAwMDA2MzA4NTQxPC9IRUFEU0VRVUVOQ0U+PFJFUVVFU1RUWVBFPlNBTEVTX0lOVk9JQ0U8L1JFUVVFU1RUWVBFPjxFWFROVU1CRVI+MTIzNDU2Nzg8L0VYVE5VTUJFUj48UkVDRUlQVE5BTUU+5a6J5b695a6L5rGf5bqf5pen54mp6LWE5Zue5pS25pyJ6ZmQ5YWs5Y+4PC9SRUNFSVBUTkFNRT48UkVDRUlQVFRBWE5PPjkxMzQwMTAwTUEyTjM0TUI5ODwvUkVDRUlQVFRBWE5PPjxSRUNFSVBUQUREUkVTU1BIT05FPuWQiOiCpeW4gueRtua1t+WMuuWQiOijlei3r+Wkp+WFtOmVh+aUv+W6nOWNl+i3r+S+pzTluaI1MDggPC9SRUNFSVBUQUREUkVTU1BIT05FPjxSRUNFSVBUVFlQRT4wMTwvUkVDRUlQVFRZUEU+PElOVk9JQ0VUWVBFPjA8L0lOVk9JQ0VUWVBFPjxCSUxMRkxBRz45PC9CSUxMRkxBRz48U09VUkNFTlVNQkVSPjU8L1NPVVJDRU5VTUJFUj4KPEdfMj4KPExJTkVTRVFVRU5DRT4xMDwvTElORVNFUVVFTkNFPjxQUk9KRUNUTlVNQkVSPumHkeeojua1i+ivlV8yMDIxMDMwM18xNzE0PC9QUk9KRUNUTlVNQkVSPjxRVUFOVElUWT4tMzwvUVVBTlRJVFk+PFBSSUNFPjM0PC9QUklDRT48QU1PVU5UPi0xMDI8L0FNT1VOVD48VEFYSU5DTFVERURGTEFHPjA8L1RBWElOQ0xVREVERkxBRz48VEFYUkFURT4xMzwvVEFYUkFURT48VEFYQU1PVU5UPi0xMy4yNjwvVEFYQU1PVU5UPjxVTklUPmtnPC9VTklUPjxDVVNUT01FUl9UUlhfSUQ+MzAwMDAwMDA2MzA4NTQxPC9DVVNUT01FUl9UUlhfSUQ+CjwvR18yPgo8R18yPgo8TElORVNFUVVFTkNFPjIwPC9MSU5FU0VRVUVOQ0U+PFBST0pFQ1ROVU1CRVI+6YeR56iO5rWL6K+VXzIwMjEwMzAzXzE3Mjc8L1BST0pFQ1ROVU1CRVI+PFFVQU5USVRZPi0xMDwvUVVBTlRJVFk+PFBSSUNFPjEwPC9QUklDRT48QU1PVU5UPi0xMDA8L0FNT1VOVD48VEFYSU5DTFVERURGTEFHPjA8L1RBWElOQ0xVREVERkxBRz48VEFYUkFURT4wPC9UQVhSQVRFPjxaRVJPVEFYUkFURUZMQUc+MDwvWkVST1RBWFJBVEVGTEFHPjxUQVhBTU9VTlQ+MDwvVEFYQU1PVU5UPjxVTklUPnNldDwvVU5JVD48Q1VTVE9NRVJfVFJYX0lEPjMwMDAwMDAwNjMwODU0MTwvQ1VTVE9NRVJfVFJYX0lEPgo8L0dfMj4KPEdfMj4KPExJTkVTRVFVRU5DRT4zMDwvTElORVNFUVVFTkNFPjxQUk9KRUNUTlVNQkVSPumHkeeojua1i+ivlV8yMDIxMDMwM18xNzI0PC9QUk9KRUNUTlVNQkVSPjxRVUFOVElUWT4yPC9RVUFOVElUWT48UFJJQ0U+LTM8L1BSSUNFPjxBTU9VTlQ+LTY8L0FNT1VOVD48VEFYSU5DTFVERURGTEFHPjA8L1RBWElOQ0xVREVERkxBRz48VEFYUkFURT42PC9UQVhSQVRFPjxUQVhBTU9VTlQ+LS4zNjwvVEFYQU1PVU5UPjxVTklUPm08L1VOSVQ+PENVU1RPTUVSX1RSWF9JRD4zMDAwMDAwMDYzMDg1NDE8L0NVU1RPTUVSX1RSWF9JRD4KPC9HXzI+CjwvR18xPgo8R18xPgo8T1JHQU5JWkFUSU9OSUQ+MzAwMDAwMDA1NzMzMjIxPC9PUkdBTklaQVRJT05JRD48Q09NUEFOWUNPREU+SUJJREVOIFNVWkhPVTwvQ09NUEFOWUNPREU+PEVNUExPWUVFTlVNPkhBTkQwMjwvRU1QTE9ZRUVOVU0+PEhFQURTRVFVRU5DRT4zMDAwMDAwMDYzMDg0OTU8L0hFQURTRVFVRU5DRT48UkVRVUVTVFRZUEU+U0FMRVNfSU5WT0lDRTwvUkVRVUVTVFRZUEU+PEVYVE5VTUJFUj4xMjM0NTY3ODwvRVhUTlVNQkVSPjxSRUNFSVBUTkFNRT7lronlvr3lrovmsZ/lup/ml6fnianotYTlm57mlLbmnInpmZDlhazlj7g8L1JFQ0VJUFROQU1FPjxSRUNFSVBUVEFYTk8+OTEzNDAxMDBNQTJOMzRNQjk4PC9SRUNFSVBUVEFYTk8+PFJFQ0VJUFRBRERSRVNTUEhPTkU+5ZCI6IKl5biC55G25rW35Yy65ZCI6KOV6Lev5aSn5YW06ZWH5pS/5bqc5Y2X6Lev5L6nNOW5ojUwOCA8L1JFQ0VJUFRBRERSRVNTUEhPTkU+PFJFQ0VJUFRUWVBFPjAxPC9SRUNFSVBUVFlQRT48SU5WT0lDRVRZUEU+MDwvSU5WT0lDRVRZUEU+PEJJTExGTEFHPjk8L0JJTExGTEFHPjxTT1VSQ0VOVU1CRVI+MzwvU09VUkNFTlVNQkVSPgo8R18yPgo8TElORVNFUVVFTkNFPjEwPC9MSU5FU0VRVUVOQ0U+PFBST0pFQ1ROVU1CRVI+6YeR56iO5oql6KGo5rWL6K+VXzIwMjEwMzAzXzE2NDQ8L1BST0pFQ1ROVU1CRVI+PFFVQU5USVRZPjEwPC9RVUFOVElUWT48UFJJQ0U+MTA8L1BSSUNFPjxBTU9VTlQ+MTAwPC9BTU9VTlQ+PFRBWElOQ0xVREVERkxBRz4wPC9UQVhJTkNMVURFREZMQUc+PFRBWFJBVEU+MTM8L1RBWFJBVEU+PFRBWEFNT1VOVD4xMzwvVEFYQU1PVU5UPjxVTklUPmtnPC9VTklUPjxDVVNUT01FUl9UUlhfSUQ+MzAwMDAwMDA2MzA4NDk1PC9DVVNUT01FUl9UUlhfSUQ+CjwvR18yPgo8L0dfMT4KPC9EQVRBX0RTPg==";

    public static void getMethod(){
        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建get请求
        HttpGet httpGet = new HttpGet("http://localhost:8080/test/getKey");
        //接收返回值信息
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpGet);
            if(response != null){
                if(response.getStatusLine().getStatusCode() == 200){
                    //返回数据成功
                    String value = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    System.out.println("httpClient-get请求不带参数：" + value);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(SmileObjectUtils.isNotEmpty(httpClient)){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void getMethodParams(Map<String,String> infoMap){
        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //接收返回值信息
        CloseableHttpResponse response = null;
        try{
            //get请求带参数
            URI build = new URIBuilder("http://localhost:8080/test/getParamKey").setParameter("key","value").build();
            HttpGet httpGet = new HttpGet(build);
            response = httpClient.execute(httpGet);
            if(response != null){
                if(response.getStatusLine().getStatusCode() == 200){
                    //返回数据成功
                    String value = EntityUtils.toString(response.getEntity(), "utf-8");
                    System.out.println("httpClient-get请求带参数：" + value);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void postMethod(){
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http POST请求
        HttpPost httpPost = new HttpPost("http://www.oschina.net/");
        // 伪装成浏览器
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            // 判断返回状态是否为200
            if(response != null){
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    System.out.println(content);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(httpclient != null){
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static CloseableHttpResponse postMothodParams(String host, String value) throws UnsupportedEncodingException {
        //创建httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建http post
        HttpPost httpPost = new HttpPost(host);
        //模拟浏览器设置头
        httpPost.addHeader("SOAPAction","application/soap+xml");
        httpPost.addHeader("Content-Type","application/xml");

        StringEntity stringEntity = new StringEntity(value);
        //将表达请求放入到httpost
        httpPost.setEntity(stringEntity);

        try {
            return httpClient.execute(httpPost);
//            String content = EntityUtils.toString(response.getEntity(), "utf-8");
//            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**

     * 读取流中前面的字符，看是否有bom，如果有bom，将bom头先读掉丢弃
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(InputStream in) throws IOException {

        PushbackInputStream testin = new PushbackInputStream(in);
        int ch = testin.read();
        if (ch != 0xEF) {
            testin.unread(ch);
        } else if ((ch = testin.read()) != 0xBB) {
            testin.unread(ch);
            testin.unread(0xef);
        } else if ((ch = testin.read()) != 0xBF) {
            throw new IOException("错误的UTF-8格式文件");
        } else {
            // 不需要做，这里是bom头被读完了
            // // System.out.println("still exist bom");

        }
        return testin;
    }


    private static Map<String, Object> getAllElements(List<Element> childElements,Map<String,Object> mapEle) {
        for (Element ele : childElements) {
            mapEle.put(ele.getName(), ele.getText());
            if(ele.elements().size()>0){
                mapEle = getAllElements(ele.elements(), mapEle);
            }
        }
        return mapEle;
    }


    public static void loadXml() throws Exception {
        Properties mappings = PropertiesLoaderUtils.loadAllProperties(CLASS_PATH);
        Map<String, Object> handlerMappings = new ConcurrentHashMap<>(mappings.size());
        CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
        String local = (String) handlerMappings.get("runReport");
        ClassPathResource classPathResource = new ClassPathResource(local);
        InputStream inputStream = classPathResource.getInputStream();
        inputStream = getInputStream(inputStream);
        byte[] bytes = new byte[0];
        bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String value = new String(bytes, "UTF-8");
        // 2021-02-25 12:06:51  2021-03-01 11:54:12
        List<String> list = new LinkedList<String>();
        list.add("2021-01-28 12:06:51");
        list.add("2021-03-03 11:54:12");
        for(int i=0;i<list.size();i++){
            value = value.replace("{"+i+"}", list.get(i));
        }
        //System.out.println(value);
        CloseableHttpResponse response = postMothodParams(RUN_REPORT_HTTP, value);
        if(response != null){
            if(response.getStatusLine().getStatusCode() == 200){
                String context = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(context);

//                inputStream = new ByteArrayInputStream(value.getBytes("UTF-8"));
//                InputSource inputSource = new InputSource(inputStream);
//                DocumentLoader documentLoader = new DefaultDocumentLoader();
//                Document document = documentLoader.loadDocument(inputSource, null, null,1, true);
                // 1、得到DOM解析器的工厂实例
//                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//                // 2、从DOM工厂获得DOM解析器
//                DocumentBuilder db = dbf.newDocumentBuilder();
//                // 3、解析XML文档，得到一个Document，即DOM树
//                Document document =  db.parse(inputSource);
                Document document = DocumentHelper.parseText(context);
                //根节点
                Element root = document.getRootElement();
                //子节点
                List<Element> childElements = root.elements();
                Map<String,Object> mapEle = new HashMap<String, Object>();
                //遍历子节点
                mapEle = getAllElements(childElements,mapEle);
                if(mapEle != null && mapEle.size() > 0){
                    value = (String) mapEle.get("reportBytes");
                    parse(value);
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        // 需要进行请求数据信息
        loadXml();
        // 测试使用的是请求后的数据信息
        //parse(DECODER_DATA);
    }



    public static void parse(String value) throws Exception {
        Dom4jParseXml.dom4jParse(value);
        Map<String, Object> parsesMap = Dom4jParseXml.getParses();
        System.out.println("解析后的数据信息" + parsesMap);
        List<RequisitionImportHeadDTO> listdto = null;
        if(parsesMap != null && parsesMap.size() > 0) {
            // 获取G_1节点下的数据信息
            List<Map<String, Object>> g1List = (List<Map<String, Object>>) parsesMap.get("G_1");
            if(g1List != null && g1List.size() > 0){
                listdto = new ArrayList<>();
                SmileUtlis<RequisitionImportHeadDTO> headUtil = new SmileUtlis<RequisitionImportHeadDTO>();
                for(Map<String, Object> g1Map : g1List){
                    if (g1Map != null) {
                        // 获取到的数据信息字段都是大写，需要进行反射进行赋值，不同的数据类型需要特殊处理
                        // 获取G_2节点下的数据信息
                        List<RequisitionImportLineDTO> lineList = null;
                        List<Map<String, Object>> g2List = (List<Map<String, Object>>) g1Map.get("G_2");
                        if(g2List != null && g2List.size() > 0){
                            SmileUtlis<RequisitionImportLineDTO> lineUtil = new SmileUtlis<RequisitionImportLineDTO>();
                            lineList = new ArrayList<>(g2List.size());
                            for(Map<String, Object> g2Map : g2List){
                                if (g2Map != null) {
                                    RequisitionImportLineDTO lineDTO =  lineUtil.mapToObject(false, RequisitionImportLineDTO.class, g2Map);
                                    if(lineDTO != null){
                                        lineList.add(lineDTO);
                                    }
                                }
                            }
                        }

                        //先进行赋值G2的数据信息，在进行赋值G_1的数据信息
                        RequisitionImportHeadDTO headDTO = headUtil.mapToObject(false, RequisitionImportHeadDTO.class, g1Map);
                        headDTO.setLineDTOS(lineList);
                        listdto.add(headDTO);
                    }
                }

                // 开始进行存放数据库信息
                System.out.println("封装成对象：" + listdto);

            }
        }


        List<RunReportDTO> runList = new ArrayList<>();
        if(listdto != null && listdto.size() > 0){
            Set<Long> organizationIdSet = new HashSet<>();
            Set<String> companyCodeSet = new HashSet<>();
            Set<String> employeeNumberSet = new HashSet<>();
            for(RequisitionImportHeadDTO headDTO : listdto){
                // 开始整合数据信息
                if(headDTO != null){
                    Long organizationId = headDTO.getOrganizationId();
                    String companyCode = headDTO.getCompanyCode();
                    String employeeNumber = headDTO.getEmployeeNum();
                    if(!StringUtils.isAnyBlank(organizationId == null ? null : String.valueOf(organizationId), companyCode, employeeNumber)){
                        //开始整合数据信息
                        // 默认是不存在信息的
                        Boolean IdFlag = false;
                        // 判断是否存在租户Id信息
                        if(organizationIdSet.contains(organizationId)){
                            IdFlag = true;
                        }

                        Boolean codeFlag = false;
                        // 判断是否存在公司编码
                        if(companyCodeSet.contains(companyCode)){
                            codeFlag = true;
                        }

                        Boolean noFlag = false;
                        // 判断是否存在员工编号
                        if(employeeNumberSet.contains(employeeNumber)){
                            noFlag = true;
                        }

                        if(IdFlag && codeFlag && noFlag){
                            // runList 里面已经存在了数据信息
                            if(runList != null && runList.size() > 0){
                                for(RunReportDTO run : runList){
                                    if(organizationId.equals(run.getOrganizationId()) && companyCode.equals(run.getCompanyCode()) && employeeNumber.equals(run.getEmployeeNum())){
                                        List<RequisitionImportHeadDTO> headDTOList = run.getHeadDTOList();
                                        if(headDTOList != null && headDTOList.size() > 0){
                                            headDTOList.add(headDTO);
                                        }
                                    }
                                }
                            }
                        }else{
                            // 开始整合数据
                            organizationIdSet.add(organizationId);
                            companyCodeSet.add(companyCode);
                            employeeNumberSet.add(employeeNumber);
                            RunReportDTO runReportDTO = new RunReportDTO();
                            runReportDTO.setOrganizationId(organizationId);
                            runReportDTO.setCompanyCode(companyCode);
                            runReportDTO.setEmployeeNum(employeeNumber);
                            List<RequisitionImportHeadDTO> headList = new ArrayList<>();
                            headList.add(headDTO);
                            runReportDTO.setHeadDTOList(headList);
                            runList.add(runReportDTO);
                        }
                    }
                }
            }
        }

        // 完成数据的合并
        System.out.println("整合完成后的数据：" + runList);
    }

}
