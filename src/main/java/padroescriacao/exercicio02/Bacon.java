package padroescriacao.exercicio02;

public class Bacon extends HamburgerDecorator {
    public Bacon(Hamburger hamburger) {
        super(hamburger);
    }

    @Override
    public String getDescription() {
        return hamburger.getDescription() + ", Bacon";
    }

    @Override
    public double getCost() {
        return hamburger.getCost() + 2.00;
    }
}

