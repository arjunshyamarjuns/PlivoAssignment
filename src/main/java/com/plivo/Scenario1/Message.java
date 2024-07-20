package com.plivo.Scenario1;

public class Message {
    public String src;
    public String dst;
    String text;

    public Message(String src, String dst, String text) {
        this.src = src;
        this.dst = dst;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
