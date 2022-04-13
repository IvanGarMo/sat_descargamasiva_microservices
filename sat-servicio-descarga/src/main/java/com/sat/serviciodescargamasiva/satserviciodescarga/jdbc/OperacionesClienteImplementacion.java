/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.Cliente;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
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
public class OperacionesClienteImplementacion implements OperacionesCliente {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Cliente getCliente(long idCliente) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cargaClienteSolicitud");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", idCliente);
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        Cliente cliente = new Cliente();
        cliente.setRfc(outParam.get("_rfc").toString());
        cliente.setNombre(outParam.get("_nombre").toString());
        cliente.setApPaterno(outParam.get("_appaterno").toString());
        cliente.setApMaterno(outParam.get("_apmaterno").toString());
        cliente.setCuentaConContrasena((boolean) outParam.get("_cuentaconcontrasena"));
        cliente.setCuentaConCertificado((boolean) outParam.get("_cuentaconcertificado"));
        cliente.setCuentaConKey((boolean) outParam.get("_cuentaconkey"));
        return cliente;
    }
}
