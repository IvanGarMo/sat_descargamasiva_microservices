/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.data;

import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Data
public class MedioContacto {
    private long idMedioRegistrado;
    private long idCliente;
    private long idMedio;
    private String descripcion;
}
