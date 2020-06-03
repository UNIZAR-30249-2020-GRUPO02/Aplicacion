package com.LS.Aplicacion.Controlador;

import DTO.BusquedaDTO;
import Enum.*;
import DTO.DatosDTO;
import DTO.EquipamientoDTO;
import com.LS.Aplicacion.Mensajeria.Emisor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller("EspacioController")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,
        RequestMethod.DELETE,RequestMethod.PATCH})
@RequestMapping(path="/espacio")
public class EspacioController {

    @Autowired
    Emisor emisor = new Emisor();

    public EspacioController() throws Exception {
    }

    @GetMapping(path = "/getInfo")
    public ResponseEntity<Object> obtenerInformacion(String id) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        String json = "obtenerEspacioPorId," + jsonObject.toString();
        emisor.enviarMensaje(json);
        String response = emisor.recibirMensaje();
        if (response.equals("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(JSONObject.stringToValue(response));
            //return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.readValue(response, EspacioDTO.class));
        }
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Object> buscar(String edificio, String tipoEspacio, String[] equipamiento,
                                         int capacidad, long fechaInicio, long fechaFin, int horaInicio,
                                         int horaFin, Dia[] dias, boolean periodo) throws Exception {

        Timestamp fechaInicioTime = new Timestamp(fechaInicio);

        Timestamp fechaFinTime = new Timestamp(fechaFin);

        List<Dia> diasLista = new ArrayList<>();
        Collections.addAll(diasLista, dias);

        List<EquipamientoDTO> equipamientoLista = new ArrayList<>();
        for (String s : equipamiento) {
            EquipamientoDTO e = new EquipamientoDTO();
            String[] eSplit = s.split(";");
            e.setTipo(TipoEquipamiento.valueOf(eSplit[0]));
            e.setCantidad(Integer.parseInt(eSplit[1]));
            equipamientoLista.add(e);
        }

        BusquedaDTO busqueda = new BusquedaDTO();
        busqueda.setEdificio(edificio);
        busqueda.setTipoEspacio(tipoEspacio);
        busqueda.setCapacidad(capacidad);
        busqueda.setFechaInicio(fechaInicioTime);
        busqueda.setFechaFin(fechaFinTime);
        busqueda.setHoraInicio(horaInicio);
        busqueda.setHoraFin(horaFin);
        busqueda.setEquipamiento(equipamientoLista);
        busqueda.setDias(diasLista);
        busqueda.setPeriodo(periodo);

        ObjectMapper mapper = new ObjectMapper();
        String dto = mapper.writeValueAsString(busqueda);
        String json = "filtrarBusquedaEspacios," + dto;
        System.out.println(json);
        emisor.enviarMensaje(json);
        String response = emisor.recibirMensaje();
        System.out.println(response);
        if (response.equals("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(JSONObject.stringToValue(response));
            //return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.readValue(response, EspacioDTO.class));
        }
    }

    @GetMapping(path = "/getInfoFiltered")
    public ResponseEntity<Object> obtenerPorEdificioYTipo(@RequestBody String edificio, @RequestBody String tipo) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("edificio", edificio);
        jsonObject.put("tipo", tipo);
        String json = "obtenerEspacioPorEdificioYTipo," + jsonObject.toString();
        emisor.enviarMensaje(json);
        String response = emisor.recibirMensaje();
        if (response.equals("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(JSONObject.stringToValue(response));
            //return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.readValue(response, EspacioDTO.class));
        }
    }

    @PatchMapping(path = "/modifySpace")
    public ResponseEntity<Object> modificarDatos(@RequestBody DatosDTO datosDTO) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String dto = mapper.writeValueAsString(datosDTO);
        String json = "modificarEspacio," + dto;
        emisor.enviarMensaje(json);
        String response = emisor.recibirMensaje();
        if (response.equals("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(JSONObject.stringToValue(response));
            //return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.readValue(response, EspacioDTO.class));
        }
    }

    @PatchMapping(path = "/getSpacesBetween")
    public ResponseEntity<Object> obtenerHorarioEntreFechas(@RequestBody String idEspacio, @RequestBody Timestamp inicio, @RequestBody Timestamp fin) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idEspacio", idEspacio);
        jsonObject.put("inicio", inicio);
        jsonObject.put("fin", fin);
        String json = "obtenerHorarioEntreFechas," + jsonObject.toString();
        emisor.enviarMensaje(json);
        String response = emisor.recibirMensaje();
        if (response.equals("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(JSONObject.stringToValue(response));
            //return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.readValue(response, EspacioDTO.class));
        }
    }
}
