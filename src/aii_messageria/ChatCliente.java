package aii_messageria;
/*
 * Classe ChatCliente que representa um cliente para um chat.
 *
 * Protocolo:
 *
 * 1. O cliente estabelece uma conexão com o servidor de chat.
 * 2. O cliente envia mensagens para o servidor de chat, contendo texto e informações sobre o emissor e destinatário.
 * 3. O servidor recebe as mensagens do cliente e encaminha para o destinatário específico ou para todos os participantes.
 * 4. O servidor envia as mensagens recebidas para os clientes destinatários.
 * 5. Os clientes recebem as mensagens do servidor e exibem na interface do chat.
 * 6. O cliente pode enviar mensagens tanto para um destinatário específico quanto para todos os participantes.
 * 7. O cliente pode receber mensagens direcionadas a ele ou mensagens enviadas para todos os participantes.
 * 8. O cliente pode visualizar todas as mensagens enviadas e recebidas no chat.
 *
 * Observação: O cliente utiliza a tecnologia JMS (Java Message Service) para comunicação assíncrona com o servidor de chat.
 * As mensagens são enviadas por meio de uma fila de mensagens ou tópico, dependendo do tipo de comunicação desejada.
 * A classe utiliza programação concorrente com threads para lidar com o envio e recebimento de mensagens de forma simultânea.
 *
 * Execução do programa:
 *
 * 1. O cliente cria uma instância da classe ChatCliente.
 * 2. O cliente estabelece uma conexão com o servidor de chat.
 * 3. O cliente inicia a interface do chat, exibindo a lista de participantes e mensagens anteriores.
 * 4. O cliente permite que o usuário digite mensagens para enviar no chat.
 * 5. O cliente envia as mensagens para o servidor de chat.
 * 6. O cliente recebe as mensagens do servidor de chat e exibe na interface do chat.
 * 7. O cliente continua recebendo e enviando mensagens até que o usuário encerre a aplicação.
 * 8. O cliente encerra a conexão com o servidor de chat e finaliza a execução do programa.
 *
 * Autores: Matheus Figueiredo, Pedro Vasconcelos, Henrique Martins
 * Data: 18/06/2023
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatCliente.
 */
public class ChatCliente implements Runnable {

    /** The saida text area. */
    private JTextArea saidaTextArea;
    
    /** The destino text field. */
    private JTextField entradaTextField, destinoTextField;
    
    /** The codigo destinatario. */
    private static String codigoDestinatario;
    
    /** The codigo remetente. */
    private static String codigoRemetente;
    
    /** The enviar button. */
    private JButton enviarButton;
    
    /** The sair chat button. */
    private JButton sairChatButton;
    
    /** The frame chat. */
    private JFrame frameChat;
    
    /** The usuario menu item. */
    private JMenuItem usuarioMenuItem;
    
    /** The servidor. */
    private Servidor servidor;

    /**
     * Instantiates a new chat cliente.
     */
    public ChatCliente() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        saidaTextArea = new JTextArea(10, 40);
        saidaTextArea.setEditable(false);
        entradaTextField = new JTextField(55);
        entradaTextField.setText("Digite aqui a mensagem que deseja enviar...");
        destinoTextField = new JTextField(12);
        enviarButton = new JButton("ENVIAR");
        sairChatButton = new JButton("SAIR");
        frameChat = new JFrame("CHAT - TRABALHO MESSAGERIA");
        frameChat.setLocationRelativeTo(null);
        sairChatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("desligar_16x16.png")));
        enviarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("message.png")));

        servidor = new Servidor(codigoDestinatario);
        servidor.iniciarRecebimentoMensagens(this);
    }

    /**
     * Iniciar GUI.
     */
    public void iniciarGUI() {
        while (codigoDestinatario == null || codigoDestinatario.equals("")) {
            codigoDestinatario = JOptionPane.showInputDialog("Digite o código do destinatário: ");

            if (codigoDestinatario == null) {
                JOptionPane.showMessageDialog(null, "Chat cancelado pelo usuário.");
                System.exit(0);
            } else if (codigoDestinatario.equals("")) {
                JOptionPane.showMessageDialog(null, "Código do destinatário inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (codigoRemetente == null || codigoRemetente.equals("")) {
            codigoRemetente = JOptionPane.showInputDialog("Digite o seu código: ");

            if (codigoRemetente == null) {
                JOptionPane.showMessageDialog(null, "Chat cancelado pelo usuário.");
                System.exit(0);
            } else if (codigoRemetente.equals("")) {
                JOptionPane.showMessageDialog(null, "Código do remetente inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        usuarioMenuItem = new JMenuItem(codigoDestinatario);
        frameChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChat.setLayout(new BorderLayout());

        JScrollPane chatScrollPane = new JScrollPane(saidaTextArea);

        frameChat.add(chatScrollPane, BorderLayout.CENTER);
        frameChat.add(entradaTextField, BorderLayout.SOUTH);

        JPanel chatPanel = new JPanel();
        Label logado = new Label("Seu Código: " + codigoDestinatario);
        logado.setBackground(Color.yellow);
        Label espacoemBranco2 = new Label("              ");
        Label enviarMsgPara = new Label("Enviar para:");
        chatPanel.add(logado);
        chatPanel.add(enviarMsgPara);
        chatPanel.add(destinoTextField);
        chatPanel.add(enviarButton);
        chatPanel.add(espacoemBranco2);
        chatPanel.add(sairChatButton);
        frameChat.add(chatPanel, BorderLayout.NORTH);
        enviarButton.addActionListener(new SendHandler());
        entradaTextField.addActionListener(new InputHandler());
        sairChatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente sair do chat?", "Sair", JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        frameChat.setSize(600, 250);
        frameChat.setVisible(true);
        entradaTextField.requestFocus();
    }

    /**
     * Sets the texto chat.
     *
     * @param texto the new texto chat
     */
    private void setTextoChat(String texto) {
        saidaTextArea.append(codigoRemetente + ": " + texto + "\n");
        entradaTextField.setText("");
    }

    /**
     * Sets the texto recebido chat.
     *
     * @param remetente the remetente
     * @param codigoDestinatario the codigo destinatario
     * @param texto the texto
     */
    public void setTextoRecebidoChat(String remetente, String codigoDestinatario, String texto) {
        if (codigoDestinatario.equals(this.codigoDestinatario) || codigoDestinatario.equals("todos")) {
            saidaTextArea.append(remetente + " para " + codigoDestinatario + ": " + texto + "\n");
        }
    }

    /**
     * The Class SendHandler.
     */
    private class SendHandler implements ActionListener {
        
        /**
         * Action performed.
         *
         * @param e the e
         */
        public void actionPerformed(ActionEvent e) {
            String texto = entradaTextField.getText();
            String codigoDestinatario = destinoTextField.getText();

            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Você não digitou a mensagem!", "Erro ao enviar", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (codigoDestinatario.isEmpty()) {
                setTextoChat(texto);
                servidor.enviarMensagemChatTodos(texto, codigoRemetente);
            } else {
                setTextoChat(texto);
                servidor.enviarMensagemChat(texto, codigoDestinatario, codigoRemetente);
            }
        }
    }

    /**
     * The Class InputHandler.
     */
    private class InputHandler implements ActionListener {
        
        /**
         * Action performed.
         *
         * @param e the e
         */
        public void actionPerformed(ActionEvent e) {
            String texto = entradaTextField.getText();
            String codigoDestinatario = destinoTextField.getText();

            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Você não digitou a mensagem!", "Erro ao enviar", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (codigoDestinatario.isEmpty()) {
                setTextoChat(texto);
                servidor.enviarMensagemChatTodos(texto, codigoRemetente);
            } else {
                setTextoChat(texto);
                servidor.enviarMensagemChat(texto, codigoDestinatario, codigoRemetente);
            }
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        ChatCliente clienteChat = new ChatCliente();
        clienteChat.iniciarGUI();
    }

    /**
     * Run.
     */
    // Implementação da interface Runnable para uso como thread
    public void run() {
        iniciarGUI();
    }
}
