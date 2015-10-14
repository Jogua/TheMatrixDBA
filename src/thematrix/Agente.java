package theMatrix;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;

/**
 *
 * @author José Guadix
 */
public class Agente extends SingleAgent {

    private String nombreRebelde;
    private final int INTERROGAR = 0, ESCUCHAR = 1, FIN = 2;
    private int faseActual;
    private boolean terminar;

    public Agente(AgentID aid, String nombreRebelde) throws Exception {
	super(aid);
	this.nombreRebelde = nombreRebelde;
    }

    @Override
    protected void init() {
	faseActual = INTERROGAR;
	terminar = false;
	System.out.println("El agente " + this.getName() + " se ha iniciado.");
	super.init();
    }

    @Override
    public void execute() {
	while (!terminar) {
	    switch (faseActual) {
		case INTERROGAR:
		    faseInterrogar();
		    break;
		case ESCUCHAR:
		    faseEscuchar();
		    break;
		case FIN:
		    faseFin();
		    break;
	    }
	}
    }

    @Override
    public void finalize() {
	System.out.println("El agente " + this.getName() + " ha finalizado.");
	super.finalize();
    }

    private void sendMenssage(AgentID agentID, String content) {
	ACLMessage message = new ACLMessage();
	message.setSender(this.getAid());
	message.setReceiver(agentID);
	message.setContent(content);
	this.send(message);
    }

    private void sendMenssage(String content) {
	this.sendMenssage(new AgentID(nombreRebelde), content);
    }

    private void faseInterrogar() {
	if (Math.random() < 0.3) {  // 30% de finalizar
	    faseActual = FIN;
	} else {			// 70% de hacer pregunta
	    sendMenssage("PREGUNTA");
	    faseActual = ESCUCHAR;
	    System.out.println("El agente " + this.getName() + " ha enviado el token PREGUNTA.");
	}
    }

    private void faseEscuchar() {
	boolean respuestaCorrecta = false, error = false;

	while (!respuestaCorrecta && !error) {
	    try {
		System.out.println("El agente " + this.getName() + " esta esperando la respuesta de " + nombreRebelde);
		ACLMessage message = this.receiveACLMessage();
		System.out.println(this.getName() + " ha recibido " + message.getContent() + " de " + message.getSender().getLocalName());

		if (message.getSender().getLocalName().equals(nombreRebelde) && message.getContent().equals("RESPUESTA")) {
		    respuestaCorrecta = true;
		    faseActual = INTERROGAR;
		}
	    } catch (InterruptedException ex) {
		System.err.println("Error al recibir el mensaje");
		error = true;
		faseActual = FIN;
	    }
	}
    }

    private void faseFin() {
	sendMenssage("LIBERADO");
	System.out.println("El agente " + this.getName() + " ha enviado el token LIBERADO y finaliza.");
	terminar = true;
    }

}
