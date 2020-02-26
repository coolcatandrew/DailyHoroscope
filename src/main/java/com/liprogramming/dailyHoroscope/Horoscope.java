package com.liprogramming.dailyHoroscope;

public class Horoscope {
    private String content;
    private String zodiacSign;
    private String source;

    public Horoscope() {
    }

    public Horoscope(String content, String zodiacSign, String source) {
        this.content = content;
        this.zodiacSign = zodiacSign;
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public String getZodiacSign() {
        return zodiacSign;
    }

    public String getSource() {
        return source;
    }

    public String toString() {
        return "Sign: " + this.zodiacSign + "\n" +
                "Content: " + this.content + "\n" +
                "Source: " + this.source + "\n";
    }
}
