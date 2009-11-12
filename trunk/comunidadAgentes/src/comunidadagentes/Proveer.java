/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

import jade.content.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Diego
 */
public class Proveer implements Predicate {

    private List<String> keywords = new ArrayList<String>();
    private int puntaje;

    public Proveer()
    {

    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

   



  

}
