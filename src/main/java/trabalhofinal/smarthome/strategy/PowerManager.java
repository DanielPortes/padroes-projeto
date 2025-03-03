/**
 * Contexto que usa a estratégia
 */
package trabalhofinal.smarthome.strategy;
//package com.smarthome.strategy;

import trabalhofinal.smarthome.devices.AbstractDevice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PowerManager {
    private PowerSavingStrategy currentStrategy;
    private final Map<String, PowerSavingStrategy> availableStrategies;

    public PowerManager() {
        this.availableStrategies = new HashMap<>();

        // Registrar estratégias disponíveis
        PowerSavingStrategy balanced = new BalancedPowerStrategy();
        registerStrategy(balanced);
        registerStrategy(new MaxEconomyPowerStrategy());
        registerStrategy(new ComfortPowerStrategy());

        // Definir estratégia padrão
        this.currentStrategy = balanced;
    }

    public void registerStrategy(PowerSavingStrategy strategy) {
        availableStrategies.put(strategy.getName().toLowerCase(), strategy);
    }

    public void setStrategy(String strategyName) {
        PowerSavingStrategy strategy = availableStrategies.get(strategyName.toLowerCase());
        if (strategy != null) {
            this.currentStrategy = strategy;
        } else {
            throw new IllegalArgumentException("Unknown strategy: " + strategyName);
        }
    }

    public PowerSavingStrategy getCurrentStrategy() {
        return currentStrategy;
    }

    public Map<String, PowerSavingStrategy> getAvailableStrategies() {
        return new HashMap<>(availableStrategies);
    }

    public String applyStrategy(AbstractDevice device) {
        return currentStrategy.applyToDevice(device);
    }

    public String applyStrategy(Collection<AbstractDevice> devices) {
        return currentStrategy.applyToDevices(devices);
    }
}