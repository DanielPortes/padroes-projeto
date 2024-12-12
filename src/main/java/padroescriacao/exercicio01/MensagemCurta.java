package padroescriacao.exercicio01;

public class MensagemCurta extends Mensagem {
    public MensagemCurta(Plataforma plataforma) {
        super(plataforma);
    }

    @Override
    public void enviar(String texto) {
        plataforma.enviarMensagem("Curta: " + texto);
    }
}
