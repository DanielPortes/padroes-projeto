package padroescriacao.exercicio02;

public class Lettuce extends HamburgerDecorator {
    public Lettuce(Hamburger hamburger) {
        super(hamburger);
    }

    @Override
    public String getDescription() {
        return hamburger.getDescription() + ", Alface";
    }

    @Override
    public double getCost() {
        return hamburger.getCost() + 0.80;
    }
}

