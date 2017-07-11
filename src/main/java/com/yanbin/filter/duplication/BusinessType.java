package com.yanbin.filter.duplication;

/**
 * Created by yanbin on 2017/7/1.
 */
public enum BusinessType {
    NULL(-1, "无效"),
    createRequisition(1, "申购"),
    createReceipt(2, "收货"),
    updateRequisition(3, "申购单修改"),
    updateReceipt(4, "收货修改"),
    checkRequisition(5, "申购单审核"),
    createPurchaseOrder(6, "申购单审核"),
    returnStockIn(7, "退货入库"),
    stockOut(8, "出库"),
    stockIn(9, "入库"),
    confirmOrder(10,"确认订单");
    private Integer value;
    private String desc;

    BusinessType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static BusinessType parse(Integer value) {
        if (null == value) {
            return null;
        }
        BusinessType[] coll = values();
        for (BusinessType item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }
}
