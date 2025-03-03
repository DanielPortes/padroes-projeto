package trabalhofinal.smarthome.flyweight;

//package com.smarthome.flyweight;

/**
 * Implementação do padrão Flyweight para compartilhar atributos de cômodos
 * Este é um padrão GoF adicional que não estava na lista original.
 */
public class RoomAttributes {
    private final String type;
    private final String defaultLightLevel;
    private final String defaultTemperature;
    private final String wallColor;
    private final String floorType;

    private RoomAttributes(Builder builder) {
        this.type = builder.type;
        this.defaultLightLevel = builder.defaultLightLevel;
        this.defaultTemperature = builder.defaultTemperature;
        this.wallColor = builder.wallColor;
        this.floorType = builder.floorType;
    }

    public String getType() {
        return type;
    }

    public String getDefaultLightLevel() {
        return defaultLightLevel;
    }

    public String getDefaultTemperature() {
        return defaultTemperature;
    }

    public String getWallColor() {
        return wallColor;
    }

    public String getFloorType() {
        return floorType;
    }

    @Override
    public String toString() {
        return String.format("RoomType: %s, LightLevel: %s, Temperature: %s, WallColor: %s, Floor: %s",
                type, defaultLightLevel, defaultTemperature, wallColor, floorType);
    }

    public static class Builder {
        private String type;
        private String defaultLightLevel = "50%";
        private String defaultTemperature = "21C";
        private String wallColor = "White";
        private String floorType = "Wood";

        public Builder(String type) {
            this.type = type;
        }

        public Builder defaultLightLevel(String level) {
            this.defaultLightLevel = level;
            return this;
        }

        public Builder defaultTemperature(String temperature) {
            this.defaultTemperature = temperature;
            return this;
        }

        public Builder wallColor(String color) {
            this.wallColor = color;
            return this;
        }

        public Builder floorType(String floor) {
            this.floorType = floor;
            return this;
        }

        public RoomAttributes build() {
            return new RoomAttributes(this);
        }
    }
}




