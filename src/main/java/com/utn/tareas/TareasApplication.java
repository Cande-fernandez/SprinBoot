package com.utn.tareas;

import com.utn.tareas.service.MensajeService;
import com.utn.tareas.service.TareaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TareasApplication implements CommandLineRunner {

    private final TareaService tareaService;
    private final MensajeService mensajeService;

    public TareasApplication (TareaService tareaService, MensajeService mensajeService){
        this.tareaService =tareaService;
        this.mensajeService =mensajeService;
    }
	public static void main(String[] args) {
		SpringApplication.run(TareasApplication.class, args);
	}

    @Override
    public void run (String ...args) throws Exception{
        mensajeService.mostrarBienvenida();
        System.out.println("Configuracion: " +tareaService.mostrarConfiguracion());

        System.out.println("--Tareas iniciales --");
        tareaService.listarTodas().forEach(System.out::println);

        System.out.println("--Marcar tarea como completada (id=1) --");
        try {
            tareaService.marcarCompletada(1L);
        }catch (Exception e) {
            System.out.println("Error al marcar completada: " + e.getMessage());
        }

        System.out.println("--Estadisticas --");
        System.out.println(tareaService.estadisticas());

        System.out.println("--Tareas completadas-- ");
        tareaService.listarCompletadas().forEach(System.out::println);

        mensajeService.mostrarDespedida();
    }

}
