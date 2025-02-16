package padroescriacao.exercicio01.factory;

import padroescriacao.exercicio01.bridge.Message;
import padroescriacao.exercicio01.bridge.Sender;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MessageFactory {
    public static Message createMessage(String type, Sender sender, String content) {
        try {
            String className = "padroescriacao.exercicio01.bridge." + type + "Message";
            Class<?> clazz = Class.forName(className);
            Method createMethod = clazz.getDeclaredMethod("create", Sender.class, String.class);
            return (Message) createMethod.invoke(null, sender, content);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar mensagem do tipo: " + type, e);
        }
    }
}