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
public interface OperacionesKey {
    ResponseData guardaKeyNube(Cliente cliente) throws IOException;
    String cargaUrlKeyNube(long idCliente);
    ResponseData guardaKeyLocal(long idCliente, byte[] keyFile);
    Object cargaKeyLocal(long idCliente);
}
