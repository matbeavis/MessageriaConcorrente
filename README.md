# MessageriaConcorrente
chat rudimentar utilizando messageria (JMS)
Atividade Auto-Instrucional
16 de maio de 2023
UNIVERSIDADE FUMEC
FACE
CIÊNCIA DA COMPUTAÇÃO
DESENVOLVIMENTO DE SISTEMAS DISTRIBUÍDOS
Professor: Flávio Velloso Laper
Turmas: 6DA e 6NA
Esta atividade consiste na criação de um chat rudimentar utilizando messageria (JMS). Devem ser criados dois aplicativos:
1. O aplicativo cliente deve exibir uma interface gráca para o usuário consistindo (pelo menos) dos seguintes elementos:
(a) Uma caixa de texto para digitação das mensagens a enviar.
(b) Uma caixa de texto para digitação do código do destinatário da mensagem (no caso de mensagens com destinatários especícos).
(c) Uma área de texto para exibição das mensagens recebidas. As mensagens devem ser exibidas na última linha da área, que deve ser rolada
se necessário.
(d) Um botão para acionar o envio de mensagens (o pressionamento da
tecla Enter em qualquer das áreas de texto deve ter o mesmo efeito).
Observações:
(a) O usuário do aplicativo deve possuir um código (uma string arbitrária) que deve ser informado quando o programa for iniciado. Este
código deve ser enviado como um atributo de cada mensagem, e deve
ser exibido pelos programas receptores precedendo o corpo da mensagem.
(b) Se o emissor digitar um código de usuário (no campo apropriado),
a mensagem deve ser enviada apenas para o usuário indicado. Caso
contrário, todos os usuários devem receber a mensagem.
2. O aplicativo servidor deve encarregar-se de distribuir as mensagens para
os usuários pertinentes.
