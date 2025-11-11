package com.utn.tareas.service;

import com.utn.tareas.model.Prioridad;
import com.utn.tareas.model.Tarea;
import com.utn.tareas.repository.TareaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaService {

    private final TareaRepository repo;

    @Value ("${app.nombre:tareas-app}")
    private String appNombre;

    @Value("${app.max-tareas:100}")
    private int maxTareas;

    @Value("${app.mostrar-estadisticas:true}")
    private boolean mostrarEstadisticas;

    public TareaService (TareaRepository repo){
        this.repo = repo;
    }

    public Tarea agregarTarea (String descripcion, Prioridad prioridad){
        List<Tarea> actuales =repo.findAll();
        if (actuales.size() >= maxTareas){
            throw new IllegalStateException("Se alcanzo el limite maximo de tamaño de tareas: " + maxTareas);
        }
        Tarea t = new Tarea(null, descripcion, false, prioridad);
        return repo.save (t);
    }

    public List <Tarea> listarTodas (){
        return repo.findAll();
    }

    public List <Tarea> listarPendientes (){
        return repo.findAll().stream().filter( t-> !t.isCompletada()).collect(Collectors.toList());
    }

    public List <Tarea> listarCompletadas (){
        return repo.findAll().stream().filter(Tarea::isCompletada).collect(Collectors.toList());
    }

    public Tarea marcarCompletada (Long id){
        Tarea tarea = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + id));
        tarea.setCompletada(true);
        repo.save (tarea);
        return tarea;
    }

    public String estadisticas (){
        if (!mostrarEstadisticas) return "Estadisticas deshabilitadas";
        List <Tarea> all = repo.findAll();
        long total = all.size();
        long completadas = all.stream().filter (Tarea::isCompletada).count();
        long pendientes = total - completadas;
        return String.format ("Total: %d,  Completadas: %d, Pendientes: %d", total, completadas, pendientes);
    }

    public String mostrarConfiguracion (){
        return String.format("app.nombre=%s, app.max-tareas= %d, app.mostrar-estadisticas=%b", appNombre, maxTareas, mostrarEstadisticas);
    }
}
