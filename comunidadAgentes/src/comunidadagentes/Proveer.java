/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

import jade.content.ContentElement;
import jade.content.Predicate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

/**
 * Clase utilizada como predicado.
 * Se utiliza para el comienzo del protocolo Contract Net.
 * El que pide las ofertas utiliza esta clase, pasando las keywords de los
 * documentos que está buscando por medio de este objeto.
 * Los proveedores se basan en estas keywords y setean
 * @author Diego
 */
public class Proveer implements Predicate, ContentElement {

    private List keywords = new ArrayList();
   

    public Proveer()
    {


    }
    public Proveer(String[] keywords)
    {
        

        this.keywords = new ArrayList(keywords.length);
		for (int i = 0; i < keywords.length; i++) {
			this.keywords.add(keywords[i]);
		}
        
    }

    public List getKeywords() {
        return keywords;
    }

    public void setKeywords(List keywords) {
        this.keywords = keywords;
    }

}
