package padroescriacao.exercicio02;

public class HamburgerFactory {
    public static Hamburger createBeefHamburger() {
        return new BeefHamburger();
    }

//        todo: implementar outros tipos de hamburguers
    public static Hamburger createChickenHamburger() {
        return new BeefHamburger();
    }
}
