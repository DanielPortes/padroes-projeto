package smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.mediator.CentralMediator;
import trabalhofinal.smarthome.mediator.PowerController;
import trabalhofinal.smarthome.mediator.PresenceController;
import trabalhofinal.smarthome.mediator.SmartHomeMediator;
import trabalhofinal.smarthome.mediator.SubSystem;
import trabalhofinal.smarthome.observer.HomeEvent;
import trabalhofinal.smarthome.observer.LoggingObserver;
import trabalhofinal.smarthome.observer.MobileAppNotifier;
import trabalhofinal.smarthome.observer.Observable;
import trabalhofinal.smarthome.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class MediatorAndObserverTest {

    @Test
    public void testCentralMediator() {
        // Criar mediador
        SmartHomeMediator mediator = new CentralMediator();

        // Criar objetos para rastrear eventos recebidos
        List<String> eventsReceived = new ArrayList<>();

        // Criar subsistema de teste
        SubSystem testSystem1 = new SubSystem() {
            @Override
            public String getName() {
                return "Test System 1";
            }

            @Override
            public void receiveNotification(String event, Object data) {
                eventsReceived.add("System 1 received: " + event + " with data: " + data);
            }
        };

        SubSystem testSystem2 = new SubSystem() {
            @Override
            public String getName() {
                return "Test System 2";
            }

            @Override
            public void receiveNotification(String event, Object data) {
                eventsReceived.add("System 2 received: " + event + " with data: " + data);
            }
        };

        // Registrar subsistemas
        mediator.register(testSystem1);
        mediator.register(testSystem2);

        // Enviar notificação do sistema 1
        mediator.notify(testSystem1, "TEST_EVENT", "test data");

        // Verificar que sistema 2 recebeu, mas sistema 1 não (pois ele é o remetente)
        assertEquals(1, eventsReceived.size(), "Only one system should receive the notification");
        assertTrue(eventsReceived.get(0).contains("System 2 received"), "System 2 should receive notification");
        assertTrue(eventsReceived.get(0).contains("TEST_EVENT"), "Notification should contain event type");
        assertTrue(eventsReceived.get(0).contains("test data"), "Notification should contain event data");

        // Limpar eventos
        eventsReceived.clear();

        // Enviar notificação do sistema 2
        mediator.notify(testSystem2, "ANOTHER_EVENT", 42);

        // Verificar que sistema 1 recebeu, mas sistema 2 não
        assertEquals(1, eventsReceived.size(), "Only one system should receive the notification");
        assertTrue(eventsReceived.get(0).contains("System 1 received"), "System 1 should receive notification");
        assertTrue(eventsReceived.get(0).contains("ANOTHER_EVENT"), "Notification should contain event type");
        assertTrue(eventsReceived.get(0).contains("42"), "Notification should contain event data");
    }

    @Test
    public void testMediator_PowerAndPresenceControllers() {
        // Criar mediador
        SmartHomeMediator mediator = new CentralMediator();

        // Criar controladores
        PowerController powerController = new PowerController(mediator);
        PresenceController presenceController = new PresenceController(mediator);

        // Verificar estado inicial
        assertFalse(presenceController.isSomeoneHome(), "No one should be home initially");

        // Criar observer para capturar eventos
        AtomicInteger powerModeChanges = new AtomicInteger(0);

        SubSystem testSystem = new SubSystem() {
            @Override
            public String getName() {
                return "Test Observer";
            }

            @Override
            public void receiveNotification(String event, Object data) {
                if ("POWER_SAVING_MODE".equals(event)) {
                    powerModeChanges.incrementAndGet();
                }
            }
        };

        mediator.register(testSystem);

        // Simular usuário chegando em casa
        presenceController.userArrived();

        // Verificar mudanças
        assertTrue(presenceController.isSomeoneHome(), "Someone should be home");
        assertEquals(1, powerModeChanges.get(), "Power saving mode should change");

        // Simular usuário saindo de casa
        presenceController.userLeft();

        // Verificar mudanças
        assertFalse(presenceController.isSomeoneHome(), "No one should be home");
        assertEquals(2, powerModeChanges.get(), "Power saving mode should change again");

        // Simular violação de segurança
        mediator.notify(testSystem, "SECURITY_BREACH", "Front door");

        // Deve causar mudança no modo de energia
        assertEquals(3, powerModeChanges.get(), "Power saving mode should change for security breach");

        // Testar relatório de uso excessivo de energia
        powerController.reportExcessivePowerUsage();

        // Deve enviar notificação, mas não mudar o modo de energia (observer verifica apenas POWER_SAVING_MODE)
        assertEquals(3, powerModeChanges.get(), "Power saving mode should not change");
    }

    @Test
    public void testObserver() {
        // Criar observable simples para teste
        TestObservable observable = new TestObservable();

        // Contadores para verificar notificações
        AtomicInteger observer1Count = new AtomicInteger(0);
        AtomicInteger observer2Count = new AtomicInteger(0);

        // Criar observers
        Observer<HomeEvent> observer1 = event -> {
            if ("TEST".equals(event.getType())) {
                observer1Count.incrementAndGet();
            }
        };

        Observer<HomeEvent> observer2 = event -> {
            if ("TEST".equals(event.getType())) {
                observer2Count.incrementAndGet();
            }
        };

        // Registrar observers
        observable.addObserver(observer1);
        observable.addObserver(observer2);

        // Enviar evento
        HomeEvent event = new HomeEvent("Test Source", "TEST", "Test Description");
        observable.notifyObservers(event);

        // Verificar que ambos observers receberam
        assertEquals(1, observer1Count.get(), "Observer 1 should receive notification");
        assertEquals(1, observer2Count.get(), "Observer 2 should receive notification");

        // Remover um observer
        observable.removeObserver(observer1);

        // Enviar outro evento
        observable.notifyObservers(event);

        // Verificar que apenas observer2 recebeu
        assertEquals(1, observer1Count.get(), "Observer 1 should not receive after removal");
        assertEquals(2, observer2Count.get(), "Observer 2 should receive again");
    }

    @Test
    public void testLoggingObserver() {
        // Criar observer
        LoggingObserver observer = new LoggingObserver();

        // Criar evento
        HomeEvent event = new HomeEvent("Test Source", "TEST", "Test Description");

        // Chamar update - como não podemos verificar log diretamente,
        // testamos apenas que não lança exceção
        assertDoesNotThrow(() -> observer.update(event), "LoggingObserver should not throw exception");
    }

    @Test
    public void testMobileAppNotifier() {
        // Contadores para verificar notificações
        AtomicBoolean notified = new AtomicBoolean(false);

        // Criar notificador
        MobileAppNotifier notifier = new MobileAppNotifier("testUser", message -> {
            notified.set(true);
        });

        // Verificar ID do usuário
        assertEquals("testUser", notifier.getUserId(), "User ID should be correct");

        // Evento de alerta deve notificar
        HomeEvent alertEvent = new HomeEvent("Test", "Alert", "Test alert");
        notifier.update(alertEvent);
        assertTrue(notified.get(), "Should notify for alert");

        // Resetar flag
        notified.set(false);

        // Evento crítico deve notificar
        HomeEvent criticalEvent = new HomeEvent("Test", "Standard", "critical error");
        notifier.update(criticalEvent);
        assertTrue(notified.get(), "Should notify for critical events");

        // Resetar flag
        notified.set(false);

        // Evento normal não deve notificar
        HomeEvent normalEvent = new HomeEvent("Test", "Standard", "Regular event");
        notifier.update(normalEvent);
        assertFalse(notified.get(), "Should not notify for normal events");
    }

    @Test
    public void testHomeEvent() {
        // Criar evento
        HomeEvent event = new HomeEvent("Test Source", "TEST_TYPE", "Test Description");

        // Verificar atributos
        assertEquals("Test Source", event.getSource(), "Source should match");
        assertEquals("TEST_TYPE", event.getType(), "Type should match");
        assertEquals("Test Description", event.getDescription(), "Description should match");
        assertNotNull(event.getTimestamp(), "Timestamp should be set");

        // Verificar toString
        String eventString = event.toString();
        assertTrue(eventString.contains("TEST_TYPE"), "toString should include type");
        assertTrue(eventString.contains("Test Source"), "toString should include source");
        assertTrue(eventString.contains("Test Description"), "toString should include description");
    }

    /**
     * Observable simples para teste
     */
    private static class TestObservable implements Observable<HomeEvent> {
        private final List<Observer<HomeEvent>> observers = new ArrayList<>();

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
            for (Observer<HomeEvent> observer : observers) {
                observer.update(event);
            }
        }
    }
}