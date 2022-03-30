/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

/**
 *
 * @author elda_
 */
public class OperacionesClienteImplementacion implements OperacionesCliente {
    @Autowired
    DataSource ds;
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public ResponseData guardaCliente(String uidUsuario, Cliente c) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("guardaCliente");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uidUsuario);
        inParam.put("_Rfc", c.getRfc());
        inParam.put("_Nombre", c.getNombre());
        inParam.put("_ApPaterno", c.getApPaterno());
        inParam.put("_ApMaterno", c.getApMaterno());
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_OpValida"));
         rd.setMensaje(out.get("_Mensaje").toString());
         return rd;
    }

    @Override
    public ResponseData actualizaCliente(String uidUsuario, Cliente c) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaCliente");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uidUsuario);
        inParam.put("_IdCliente", c.getIdCliente());
        inParam.put("_Rfc", c.getRfc());
        inParam.put("_Nombre", c.getNombre());
        inParam.put("_ApPaterno", c.getApPaterno());
        inParam.put("_ApMaterno", c.getApMaterno());
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_OpValida"));
         rd.setMensaje(out.get("_Mensaje").toString());
         return rd;
    }
    
    @Override
    public ResponseData eliminaCliente(String uidUsuario, int idCliente) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaCliente");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uidUsuario);
        inParam.put("_IdCliente", idCliente);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_OpValida"));
         rd.setMensaje(out.get("_Mensaje").toString());
         return rd;
    }

    @Override
    public ResponseData guardaContrasenaCliente(String uidUsuario, long idCliente, String contrasena) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaContrasena");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uidUsuario);
        inParam.put("_IdCliente", idCliente);
        inParam.put("_Contrasena", contrasena);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_OpValida"));
         rd.setMensaje(out.get("_Mensaje").toString());
         return rd;
    }

    @Override
    public ResponseData guardaCertificadoCliente(String uidUsuario, long idCliente, byte[] certificado) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("actualizaCertificado");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uidUsuario);
        inParam.put("_IdCliente", idCliente);
        inParam.put("_Certificado", certificado);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_OpValida"));
         rd.setMensaje(out.get("_Mensaje").toString());
         return rd;
    }

    @Override
    public Cliente getCliente(long idCliente) {
        return jdbc.queryForObject(String.format("getCliente {0}", idCliente), 
                new ClienteRowMapper());
    }

    @Override
    public List<Cliente> getClientes(String uuid, String rfc, String nombre, String apPaterno, String apMaterno) {
        return jdbc.query(String.format("listaClientes {0} {1} {2} {3} {4}", 
                uuid, rfc, nombre, apPaterno, apMaterno), 
                new ClienteRowMapper());
    }    

    private class ClienteRowMapper implements RowMapper<Cliente> {
        @Override
        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cliente c = new Cliente();
            c.setIdCliente(rs.getLong("IdCliente"));
            c.setRfc(rs.getString("Rfc"));
            c.setNombre(rs.getString("Nombre"));
            c.setApPaterno(rs.getString("ApPaterno"));
            c.setApMaterno(rs.getString("ApMaterno"));
            c.setCuentaConContrasena(rs.getBoolean("CuentaConContrasena"));
            c.setContrasena(rs.getString("Contrasena"));
            c.setCuentaConCertificado(rs.getBoolean("CuentaConCertificado"));
            c.setCertificado(rs.getBytes("Certificado"));
            return c;
        }
    }
}
