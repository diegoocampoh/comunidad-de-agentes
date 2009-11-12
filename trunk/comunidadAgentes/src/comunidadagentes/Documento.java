package comunidadagentes;
import jade.content.Concept;
import java.util.List;

public final class Documento implements Concept {

	private String titulo;	
	private String autor;
        private String contenido;
	private List<String> categorias;
	public Documento() {
		super();		
	}	
	
	/**
	 * Constructor de documentos
	 */
	public Documento(String titlulo, String autor, String contenido) {
		this.titulo = titlulo;
		this.autor=autor;
                this.contenido=contenido;
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
	public List<String> getCategorias() {
		return categorias;
	}

    public Boolean esCategoria(String categoria)
    {
        return categorias.contains(categoria);
    }
        

}