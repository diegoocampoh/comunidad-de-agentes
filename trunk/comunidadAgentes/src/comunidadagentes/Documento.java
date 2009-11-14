package comunidadagentes;
import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.util.HashMap;
import java.util.Map;

public final class Documento implements Concept {

    public void setPuntajes(Map<String, Double> puntajes) {
        this.puntajes = puntajes;
    }

	private String titulo;	
	private String autor;
    private String contenido;
	private List categorias;

    private Map<String,Double> puntajes = new HashMap<String, Double>();

	public Documento() {
		super();
    
	}	
	
	/**
     *
     * @param titlulo
     * @param autor
     * @param contenido - Ruta al archivo relativa a la carpeta donde se encuentran
     * los archivos PHP
     */
	public Documento(String titlulo, String autor, String contenido) {
		this.titulo = titlulo;
		this.autor=autor;
                this.contenido=contenido;
        this.categorias = new ArrayList();
   
        }
       
        public void setAutor(String autor) {
		this.autor = autor;
	}
        
	public String getAutor() {
		return autor;
	}
	
	public void setTitulo(String titulo) {
		this.titulo=titulo;
	}
	public String getTitulo() {
		return titulo;
	}
        	
	public void setContenido(String contenido) {
		this.contenido=contenido;
	}
	public String getContenido() {
		return contenido;
	}
        
    public void putCategoria(String categoria) {
    categorias.add(categoria);
	}
	public List getCategorias() {
		return categorias;
	}

    public Boolean esCategoria(String categoria)
    {
        return puntajes.containsKey(categoria);
    }

        public void addPuntajeAPalabra(String palabra, Double punjate)
    {
        if(puntajes.get(palabra)==null)
        {
            puntajes.put(palabra, punjate);

            assert get(palabra) == punjate;
        }
    }

    public Double get(Object key) {
        return puntajes.get(key);
    }

    @Override
    public String toString() {
        return "Titulo: "+ titulo+"\n" +
                "Autor: "+ autor+"\n"+
                "Contenido: "+contenido+ "\n";
    }

    
        

}