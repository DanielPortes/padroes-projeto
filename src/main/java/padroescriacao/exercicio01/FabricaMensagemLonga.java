package padroescriacao.exercicio01;

public class FabricaMensagemLonga extends FabricaMensagem {
    @Override
    public Mensagem criarMensagem(Plataforma plataforma) {
        return new MensagemLonga(plataforma);
    }
}

