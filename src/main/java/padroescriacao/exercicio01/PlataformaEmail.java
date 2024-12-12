package padroescriacao.exercicio01;

public class PlataformaEmail implements Plataforma {
    @Override
    public void enviarMensagem(String mensagem) {
        System.out.println("Enviando e-mail: " + mensagem);
    }
}

