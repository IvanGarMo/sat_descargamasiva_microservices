/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataSource;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OperacionesClienteImplementacion implements OperacionesCliente {
//    @Autowired
//    DataSource ds;
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public ResponseData guardaCliente(String uidUsuario, Cliente c) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("guardaCliente");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_uidUserFirebase", uidUsuario);
        inParam.put("_rfc", c.getRfc());
        inParam.put("_nombre", c.getNombre());
        inParam.put("_apPaterno", c.getApPaterno());
        inParam.put("_apMaterno", c.getApMaterno());
        inParam.put("_certificadoNube", c.isCertificadoNube());
        inParam.put("_certificadoBaseDatos", c.isCertificadoBaseDatos());
        
        Map<String, Object> out = jdbcCall.execute(inParam);
        ResponseData rd = new ResponseData();
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        rd.setIdCliente((int) out.get("_idcliente"));
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
        rd.setOpValida((boolean) out.get("_opvalida"));
        rd.setMensaje(out.get("_mensaje").toString());
        return rd;
    }
    
    @Override
    public ResponseData eliminaCliente(String uidUsuario, int idCliente) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("desactivaCliente");
        
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_UidUserFirebase", uidUsuario);
        inParam.put("_IdCliente", idCliente);
        
         Map<String, Object> out = jdbcCall.execute(inParam);
         ResponseData rd = new ResponseData();
         rd.setOpValida((boolean) out.get("_opvalida"));
         rd.setMensaje(out.get("_mensaje").toString());
         return rd;
    }

    @Override
    public Cliente getCliente(long idCliente) {
         SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc).withProcedureName("getCliente");
         
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("_idCliente", idCliente);
         
        Map<String, Object> out = jdbcCall.execute(inParam);
        Cliente c = new Cliente();
        c.setIdCliente(idCliente);
        c.setRfc(out.get("_rfc").toString());
        c.setNombre(out.get("_nombre").toString());
        c.setApPaterno(out.get("_appaterno").toString());
        c.setApMaterno(out.get("_apmaterno").toString());
        c.setCuentaConContrasena((boolean) out.get("_cuentaconcontrasena"));
        c.setCuentaConCertificado((boolean) out.get("_cuentaconcertificado"));
        return c;
    }
    
    @Override
    public Object getClientes(String uuid, String rfc, String nombre, String apPaterno, String apMaterno) {
        MyStoredProcedure mp = new MyStoredProcedure(jdbc, "listaClientes");
        SqlParameter paramUid = new SqlParameter("_UidUserFirebase", Types.VARCHAR);
        SqlParameter paramRfc = new SqlParameter("_Rfc", Types.VARCHAR);
        SqlParameter paramNombre = new SqlParameter("_Nombre", Types.VARCHAR);
        SqlParameter paramApPat = new SqlParameter("_ApPat", Types.VARCHAR);
        SqlParameter paramApMat = new SqlParameter("_ApMat", Types.VARCHAR);
        SqlParameter[] paramArray = { paramUid, paramRfc, paramNombre, paramApPat, paramApMat };
        mp.setParameters(paramArray);
        mp.compile();
        Map<String, Object> storedProcResult = mp.execute(uuid, rfc, nombre, apPaterno, apMaterno);
        return storedProcResult.get("#result-set-1");
//        storedProcResult.keySet().forEach(s -> {
//            if(s.equalsIgnoreCase("#result-set-1")) {
//                return storedProcResult.get(s);
//            }
//        });
    }

    @Override
    public Object getClientesSimplificado(String uuid) {
        MyStoredProcedure mp = new MyStoredProcedure(jdbc, "getListaClientesIdNombre");
        SqlParameter paramUid = new SqlParameter("_UidUserFirebase", Types.VARCHAR);
        SqlParameter[] paramArray = { paramUid };
        mp.setParameters(paramArray);
        mp.compile();
        Map<String, Object> storedProcResult = mp.execute(uuid);
        return storedProcResult.get("#result-set-1");
    }
}

    class MyStoredProcedure extends StoredProcedure {
        public MyStoredProcedure(JdbcTemplate template, String name) {
            super(template, name);
            setFunction(false);
        }
    } 
    
//    @Override
//    public List<Cliente> getClientes(String uuid, String rfc, String nombre, String apPaterno, String apMaterno) {
//        
//        List paramList = new ArrayList();
//        paramList.add(new SqlParameter(Types.VARCHAR));
//        
//        
//        public Map<String, Object> findData() {
//        List prmtrsList = new ArrayList();
//        prmtrsList.add(new SqlParameter(Types.VARCHAR));
//        prmtrsList.add(new SqlParameter(Types.VARCHAR));
//        prmtrsList.add(new SqlOutParameter("result", Types.VARCHAR));
//
//        Map<String, Object> resultData = jdbcTemplate.call(connection -> {
//            CallableStatement callableStatement = connection.prepareCall("{call STORED_PROC(?, ?, ?)}");
//            callableStatement.setString(1, "first");
//            callableStatement.setString(2, "last");
//            callableStatement.registerOutParameter(3, Types.VARCHAR);
//            return callableStatement;
//        }, prmtrsList);
//        return resultData;
//    }
//        
//        return new ArrayList<Cliente>();
////        return jdbc.query(String.format("listaClientes {0} {1} {2} {3} {4}", 
////                uuid, rfc, nombre, apPaterno, apMaterno), 
////                new ClienteRowMapper());
//    }    

//    private class ClienteRowMapper implements RowMapper<Cliente> {
//        @Override
//        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
//            Cliente c = new Cliente();
//            c.setIdCliente(rs.getLong("IdCliente"));
//            c.setRfc(rs.getString("Rfc"));
//            c.setNombre(rs.getString("Nombre"));
//            c.setApPaterno(rs.getString("ApPaterno"));
//            c.setApMaterno(rs.getString("ApMaterno"));
//            c.setCuentaConContrasena(rs.getBoolean("CuentaConContrasena"));
//            c.setContrasena(rs.getString("Contrasena"));
//            c.setCuentaConCertificado(rs.getBoolean("CuentaConCertificado"));
//            c.setCertificado(rs.getBytes("Certificado"));
//            return c;
//        }
//    }

