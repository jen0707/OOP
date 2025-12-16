package teamProjectFinal;

public class Restaurant {
    String name, address, placeUrl, stationName, category;
    float rating;
    int reviewCount, priceLevel;
    double lat, lng;
    int originalRank;

    public Restaurant(String name, String address, String placeUrl,
                      String stationName, String category,
                      float rating, int reviewCount, int priceLevel,
                      double lat, double lng, int originalRank) {
        this.name = name;
        this.address = address;
        this.placeUrl = placeUrl;
        this.stationName = stationName;
        this.category = category;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.priceLevel = priceLevel;
        this.lat = lat;
        this.lng = lng;
        this.originalRank = originalRank;
    }
}

