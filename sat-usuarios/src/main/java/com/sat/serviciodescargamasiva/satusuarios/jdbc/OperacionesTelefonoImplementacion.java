/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jdbc;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

/**
 *
 * @author IvanGarMo
 */
public class OperacionesTelefonoImplementacion implements OperacionesTelefono {
    @Autowired
    DataSource ds;
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public ResponseData capturaTelefonoUsuario(String uuid, String telefono) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("anadeTelefonoUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_Telefono", telefono);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_OpValida"));
        rd.setMensaje(out.get("_Mensaje").toString());
        return rd;

    }

    @Override
    public ResponseData eliminaTelefonoUsuario(String uuid, long idTelefono) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("eliminaTelefonoUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_IdTelefono", idTelefono);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_OpValida"));
        rd.setMensaje(out.get("_Mensaje").toString());
        return rd;
    }

    @Override
    public List<Telefono> listaTelefonosUsuario(String uuid) {
        return jdbc.query(String.format("listaTelefonoUsuario {0}", uuid), 
                new TelefonoRowMapper());
    }
    
    private class TelefonoRowMapper implements RowMapper<Telefono> {
        @Override
        public Telefono mapRow(ResultSet rs, int rowNum) throws SQLException {
            Telefono tel = new Telefono();
            tel.setIdUsuario(rs.getLong("IdUsuario"));
            tel.setIdTelefono(rs.getLong("IdTelefono"));
            tel.setTelefono(rs.getString("Telefono"));
            return tel;
        }
    }
}
