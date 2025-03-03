package trabalhofinal.smarthome.automation;

//package com.smarthome.automation;

import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.observer.HomeEvent;
import trabalhofinal.smarthome.observer.Observer;
import trabalhofinal.smarthome.strategy.PowerManager;
//import trabalhofinal.smarthome.visitor.Room;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;

/**
 * Implementação do padrão Template Method para rotinas de automação
 */
public abstract class AutomationRoutine implements Observer<HomeEvent> {
    private static final Logger LOGGER = Logger.getLogger(AutomationRoutine.class.getName());

    private final String name;
    private boolean active;

    protected AutomationRoutine(String name) {
        this.name = name;
        this.active = true;

        // Registrar para receber eventos
        HomeCentral.getInstance().getNotificationCenter().addObserver(this);
    }

    /**
     * Método template que define o esqueleto da execução da rotina
     */
    public final String execute() {
        if (!active) {
            return name + ": Routine is currently disabled";
        }

        if (!shouldExecute()) {
            return name + ": Conditions not met for execution";
        }

        StringBuilder result = new StringBuilder();
        result.append(name).append(": Starting routine execution\n");

        try {
            result.append(beforeRoutine()).append("\n");
            result.append(executeRoutineSteps()).append("\n");
            result.append(afterRoutine()).append("\n");

            LOGGER.info("Successfully executed routine: " + name);
            result.append(name).append(": Routine completed successfully");
        } catch (Exception e) {
            LOGGER.warning("Error executing routine " + name + ": " + e.getMessage());
            result.append(name).append(": Error during execution - ").append(e.getMessage());
        }

        return result.toString();
    }

    /**
     * Hook método para determinar se a rotina deve ser executada
     */
    protected boolean shouldExecute() {
        return true;
    }

    /**
     * Hook método para ações antes da rotina principal
     */
    protected String beforeRoutine() {
        return name + ": No pre-processing required";
    }

    /**
     * Método abstrato que as subclasses devem implementar
     */
    protected abstract String executeRoutineSteps();

    /**
     * Hook método para ações após a rotina principal
     */
    protected String afterRoutine() {
        return name + ": No post-processing required";
    }

    /**
     * Recebe eventos do sistema e pode reagir a eles
     */
    @Override
    public void update(HomeEvent event) {
        if (shouldReactToEvent(event)) {
            LOGGER.info(name + ": Reacting to event: " + event.getType());
            reactToEvent(event);
        }
    }

    /**
     * Hook método para determinar se a rotina deve reagir ao evento
     */
    protected boolean shouldReactToEvent(HomeEvent event) {
        return false;
    }

    /**
     * Hook método para definir como reagir a um evento
     */
    protected void reactToEvent(HomeEvent event) {
        // Implementação padrão não faz nada
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

