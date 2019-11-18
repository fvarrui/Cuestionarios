package fvarrui.cuestionarios.gen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import fvarrui.cuestionarios.model.Cuestionario;

public class HtmlGenerator {

	public static void generate(File destino, Cuestionario cuestionario) throws IOException {
		// inicializa la API Velocity
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		// contexto de Velocity (datos para las plantillas)
		VelocityContext contexto = new VelocityContext(); 
		contexto.put("cuestionario", cuestionario);
		
		// carga la plantilla correspondiente
		Template plantilla = ve.getTemplate("dad/cuestionarios/gen/templates/web.vtl", "UTF-8");
		
		// crea el FileWriter para guardar el c—digo generado por la plantilla
		FileWriter fw = new FileWriter(destino);
		
		// "ejecuta" la plantilla
		plantilla.merge(contexto, fw);
		
		// cierra el FileWriter
		fw.flush();
		fw.close();

		System.out.println("Web generada: " + destino.getName() + "\n");	
	}
	
}
