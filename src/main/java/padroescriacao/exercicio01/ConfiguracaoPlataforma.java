package padroescriacao.exercicio01;

public class ConfiguracaoPlataforma {
    private static ConfiguracaoPlataforma instancia;
    private Plataforma plataforma;

    private ConfiguracaoPlataforma() {
        // Configuração padrão
        plataforma = new PlataformaEmail();
    }

    public static ConfiguracaoPlataforma getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracaoPlataforma();
        }
        return instancia;
    }

    public Plataforma getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(Plataforma plataforma) {
        this.plataforma = plataforma;
    }
}

