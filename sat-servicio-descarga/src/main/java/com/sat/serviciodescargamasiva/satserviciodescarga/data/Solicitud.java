/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.data;

import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Data
public class Solicitud {
    private long idDescarga;
    private String idDescargaSat;
    private long idCliente;
    private String fechaInicioPeriodo;
    private String fechaFinPeriodo;
    private String rfcEmisor;
    private String rfcReceptor;
    private String rfcSolicitante;
    private int estado;
    private int noFacturas;
    private int descargasPermitidas;
}
