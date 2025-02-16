package comportamental.exercicio04;


/*
 * Implementação de um Observer: Central de Controle.
 */
class GroundControl implements DroneObserver {
    private String name;

    public GroundControl(String name) {
        this.name = name;
    }

    @Override
    public String update(Drone drone, String message) {
        return ("[" + name + "] " + message);
    }
}
