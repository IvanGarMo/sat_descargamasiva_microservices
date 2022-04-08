/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jdbc;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
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
public class OperacionesRegistroImplementacion implements OperacionesRegistro {
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public void salvaNuevoUsuario(Usuario usuario) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("guardaUsuarioRegistrado");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_uidUserFirebase", usuario.getUid());
        inParam.put("_idMedio", usuario.getIdMedio());
        jdbcCall.execute(inParam);
    }

    @Override
    public void activaCorreo(Usuario usuario) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("registraCorreoActivado");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", usuario.getUid());
        inParam.put("_CorreoActivado", usuario.getCorreoConfirmado());
        jdbcCall.execute(inParam);
    }

    @Override
    public Usuario estaUsuarioActivo(String uidFirebase) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("estaUsuarioActivo");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_uidUserFirebase", uidFirebase);
        
        Map<String, Object> outParam = jdbcCall.execute(inParam);       
        Usuario u = new Usuario();
        u.setIdUsuario((long) outParam.get("_idusuario"));
        u.setActivo((boolean) outParam.get("_activo"));
        u.setIdMedio((int) outParam.get("_medioautenticacion"));
        u.setCorreoConfirmado((boolean) outParam.get("_correoverificado"));
        return u;
    }

    @Override
    public ResponseData terminaRegistro(Usuario usuario) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("TerminaRegistro");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", usuario.getUid());
        inParam.put("_Nombre", usuario.getNombre());
        inParam.put("_ApPaterno", usuario.getApPaterno());
        inParam.put("_ApMaterno", usuario.getApMaterno());
        inParam.put("_Correo", usuario.getCorreo());
        inParam.put("_IdSuscripcion", usuario.getIdSuscripcion());
        inParam.put("_Organizacion", usuario.getOrganizacion());
        
        Map<String, Object> outParam = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_opvalida"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }
    
}
