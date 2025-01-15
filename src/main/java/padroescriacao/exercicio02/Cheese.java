package padroescriacao.exercicio02;


public class Cheese extends HamburgerDecorator {
    public Cheese(Hamburger hamburger) {
        super(hamburger);
    }

    @Override
    public String getDescription() {
        return hamburger.getDescription() + ", Queijo";
    }

    @Override
    public double getCost() {
        return hamburger.getCost() + 1.50;
    }
}
