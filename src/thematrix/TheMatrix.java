package theMatrix;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author José Guadix
 */
public class TheMatrix {

    public static void main(String[] args) throws Exception {
	String nombreAgente = "Smith", nombreRebelde = "Neo";

	AgentsConnection.connect("localhost", 5672, "test", "guest", "guest", false);

	try {
	    Rebelde neo = new Rebelde(new AgentID(nombreRebelde));
	    Agente smith = new Agente(new AgentID(nombreAgente), nombreRebelde);

	    neo.start();
	    smith.start();
	} catch (Exception ex) {
	    System.err.println("Error al crear los agentes.");
	}

    }
}
