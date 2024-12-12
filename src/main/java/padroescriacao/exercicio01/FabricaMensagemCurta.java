package padroescriacao.exercicio01;

public class FabricaMensagemCurta extends FabricaMensagem {
    @Override
    public Mensagem criarMensagem(Plataforma plataforma) {
        return new MensagemCurta(plataforma);
    }
}
