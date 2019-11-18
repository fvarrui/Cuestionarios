package fvarrui.cuestionarios.model;

import java.util.ArrayList;
import java.util.List;

public class Correccion {

	private List<Integer> correctas = new ArrayList<Integer>();
	private List<Integer> sinContestar = new ArrayList<Integer>();
	private List<Integer> fallos = new ArrayList<Integer>();
	private Integer penalizacion;
	private Integer calificacion;
	private Integer calificacionSobre100;
	private Nota nota;

	public Integer getPenalizacion() {
		return penalizacion;
	}

	public void setPenalizacion(Integer penalizacion) {
		this.penalizacion = penalizacion;
	}

	public List<Integer> getCorrectas() {
		return correctas;
	}

	public List<Integer> getSinContestar() {
		return sinContestar;
	}

	public List<Integer> getFallos() {
		return fallos;
	}

	public Integer getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

	public Integer getCalificacionSobre100() {
		return calificacionSobre100;
	}

	public void setCalificacionSobre100(Integer calificacionSobre100) {
		this.calificacionSobre100 = calificacionSobre100;
	}

	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

}
