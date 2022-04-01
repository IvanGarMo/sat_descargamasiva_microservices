/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satsuscripciones.jdbc;

import com.sat.serviciodescargamasiva.satsuscripciones.data.ResponseData;
import com.sat.serviciodescargamasiva.satsuscripciones.data.Suscripcion;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
/**
 *
 * @author IvanGarMo
 */
@Repository
public class OperacionesSuscripcionesImplementation implements OperacionesSuscripciones {
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public ResponseData cambiaSuscripcionUsuario(String uuid, int idSuscripcion) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("CambiaSuscripcionCliente");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_IdSuscripcion", idSuscripcion);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_opvalida"));
         rd.setMensaje(out.get("_mensaje").toString());
         return rd;
    }

    @Override
    public int getSuscripcionPorUsuario(String uuid) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("CargaSuscripcionCliente");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        return (int) out.get("_idsuscripcion");
    }
    
}
