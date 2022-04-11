/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import com.sat.serviciodescargamasiva.satclientes.googlecloud.GoogleStorageClientAdapter;
import java.io.IOException;
/**
 *
 * @author IvanGarMo
 */
@Repository
public class OperacionesKeyImplementacion implements OperacionesKey {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GoogleStorageClientAdapter adapter;
         
    @Override
    public ResponseData guardaKeyNube(Cliente cliente) throws IOException {
        String nombreKeyBlob = generaNombre(cliente);
        adapter.upload(cliente.getKey(), nombreKeyBlob, adapter.BUCKET_KEY);
        
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("guardaClienteKeyNube");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", cliente.getIdCliente());
        inParam.put("_urlKey", nombreKeyBlob);
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) outParam.get("_opvalida"));
        rd.setMensaje(outParam.get("_mensaje").toString());
        return rd;
    }

    @Override
    public String cargaUrlKeyNube(long idCliente) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cargaClienteKeyNube");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", idCliente);
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        return outParam.get("_urlKey").toString();
    }

    @Override
    public ResponseData guardaKeyLocal(long idCliente, byte[] keyFile) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("guardaClienteKeyLocal");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", idCliente);
        inParam.put("_urlKey", keyFile);
        
         Map<String, Object> outParam = jdbc.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) outParam.get("_opvalida"));
         rd.setMensaje(outParam.get("_mensaje").toString());
         return rd;
    }

    @Override
    public Object cargaKeyLocal(long idCliente) {
        SimpleJdbcCall jdbc = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cargaClienteKeyLocal");
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", idCliente);
        
        Map<String, Object> outParam = jdbc.execute(inParam);
        return outParam.get("_fileKey");
    }
    
    public String generaNombre(Cliente cliente) {
        String FORMATO_CERTIFICADO = "Key_"+cliente.getRfc()+"_"+cliente.getApPaterno()+cliente.getApMaterno()+".key";
        return FORMATO_CERTIFICADO;
    }
    
}
