package com.ecloth.beta.weather.service;


import com.ecloth.beta.weather.dto.WeatherDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WeatherService {
//    @Value("${custom.service.key}")
//    private String key;
//
//    public List<WeatherDTO> getWeather(Long x, Long y) throws IOException, ParseException {
//        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + key); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
//        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20230309", "UTF-8")); /*‘21년 6월 28일 발표*/
//        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0600", "UTF-8")); /*06시 발표(정시단위) */
//        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(String.valueOf(x), "UTF-8")); /*예보지점의 X 좌표값*/
//        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(String.valueOf(y), "UTF-8")); /*예보지점의 Y 좌표값*/
//        System.out.println(urlBuilder.toString());
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        System.out.println("Response code: " + conn.getResponseCode());
//        BufferedReader rd;
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//        String data = sb.toString();
//        log.info(data);
//        JSONParser parser = new JSONParser();
//        JSONObject parse = (JSONObject)parser.parse(data);
//        JSONObject response = (JSONObject)parse.get("response");
//        log.info(parse.get("response").toString());
//        JSONObject body2 = (JSONObject) response.get("body");
//        log.info(response.get("body").toString());
//
//        JSONObject items = (JSONObject) body2.get("items");
//        log.info(body2.get("items").toString());
//
//        JSONArray array = (JSONArray) items.get("item");
//        log.info(items.get("item").toString());
//
//
//
//        List<WeatherDTO> weatherDTOList = new ArrayList<>();
//        for (int i = 0; i < array.size(); i++) {
//            WeatherDTO weatherDTO = new WeatherDTO();
//            JSONObject o = (JSONObject) array.get(i);
//            weatherDTO.setBaseDate((String) o.get("baseDate"));
//            weatherDTO.setBaseTime((String) o.get("baseTime"));
//            weatherDTO.setCategory((String) o.get("category"));
//            weatherDTO.setNx((Long) o.get("nx"));
//            weatherDTO.setNy((Long) o.get("ny"));
//            weatherDTO.setObsrValue((String) o.get("obsrValue"));
//            weatherDTOList.add(weatherDTO);
//        }
//
//        return weatherDTOList;
//    }
}
