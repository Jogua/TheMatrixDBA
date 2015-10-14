package theMatrix;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;

/**
 *
 * @author José Guadix
 */
public class Rebelde extends SingleAgent {

    private final int INTERROGADO = 0, RESPUESTA = 1, FIN = 2;
    private int faseActual;
    private boolean terminar;
    private AgentID aidMessageRecived;

    public Rebelde(AgentID aid) throws Exception {
	super(aid);
    }

    @Override
    protected void init() {
	terminar = false;
	faseActual = INTERROGADO;
	aidMessageRecived = null;
	System.out.println("El rebelde " + this.getName() + " se ha iniciado.");
	super.init();
    }

    @Override
    protected void execute() {
	while (!terminar) {
	    switch (faseActual) {
		case INTERROGADO:
		    faseInterrogado();
		    break;
		case RESPUESTA:
		    faseRespuesta();
		    break;
		case FIN:
		    faseFin();
		    break;
	    }
	}
    }

    @Override
    public void finalize() {
	System.out.println("El rebelde " + this.getName() + " ha finalizado.");
	super.finalize();
    }

    private void sendMenssage(AgentID agentID, String content) {
	ACLMessage message = new ACLMessage();
	message.setSender(this.getAid());
	message.setReceiver(agentID);
	message.setContent(content);
	this.send(message);
    }

    private void faseInterrogado() {
	boolean validToken = false, error = false;
	String content;
	while (!validToken && !error) {
	    try {
		System.out.println("El rebelde " + this.getName() + " esta esperando un token");
		ACLMessage message = this.receiveACLMessage();
		content = message.getContent();
		System.out.println(this.getName() + " ha recibido " + content + " de " + message.getSender().getLocalName());
		switch (content) {
		    case "PREGUNTA":
			aidMessageRecived = message.getSender();
			faseActual = RESPUESTA;
			validToken = true;
			break;
		    case "LIBERADO":
			faseActual = FIN;
			validToken = true;
			break;
		}
	    } catch (InterruptedException ex) {
		System.err.println("Error al recibir el mensaje");
		error = true;
		faseActual = FIN;
	    }
	}
    }

    private void faseRespuesta() {
	sendMenssage(aidMessageRecived, "RESPUESTA");
	System.out.println("El rebelde " + this.getName() + " ha enviado el token RESPUESTA.");
	aidMessageRecived = null;
	faseActual = INTERROGADO;
    }

    private void faseFin() {
	terminar = true;
    }

}
