package ec.edu.utn.service;

import ec.edu.utn.model.EstadisticaSeleccion;
import ec.edu.utn.model.Partido;
import ec.edu.utn.model.PosicionTabla;
import ec.edu.utn.model.Seleccion;
import ec.edu.utn.repository.PartidoRepository;
import ec.edu.utn.repository.SeleccionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class EstadisticaService {

    @Inject
    private PartidoRepository partidoRepo;

    @Inject
    private SeleccionRepository seleccionRepo;

    // Calcula la tabla de posiciones completa de un grupo
    public List<PosicionTabla> calcularPosiciones(String codigoGrupo) {
        List<Seleccion> selecciones = seleccionRepo.listarPorGrupo(codigoGrupo);
        Map<Long, PosicionTabla> tabla = new LinkedHashMap<>();

        for (Seleccion s : selecciones) {
            tabla.put(s.getId(), new PosicionTabla(s));
        }

        List<Partido> finalizados = partidoRepo.listarFinalizadosPorGrupo(codigoGrupo);

        for (Partido p : finalizados) {
            aplicarResultado(tabla, p);
        }

        return tabla.values().stream()
                .sorted(Comparator.comparingInt(PosicionTabla::getPuntos).reversed()
                        .thenComparing(Comparator.comparingInt(PosicionTabla::getDiferenciaGoles).reversed())
                        .thenComparing(Comparator.comparingInt(PosicionTabla::getGolesFavor).reversed()))
                .collect(Collectors.toList());
    }

    private void aplicarResultado(Map<Long, PosicionTabla> tabla, Partido p) {
        PosicionTabla local = tabla.get(p.getSeleccionLocal().getId());
        PosicionTabla visitante = tabla.get(p.getSeleccionVisitante().getId());

        if (local == null || visitante == null) {
            return; // por seguridad, si alguna selección no pertenece al grupo (no debería pasar)
        }

        int golesLocal = p.getGolesLocal();
        int golesVisitante = p.getGolesVisitante();

        local.setPartidosJugados(local.getPartidosJugados() + 1);
        visitante.setPartidosJugados(visitante.getPartidosJugados() + 1);

        local.setGolesFavor(local.getGolesFavor() + golesLocal);
        local.setGolesContra(local.getGolesContra() + golesVisitante);
        visitante.setGolesFavor(visitante.getGolesFavor() + golesVisitante);
        visitante.setGolesContra(visitante.getGolesContra() + golesLocal);

        if (golesLocal > golesVisitante) {
            local.setGanados(local.getGanados() + 1);
            local.setPuntos(local.getPuntos() + 3);
            visitante.setPerdidos(visitante.getPerdidos() + 1);
        } else if (golesLocal < golesVisitante) {
            visitante.setGanados(visitante.getGanados() + 1);
            visitante.setPuntos(visitante.getPuntos() + 3);
            local.setPerdidos(local.getPerdidos() + 1);
        } else {
            local.setEmpatados(local.getEmpatados() + 1);
            local.setPuntos(local.getPuntos() + 1);
            visitante.setEmpatados(visitante.getEmpatados() + 1);
            visitante.setPuntos(visitante.getPuntos() + 1);
        }
    }

    // Calcula las estadísticas de una selección específica (todos sus partidos finalizados)
    public Optional<EstadisticaSeleccion> calcularEstadisticas(Long seleccionId) {
        Optional<Seleccion> seleccionOpt = seleccionRepo.buscarPorId(seleccionId);
        if (seleccionOpt.isEmpty()) {
            return Optional.empty();
        }

        EstadisticaSeleccion estadistica = new EstadisticaSeleccion(seleccionOpt.get());
        List<Partido> finalizados = partidoRepo.listarFinalizadosPorSeleccion(seleccionId);

        for (Partido p : finalizados) {
            boolean esLocal = p.getSeleccionLocal().getId().equals(seleccionId);
            int golesPropios = esLocal ? p.getGolesLocal() : p.getGolesVisitante();
            int golesRival = esLocal ? p.getGolesVisitante() : p.getGolesLocal();

            estadistica.setPartidosJugados(estadistica.getPartidosJugados() + 1);
            estadistica.setGolesFavor(estadistica.getGolesFavor() + golesPropios);
            estadistica.setGolesContra(estadistica.getGolesContra() + golesRival);

            if (golesPropios > golesRival) {
                estadistica.setGanados(estadistica.getGanados() + 1);
            } else if (golesPropios < golesRival) {
                estadistica.setPerdidos(estadistica.getPerdidos() + 1);
            } else {
                estadistica.setEmpatados(estadistica.getEmpatados() + 1);
            }
        }

        return Optional.of(estadistica);
    }
}