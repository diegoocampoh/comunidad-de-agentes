/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.Predicate;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsObject;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

/**
 *
 * @author Diego
 */
public class Resultado implements Predicate{

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
        
        this.documentos = new ArrayList(busqueda.size());
		for (int i = 0; i < busqueda.size(); i++) {
			this.documentos.add(busqueda.get(i));
		}
    }

    public List getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List documentos) {
        this.documentos = documentos;
    }

    public boolean isAContentExpression() {
        return false;
    }

    public void setIsAContentExpression(boolean flag) {

    }

    public String getTypeName() {
        return Resultado.class.getName();
    }

    public AbsObject getAbsObject(String name) {
        return null;
    }

    public String[] getNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isGrounded() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCount() {
        return 1;
    }





}
