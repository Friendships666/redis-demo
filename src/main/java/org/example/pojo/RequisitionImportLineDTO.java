package org.example.pojo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @author congmian.lv@hand-china.com
 * @description
 **/
@Data
public class RequisitionImportLineDTO {

    /**
     * 行序号
     */
    private Integer lineSequence;
    /**
     * 商品（自行编码）
     */
    private String projectNumber;
    /**
     * 商品编码
     */
    private String commodityNumber;
    /**
     * 开具名称
     */
    private String issues;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 含税标志
     */
    private String taxIncludedFlag;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 零税率标识
     */
    private String zeroTaxRateFlag;
    /**
     * 税额
     */
    private BigDecimal taxAmount;
    /**
     * 扣除额
     */
    private BigDecimal deductionAmount;
    /**
     * 规则型号
     */
    private String specificationModel;
    /**
     * 单位
     */
    private String unit;
    /**
     * 申请来源号行号
     */
    private String sourceLineNum;
    /**
     * 申请来源号3
     */
    private String sourceNumber3;
    /**
     * 申请来源号4
     */
    private String sourceNumber4;
}
