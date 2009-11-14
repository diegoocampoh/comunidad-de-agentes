/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

/**
 *
 * @author Diego
 */
public class Puntaje implements Comparable{

    private Double puntaje;
    private Documento doc;

    public Puntaje(double puntaje, Documento doc) {
        this.puntaje = puntaje;
        this.doc = doc;
    }

    public Documento getDoc() {
        return doc;
    }

    public void setDoc(Documento doc) {
        this.doc = doc;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    public int compareTo(Object o) {
        Puntaje aComparar = (Puntaje) o;

        if (this.puntaje< aComparar.puntaje) return 1;
        if (this.puntaje == aComparar.puntaje)
        {
            return 0;
        }else
        {

            return -1;
        }

    }

     
}
