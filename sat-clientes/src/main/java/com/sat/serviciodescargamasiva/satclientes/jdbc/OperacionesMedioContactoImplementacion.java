/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.MedioContacto;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import java.sql.Types;
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
public class OperacionesMedioContactoImplementacion implements OperacionesMedioContacto {
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Object cargaListaMedioContacto() {
        MyStoredProcedure mp = new MyStoredProcedure(jdbc, "listaMedioContacto");
        mp.compile();
        Map<String, Object> storedProcResult = mp.execute();
        return storedProcResult.get("#result-set-1");
    }

    @Override
    public Object cargaListaMedioContactoCliente(long idCliente) {
        MyStoredProcedure mp = new MyStoredProcedure(jdbc, "cargaMedioContactoCliente");
        SqlParameter paramUid = new SqlParameter("_IdCliente", Types.BIGINT);
        SqlParameter[] paramArray = { paramUid };
        mp.setParameters(paramArray);
        mp.compile();
        Map<String, Object> storedProcResult = mp.execute(idCliente);
        return storedProcResult.get("#result-set-1");
    }

    @Override
    public ResponseData insertaMedioContactoCliente(long idCliente, MedioContacto medio) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("insertaMedioContactoCliente");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idMedio", medio.getIdMedio());
        inParam.put("_idCliente", medio.getIdCliente());
        inParam.put("_Descripcion", medio.getDescripcion());
        
        Map<String, Object> outParam = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_opvalida"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }

    @Override
    public ResponseData actualizaMedioContactoCliente(long idCliente, MedioContacto medio) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaMedioContactoCliente");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idMedio", medio.getIdMedioRegistrado());
        inParam.put("_Descripcion", medio.getDescripcion());
        
        Map<String, Object> outParam = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_opvalida"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }

    @Override
    public ResponseData eliminaMedioContactoCliente(long idCliente, MedioContacto medio) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("eliminaMedioContactoCliente");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idMedioRegistrado", medio.getIdMedioRegistrado());
        
        Map<String, Object> outParam = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_opvalida"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }
    
    class MedioContactoStoredProcedure extends StoredProcedure {
        public MedioContactoStoredProcedure(JdbcTemplate template, String name) {
            super(template, name);
            setFunction(false);
        }
    } 
    
}
