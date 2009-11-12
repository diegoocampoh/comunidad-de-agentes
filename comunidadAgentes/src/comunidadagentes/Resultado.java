/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

/**
 *
 * @author Diego
 */
public class Resultado implements Concept, ContentElement{

    private List documentos;

    public Resultado() {
    }

    public Resultado(Documento[] documentos) {

        this.documentos = new ArrayList(documentos.length);
		for (int i = 0; i < documentos.length; i++) {
			this.documentos.add(documentos[i]);
		}

    }

    Resultado(List busqueda) {
        this.documentos = busqueda;
    }

    public List getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List documentos) {
        this.documentos = documentos;
    }




}
