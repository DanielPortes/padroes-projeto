package comportamental.exercicio04;


/*
 * Interface Observer: define o método de atualização que receberá notificações
 */
interface DroneObserver {
    String update(Drone drone, String message);
}
