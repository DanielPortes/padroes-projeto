package trabalhofinal.smarthome.core;

import trabalhofinal.smarthome.observer.HomeEvent;
import trabalhofinal.smarthome.observer.Observable;
import trabalhofinal.smarthome.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationCenter implements Observable<HomeEvent> {
    private final List<Observer<HomeEvent>> observers;
    private final List<HomeEvent> eventHistory;

    public NotificationCenter() {
        this.observers = new CopyOnWriteArrayList<>();
        this.eventHistory = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer<HomeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<HomeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(HomeEvent event) {
        eventHistory.add(event);
        observers.forEach(observer -> observer.update(event));
    }

    public void sendNotification(String message) {
        HomeEvent event = new HomeEvent("System", "Notification", message);
        notifyObservers(event);
    }

    public void sendAlert(String source, String message) {
        HomeEvent event = new HomeEvent(source, "Alert", message);
        notifyObservers(event);
    }

    public List<HomeEvent> getRecentEvents(int count) {
        int size = eventHistory.size();
        return eventHistory.subList(Math.max(0, size - count), size);
    }
}
