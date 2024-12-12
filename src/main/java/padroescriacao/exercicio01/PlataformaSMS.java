package padroescriacao.exercicio01;

public class PlataformaSMS implements Plataforma {
    @Override
    public void enviarMensagem(String mensagem) {
        System.out.println("Enviando SMS: " + mensagem);
    }
}

