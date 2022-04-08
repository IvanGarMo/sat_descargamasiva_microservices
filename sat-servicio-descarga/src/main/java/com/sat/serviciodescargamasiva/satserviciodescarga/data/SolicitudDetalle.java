/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.data;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Data
public class SolicitudDetalle {
    private long idDescarga;
    private String idDescargaSat;
    private long idCliente;
    private String nombreCliente;
    private Date fechaInicioPeriodo;
    private Date fechaFinPeriodo;
    private String rfcEmisor;
    private String rfcReceptor;
    private String rfcSolicitante;
    private String estado;
    private int noFacturas;
    private int descargasPermitidas;
    private boolean listoParaDescarga;
    private String urlPaquetes;
}
