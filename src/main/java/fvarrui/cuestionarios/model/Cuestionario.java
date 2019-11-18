package fvarrui.cuestionarios.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType
public class Cuestionario {

	private String tema;
	private Date fecha;
	private String solucion;
	private Integer limite = 0;
	private Integer penalizacion = 0;
	private List<Hoja> hojas = new ArrayList<Hoja>();
	
	// datos calculados
	private Integer aprobados;
	private Integer suspendidos;

	@XmlAttribute
	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	@XmlAttribute
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@XmlElement
	public String getSolucion() {
		if (solucion == null) return null;
		return solucion.toUpperCase();
	}

	public void setSolucion(String solucion) {
		this.solucion = solucion;
	}

	@XmlAttribute
	public Integer getLimite() {
		return limite;
	}

	public void setLimite(Integer limite) {
		this.limite = limite;
	}

	public Integer getPenalizacion() {
		return penalizacion;
	}

	public void setPenalizacion(Integer penalizacion) {
		this.penalizacion = penalizacion;
	}

	@XmlElement
	public List<Hoja> getHojas() {
		return hojas;
	}

	public void setHojas(List<Hoja> hojas) {
		this.hojas = hojas;
	}

	public Integer getAprobados() {
		return aprobados;
	}

	public Integer getSuspendidos() {
		return suspendidos;
	}
	
	public Integer getAnuladas() {
		int anuladas = 0;
		for (int i = 0; i < solucion.length(); i++) {
			if (solucion.toUpperCase().charAt(i) == 'X') anuladas++;
		}
		return anuladas;
	}

	public void corregir() {

		for (Hoja hoja : hojas) {
			hoja.corregir(solucion, limite, penalizacion);
		}
		
	}

}
