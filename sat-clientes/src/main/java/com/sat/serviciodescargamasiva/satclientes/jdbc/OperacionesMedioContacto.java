/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.MedioContacto;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesMedioContacto {
    Object cargaListaMedioContacto();
    Object cargaListaMedioContactoCliente(long idCliente);
    ResponseData insertaMedioContactoCliente(long idCliente, MedioContacto medio);
    ResponseData actualizaMedioContactoCliente(long idCliente, MedioContacto medio);
    ResponseData eliminaMedioContactoCliente(long idCliente, MedioContacto medio);
}
