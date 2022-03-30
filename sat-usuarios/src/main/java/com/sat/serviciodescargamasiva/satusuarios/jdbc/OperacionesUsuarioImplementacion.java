/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jdbc;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

/**
 *
 * @author IvanGarMo
 */
public class OperacionesUsuarioImplementacion implements OperacionesUsuario {
    @Autowired
    DataSource ds;
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public ResponseData actualizaUsuario(Usuario u) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UsuarioUidFirebase", u.getUid());
        inParam.put("_Nombre", u.getNombre());
        inParam.put("_ApPaterno", u.getApPaterno());
        inParam.put("_ApMaterno", u.getApMaterno());
        inParam.put("_Organizacion", u.getOrganizacion());
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_OpValida"));
        rd.setMensaje(out.get("_Mensaje").toString());
        return rd;
    }
    
    @Override
    public ResponseData registraUsuario(Usuario u) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UsuarioUidFirebase", u.getUid());
        inParam.put("_Nombre", u.getNombre());
        inParam.put("_ApPaterno", u.getApPaterno());
        inParam.put("_ApMaterno", u.getApMaterno());
        inParam.put("_IdSuscripcion", u.getIdSuscripcion());
        inParam.put("_Organizacion", u.getOrganizacion());
        //inParam.put("_Activo", u.getActivo());
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_OpValida"));
        rd.setMensaje(out.get("_Mensaje").toString());
        return rd;
    }
    
}
