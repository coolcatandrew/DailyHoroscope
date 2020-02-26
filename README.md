# DailyHoroscope
Java library which calls HoroscopeAPI (https://github.com/coolcatandrew/Horoscope-API-Endpoints) used to get daily horiscope and send them to friends as SMS messages.

## Installation

Download Jar file and import into project.

If using a maven project, include the following in your pom.xml after importing the jar into the project where PATH_TO_JAR is the file path to the jar file.

    <dependency>
      <groupId>com.liprogramming</groupId>
      <artifactId>DailyHoroscope</artifactId>
      <version>1.0</version>
      <scope>system</scope>
      <systemPath>PATH_TO_JAR/DailyHoroscope.jar</systemPath>
    </dependency>
    
Create a properties file with the following attributes:

    TWILIO.ACCOUNT_SID=ACXXXXXXXXXXXXXXXXXXXXXXX
    TWILIO.AUTH_TOKEN=XXXXXXXXXXXXXXXXXXXXXXXXXX
    TWILIO.PHONE_NUMBER=+12223334444
    
Where the content is your Twilio Account SID, Twilio AuthToken and Twilio phone number all available on the Twilio's user console. 
Phone numbers must be in E.123 international notation (include "+" international prefix). 

**Please note the file must exist but can be empty if no texts are being sent**
 
## Example Usage

    try {
        DailyHoroscope horoscopeLibrary = new DailyHoroscopeImpl(
                "https://daily-horoscope-api.herokuapp.com", //horoscope-api-endpoints url
                "src/horoscope.properties" //path to properties file
        );

        //Get horoscope based on zodiac sign
        HoroscopeResponse<Horoscope> response = horoscopeLibrary.getHoroscope("leo");

        //Use the horoscope if successfully retrieved
        if (response.getStatus() == 200) {
            System.out.println(response.getData().toString());
        } else {
            System.out.println(response.getMessage());
        }

        //send a default horoscope text to someone and print if it was successful or not
        System.out.println(
                horoscopeLibrary.text(
                    horoscopeLibrary.getHoroscope(8, 4).getData(), //get horoscope based on birth date
                    "+12223334444") //destination phone number in E.123 international notation
                .getMessage() //Get the message if the text was successfully sent or not
        );
        
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
   

## Methods

    /**
     * Retrieve all of todays horoscopes
     */
    HoroscopeResponse<List<Horoscope>> getHoroscopes()
    
    /**
     * Get todays horoscope based on zodiac sign
     */
    HoroscopeResponse<Horoscope> getHoroscope(String sign)

    /**
     * Get todays horoscope based on birth date
     */
    HoroscopeResponse<Horoscope> getHoroscope(Integer birthMonth, Integer birthDay)

    /**
     * Send a text with a horoscope based on birth date
     */
    HoroscopeResponse<Message> text(Integer birthMonth, Integer birthDay, String number)

    /**
     * Send a text with a horoscope based on zodiac sign
     */
    HoroscopeResponse<Message> text(String sign, String number)

    /**
     * Send texts to multiple numbers with horoscopes
     */
    List<HoroscopeResponse<Message>> text(Map<String, Horoscope> text)

    /**
     * Send a text with a horoscope
     */
    HoroscopeResponse<Message> text(Horoscope horoscope, String dest)

    /**
     * Send a text with custom text
     */
    HoroscopeResponse<Message> customText(String content, String number)

## Response
HoroscopeResponse Objects have a status, message, and optionally data.  
    
    Successful Example:
    {
        status: 200
        message: "Successfully retrieved horoscope"
        data: {
            content: "If you want to get closer to someone, you have to be true to who you are. Don't use top-secret information to make an impression on them. Just be yourself."
            zodiacSign: "LEO"
            source: "www.astrology.com"
        }
    }
    
    Unsuccessful Example:
    {
        status: 404
        message: "Horoscopes not found"
        data: null
    }
    
