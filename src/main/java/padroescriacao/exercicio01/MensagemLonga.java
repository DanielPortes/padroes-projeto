package padroescriacao.exercicio01;

public class MensagemLonga extends Mensagem {
    public MensagemLonga(Plataforma plataforma) {
        super(plataforma);
    }

    @Override
    public void enviar(String texto) {
        plataforma.enviarMensagem("Longa: " + texto);
    }
}
