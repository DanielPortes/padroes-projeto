package comportamental.exercicio04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Casos de teste para todas as operações e transições do Drone.
 */
public class DroneTest {
    private Drone drone;
    private TestObserver testObserver;

    @BeforeEach
    public void setUp() {
        drone = new Drone();
        testObserver = new TestObserver();
        drone.addObserver(testObserver);
        testObserver.clear(); // Limpa a notificação inicial ("Estado inicial: Idle")
    }

    // Métodos auxiliares para colocar o drone em estados específicos
    private void setStateToTakingOff() {
        drone.takeOff(); // Idle -> TakingOff
        testObserver.clear();
    }

    private void setStateToInFlight() {
        drone.takeOff();
        drone.fly(); // TakingOff -> InFlight
        testObserver.clear();
    }

    private void setStateToLanding() {
        drone.takeOff();
        drone.fly(); // Em voo (InFlight)
        drone.land(); // InFlight -> Landing
        testObserver.clear();
    }

    private void setStateToEmergency() {
        drone.takeOff();
        drone.emergency(); // TakingOff -> Emergency
        testObserver.clear();
    }

    // Testa a notificação inicial do construtor
    @Test
    public void testInitialNotification() {
        TestObserver localObserver = new TestObserver();
        Drone localDrone = new Drone();
        localDrone.addObserver(localObserver);
        List<String> msgs = localObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Estado inicial: Idle", msgs.get(0));
    }

    // Testes para o estado Idle
    @Test
    public void testIdle_takeOff() {
        drone.takeOff();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Iniciando decolagem...", msgs.get(0));
        assertEquals("Transição de estado: TakingOffState", msgs.get(1));
        assertEquals("TakingOffState", drone.getCurrentState());
    }

    @Test
    public void testIdle_land() {
        drone.land();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Drone já está no solo. Aterrissagem desnecessária.", msgs.get(0));
        assertEquals("IdleState", drone.getCurrentState());
    }

    @Test
    public void testIdle_fly() {
        drone.fly();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Não é possível voar. Inicie a decolagem primeiro.", msgs.get(0));
        assertEquals("IdleState", drone.getCurrentState());
    }

    @Test
    public void testIdle_emergency() {
        drone.emergency();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Nenhuma emergência. Drone está inativo.", msgs.get(0));
        assertEquals("IdleState", drone.getCurrentState());
    }

    // Testes para o estado TakingOff
    @Test
    public void testTakingOff_takeOff() {
        setStateToTakingOff();
        drone.takeOff();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Decolagem já em andamento.", msgs.get(0));
        assertEquals("TakingOffState", drone.getCurrentState());
    }

    @Test
    public void testTakingOff_land() {
        setStateToTakingOff();
        drone.land();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Não é possível aterrissar durante a decolagem.", msgs.get(0));
        assertEquals("TakingOffState", drone.getCurrentState());
    }

    @Test
    public void testTakingOff_fly() {
        setStateToTakingOff();
        drone.fly();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Decolagem concluída. Drone agora em voo.", msgs.get(0));
        assertEquals("Transição de estado: InFlightState", msgs.get(1));
        assertEquals("InFlightState", drone.getCurrentState());
    }

    @Test
    public void testTakingOff_emergency() {
        setStateToTakingOff();
        drone.emergency();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Emergência detectada durante a decolagem!", msgs.get(0));
        assertEquals("Transição de estado: EmergencyState", msgs.get(1));
        assertEquals("EmergencyState", drone.getCurrentState());
    }

    // Testes para o estado InFlight
    @Test
    public void testInFlight_takeOff() {
        setStateToInFlight();
        drone.takeOff();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Drone já está em voo.", msgs.get(0));
        assertEquals("InFlightState", drone.getCurrentState());
    }

    @Test
    public void testInFlight_land() {
        setStateToInFlight();
        drone.land();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Iniciando procedimento de aterrissagem.", msgs.get(0));
        assertEquals("Transição de estado: LandingState", msgs.get(1));
        assertEquals("LandingState", drone.getCurrentState());
    }

    @Test
    public void testInFlight_fly() {
        setStateToInFlight();
        drone.fly();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Drone está voando normalmente.", msgs.get(0));
        assertEquals("InFlightState", drone.getCurrentState());
    }

    @Test
    public void testInFlight_emergency() {
        setStateToInFlight();
        drone.emergency();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Emergência em voo! Executando procedimentos de emergência.", msgs.get(0));
        assertEquals("Transição de estado: EmergencyState", msgs.get(1));
        assertEquals("EmergencyState", drone.getCurrentState());
    }

    // Testes para o estado Landing
    @Test
    public void testLanding_takeOff() {
        setStateToLanding();
        drone.takeOff();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Aterrissagem em progresso. Não é possível decolar.", msgs.get(0));
        assertEquals("LandingState", drone.getCurrentState());
    }

    @Test
    public void testLanding_land() {
        setStateToLanding();
        drone.land();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Aterrissagem concluída. Drone no solo.", msgs.get(0));
        assertEquals("Transição de estado: IdleState", msgs.get(1));
        assertEquals("IdleState", drone.getCurrentState());
    }

    @Test
    public void testLanding_fly() {
        setStateToLanding();
        drone.fly();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Aterrissagem em progresso. Não é possível voar.", msgs.get(0));
        assertEquals("LandingState", drone.getCurrentState());
    }

    @Test
    public void testLanding_emergency() {
        setStateToLanding();
        drone.emergency();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Emergência durante aterrissagem!", msgs.get(0));
        assertEquals("Transição de estado: EmergencyState", msgs.get(1));
        assertEquals("EmergencyState", drone.getCurrentState());
    }

    // Testes para o estado Emergency
    @Test
    public void testEmergency_takeOff() {
        setStateToEmergency();
        drone.takeOff();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Emergência ativa! Não é possível decolar.", msgs.get(0));
        assertEquals("EmergencyState", drone.getCurrentState());
    }

    @Test
    public void testEmergency_land() {
        setStateToEmergency();
        drone.land();
        List<String> msgs = testObserver.getMessages();
        assertEquals(2, msgs.size());
        assertEquals("Realizando aterrissagem de emergência.", msgs.get(0));
        assertEquals("Transição de estado: IdleState", msgs.get(1));
        assertEquals("IdleState", drone.getCurrentState());
    }

    @Test
    public void testEmergency_fly() {
        setStateToEmergency();
        drone.fly();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Emergência ativa! Não é possível voar.", msgs.get(0));
        assertEquals("EmergencyState", drone.getCurrentState());
    }

    @Test
    public void testEmergency_emergency() {
        setStateToEmergency();
        drone.emergency();
        List<String> msgs = testObserver.getMessages();
        assertEquals(1, msgs.size());
        assertEquals("Drone já está em estado de emergência.", msgs.get(0));
        assertEquals("EmergencyState", drone.getCurrentState());
    }
}
