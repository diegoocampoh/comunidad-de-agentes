/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

import jade.content.Concept;
import jade.content.ContentElement;
import java.util.List;

/**
 *
 * @author Diego
 */
public class Resultado implements Concept, ContentElement{

    private List<Documento> documentos;

    public Resultado() {
    }

    public Resultado(List documentos) {
        this.documentos = documentos;
    }

    public List getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List documentos) {
        this.documentos = documentos;
    }




}
