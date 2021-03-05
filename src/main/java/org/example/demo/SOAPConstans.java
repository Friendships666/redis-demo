package org.example.demo;

/**
 *  SOAP 服务常用字段类
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/4 13:31
 */
public class SOAPConstans {

    // 请求获取发票信息
    public static final String RUN_REPORT_HTTP = "https://ekjc-test.fa.ap1.oraclecloud.com/xmlpserver/services/PublicReportService/runReport/";

    // G_1 节点名称
    public static final String NODE_NAME_G_1 = "G_1";
    // G_2 节点名称
    public static final String NODE_NAME_G_2 = "G_2";

    // 请求获取发票信息 所需要的key,该key值对应该发票的xml请求格式，和smile.properties配置文件中的key保持一致
    public static final String RUN_REPORT_KEY = "runReport";
}
