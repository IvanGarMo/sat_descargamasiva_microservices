/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
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
public class OperacionesContrasenaSatImplementacion implements OperacionesContrasenaSat {
    @Autowired
    private JdbcTemplate jdbc;
    
    @Override
    public ResponseData guardaContrasenaCliente(String uidUsuario, long idCliente, String contrasena) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaContrasena");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uidUsuario);
        inParam.put("_IdCliente", idCliente);
        inParam.put("_Contrasena", contrasena);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_opvalida"));
         rd.setMensaje(out.get("_mensaje").toString());
         return rd;
    }
    
}
