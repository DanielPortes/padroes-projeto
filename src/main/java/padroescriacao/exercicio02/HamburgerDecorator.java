package padroescriacao.exercicio02;

public abstract class HamburgerDecorator implements Hamburger {
    protected Hamburger hamburger;

    public HamburgerDecorator(Hamburger hamburger) {
        this.hamburger = hamburger;
    }

    @Override
    public String getDescription() {
        return hamburger.getDescription();
    }

    @Override
    public double getCost() {
        return hamburger.getCost();
    }
}

