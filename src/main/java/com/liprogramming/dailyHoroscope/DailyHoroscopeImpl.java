package com.liprogramming.dailyHoroscope;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class DailyHoroscopeImpl implements DailyHoroscope{
    private String horoscopeAPIUrl;
    private Gson gson;
    private PhoneNumber fromPhone;

    public DailyHoroscopeImpl(String horoscopeAPIUrl, String propertiesFileLocation) throws IOException{
        this.horoscopeAPIUrl = horoscopeAPIUrl;
        gson = new Gson();

        InputStream propertiesFile = new FileInputStream(propertiesFileLocation);
        Properties prop = new Properties();
        prop.load(propertiesFile); //load properties file
        fromPhone = new PhoneNumber(prop.getProperty("TWILIO.PHONE_NUMBER"));
        Twilio.init(prop.getProperty("TWILIO.ACCOUNT_SID"), prop.getProperty("TWILIO.AUTH_TOKEN"));
    }

    @Override
    public HoroscopeResponse<List<Horoscope>> getHoroscopes() {
        try {
            HoroscopeResponse<String> horoscopeJSON = GETRequest(horoscopeAPIUrl + "/api/horoscopes");
            if (horoscopeJSON.getStatus() != 200) {
                return new HoroscopeResponse<>(horoscopeJSON.getStatus(), horoscopeJSON.getMessage());
            }
            List<Horoscope> data = gson.fromJson(horoscopeJSON.getData(), new TypeToken<List<Horoscope>>() {}.getType());
            if (data == null || data.size() == 0) {
                return new HoroscopeResponse<>(404, "No horoscopes found.");
            }
            return new HoroscopeResponse<>(200, "Successfully retrieved horoscopes.", data);
        } catch (IOException e) {
            System.out.println(e);
            return new HoroscopeResponse<>(500, "Error: could not retrieve horoscopes.");
        }
    }

    @Override
    public HoroscopeResponse<Horoscope> getHoroscope(String sign) {
        return getHoroscopeResponse(horoscopeAPIUrl + "/api/sign/" + sign.toUpperCase());
    }

    @Override
    public HoroscopeResponse<Horoscope> getHoroscope(Integer birthMonth, Integer birthDay) {
        return getHoroscopeResponse(horoscopeAPIUrl + "/api/month/" + birthMonth + "/day/" + birthDay);
    }

    private HoroscopeResponse<Horoscope> getHoroscopeResponse(String url) {
        try {
            HoroscopeResponse<String> horoscopeJSON = GETRequest(url);
            if (horoscopeJSON.getStatus() != 200) {
                return new HoroscopeResponse<>(horoscopeJSON.getStatus(), horoscopeJSON.getMessage());
            }
            Horoscope horoscope = gson.fromJson(horoscopeJSON.getData(), Horoscope.class);
            if (horoscope == null) {
                return new HoroscopeResponse<>(404, "Horoscope not found.");
            }
            return new HoroscopeResponse<>(200, "Successfully retrieved horoscope.", horoscope);
        } catch (IOException e) {
            System.out.println(e);
            return new HoroscopeResponse<>(500, "Error: could not retrieve horoscope.");
        }
    }

    @Override
    public HoroscopeResponse<Message> text(Integer birthMonth, Integer birthDay, String number) {
        return text(getHoroscope(birthMonth, birthDay).getData(), number);
    }

    @Override
    public HoroscopeResponse<Message> text(String sign, String number) {
        return text(getHoroscope(sign).getData(), number);
    }

    @Override
    public List<HoroscopeResponse<Message>> text(Map<String, Horoscope> texts) {
        List<HoroscopeResponse<Message>> responses = new ArrayList<>();
        for (Map.Entry<String, Horoscope> text : texts.entrySet()) {
            responses.add(text(text.getValue(), text.getKey()));
        }
        return responses;
    }

    @Override
    public HoroscopeResponse<Message> text(Horoscope horoscope, String number) {
        if (horoscope == null) {
            return new HoroscopeResponse<>(404, "Horoscope not found.");
        }
        String body = "Hello you beautiful " + horoscope.getZodiacSign() + ", " + horoscope.getContent();
        return customText(body, number);
    }

    @Override
    public HoroscopeResponse<Message> customText(String content, String number) {
        try {
            Message message = Message
                    .creator(new PhoneNumber(number), // to
                            fromPhone, // from
                            content)
                    .create();
            if (message.getErrorCode() != null) {
                return new HoroscopeResponse<>(message.getErrorCode(), message.getErrorMessage());
            }
            return new HoroscopeResponse<>(200, "Successfully sent horoscope.", message);
        } catch (TwilioException e) {
            return new HoroscopeResponse<>(500, e.getMessage());
        }
    }

    private HoroscopeResponse<String> GETRequest(String url) throws IOException {
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            for (String line = in.readLine(); line != null; line = in.readLine()) {
                response.append(line);
            }
            in.close();

            return new HoroscopeResponse<>(responseCode, "Successfully retrieved horoscope.", response.toString());
        } else {
            return new HoroscopeResponse<>(responseCode, "Failed to retrieved horoscope.");
        }

    }
}