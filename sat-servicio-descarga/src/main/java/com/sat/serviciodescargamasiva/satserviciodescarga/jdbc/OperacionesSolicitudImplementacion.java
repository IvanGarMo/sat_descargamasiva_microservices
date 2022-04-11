/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.FiltroBusqueda;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.SolicitudDetalle;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IvanGarMo
 */
@Repository
public class OperacionesSolicitudImplementacion implements OperacionesSolicitud {
    @Autowired
    private JdbcTemplate jdbcTemplate;
   
    @Override
    public Object listaSolicitudes(String uuid, FiltroBusqueda filtro) {
        MyStoredProcedure mp = new MyStoredProcedure(jdbcTemplate, "ListaSolicitudes");
        SqlParameter paramUid = new SqlParameter("_uidUserFirebase", Types.VARCHAR);
        SqlParameter rfcEmisor = new SqlParameter("_rfcSolicitante", Types.VARCHAR);
        SqlParameter fechaInicioPeriodo = new SqlParameter("_fechaInicioPeriodo", Types.DATE);
        SqlParameter fechaFinPeriodo = new SqlParameter("_fechaFinPeriodo", Types.DATE);
        SqlParameter estado = new SqlParameter("_estadoSolicitud", Types.INTEGER);
        SqlParameter idComplemento = new SqlParameter("_idComplemento", Types.INTEGER);
        SqlParameter estadoComprobante = new SqlParameter("_estadoComprobante", Types.INTEGER);
        SqlParameter tipoSolicitud = new SqlParameter("_tipoSolicitud", Types.VARCHAR);
        SqlParameter uid = new SqlParameter("_uid", Types.VARCHAR);
        SqlParameter[] paramArray =  { paramUid, rfcEmisor, fechaInicioPeriodo, fechaFinPeriodo, 
        estado, idComplemento, estadoComprobante, tipoSolicitud, uid };
        mp.setParameters(paramArray);
        mp.compile();
        
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("_uidUserFirebase", uuid);
        inParams.put("_rfcSolicitante", filtro.getRfcSolicitante());
        inParams.put("_fechaInicioPeriodo", filtro.getFechaInicioPeriodo());
        inParams.put("_fechaFinPeriodo", filtro.getFechaFinPeriodo());
        inParams.put("_estadoSolicitud", filtro.getEstadoSolicitud());
        inParams.put("_idComplemento", filtro.getIdComplemento());
        inParams.put("_estadoComprobante", filtro.getEstadoComprobante());
        inParams.put("_tipoSolicitud", filtro.getTipoSolicitud());
        inParams.put("_uid", filtro.getUid());
        
        Map<String, Object> out = mp.execute(inParams);
        return out.get("#result-set-1");
    }
    
    @Override
    public Object listaEstados() {
        MyStoredProcedure mp = new MyStoredProcedure(jdbcTemplate, "listaEstadoSolicitud");
        mp.compile();
        
        Map<String, Object> inParams = new HashMap<>();
        Map<String, Object> out = mp.execute(inParams);
        return out.get("#result-set-1");
    }

    @Override
    public ResponseData guardaSolicitud(Solicitud solicitud) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GuardaSolicitud");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_IdCliente", solicitud.getIdCliente());
        inParam.put("_IdDescargaSat", solicitud.getIdDescarga());
        inParam.put("_RfcSolicitante", solicitud.getRfcSolicitante());
        inParam.put("_RfcEmisor", solicitud.getRfcEmisor());
        inParam.put("_RfcReceptor", solicitud.getRfcReceptor());
        inParam.put("_FechaInicioPeriodo", solicitud.getFechaInicioPeriodo());
        inParam.put("_FechaFinPeriodo", solicitud.getFechaFinPeriodo());
        
        Map<String, Object> out = jdbc.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;
    }

    @Override
    public SolicitudDetalle cargaDetalleSolicitud(long idDescarga) {
        SolicitudDetalle solicitud = new SolicitudDetalle();
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName(("CargaDetalleSolicitud"));
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idDescarga", idDescarga);
        
        Map<String, Object> out = jdbc.execute(inParam);
        solicitud.setIdDescarga(idDescarga);
        solicitud.setIdDescargaSat(out.get("_iddescargasat").toString());
        solicitud.setIdCliente((long) out.get("_idcliente"));
        solicitud.setNombreCliente(out.get("_nombrecliente").toString());
        solicitud.setFechaInicioPeriodo((Date) out.get("_fechainicioperiodo"));
        solicitud.setFechaFinPeriodo((Date) out.get("_fechafinperiodo"));
        solicitud.setRfcEmisor(out.get("_rfcemisor").toString());
        solicitud.setRfcReceptor(out.get("_rfcreceptor").toString());
        solicitud.setRfcSolicitante(out.get("_rfcsolicitante").toString());
        solicitud.setEstado(out.get("_estado").toString());
        solicitud.setNoFacturas((int) out.get("_nofacturas"));
        solicitud.setDescargasPermitidas((int) out.get("_descargaspermitidas"));
        solicitud.setListoParaDescarga((boolean) out.get("_estalista"));
        solicitud.setUrlPaquetes(out.get("_urlpaquetes").toString());
        return solicitud;
    }

    @Override
    public String cargaUrlPaquetes(long idDescarga) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cargaUrlPaquetes");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idDescarga", idDescarga);
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        return outParam.get("_urlpaquetes").toString();
    }
    
    @Override
    public ResponseData guardaUrlPaquete(long idDescarga, String urlPaquete) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("guardaUrlPaquete");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idDescarga", idDescarga);
        inParam.put("_urlPaquetes", urlPaquete);
        
        ResponseData rd = new ResponseData();
        Map<String, Object> outParam = jdbc.execute(inParam);
        rd.setOpValida((boolean) outParam.get(outParam.get("_opvalida")));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }

    @Override
    public void guardaEstadoSolicitud(long idDescarga, int nuevoEstado) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("guardaEstadoSolicitud");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idDescarga", idDescarga);
        inParam.put("_nuevoEstado", nuevoEstado);
        jdbc.execute(inParam);
    }

    @Override
    public Object getComplementos() {
        MyStoredProcedure mp = new MyStoredProcedure(jdbcTemplate, "getSolicitudDescargaComplemento");
        mp.compile();
        
        Map<String, Object> inParams = new HashMap<>();
        Map<String, Object> out = mp.execute(inParams);
        return out.get("#result-set-1");
    }

    @Override
    public Object getSolicitudDescargaEstado() {
        MyStoredProcedure mp = new MyStoredProcedure(jdbcTemplate, "getSolicitudDescargaEstadoComprobante");
        mp.compile();
        
        Map<String, Object> inParams = new HashMap<>();
        Map<String, Object> out = mp.execute(inParams);
        return out.get("#result-set-1");
    }

    @Override
    public Object getSolicitudDescargaTipoSolicitud() {
        MyStoredProcedure mp = new MyStoredProcedure(jdbcTemplate, "getSolicitudDescargaTipoSolicitud");
        mp.compile();
        
        Map<String, Object> inParams = new HashMap<>();
        Map<String, Object> out = mp.execute(inParams);
        return out.get("#result-set-1");
    }

    @Override
    public void guardaReceptorSolicitudDescarga(long idDescarga, String rfcReceptor) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("guardaRfcReceptorSolicitud");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idDescarga", idDescarga);
        inParam.put("_rfcReceptor", rfcReceptor);
        jdbc.execute(inParam);
    }
}

    class MyStoredProcedure extends StoredProcedure {
        public MyStoredProcedure(JdbcTemplate template, String name) {
            super(template, name);
            setFunction(false);
        }
    } 
