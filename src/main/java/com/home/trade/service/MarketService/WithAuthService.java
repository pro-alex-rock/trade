package com.home.trade.service.MarketService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.trade.entity.AccountInfo;
import com.home.trade.entity.enums.SymbolEnum;
import com.home.trade.entity.enums.UriEnum;
import com.home.trade.util.SignatureGenerator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class WithAuthService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SignatureGenerator signatureGenerator;
    @Value("${spring.binance.testnet.api.key}")
    private  String apiKey;

    @Value("${spring.binance.test.base.url}")
    private String baseUrl;
    private static final String SIDE = "BUY"; // "BUY" або "SELL"
    private static final String TYPE = "MARKET"; // "LIMIT", "MARKET", тощо
    private static final String TIME_IN_FORCE = "GTC"; // "GTC" (Good Till Cancelled), "IOC" (Immediate Or Cancel), тощо
    private static final String QUANTITY = "0.1"; // Кількість деривативів для покупки або продажу
    private static final String PRICE = "<price>"; // Ціна для ордера (для LIMIT ордера)


    private final HttpClient client = HttpClient.newHttpClient();

    public String balance() { // Метод працює!!! Авторизація робоча!!!
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String signature = signatureGenerator.generateSignature(timestamp);

        String result;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.BALANCE.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            httpGet.setHeader("X-MBX-APIKEY", apiKey);
            URI uri = new URIBuilder(httpGet.getURI()).addParameter("timestamp", timestamp)
                    .addParameter("signature", signature)
                    .build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                result = EntityUtils.toString(entity);
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String account() { // Метод працює!!! Авторизація робоча!!!
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String signature = signatureGenerator.generateSignature(timestamp);

        AccountInfo result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.ACCOUNT.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            httpGet.setHeader("X-MBX-APIKEY", apiKey);
            URI uri = new URIBuilder(httpGet.getURI()).addParameter("timestamp", timestamp)
                    .addParameter("signature", signature)
                    .build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                result = objectMapper.readValue(input, AccountInfo.class);
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public String openOrder(String code) { // Наприклад, "BTCUSDT" TODO Ще не прописаний!!!
        if (code == null || !SymbolEnum.isSymbol(code)) {
            return "Wrong symbol!";
        }
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String signature = signatureGenerator.generateSignature(timestamp);

        // Побудова URL для відправки ордера
        String url = baseUrl + "/fapi/v1/order";

        // Побудова запиту для створення ордера
        String requestBody = "symbol=" + code +
                "&side=" + SIDE +
                "&type=" + TYPE +
                "&timeInForce=" + TIME_IN_FORCE +
                "&quantity=" + QUANTITY +
                //"&price=" + PRICE +
                "&timestamp=" + timestamp +
                "&signature=" + signature;

        // Створення HTTP POST-запиту
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-MBX-APIKEY", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Відправка запиту та отримання відповіді
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Обробка відповіді
        if (response.statusCode() == 200) {
            System.out.println("Order placed successfully!");
            System.out.println("Response body: " + response.body());
        } else {
            System.out.println("Failed to place order!");
            System.out.println("Response body: " + response.body());
        }
        return response.body();
    }

    /*public List<Candlestick> getCandlestick() {
        List<Candlestick> candlesticks = client.getCandlestickBars("BTCUSDT", CandlestickInterval.WEEKLY);
        System.out.println(candlesticks);
        return candlesticks;
    }*/
}
