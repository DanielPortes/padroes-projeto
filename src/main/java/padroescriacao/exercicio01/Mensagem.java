package padroescriacao.exercicio01;

public abstract class Mensagem {
    protected Plataforma plataforma;

    protected Mensagem(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    public abstract void enviar(String texto);
}
