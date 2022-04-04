/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.MedioContacto;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

/**
 *
 * @author IvanGarMo
 */
@Service
public class AutorizacionOperacionImplementacion implements AutorizacionOperacion {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public ResponseData puedeAcceder(String uuid, long idCliente) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("puedeAccederCliente");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_uidUsuario", uuid);
        inParam.put("_idCliente", idCliente);
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;
    }

    @Override
    public boolean puedeRealizarOperacionMedioContacto(MedioContacto medioContacto) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("puedeRealizarOperacionMedioContacto");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", medioContacto.getIdCliente());
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        boolean opValida = (boolean) out.get("_opvalida");
        return opValida;
    }
}
