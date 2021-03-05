package org.example.sec;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.demo.HttpClientUtils;
import org.example.demo.SOAPConstans;
import org.example.pojo.RequisitionImportHeadDTO;
import org.example.pojo.RunReportDTO;

import java.util.*;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/5 9:42
 */
public class RunReportServiceImpl extends SmileLoadXml implements RunReportService {
    @Override
    public Object runReport(String startTime, String endTime) throws Exception {

        String key = "runReport";

        String value = loadXml(key, new LinkedList<>());

        CloseableHttpResponse response = HttpClientUtils.postMothodParams(SOAPConstans.RUN_REPORT_HTTP, value);
        List<RequisitionImportHeadDTO> allList = null;
        if(response != null) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String context = EntityUtils.toString(response.getEntity(), "UTF-8");
                SmileHandler handler = getInstance(key);
                allList = (List<RequisitionImportHeadDTO>) handler.parse(context);
            }
        }


        List<RunReportDTO> runList = new ArrayList<>();
        if(allList != null && allList.size() > 0){
            Set<Long> organizationIdSet = new HashSet<>();
            Set<String> companyCodeSet = new HashSet<>();
            Set<String> employeeNumberSet = new HashSet<>();
            for(RequisitionImportHeadDTO headDTO : allList){
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

        return runList;
    }


    public void doTest(String host, String key, LinkedList<String> list) throws Exception {

        String value = loadXml(key, list);

        CloseableHttpResponse response = HttpClientUtils.postMothodParams(SOAPConstans.RUN_REPORT_HTTP, value);
        List<RequisitionImportHeadDTO> allList = null;
        if(response != null) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String context = EntityUtils.toString(response.getEntity(), "UTF-8");
                SmileHandler handler = getInstance(key);
                allList = (List<RequisitionImportHeadDTO>) handler.parse(context);
            }
        }


        List<RunReportDTO> runList = new ArrayList<>();
        if(allList != null && allList.size() > 0){
            Set<Long> organizationIdSet = new HashSet<>();
            Set<String> companyCodeSet = new HashSet<>();
            Set<String> employeeNumberSet = new HashSet<>();
            for(RequisitionImportHeadDTO headDTO : allList){
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

        System.out.println("整合完成的数据信息：" + runList);
    }



}
