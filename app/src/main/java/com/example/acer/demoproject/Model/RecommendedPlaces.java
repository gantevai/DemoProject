package com.example.acer.demoproject.Model;

public class RecommendedPlaces {
    public int place_id;
    public String placeName,place_description,place_type;
    public Double placeLat,placeLong,rating_value;



    public RecommendedPlaces(int place_id, String placeName, String place_description, String place_type, Double placeLat, Double placeLong, Double rating_value) {
        this.place_id = place_id;
        this.placeName = placeName;
        this.place_description = place_description;
        this.place_type = place_type;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
        this.rating_value = rating_value;

    }

    public int getPlace_id() {
        return place_id;
    }

    public String getPlace_description() {
        return place_description;
    }

    public String getPlace_type() {
        return place_type;
    }

    public Double getPlaceLat() {
        return placeLat;
    }

    public Double getPlaceLong() {
        return placeLong;
    }
    public Double getRating_value() {
        return rating_value;
    }

    public String getTitle() {
        return placeName;
    }
    public String getImage_Url() {
        return String.format("http://pasang1422.000webhostapp.com/place_images/%s.png",String.valueOf(place_id));
    }
}
