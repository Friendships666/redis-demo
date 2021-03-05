package org.example.pojo;


import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author congmian.lv@hand-china.com
 * @description
 **/
@Data
public class RequisitionImportHeadDTO {

    /**
     * 租户ID
     */
    private Long organizationId;

    /**
     * 公司Code
     */
    private String companyCode;

    /**
     * 员工编码
     */
    private String employeeNum;

    /**
     * 头序号
     */
    private Long headSequence;
    /**
     * 业务类型
     */
    private String requestType;
    /**
     * 分机号
     */
    private String extNumber;
    /**
     * 收票方名称
     */
    private String receiptName;
    /**
     * 收票方纳税人识别号
     */
    private String receiptTaxNo;
    /**
     * 收票方地址电话
     */
    private String receiptAddressPhone;
    /**
     * 收票方开户行及账号
     */
    private String receiptAccount;
    /**
     * 收票方企业类型
     */
    private String receiptType;
    /**
     * 纸票收件人
     */
    private String paperRecipient;
    /**
     * 纸票收件电话
     */
    private String paperPhone;
    /**
     * 纸票收件地址
     */
    private String paperAddress;
    /**
     * 手机邮件交付
     */
    private String emailOrPhone;
    /**
     * 申请开票种类
     */
    private String invoiceType;
    /**
     * 购货清单标志
     */
    private String billFlag;
    /**
     * 附加备注
     */
    private String remark;
    /**
     * 来源单号
     */
    private String sourceNumber;
    /**
     * 申请来源单号1
     */
    private String sourceNumber1;
    /**
     * 申请来源单号2
     */
    private String sourceNumber2;

    List<RequisitionImportLineDTO> lineDTOS;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequisitionImportHeadDTO headDTO = (RequisitionImportHeadDTO) o;
        return Objects.equals(requestType, headDTO.requestType) &&
                Objects.equals(extNumber, headDTO.extNumber) &&
                Objects.equals(receiptName, headDTO.receiptName) &&
                Objects.equals(receiptTaxNo, headDTO.receiptTaxNo) &&
                Objects.equals(receiptAddressPhone, headDTO.receiptAddressPhone) &&
                Objects.equals(receiptAccount, headDTO.receiptAccount) &&
                Objects.equals(receiptType, headDTO.receiptType) &&
                Objects.equals(paperRecipient, headDTO.paperRecipient) &&
                Objects.equals(paperPhone, headDTO.paperPhone) &&
                Objects.equals(paperAddress, headDTO.paperAddress) &&
                Objects.equals(emailOrPhone, headDTO.emailOrPhone) &&
                Objects.equals(invoiceType, headDTO.invoiceType) &&
                Objects.equals(billFlag, headDTO.billFlag) &&
                Objects.equals(remark, headDTO.remark) &&
                Objects.equals(sourceNumber, headDTO.sourceNumber) &&
                Objects.equals(sourceNumber1, headDTO.sourceNumber1) &&
                Objects.equals(sourceNumber2, headDTO.sourceNumber2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestType, extNumber, receiptName, receiptTaxNo, receiptAddressPhone, receiptAccount,
                        receiptType, paperRecipient, paperPhone, paperAddress, emailOrPhone, invoiceType, billFlag,
                        remark, sourceNumber, sourceNumber1, sourceNumber2);
    }
}
