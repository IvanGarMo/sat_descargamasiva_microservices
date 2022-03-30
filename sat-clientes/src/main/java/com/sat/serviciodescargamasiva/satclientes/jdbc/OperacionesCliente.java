/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import java.util.List;

/**
 *
 * @author elda_
 */
public interface OperacionesCliente {
    ResponseData guardaCliente(String uidUsuario, Cliente c);
    ResponseData actualizaCliente(String uidUsuario, Cliente c);
    ResponseData eliminaCliente(String uidUsuario, int idCliente);
    ResponseData guardaContrasenaCliente(String uidUsuario, long idCliente, String contrasena);
    ResponseData guardaCertificadoCliente(String uidUsuario, long idCliente, byte[] certificado);
    Cliente getCliente(long idCliente);
    List<Cliente> getClientes(String uuid, String rfc, String nombre, String apPaterno, String apMaterno);
}