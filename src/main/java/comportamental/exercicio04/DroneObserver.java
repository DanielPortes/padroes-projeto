package comportamental.exercicio04;

import java.util.ArrayList;
import java.util.List;

/*
 * Interface Observer: define o método de atualização que receberá notificações
 */
interface DroneObserver {
    String update(Drone drone, String message);
}
