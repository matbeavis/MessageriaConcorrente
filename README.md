# Messageria Concorrente

Este é um projeto de chat rudimentar utilizando messageria (JMS), desenvolvido como parte de uma atividade auto-instrucional na Universidade FUMEC, na disciplina de Desenvolvimento de Sistemas Distribuídos, ministrada pelo Professor Flávio Velloso Laper.

## Detalhes da Atividade

- **Data:** 16 de maio de 2023
- **Universidade:** Universidade FUMEC
- **Faculdade:** FACE
- **Curso:** Ciência da Computação
- **Turmas:** 6DA e 6NA

## Descrição da Atividade

Esta atividade consiste na criação de um chat rudimentar utilizando messageria (JMS). O projeto deve conter dois aplicativos:

1. **Aplicativo Cliente:**
   - Interface gráfica para o usuário com os seguintes elementos:
     - Caixa de texto para digitação das mensagens a enviar.
     - Caixa de texto para digitação do código do destinatário da mensagem (no caso de mensagens com destinatários específicos).
     - Área de texto para exibição das mensagens recebidas. As mensagens devem ser exibidas na última linha da área, que deve ser rolada se necessário.
     - Botão para acionar o envio de mensagens (o pressionamento da tecla Enter em qualquer das áreas de texto deve ter o mesmo efeito).
   - Observações:
     - O usuário do aplicativo deve possuir um código (uma string arbitrária) que deve ser informado quando o programa for iniciado. Este código deve ser enviado como um atributo de cada mensagem, e deve ser exibido pelos programas receptores precedendo o corpo da mensagem.
     - Se o emissor digitar um código de usuário (no campo apropriado), a mensagem deve ser enviada apenas para o usuário indicado. Caso contrário, todos os usuários devem receber a mensagem.

2. **Aplicativo Servidor:**
   - Responsável por distribuir as mensagens para os usuários pertinentes.

## Autores

[]
