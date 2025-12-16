package teamProjectFinal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GooglePlacesService {

    private String apiKey;

    public GooglePlacesService(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Restaurant> fetchPlaces(String station, String foodType, boolean openNow) throws Exception {
        List<Restaurant> list = new ArrayList<>();

        String query = station + " " + foodType;
        String openNowParam = openNow ? "&opennow=true" : "";
        String apiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                + URLEncoder.encode(query, "UTF-8")
                + "&key=" + apiKey
                + "&language=ko"
                + openNowParam;

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() != 200) throw new Exception("HTTP Error: " + conn.getResponseCode());

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return parseJson(sb.toString(), station, foodType);
    }

    public String generateStaticMapUrl(List<Restaurant> items, int limit) {
        if (items == null || items.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?");
        sb.append("size=400x250&scale=2&maptype=roadmap&language=ko");

        int count = 0;
        for (Restaurant r : items) {
            if (count >= limit) break;
            if (r.lat != 0 && r.lng != 0) {
                sb.append("&markers=color:red%7Clabel:")
                        .append(count + 1)
                        .append("%7C")
                        .append(r.lat)
                        .append(",")
                        .append(r.lng);
                count++;
            }
        }

        sb.append("&key=").append(apiKey);
        return sb.toString();
    }

    public void sortRestaurants(List<Restaurant> list, String sortOption) {
        if (sortOption.contains("구글 추천순 (역별)")) {
            return;
        }

        if (sortOption.contains("구글 추천순 (섞기)")) {
            list.sort(Comparator.comparingInt(r -> r.originalRank));
            return;
        }

        list.sort((r1, r2) -> {
            if (sortOption.equals("리뷰 많은 순")) {
                int reviewCmp = Integer.compare(r2.reviewCount, r1.reviewCount);
                return (reviewCmp != 0) ? reviewCmp : Float.compare(r2.rating, r1.rating);
            } else {
                int ratingCmp = Float.compare(r2.rating, r1.rating);
                return (ratingCmp != 0) ? ratingCmp : Integer.compare(r2.reviewCount, r1.reviewCount);
            }
        });
    }

    private List<Restaurant> parseJson(String json, String station, String foodType) {
        List<Restaurant> items = new ArrayList<>();
        List<String> objects = extractJsonObjects(json);

        for (int i = 0; i < objects.size(); i++) {
            String block = objects.get(i);
            String name = extractValue(block, "name", false);
            String addr = extractValue(block, "formatted_address", false);
            float rating = parseFloat(extractValue(block, "rating", true));
            int reviews = parseInt(extractValue(block, "user_ratings_total", true));
            int price = parseInt(extractValue(block, "price_level", true));

            double lat = 0, lng = 0;
            int locIdx = block.indexOf("\"location\"");
            if (locIdx != -1) {
                String locBlock = block.substring(locIdx);
                lat = parseFloat(extractValue(locBlock, "lat", true));
                lng = parseFloat(extractValue(locBlock, "lng", true));
            }

            String url = "https://www.google.com/maps/search/?api=1&query="
                    + URLEncoder.encode(name, StandardCharsets.UTF_8);

            items.add(new Restaurant(name, addr, url, station, foodType, rating, reviews, price, lat, lng, i));
        }

        return items;
    }

    private List<String> extractJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int resultsKeyIndex = json.indexOf("\"results\"");
        if (resultsKeyIndex == -1) return objects;

        int start = json.indexOf("[", resultsKeyIndex);
        if (start == -1) return objects;

        int braceCount = 0, itemStart = -1;
        boolean inQuote = false;

        for (int i = start + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"' && json.charAt(i - 1) != '\\') inQuote = !inQuote;
            if (inQuote) continue;

            if (c == '{') {
                if (braceCount == 0) itemStart = i;
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0 && itemStart != -1) {
                    objects.add(json.substring(itemStart, i + 1));
                    itemStart = -1;
                }
            }

            if (braceCount == 0 && c == ']') break;
        }

        return objects;
    }

    private String extractValue(String src, String key, boolean isNum) {
        int idx = src.indexOf("\"" + key + "\"");
        if (idx == -1) return "";

        int col = src.indexOf(":", idx);

        if (isNum) {
            int s = col + 1;
            while (s < src.length() && !Character.isDigit(src.charAt(s)) && src.charAt(s) != '.' && src.charAt(s) != '-') s++;
            int e = s;
            while (e < src.length() && (Character.isDigit(src.charAt(e)) || src.charAt(e) == '.' || src.charAt(e) == '-')) e++;
            return src.substring(s, e);
        } else {
            int s = src.indexOf("\"", col) + 1;
            int e = src.indexOf("\"", s);
            return (s > 0 && e > s) ? src.substring(s, e) : "";
        }
    }

    private float parseFloat(String s) {
        return s.isEmpty() ? 0 : Float.parseFloat(s);
    }

    private int parseInt(String s) {
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }
}


