/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.googlecloud.GoogleStorageClientAdapter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author IvanGarMo
 */
@Repository
@Slf4j
public class OperacionesCertificadoImplementacion implements OperacionesCertificado {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private GoogleStorageClientAdapter adapter;
    
    @Override
    public ResponseData guardaCertificadoBaseDatos(String uuid, Cliente cliente) throws IOException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaCertificadoBlob");
        
        byte[] certBytes = cliente.getCertificado().getBytes();
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_IdCliente", cliente.getIdCliente());
        inParam.put("_Certificado", certBytes);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_opvalida"));
         rd.setMensaje(out.get("_mensaje").toString());
         return rd;
    }

    @Override
    public ResponseData guardaCertificadoBlob(String uuid, Cliente cliente)
        throws IOException {
        
        String nombreCertificadoBlob = generaNombreCertificado(cliente);
        adapter.upload(cliente.getCertificado(), nombreCertificadoBlob, adapter.BUCKET_CERTIFICADO);
        
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaCertificadoNube");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uuid);
        inParam.put("_IdCliente", cliente.getIdCliente());
        inParam.put("_Url", nombreCertificadoBlob);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_opvalida"));
         rd.setMensaje(out.get("_mensaje").toString());
         return rd;
    }

    @Override
    public String generaNombreCertificado(Cliente cliente) {
        String FORMATO_CERTIFICADO = "Certificado_"+cliente.getRfc()+"_"+cliente.getApPaterno()+cliente.getApMaterno()+".cer";
        return FORMATO_CERTIFICADO;
    }
}
