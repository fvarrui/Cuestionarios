package fvarrui.cuestionarios.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Hoja {

	private String nombre;
	private String respuestas;
	
	// datos calculados
	private Correccion correccion;

	public Hoja() {
	}

	public Hoja(String nombre, String respuestas) {
		this.nombre = nombre;
		this.respuestas = respuestas;
	}

	@XmlAttribute
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@XmlAttribute
	public String getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(String respuestas) {
		this.respuestas = respuestas;
	}

	public Correccion getCorreccion() {
		return correccion;
	}
	
	public void corregir(String solucion, Integer limite, Integer penalizacion) {
		correccion = new Correccion(); 

		while (respuestas.length() < solucion.length()) {
			respuestas += "-";
		}
		
		int anuladas = 0;
					
		for (int i = 0; i < respuestas.length(); i++) {
			int numPregunta = i + 1;
			if (solucion.length() >= respuestas.length()) {
				
				if (solucion.charAt(i) != 'x' && solucion.charAt(i) != 'X') { // no contamos la pregunta anulada
					String pregunta = "" + respuestas.charAt(i);
					if (pregunta.equals("-")) {
						correccion.getSinContestar().add(numPregunta);
					} else {
						boolean esCorrecta = pregunta.equalsIgnoreCase("" + solucion.charAt(i));
						if (esCorrecta) {
							correccion.getCorrectas().add(numPregunta);
						} else {
							correccion.getFallos().add(numPregunta);
						}						
					}
					
				} else {
					anuladas++;
				}
					
			}
					
		}
		
		Integer descuento = (penalizacion == 0) ? 0 : (int) Math.floor(correccion.getFallos().size() / penalizacion);
		Integer calificacion = correccion.getCorrectas().size() - descuento;
			
		correccion.setPenalizacion(descuento);
		correccion.setCalificacion(calificacion);
		
		int total = solucion.length() - anuladas;
		limite -= anuladas;
			
		int sobre100;
		if (calificacion >= limite) {
			sobre100 = ((calificacion - limite) * 100 / ((total - limite) * 2)) + 50;
		} else {
			sobre100 = (calificacion * 100) / (limite * 2);
		}
		if (sobre100 < 0) sobre100 = 0;
			
		correccion.setCalificacionSobre100(sobre100);

	}

	@Override
	public String toString() {
		return "hoja { nombre: " + nombre + ", respuestas(" + respuestas.length() + "): " + respuestas + " }";
	}

}
