/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jdbc;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IvanGarMo
 */
@Repository
public class OperacionesTelefonoImplementacion implements OperacionesTelefono {
//    @Autowired
//    DataSource ds;
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public ResponseData capturaTelefonoUsuario(String uuid, String telefono) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("SalvaTelefono");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_Telefono", telefono);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;

    }

    @Override
    public ResponseData eliminaTelefonoUsuario(String uuid, long idTelefono) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("EliminaTelefono");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_IdTelefono", idTelefono);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;
    }

    @Override
    public Object listaTelefonosUsuario(String uuid) {
        StoredProcedureTelefono mp = new StoredProcedureTelefono(jdbc, "listaTelefonoUsuario");
        SqlParameter paramUid = new SqlParameter("_UidUserFirebase", Types.VARCHAR);
        
        SqlParameter[] paramArray = { paramUid };
        mp.setParameters(paramArray);
        mp.compile();
        Map<String, Object> storedProcResult = mp.execute(uuid);
        return storedProcResult.get("#result-set-1");
    }
    
    class StoredProcedureTelefono extends StoredProcedure {
        public StoredProcedureTelefono(JdbcTemplate template, String name) {
            super(template, name);
            setFunction(false);
        }
    }
}
