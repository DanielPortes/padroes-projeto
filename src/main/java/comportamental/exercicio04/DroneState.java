package comportamental.exercicio04;

interface DroneState {
    void takeOff(Drone drone);
    void land(Drone drone);
    void fly(Drone drone);
    void emergency(Drone drone);
}