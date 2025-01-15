package padroescriacao.exercicio02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HamburgerTest {

    @Test
    public void testBeefHamburger() {
        Hamburger hamburger = new BeefHamburger();
        assertEquals("Hambúrguer de Carne Bovina", hamburger.getDescription(), "Descrição do hambúrguer base incorreta.");
        assertEquals(8.00, hamburger.getCost(), 0.001, "Custo do hambúrguer base incorreto.");
    }

    @Test
    public void testBeefHamburgerWithCheese() {
        Hamburger hamburger = new BeefHamburger();
        hamburger = new Cheese(hamburger);
        assertEquals("Hambúrguer de Carne Bovina, Queijo", hamburger.getDescription(), "Descrição com queijo incorreta.");
        assertEquals(9.50, hamburger.getCost(), 0.001, "Custo com queijo incorreto.");
    }

    @Test
    public void testBeefHamburgerWithBacon() {
        Hamburger hamburger = new BeefHamburger();
        hamburger = new Bacon(hamburger);
        assertEquals("Hambúrguer de Carne Bovina, Bacon", hamburger.getDescription(), "Descrição com bacon incorreta.");
        assertEquals(10.00, hamburger.getCost(), 0.001, "Custo com bacon incorreto.");
    }

    @Test
    public void testBeefHamburgerWithLettuce() {
        Hamburger hamburger = new BeefHamburger();
        hamburger = new Lettuce(hamburger);
        assertEquals("Hambúrguer de Carne Bovina, Alface", hamburger.getDescription(), "Descrição com alface incorreta.");
        assertEquals(8.80, hamburger.getCost(), 0.001, "Custo com alface incorreto.");
    }

    @Test
    public void testBeefHamburgerWithCheeseAndBacon() {
        Hamburger hamburger = new BeefHamburger();
        hamburger = new Cheese(hamburger);
        hamburger = new Bacon(hamburger);
        assertEquals("Hambúrguer de Carne Bovina, Queijo, Bacon", hamburger.getDescription(), "Descrição com queijo e bacon incorreta.");
        assertEquals(11.50, hamburger.getCost(), 0.001, "Custo com queijo e bacon incorreto.");
    }

    @Test
    public void testBeefHamburgerWithCheeseBaconAndLettuce() {
        Hamburger hamburger = new BeefHamburger();
        hamburger = new Cheese(hamburger);
        hamburger = new Bacon(hamburger);
        hamburger = new Lettuce(hamburger);
        assertEquals("Hambúrguer de Carne Bovina, Queijo, Bacon, Alface", hamburger.getDescription(), "Descrição com queijo, bacon e alface incorreta.");
        assertEquals(12.30, hamburger.getCost(), 0.001, "Custo com queijo, bacon e alface incorreto.");
    }

    @Test
    public void testBeefHamburgerWithLettuceAndCheese() {
        Hamburger hamburger = new BeefHamburger();
        hamburger = new Lettuce(hamburger);
        hamburger = new Cheese(hamburger);
        assertEquals("Hambúrguer de Carne Bovina, Alface, Queijo", hamburger.getDescription(), "Descrição com alface e queijo incorreta.");
        assertEquals(10.30, hamburger.getCost(), 0.001, "Custo com alface e queijo incorreto.");
    }

    @Test
    public void testBeefHamburgerWithNoDecorators() {
        Hamburger hamburger = HamburgerFactory.createBeefHamburger();
        assertEquals("Hambúrguer de Carne Bovina", hamburger.getDescription(), "Descrição sem decoradores incorreta.");
        assertEquals(8.00, hamburger.getCost(), 0.001, "Custo sem decoradores incorreto.");
    }

    @Test
    public void testBeefHamburgerWithMultipleCheese() {
        Hamburger hamburger = new BeefHamburger();
        hamburger = new Cheese(hamburger);
        hamburger = new Cheese(hamburger);
        assertEquals("Hambúrguer de Carne Bovina, Queijo, Queijo", hamburger.getDescription(), "Descrição com múltiplos queijos incorreta.");
        assertEquals(11.00, hamburger.getCost(), 0.001, "Custo com múltiplos queijos incorreto.");
    }

    @Test
    public void testHamburgerFactoryCreatesBeefHamburger() {
        Hamburger hamburger = HamburgerFactory.createBeefHamburger();
        assertInstanceOf(BeefHamburger.class, hamburger, "Factory não criou um BeefHamburger.");
        assertEquals("Hambúrguer de Carne Bovina", hamburger.getDescription(), "Descrição do hambúrguer criado pela factory incorreta.");
        assertEquals(8.00, hamburger.getCost(), 0.001, "Custo do hambúrguer criado pela factory incorreto.");
    }
}
