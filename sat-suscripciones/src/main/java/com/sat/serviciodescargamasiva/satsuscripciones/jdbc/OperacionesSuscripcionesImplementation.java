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
/**
 *
 * @author IvanGarMo
 */
public class OperacionesSuscripcionesImplementation implements OperacionesSuscripciones {
    @Autowired
    DataSource ds;
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Suscripcion getSuscripcionPorUsuario(String uuid) {
        return jdbc.queryForObject(String.format("cargaSuscripcionUsuario {0}", uuid), 
                new SuscripcionRowMapper());
    }

    @Override
    public ResponseData cambiaSuscripcionUsuario(String uuid, int idSuscripcion) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("cambiaSuscripcionUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_IdSuscripcion", idSuscripcion);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_OpValida"));
         rd.setMensaje(out.get("_Mensaje").toString());
         return rd;
    }
    
    private class SuscripcionRowMapper implements RowMapper<Suscripcion> {
        @Override
        public Suscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
            Suscripcion s = new Suscripcion();
            s.setIdSuscripcion(rs.getInt("IdSuscripcion"));
            s.setDescripcion(rs.getString("Descripcion"));
            s.setLimiteInferiorDescargas(rs.getLong("LimiteInferiorDescargas"));
            s.setLimiteSuperiorDescargas(rs.getLong("LimiteSuperiorDescargas"));
            s.setCostoPorXml(rs.getBigDecimal("CostoPorXml"));
            s.setActivo(rs.getBoolean("Activo"));
            return s;
        }
    }
    
}
