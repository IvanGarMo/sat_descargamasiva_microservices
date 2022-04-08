/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IvanGarMo
 */
@Repository
public class OperacionesSolicitudImplementacion implements OperacionesSolicitud {
    @Autowired
    private JdbcTemplate jdbc;
    
    @Override
    public ResponseData guardaSolicitud(Solicitud solicitud) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("GuardaSolicitud");
        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("_IdCliente", solicitud.getIdCliente());
        inParamMap.put("_IdDescargaSat", solicitud.getIdDescargaSat());
        inParamMap.put("_RfcSolicitante", solicitud.getRfcSolicitante());
        inParamMap.put("_RfcEmisor", solicitud.getRfcEmisor());
        inParamMap.put("_RfcReceptor", solicitud.getRfcReceptor());
        inParamMap.put("_FechaInicioPeriodo", solicitud.getFechaInicioPeriodo());
        inParamMap.put("_FechaFinPeriodo", solicitud.getFechaFinPeriodo());
        
        Map<String, Object> out = jdbcCall.execute(inParamMap);
        ResponseData rd = new ResponseData();
        rd.setActivo((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;
    }

    @Override
    public Solicitud cargaDetalleSolicitud(long idDescarga) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("CargaDetalleSolicitud");
        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("_IdDescarga", idDescarga);
        
        Map<String, Object> out = jdbcCall.execute(inParamMap);
        Solicitud solicitud = new Solicitud();
        solicitud.setIdDescargaSat(out.get("_iddescargasat").toString());
        solicitud.setIdCliente((long) out.get("_idcliente"));
        solicitud.setFechaInicioPeriodo(out.get("_fechainicioperiodo").toString());
        solicitud.setFechaFinPeriodo(out.get("_fechafinperiodo").toString());
        solicitud.setRfcEmisor(out.get("_rfcemisor").toString());
        solicitud.setRfcReceptor(out.get("_rfcreceptor").toString());
        solicitud.setRfcSolicitante(out.get("_rfcsolicitante").toString());
        solicitud.setEstado((int) out.get("_estado"));
        solicitud.setNoFacturas((int) out.get("_nofacturas"));
        solicitud.setDescargasPermitidas((int) out.get("_descargaspermitidas"));
        return solicitud;
    }
    
}
