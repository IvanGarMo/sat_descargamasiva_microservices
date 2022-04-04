/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jdbc;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IvanGarMo
 */
@Repository
@Slf4j
public class OperacionesUsuarioImplementacion implements OperacionesUsuario {
    @Autowired
    DataSource ds;
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public ResponseData registraUsuario(Usuario u) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("SalvaUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", u.getUid());
        inParam.put("_Nombre", u.getNombre());
        inParam.put("_ApPaterno", u.getApPaterno());
        inParam.put("_ApMaterno", u.getApMaterno());
        inParam.put("_Correo", u.getCorreo());
        inParam.put("_IdSuscripcion", u.getIdSuscripcion());
        inParam.put("_Organizacion", u.getOrganizacion());
        inParam.put("_Activo", u.getActivo());
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;
    }
    
    @Override
    public ResponseData actualizaUsuario(Usuario u) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("ActualizaUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", u.getUid());
        inParam.put("_Nombre", u.getNombre());
        inParam.put("_ApPaterno", u.getApPaterno());
        inParam.put("_ApMaterno", u.getApMaterno());
        inParam.put("_Organizacion", u.getOrganizacion());
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;
    }

    @Override
    public Usuario cargaUsuario(String uuid) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("CargaUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        Usuario u = new Usuario();
        u.setIdUsuario((int) out.get("_idusuario"));
        u.setUid(out.get("_uiduserfirebaseout").toString());
        u.setNombre(out.get("_nombre").toString());
        u.setApPaterno(out.get("_appaterno").toString());
        u.setApMaterno(out.get("_apmaterno").toString());
        u.setCorreo(out.get("_correo").toString());
        u.setIdSuscripcion((int) out.get("_idsuscripcion"));
        u.setOrganizacion(out.get("_organizacion").toString());
        u.setActivo((boolean) out.get("_activo"));
        return u;
    }

    @Override
    public int getIdUsuario(String uuid) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("ReturnIdUsuario");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_uidUsuarioFirebase", uuid);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        return (int) out.get("_idusuario");
    }
}
