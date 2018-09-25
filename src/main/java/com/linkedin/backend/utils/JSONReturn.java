package com.linkedin.backend.utils;

public class JSONReturn<T> {
    T payload;

    public JSONReturn(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
