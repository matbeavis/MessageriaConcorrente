package aii_messageria;
/*
 * Classe Servidor
 * 
 * Descrição: 
 * Esta classe representa um servidor de mensageria que lida com o envio e recebimento de mensagens de chat.
 * O servidor utiliza a API JMS (Java Message Service) para se comunicar com os clientes.
 * Ele utiliza filas e tópicos JMS para enviar e receber mensagens.
 * 
 * Funcionalidades:
 * - Enviar mensagem de chat para um destinatário específico.
 * - Enviar mensagem de chat para todos os participantes.
 * - Iniciar o recebimento de mensagens.
 * 
 * Utilização:
 * - Crie uma instância da classe Servidor, passando o código do usuário como parâmetro no construtor.
 * - Utilize os métodos disponíveis para enviar mensagens e iniciar o recebimento de mensagens.
 * 
 * Observações:
 * - A classe utiliza o contexto JNDI para obter as conexões e recursos necessários para a comunicação JMS.
 * - Os recursos utilizados são obtidos através de lookup no contexto JNDI.
 * - O servidor utiliza as classes QueueSender e TopicPublisher para enviar mensagens JMS.
 * - O servidor utiliza as classes QueueReceiver e TopicSubscriber para receber mensagens JMS.
 * - A classe MensagemListener implementa a interface MessageListener e é responsável por tratar as mensagens recebidas.
 * 
 * Dependências:
 * - É necessário ter uma implementação do JMS disponível no classpath.
 * - É necessário configurar os recursos JMS (filas, tópicos, fábrica de conexões) no servidor de aplicação ou no ambiente de execução.
 * - As configurações dos recursos JMS devem ser atualizadas de acordo com o ambiente de execução.
 * 
 * Autores: Matheus Figueiredo, Pedro Vasconcelos, Henrique Martins
 * Data: 18/06/2023
 */
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

// TODO: Auto-generated Javadoc
/**
 * The Class Servidor.
 */
public class Servidor {

    /** The codigo usuario. */
    private String codigoUsuario;
    
    /** The queue sender. */
    private QueueSender queueSender;
    
    /** The topic publisher. */
    private TopicPublisher topicPublisher;

    /**
     * Instantiates a new servidor.
     *
     * @param codigoUsuario the codigo usuario
     */
    public Servidor(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    /**
     * Enviar mensagem chat.
     *
     * @param mensagem the mensagem
     * @param codigoEmissor the codigo emissor
     * @param codigoDestinatario the codigo destinatario
     */
    public synchronized void enviarMensagemChat(String mensagem, String codigoEmissor, String codigoDestinatario) {
        Context jndiContext = null;

        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            System.exit(-1);
        }

        ConnectionFactory connectionFactory = null;
        Queue queue = null;

        try {
            connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            queue = (Queue) jndiContext.lookup("FilaChat");
        } catch (NamingException e) {
            System.exit(-1);
        }

        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            if (queueSender == null) {
                queueSender = session.createSender(queue);
            }

            TextMessage message = session.createTextMessage();
            message.setStringProperty("KEY", codigoDestinatario);
            message.setStringProperty("Usuario", codigoEmissor);
            message.setText(mensagem);
            queueSender.send(message);

        } catch (JMSException e) {

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }

    /**
     * Enviar mensagem chat todos.
     *
     * @param mensagem the mensagem
     * @param codigoEmissor the codigo emissor
     */
    public synchronized void enviarMensagemChatTodos(String mensagem, String codigoEmissor) {
        Context jndiContext = null;

        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        ConnectionFactory connectionFactory = null;
        Topic topic = null;

        try {
            connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            topic = (Topic) jndiContext.lookup("TopicoChat");
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Cria um TopicPublisher específico para o remetente
            if (topicPublisher == null) {
                topicPublisher = session.createPublisher(topic);
                topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);  // Configura o modo de entrega
            }

            TextMessage message = session.createTextMessage();
            message.setText(mensagem);
            message.setStringProperty("Usuario", codigoEmissor);
            topicPublisher.publish(message);

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }

    /**
     * Iniciar recebimento mensagens.
     *
     * @param chatCliente the chat cliente
     */
    public void iniciarRecebimentoMensagens(ChatCliente chatCliente) {
        Context jndiContext = null;

        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        ConnectionFactory connectionFactory = null;
        Queue queue = null;
        Topic topic = null;

        try {
            connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            queue = (Queue) jndiContext.lookup("FilaChat");
            topic = (Topic) jndiContext.lookup("TopicoChat");
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            QueueReceiver queueReceiver = session.createReceiver(queue, "KEY='" + codigoUsuario + "'");
            queueReceiver.setMessageListener(new MensagemListener(chatCliente));

            TopicSubscriber topicSubscriber = session.createSubscriber(topic);
            topicSubscriber.setMessageListener(new MensagemListener(chatCliente));

            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * The listener interface for receiving mensagem events.
     * The class that is interested in processing a mensagem
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addMensagemListener<code> method. When
     * the mensagem event occurs, that object's appropriate
     * method is invoked.
     *
     * @see MensagemEvent
     */
    private class MensagemListener implements MessageListener {

        /** The chat cliente. */
        private ChatCliente chatCliente;

        /**
         * Instantiates a new mensagem listener.
         *
         * @param chatCliente the chat cliente
         */
        public MensagemListener(ChatCliente chatCliente) {
            this.chatCliente = chatCliente;
        }

        /**
         * On message.
         *
         * @param message the message
         */
        public void onMessage(Message message) {
            String usuario;

            try {
                usuario = message.getStringProperty("Usuario");
            } catch (JMSException e1) {
                return;
            }

            try {
                if (message == null) {
                    chatCliente.setTextoRecebidoChat("XXXXXXX", codigoUsuario , "Recebida mensagem inválida");
                } else if (message instanceof TextMessage) {
                    TextMessage msg = (TextMessage) message;
                    chatCliente.setTextoRecebidoChat(usuario, codigoUsuario , msg.getText());
                } else {
                    chatCliente.setTextoRecebidoChat("XXXXXX", codigoUsuario , "Recebida mensagem inválida");
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}