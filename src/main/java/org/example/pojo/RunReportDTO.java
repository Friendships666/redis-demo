package org.example.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Autor Liutao  tao.liu15@hand-china.com
 * @Date 2021/3/4 15:03
 */

@Data
public class RunReportDTO {
    /**
     * 租户ID
     */
    private Long organizationId;

    /**
     * 公司Code
     */
    private String companyCode;

    /**
     * 员工编码 EMPLOYEENUM
     */
    private String employeeNum;

    private List<RequisitionImportHeadDTO> headDTOList;
}
