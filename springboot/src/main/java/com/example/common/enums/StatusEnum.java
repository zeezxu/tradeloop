package com.example.common.enums;

public enum StatusEnum {
    NOT_AUDIT("Pending"),
    APPROVE("Approved"),
    REJECT("Rejected");

    public String value;

    StatusEnum(String value) {
        this.value = value;
    }
}
