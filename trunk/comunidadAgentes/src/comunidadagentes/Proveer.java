/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

import jade.content.ContentElement;
import jade.content.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilizada como predicado.
 * Se utiliza para el comienzo del protocolo Contract Net.
 * El que pide las ofertas utiliza esta clase, pasando las keywords de los
 * documentos que está buscando por medio de este objeto.
 * Los proveedores se basan en estas keywords y setean
 * @author Diego
 */
public class Proveer implements Predicate, ContentElement {

    private List<String> keywords = new ArrayList<String>();
   

    public Proveer()
    {


    }
    public Proveer(List<String> keywords)
    {
        
        
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

}
