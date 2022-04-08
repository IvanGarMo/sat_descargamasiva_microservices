/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import java.io.IOException;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesCertificado {
    ResponseData guardaCertificadoBaseDatos(String uuid, Cliente cliente) throws IOException;
    ResponseData guardaCertificadoBlob(String uuid, Cliente cliente) throws IOException;
    String generaNombreCertificado(Cliente cliente);
}
