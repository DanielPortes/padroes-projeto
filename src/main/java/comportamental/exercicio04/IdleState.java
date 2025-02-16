package comportamental.exercicio04;

class IdleState implements DroneState {
    private static IdleState instance = new IdleState();
    private IdleState() {}
    public static IdleState getInstance() { return instance; }

    @Override
    public void takeOff(Drone drone) {
        drone.notifyObservers("Iniciando decolagem...");
        drone.setState(TakingOffState.getInstance());
    }

    @Override
    public void land(Drone drone) {
        drone.notifyObservers("Drone já está no solo. Aterrissagem desnecessária.");
    }

    @Override
    public void fly(Drone drone) {
        drone.notifyObservers("Não é possível voar. Inicie a decolagem primeiro.");
    }

    @Override
    public void emergency(Drone drone) {
        drone.notifyObservers("Nenhuma emergência. Drone está inativo.");
    }
}
