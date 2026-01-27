package com.example.tjfw.entity;

public enum ProductType {

    ABAYA("Abaya"),
    HIJAB("Hijab"),
    DRESS("Dress"),
    JILBAB("Jilbaab"),
    KHIMAR("Khimar"),
    THOWB("Thowb");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
