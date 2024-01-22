package org.example.model;

public class Plant {
    private String Common;
    private String Botanical;
    private int Zone;
    private String Light;
    private double Price;
    private int Availability;
    public Plant(String common, String botanical, int zone, String light, double price, int availability) {
        this.Common = common;
        this.Botanical = botanical;
        this.Zone = zone;
        this.Light = light;
        this.Price = price;
        this.Availability = availability;

    }
    public String getCommon() {
        return Common;
    }

    public String getBotanical() {
        return Botanical;
    }

    public int getZone() {
        return Zone;
    }

    public String getLight() {
        return Light;
    }

    public double getPrice() {
        return Price;
    }

    public int getAvailability() {
        return Availability;
    }
}
