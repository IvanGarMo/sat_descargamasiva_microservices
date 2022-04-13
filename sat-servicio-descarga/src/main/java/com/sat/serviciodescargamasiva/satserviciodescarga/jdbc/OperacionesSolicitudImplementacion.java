/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author IvanGarMo
 */
@Repository
@Slf4j
public class OperacionesSolicitudImplementacion implements OperacionesSolicitud {
    @Autowired
    private JdbcTemplate jdbcTemplate;
   
    @Override
    public Object listaSolicitudes(String uuid, FiltroBusqueda filtro) {
        MyStoredProcedure mp = new MyStoredProcedure(jdbcTemplate, "ListaSolicitudes");
        SqlParameter paramUid = new SqlParameter("_uidUserFirebase", Types.VARCHAR);
        SqlParameter rfcEmisor = new SqlParameter("_rfcSolicitante", Types.VARCHAR);
        SqlParameter fechaInicioPeriodo = new SqlParameter("_fechaInicioPeriodo", Types.VARCHAR);
        SqlParameter fechaFinPeriodo = new SqlParameter("_fechaFinPeriodo", Types.VARCHAR);
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
        MyStoredProcedure mp = new MyStoredProcedure(jdbcTemplate, "estadoListaSolicitud");
        mp.compile();
        
        Map<String, Object> inParams = new HashMap<>();
        Map<String, Object> out = mp.execute(inParams);
        return out.get("#result-set-1");
    }

    @Override
    public long guardaSolicitud(Solicitud solicitud) throws ParseException {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GuardaSolicitud");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", solicitud.getIdCliente());
        inParam.put("_idDescargaSat", solicitud.getIdDescarga());
        inParam.put("_rfcSolicitante", solicitud.getRfcSolicitante());
        inParam.put("_rfcEmisor", solicitud.getRfcEmisor());
        inParam.put("_fechaInicioPeriodo", solicitud.getFechaInicioPeriodo());
        inParam.put("_fechaFinPeriodo", solicitud.getFechaFinPeriodo());
        inParam.put("_estado", solicitud.getEstadoSolicitud());
        inParam.put("_esUidSolicitado", solicitud.isEsUidSolicitado());
        inParam.put("_uid", solicitud.getUid());
        inParam.put("_idComplemento", solicitud.getComplemento());
        inParam.put("_estadoComprobante", solicitud.getEstadoComprobante());
        inParam.put("_tipoSolicitud", solicitud.getTipoSolicitud());
        
        
        Map<String, Object> out = jdbc.execute(inParam);
        out.keySet().forEach(k -> {
            log.info("Key: "+k);
            log.info("Value: "+out.get(k).toString());
        });
        return ((long) out.get("_iddescarga"));
    }

    @Override
    public SolicitudDetalle cargaDetalleSolicitud(long idDescarga) throws ParseException, JsonProcessingException {
        SolicitudDetalle solicitud = new SolicitudDetalle();
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName(("CargaDetalleSolicitud"));
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idDescarga", idDescarga);
        
        Map<String, Object> out = jdbc.execute(inParam);
        solicitud.setIdDescarga(idDescarga);
        solicitud.setIdDescargaSat(out.get("_iddescargasat").toString());
        solicitud.setIdCliente((long) out.get("_idcliente"));
        solicitud.setNombreCliente(out.get("_nombrecliente").toString());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInicioPeriodo = out.get("_fechainicioperiodo").toString();
        solicitud.setFechaInicioPeriodo(sdf.parse(fechaInicioPeriodo));
        
        String fechaFinPeriodo = out.get("_fechafinperiodo").toString();
        solicitud.setFechaFinPeriodo(sdf.parse(fechaFinPeriodo));
        solicitud.setRfcSolicitante(out.get("_rfcsolicitante").toString());
        
        String json;
        if(out.get("_rfcreceptores") == null) { //Lo regreso como nulo de la BD debido a errores de procesamiento allí
            json = "";
        } else {
            json = out.get("_rfcreceptores").toString();
        }
        List<String> rfcReceptores = new ArrayList<>();
        if(!json.isEmpty() && !json.isBlank()) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ObjetoJson> listReceptores = objectMapper.readValue(json, new TypeReference<List<ObjetoJson>>(){});
            listReceptores.forEach(objetoJson -> {
                rfcReceptores.add(objetoJson.getRfcReceptor());
            });
        }
        solicitud.setRfcReceptores(rfcReceptores);
        solicitud.setNoFacturas(out.get("_nofacturas").toString());
        solicitud.setEsUid((boolean) out.get("_esuidsolicitado"));
        solicitud.setUid(out.get("_uid").toString());
        solicitud.setIdEstadoSolicitud((int) out.get("_idestado"));
        solicitud.setDescripcionEstadoSolicitud(out.get("_descripcionestado").toString());
        solicitud.setComplemento(out.get("_complemento").toString());
        solicitud.setEstadoComprobante(out.get("_estadocomprobante").toString());
        solicitud.setTipoSolicitud(out.get("_tiposolicitud").toString());

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
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GuardaReceptor");
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

@Data
class ObjetoJson {
    private String rfcReceptor;
    private long idDescarga;
}
