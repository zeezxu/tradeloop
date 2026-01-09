package com.example.common.enums;

public enum OrderStatusEnum {
    CANCEL("Cancelled"),
    NOTPAY("Pending Payment"),
    NOTSEND("Pending Shipment"),
    NOTRECIEVE("Pending Receipt"),
    DONE("Completed");

    public String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }
}
