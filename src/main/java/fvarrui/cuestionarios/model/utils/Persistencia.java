package fvarrui.cuestionarios.model.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fvarrui.cuestionarios.model.Cuestionario;

public class Persistencia {

	public static void guardar(Cuestionario cuestionario, File destino) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Cuestionario.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(cuestionario, destino);
	}
	
	public static Cuestionario leer(File origen) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Cuestionario.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (Cuestionario) unmarshaller.unmarshal(origen);
	}
	
}
