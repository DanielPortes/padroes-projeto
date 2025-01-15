package padroescriacao.exercicio02;

public class BeefHamburger implements Hamburger {
    @Override
    public String getDescription() {
        return "Hambúrguer de Carne Bovina";
    }

    @Override
    public double getCost() {
        return 8.00;
    }
}

