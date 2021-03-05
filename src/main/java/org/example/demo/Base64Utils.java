package org.example.demo;

import io.netty.handler.codec.base64.Base64Encoder;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.xml.sax.InputSource;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Base64Utils {

    private static final String DATA = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCEtLUdlbmVyYXRlZCBieSBPcmFjbGUgQkkgUHVibGlzaGVyIC1EYXRhZW5naW5lLCBkYXRhbW9kZWw6X0N1c3RvbV9JU1VfQVJfREFUQV9NT0RFTF9BUl9JTlRFUkZBQ0VfeGRtIC0tPgo8REFUQV9EUz4KPEdfMT4KPE9SR0FOSVpBVElPTklEPjMwMDAwMDAwNTMyNDMxMTwvT1JHQU5JWkFUSU9OSUQ+PENPTVBBTllDT0RFPklCSURFTiBTVVpIT1U8L0NPTVBBTllDT0RFPjxFTVBMT1lFRU5VTT5JU1UwMDI8L0VNUExPWUVFTlVNPjxIRUFEU0VRVUVOQ0U+MzAwMDAwMDA1NDUxOTM5PC9IRUFEU0VRVUVOQ0U+PFJFUVVFU1RUWVBFPlNBTEVTX0lOVk9JQ0U8L1JFUVVFU1RUWVBFPjxFWFROVU1CRVI+MTIzNDU2Nzg8L0VYVE5VTUJFUj48UkVDRUlQVE5BTUU+VEVTVF9DVVNUT01FUjwvUkVDRUlQVE5BTUU+PFJFQ0VJUFRBRERSRVNTUEhPTkU+U0hBTkdIQUkgPC9SRUNFSVBUQUREUkVTU1BIT05FPjxSRUNFSVBUVFlQRT4wMTwvUkVDRUlQVFRZUEU+PElOVk9JQ0VUWVBFPjA8L0lOVk9JQ0VUWVBFPjxCSUxMRkxBRz45PC9CSUxMRkxBRz48U09VUkNFTlVNQkVSPjQwMjAwMDAwMjwvU09VUkNFTlVNQkVSPjxSRUNFSVBUQUNDT1VOVD7kuK3lm73lt6XllYbpk7booYwg5byg5a625riv57uP5rWO5byA5Y+R5Yy65pSv6KGMIDEyMzQ1Njc4OTAxPC9SRUNFSVBUQUNDT1VOVD4KPEdfMj4KPExJTkVTRVFVRU5DRT4xMDwvTElORVNFUVVFTkNFPjxQUk9KRUNUTlVNQkVSPkxZMk4tRDIgMjREQzwvUFJPSkVDVE5VTUJFUj48UFJJQ0U+MTA8L1BSSUNFPjxBTU9VTlQ+MTAwPC9BTU9VTlQ+PFRBWElOQ0xVREVERkxBRz4wPC9UQVhJTkNMVURFREZMQUc+PFpFUk9UQVhSQVRFRkxBRz4wPC9aRVJPVEFYUkFURUZMQUc+PFRBWEFNT1VOVD4xMDA8L1RBWEFNT1VOVD48VU5JVD5zZXQ8L1VOSVQ+PENVU1RPTUVSX1RSWF9JRD4zMDAwMDAwMDU0NTE5Mzk8L0NVU1RPTUVSX1RSWF9JRD4KPC9HXzI+CjwvR18xPgo8L0RBVEFfRFM+";

    private static final String XML_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<DATA_DS>\n" +
            "\t<G_1>\n" +
            "\t\t<ORGANIZATIONID>300000005324311</ORGANIZATIONID>\n" +
            "\t\t<COMPANYCODE>IBIDEN SUZHOU</COMPANYCODE>\n" +
            "\t\t<EMPLOYEENUM>ISU002</EMPLOYEENUM>\n" +
            "\t\t<HEADSEQUENCE>300000005451939</HEADSEQUENCE>\n" +
            "\t\t<REQUESTTYPE>SALES_INVOICE</REQUESTTYPE>\n" +
            "\t\t<EXTNUMBER>12345678</EXTNUMBER>\n" +
            "\t\t<RECEIPTNAME>TEST_CUSTOMER</RECEIPTNAME>\n" +
            "\t\t<RECEIPTADDRESSPHONE>SHANGHAI </RECEIPTADDRESSPHONE>\n" +
            "\t\t<RECEIPTTYPE>01</RECEIPTTYPE>\n" +
            "\t\t<INVOICETYPE>0</INVOICETYPE>\n" +
            "\t\t<BILLFLAG>9</BILLFLAG>\n" +
            "\t\t<SOURCENUMBER>402000002</SOURCENUMBER>\n" +
            "\t\t<RECEIPTACCOUNT>中国工商银行 张家港经济开发区支行 12345678901</RECEIPTACCOUNT>\n" +
            "\t\t<G_2>\n" +
            "\t\t\t<LINESEQUENCE>10</LINESEQUENCE>\n" +
            "\t\t\t<PROJECTNUMBER>LY2N-D2 24DC</PROJECTNUMBER>\n" +
            "\t\t\t<PRICE>10</PRICE><AMOUNT>100</AMOUNT>\n" +
            "\t\t\t<TAXINCLUDEDFLAG>0</TAXINCLUDEDFLAG>\n" +
            "\t\t\t<ZEROTAXRATEFLAG>0</ZEROTAXRATEFLAG>\n" +
            "\t\t\t<TAXAMOUNT>100</TAXAMOUNT>\n" +
            "\t\t\t<UNIT>set</UNIT>\n" +
            "\t\t\t<CUSTOMER_TRX_ID>300000005451939</CUSTOMER_TRX_ID>\n" +
            "\t\t</G_2>\n" +
            "\t</G_1>\n" +
            "</DATA_DS>";

    public static String test(String value){
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = null;
        try {
            bytes = decoder.decodeBuffer(DATA);
        } catch (IOException e) {
            e.printStackTrace();
        }

        value = new String(bytes, StandardCharsets.UTF_8);
        return value;
    }

    public static void test2(String value) throws Exception {
        InputStream inputStream = new StringBufferInputStream(value);
        InputSource inputSource = new InputSource(inputStream);
//        final DocumentLoader documentLoader = new DefaultDocumentLoader();
//        Document document = documentLoader.loadDocument(inputSource, null, null, 1, true);
//        System.out.println(document);
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputSource);
        Element root = document.getRootElement();
        Iterator it = root.elementIterator();
        while(it.hasNext()){
            Element element = (Element) it.next();
        }
        System.out.println(document);
    }

    public static void test3() throws Exception {
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(XML_DATA.getBytes());
        String value = test(data);
        test2(value);
    }

    public static void test4() throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element envelope = doc.addElement("soapenv:Envelope");
//        envelope.addElement("soapenv:Header");
        Element body = envelope.addElement("soapenv:Body");
        Element runReport = body.addElement("pub:runReport");
        Element reportRequest =  runReport.addElement("pub:reportRequest");
        reportRequest.addElement("pub:attributeLocale").addText("en-US");
        reportRequest.addElement("pub:attributeTemplate").addText("Default");
        reportRequest.addElement("pub:attributeFormat").addText("xml");
        reportRequest.addElement("pub:parameterNameValues").addText("");
        reportRequest.addElement("pub:item").addText("");
        runReport.addElement("pub:reportAbsolutePath").addText("/Custom/ISU/AR/AR INTERFACE.xdo");
        runReport.addElement("pub:userID").addText("FIN_IMPL");
        runReport.addElement("pub:password").addText("Ibiden!619834");
        doc.setRootElement(envelope);
        System.out.println(doc.toString());

    }



    public static void main(String[] args) throws Exception {
//        test2(test());
        test4();
    }

}
