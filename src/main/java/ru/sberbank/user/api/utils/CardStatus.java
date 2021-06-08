package ru.sberbank.user.api.utils;

public enum CardStatus {

    IN_REVIEW("IN_REVIEW"),
    APPROVED("APPROVED"),
    DECLINED("DECLINED");

    private final String status;

    CardStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
