package com.mcfire.autoplay.client;

public class ClientEnergyData {
    private static int energy; // to sync energy

    public static int getEnergy() {
        return energy;
    }

    public static void setEnergy(int energy) {
        ClientEnergyData.energy = energy;
    }
}
