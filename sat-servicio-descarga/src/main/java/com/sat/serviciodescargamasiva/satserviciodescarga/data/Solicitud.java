/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.data;

import java.util.List;
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
    private String rfcSolicitante;
    private String rfcEmisor;
    private List<String> rfcReceptor;
    private String tipoComprobante;
    private String estado;
    private String complemento;
    private boolean esUidSolicitado;
    private String uid;
    private int noFacturas;
    private int descargasPermitidas;
    
    @Override
    public String toString() {
        return "[IdDescarga: "+this.idDescarga+" IdDescargaSat "+this.idDescargaSat+
                " idCliente: "+this.idCliente+" FechaInicioPeriodo "+this.fechaInicioPeriodo+
                " fechaFinperiodo: "+this.fechaFinPeriodo+" rfcEmisor "+this.rfcEmisor+
                " rfcReceptor "+this.rfcReceptor+" rfcSolicitante "+this.rfcSolicitante+
                " Estado: "+this.estado+" noFacturas "+noFacturas+
                " DescargasPermitidas "+this.descargasPermitidas+
                "]";
    }
}
