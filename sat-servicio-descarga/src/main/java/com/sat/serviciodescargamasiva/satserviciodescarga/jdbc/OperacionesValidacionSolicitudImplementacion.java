/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
/**
 *
 * @author IvanGarMo
 */
@Service
@Slf4j
public class OperacionesValidacionSolicitudImplementacion implements OperacionesValidacionSolicitud {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final String FECHA_FORMATO = "yyyy-MM-dd";
    private final String PATRON_RFC = "[A-Z]{4}[0-9]{6}[A-Z0-9]{3}";
    private final String FECHA_POR_DEFECTO = "1000-01-01";
    private final String FECHA_INICIO_SERVICIO = "2004-01-01";
    private final String RFC_NO_VALIDA = "Una de las RFC no es valida";
    private final String RFC_CLIENTE_NO_PRESENTE = "No esta presente la RFC del solicitante";
    private final String FECHA_INICIO_NO_PRESENTE = "No se ha especificado la fecha de inicio";
    private final String FECHA_FINAL_NO_PRESENTE = "No se ha especificado la fecha de final";
    private final String FECHA_INICIO_POSTERIOR = "La fecha de inicio no puede ser posterior a la fecha final";
    private final String YA_EXISTE_SOLICITUD = "Ya existe una solicitud con estos parámetros";
    private final String RFC_REPETIDO = "Hay RFC receptidos en el campo Receptores";
    
    @Override
    public ResponseData esValidaSolicitud(Solicitud solicitud) throws ParseException, JsonProcessingException {
        ResponseData rd = new ResponseData();
        String rfcCliente = solicitud.getRfcSolicitante();
        String rfcEmisor = solicitud.getRfcEmisor();
        List<String> rfcReceptores = solicitud.getRfcReceptor();
        
        //El rfc del cliente debe ser o emisor o receptor de la factura
        if (rfcCliente.equalsIgnoreCase(rfcEmisor) || rfcCliente.equalsIgnoreCase(rfcReceptores.get(0))) {
            //Verifico que el RFC sea valido, y por lo tanto el otro también lo será
            if(!rfcCliente.toUpperCase().matches(PATRON_RFC)) {
                rd.setOpValida(false);
                rd.setMensaje(RFC_NO_VALIDA);
                return rd;
            }
        } else {
            rd.setOpValida(false);
            rd.setMensaje(RFC_CLIENTE_NO_PRESENTE);
            return rd;
        }
        
        //Puede que haya otras RFC presentes, en ese caso debo verificar que sean válidas también
        if(!rfcEmisor.isBlank() && !rfcEmisor.toUpperCase().matches(PATRON_RFC)) {
            rd.setOpValida(false);
            rd.setMensaje(RFC_NO_VALIDA);
        }
        
        for(String rfc : rfcReceptores) {
            if(!rfc.toUpperCase().matches(PATRON_RFC)) {
                rd.setOpValida(false);
                rd.setMensaje(RFC_NO_VALIDA);
                return rd;
            }
        }
        
        //Luego verifico que las fechas sean válidas. Esto es:
        //-Que ambas sean válidas
        //-Que ambas sean posteriores o iguales al 01-01-2004
        //-Que la fecha de fin de período no sea anterior  a fecha de final de período
        SimpleDateFormat formatter = new SimpleDateFormat(FECHA_FORMATO);
        Date objFechaPorDefecto = formatter.parse(FECHA_POR_DEFECTO);
        Date objFechaInicioServicio = formatter.parse(FECHA_INICIO_SERVICIO);
        Date objFechaInicio = formatter.parse(solicitud.getFechaInicioPeriodo());
        if(objFechaInicio.equals(objFechaPorDefecto) || objFechaInicio.before(objFechaPorDefecto) 
                || objFechaInicio.before(objFechaInicioServicio)) {
            rd.setOpValida(false);
            rd.setMensaje(FECHA_INICIO_NO_PRESENTE);
            return rd;
        }
        
        Date objFechaFinal = formatter.parse(solicitud.getFechaFinPeriodo());
        if(objFechaFinal.equals(objFechaPorDefecto) || objFechaFinal.before(objFechaPorDefecto)
                || objFechaFinal.before(objFechaInicioServicio)) {
            rd.setOpValida(false);
            rd.setMensaje(FECHA_FINAL_NO_PRESENTE);
            return rd;
        }
        
        if(objFechaFinal.before(objFechaInicio)) {
            rd.setOpValida(false);
            rd.setMensaje(FECHA_INICIO_POSTERIOR);
            return rd;
        }
        
        //Luego reviso si no existe una solicitud exactamente igual
        Solicitud solicitudRecuperada = getSolicitud(solicitud);
        
        //Si no existe una solicitud igual, no tiene caso proceder con las validaciones
        if(solicitudRecuperada == null) {
            rd.setOpValida(true);
            return rd;
        }
        
        if(solicitud.esIgual(solicitudRecuperada)) {
            rd.setOpValida(false);
            rd.setMensaje(YA_EXISTE_SOLICITUD);
        }
        
        //Luego verifico que no haya RFCs repetidos, o marcará error
        for(String rfc : solicitud.getRfcReceptor()) {
            if(solicitud.getRfcReceptor().contains(rfc)) {
                rd.setOpValida(false);
                rd.setMensaje(RFC_REPETIDO);
            }
        }
        
        rd.setOpValida(true);
        return rd;
    }
    
    private Solicitud getSolicitud(Solicitud solicitud) throws JsonProcessingException {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PuedeHacerSolicitud");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdCliente", solicitud.getIdCliente());
        inParam.put("_fechaInicioPeriodo", solicitud.getFechaInicioPeriodo());
        inParam.put("_fechaFinPeriodo", solicitud.getFechaFinPeriodo());
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        boolean existeSolicitud = ((boolean) outParam.get("_existeSolicitud"));
        
        if(!existeSolicitud) {
            return null;
        }
        
        Solicitud solicitudRecuperada = new Solicitud();
        solicitudRecuperada.setIdDescarga((long) outParam.get("_idDescarga"));
        solicitudRecuperada.setComplemento(outParam.get("_complemento").toString());
        solicitudRecuperada.setEstadoComprobante(outParam.get("_estadoComprobante").toString());
        solicitudRecuperada.setTipoSolicitud(outParam.get("_tipoSolicitud").toString());
        
        String json = outParam.get("_receptores").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjetoJson> listReceptores = objectMapper.readValue(json, new TypeReference<List<ObjetoJson>>(){});
        
        List<String> rfcReceptores = new ArrayList<>();
        for(ObjetoJson objetoJson: listReceptores) {
            rfcReceptores.add(objetoJson.getRfcReceptor());
        }
        solicitudRecuperada.setRfcReceptor(rfcReceptores);
        return solicitud;
    }
}

@Data
class ObjetoJson {
    private String rfcReceptor;
    private long idDescarga;
}