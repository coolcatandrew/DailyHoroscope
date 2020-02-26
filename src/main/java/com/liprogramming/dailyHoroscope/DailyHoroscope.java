package com.liprogramming.dailyHoroscope;

import com.twilio.rest.api.v2010.account.Message;
import java.util.List;
import java.util.Map;

public interface DailyHoroscope {

    /**
     * Retrieve all of todays horoscopes
     * @return
     */
    HoroscopeResponse<List<Horoscope>> getHoroscopes();

    /**
     * Get todays horoscope based on zodiac sign
     * @param sign
     * @return
     */
    HoroscopeResponse<Horoscope> getHoroscope(String sign);

    /**
     * Get todays horoscope based on birth date
     * @param birthMonth
     * @param birthDay
     * @return
     */
    HoroscopeResponse<Horoscope> getHoroscope(Integer birthMonth, Integer birthDay);

    /**
     * Send a text with a horoscope based on birth date
     * @param birthMonth
     * @param birthDay
     * @param number
     * @return
     */
    HoroscopeResponse<Message> text(Integer birthMonth, Integer birthDay, String number);

    /**
     * Send a text with a horoscope based on zodiac sign
     * @param sign
     * @param number
     * @return
     */
    HoroscopeResponse<Message> text(String sign, String number);

    /**
     * Send texts to multiple numbers with horoscopes
     * @param text Map of phone numbers and horoscopes
     * @return
     */
    List<HoroscopeResponse<Message>> text(Map<String, Horoscope> text);

    /**
     * Send a text with a horoscope
     * @param horoscope
     * @param dest
     * @return
     */
    HoroscopeResponse<Message> text(Horoscope horoscope, String dest);

    /**
     * Send a text with custom text
     * @param content
     * @param number
     * @return
     */
    HoroscopeResponse<Message> customText(String content, String number);
}
