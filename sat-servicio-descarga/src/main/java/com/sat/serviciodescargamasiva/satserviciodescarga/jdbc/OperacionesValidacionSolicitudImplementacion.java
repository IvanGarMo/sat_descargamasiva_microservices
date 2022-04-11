/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author IvanGarMo
 */
@Service
@Slf4j
public class OperacionesValidacionSolicitudImplementacion implements OperacionesValidacionSolicitud {
    private final String FECHA_FORMATO = "yyyy-MM-dd";
    private final String PATRON_RFC = "[A-Z]{4}[0-9]{6}[A-Z0-9]{3}";
    private final String FECHA_POR_DEFECTO = "1000-01-01";
    private final String FECHA_INICIO_SERVICIO = "2004-01-01";
    private final String RFC_NO_VALIDA = "Una de las RFC no es valida";
    private final String RFC_CLIENTE_NO_PRESENTE = "No esta presente la RFC del solicitante";
    private final String FECHA_INICIO_NO_PRESENTE = "No se ha especificado la fecha de inicio";
    private final String FECHA_FINAL_NO_PRESENTE = "No se ha especificado la fecha de final";
    private final String FECHA_INICIO_POSTERIOR = "La fecha de inicio no puede ser posterior a la fecha final";
        
    @Override
    public ResponseData esValidaSolicitud(Solicitud solicitud) throws ParseException {
        ResponseData rd = new ResponseData();
        String rfcCliente = solicitud.getRfcSolicitante();
        String rfcEmisor = solicitud.getRfcEmisor();
        String rfcReceptor = solicitud.getRfcReceptor();
        
        //El rfc del cliente debe ser o emisor o receptor de la factura
        if (rfcCliente.equalsIgnoreCase(rfcEmisor) || rfcCliente.equalsIgnoreCase(rfcReceptor)) {
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
        
        log.info("rfcReceptor: "+rfcReceptor);
        log.info("rfcReceptor - isBlank: "+rfcReceptor.isBlank());
        log.info("rfcReceptor - matches: "+rfcReceptor.matches(PATRON_RFC));
        if(!rfcReceptor.isBlank() && !rfcReceptor.toUpperCase().matches(PATRON_RFC)) {
            rd.setOpValida(false);
            rd.setMensaje(RFC_NO_VALIDA);
            return rd;
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
        
        rd.setOpValida(true);
        return rd;
    }
}
