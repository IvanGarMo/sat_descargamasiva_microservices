/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ContrasenaCertificado;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IvanGarMo
 */
@Repository
@Slf4j
public class OperacionesAutorizacionImplementacion implements OperacionesAutorizacion {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public boolean tieneAccesoCliente(long idCliente, String uidFirebase) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("TieneAccesoCliente");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdCliente", idCliente);
        inParam.put("_UidUserFirebase", uidFirebase);
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        return ((boolean) outParam.get("_tieneacceso"));
    }

    @Override
    public ResponseData tieneContrasena(long idCliente) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("TieneContrasena");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdCliente", idCliente);
      
        Map<String, Object> outParam = jdbc.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_tienecontrasena"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }

    @Override
    public ResponseData tieneCertificado(long idCliente) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("TieneCertificado");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdCliente", idCliente);
      
        Map<String, Object> outParam = jdbc.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_tienecertificado"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }

    @Override
    public String cargaContrasena(long idCliente) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("CargaContrasena");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdCliente", idCliente);
      
        Map<String, Object> outParam = jdbc.execute(inParam);
        return outParam.get("_Contrasena").toString();
    }

    @Override
    public ByteArrayInputStream cargaCertificadoCliente(long idCliente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseData puedeHacerSolicitud(Solicitud solicitud) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PuedeHacerSolicitud");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdCliente", solicitud.getIdCliente());
        inParam.put("_fechaInicioPeriodo", solicitud.getFechaInicioPeriodo());
        inParam.put("_fechaFinPeriodo", solicitud.getFechaFinPeriodo());
        inParam.put("_rfcEmisor", solicitud.getRfcEmisor());
        inParam.put("_rfcReceptor", solicitud.getRfcReceptor());
        log.info(solicitud.toString());
        Map<String, Object> outParam = jdbc.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_opvalida"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }

    @Override
    public boolean tieneAccesoSolicitud(long idDescarga, String uidUser) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("TieneAccesoDetalleSolicitud");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdDescarga", idDescarga);
        inParam.put("_uidUserFirebase", uidUser);
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        return ((boolean) outParam.get("_tieneacceso"));
    }

    @Override
    public ResponseData existeSolicitud(Solicitud solicitud) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }
}
