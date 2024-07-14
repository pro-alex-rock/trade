package com.home.trade.service.MarketService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.trade.entity.ChangesPrice24Hr;
import com.home.trade.entity.MarkPrice;
import com.home.trade.entity.Symbol;
import com.home.trade.entity.enums.UriEnum;
import com.home.trade.repository.ItemRepository;
import com.home.trade.repository.entities.HistoryCandle;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NoAuthService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ItemRepository itemRepository;

    @Value("${spring.binance.test.base.url}")
    private String baseUrl;

    private final HttpClient client = HttpClient.newHttpClient();

    public String orderBook(String symbol) { // Метод працює!!! Авторизація робоча!!!

        String result;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.ORDER_BOOK.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).addParameter("symbol", symbol)
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

    public String priceOne(String symbol) {
        Symbol result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.PRICE.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).addParameter("symbol", symbol)
                    .build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                result = objectMapper.readValue(input, Symbol.class);
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public String priceList() {
        List<Symbol> result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.PRICE.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                result = objectMapper.readValue(input, new TypeReference<>() {});
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public String markPriceOne(String symbol) {
        MarkPrice result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.MARK_PRICE.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).addParameter("symbol", symbol)
                    .build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                result = objectMapper.readValue(input, MarkPrice.class);
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public String markPriceList() {
        List<MarkPrice> result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.MARK_PRICE.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                result = objectMapper.readValue(input, new TypeReference<>() {});
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public String changesPrice24HrOne(String symbol) {
        ChangesPrice24Hr result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.PRICE_24_HOURS.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).addParameter("symbol", symbol)
                    .build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                result = objectMapper.readValue(input, ChangesPrice24Hr.class);
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public String changesPrice24HrList() {
        List<ChangesPrice24Hr> result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.PRICE_24_HOURS.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                result = objectMapper.readValue(input, new TypeReference<>() {});
                System.out.println(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    /**
     * <h1>Get all ticks by history</h1>
     * Get all ticks by history starting from 2019-09-01 03:00:00
     * depends on the @param (interval)
     * @param symbol - for example "BTCUSDT"
     * @param interval - 1m, 3m, 5m, 15m, 30m, 1h, 2h, 4h, 6h, 8h, 12h, 1d, 3d, 1w, 1M
     * @return result in JSON format
     */
    public List<HistoryCandle> candlestick(String symbol, String interval, Optional<HistoryCandle> isRecordExists) { //all history by symbol
        List<HistoryCandle> result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + UriEnum.KLINES.getLabel());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = createUri(httpGet, symbol, interval, isRecordExists);
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                List<List<Object>> rawData = objectMapper.readValue(input, new TypeReference<>(){});
                result = mapToCandlestick(rawData , interval);
                //System.out.println("*** result ***" + result);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private URI createUri(HttpGet httpGet, String symbol, String interval, Optional<HistoryCandle> isRecordExists) throws URISyntaxException {
        URI uri;
        if (isRecordExists.isEmpty()) {
            uri = new URIBuilder(httpGet.getURI()).addParameter("symbol", symbol)
                    .addParameter("interval", interval)
                    .build();
            httpGet.setURI(uri);
        } else {
            uri = new URIBuilder(httpGet.getURI()).addParameter("symbol", symbol)
                    .addParameter("interval", interval)
                    .addParameter("startTime", String.valueOf(isRecordExists.get().getTimestamp()))
                    .build();
            httpGet.setURI(uri);
        }
        return uri;
    }

    /*public List<HistoryCandle1M> candlestick(String symbol, String interval) { //all history by symbol
        //List<HistoryCandle1M> result;
        List<HistoryCandle1M> result;
        String input;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(baseUrl + "/fapi/v1/klines");
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "x-www-form-urlencoded");
            URI uri = new URIBuilder(httpGet.getURI()).addParameter("symbol", symbol)
                    .addParameter("interval", interval)
                    .build();
            httpGet.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                input = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                //result = objectMapper.readValue(input, new TypeReference<>() {});
                //System.out.println(result);

                List<List<Object>> rawData = objectMapper.readValue(input, new TypeReference<>(){});
                result = mapToCandlestick(rawData);
                System.out.println("*** result ***" + result);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        createHistoryCandle1M(result);
        return input;
    }*/

    public String time() {
        String url = baseUrl + UriEnum.TIME.getLabel();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        String body = response.body();
        System.out.println(body);
        return printTime(response.body());
    }

    private List<HistoryCandle> mapToCandlestick(List<List<Object>> rawData, String type) {
        List<HistoryCandle> candlesticks = new ArrayList<>();
        for (List<Object> entry : rawData) {
            //HistoryCandle candlestick = createCandlestick(type);
            HistoryCandle candlestick = new HistoryCandle();
            candlestick.setTimestamp(Long.parseLong(entry.get(0).toString()));
            candlestick.setOpenPrice(entry.get(1).toString());
            candlestick.setHighPrice(entry.get(2).toString());
            candlestick.setLowPrice(entry.get(3).toString());
            candlestick.setClosePrice(entry.get(4).toString());
            candlestick.setVolume(entry.get(5).toString());
            candlestick.setCloseTime(Long.parseLong(entry.get(6).toString()));
            candlestick.setQuoteAssetVolume(entry.get(7).toString());
            candlestick.setNumberOfTrades(Integer.parseInt(entry.get(8).toString()));
            candlestick.setTakerBuyBaseAssetVolume(entry.get(9).toString());
            candlestick.setTakerBuyQuoteAssetVolume(entry.get(10).toString());
            candlestick.setIgnore(entry.get(11).toString());
            candlesticks.add(candlestick);
        }
        return candlesticks;
    }

    /*private List<HistoryCandle1M> mapToCandlestick(List<List<Object>> rawData) {
        List<HistoryCandle1M> candlesticks = new ArrayList<>();
        for (List<Object> entry : rawData) {
            HistoryCandle1M candlestick = new HistoryCandle1M();
            candlestick.setTimestamp(Long.parseLong(entry.get(0).toString()));
            candlestick.setOpenPrice(entry.get(1).toString());
            candlestick.setHighPrice(entry.get(2).toString());
            candlestick.setLowPrice(entry.get(3).toString());
            candlestick.setClosePrice(entry.get(4).toString());
            candlestick.setVolume(entry.get(5).toString());
            candlestick.setCloseTime(Long.parseLong(entry.get(6).toString()));
            candlestick.setQuoteAssetVolume(entry.get(7).toString());
            candlestick.setNumberOfTrades(Integer.parseInt(entry.get(8).toString()));
            candlestick.setTakerBuyBaseAssetVolume(entry.get(9).toString());
            candlestick.setTakerBuyQuoteAssetVolume(entry.get(10).toString());
            candlestick.setIgnore(entry.get(11).toString());
            candlesticks.add(candlestick);
        }
        return candlesticks;
    }*/

    private void createHistoryCandle1M(List<HistoryCandle> list) {
        itemRepository.saveAll(list);
    }

    /*private void createHistoryCandle1M(List<HistoryCandle1M> list) {
        itemRepository.saveAll(list);
    }*/

    private String printTime(String result) {
        String timeString = result.substring(result.indexOf(":") + 1, result.length() - 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(Long.parseLong(timeString));
        String timeLocal = format.format(new Date());
        System.out.println("Server Time: " + time);
        System.out.println("Local Time: " + timeLocal);
        return "Server Time: " + time + "\n\nLocal Time: " + timeLocal;
    }
}
