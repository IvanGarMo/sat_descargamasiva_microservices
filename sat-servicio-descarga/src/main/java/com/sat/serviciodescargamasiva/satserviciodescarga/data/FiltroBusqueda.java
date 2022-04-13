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
public class FiltroBusqueda {
    private String rfcSolicitante;
    private String fechaInicioPeriodo;
    private String fechaFinPeriodo;
    private int estadoSolicitud;
    private int idComplemento;
    private int estadoComprobante;
    private String tipoSolicitud;
    private String uid;
    
    @Override
    public String toString() {
        return "[ RfcSolicitante: "+this.rfcSolicitante+
                " fechaInicioPeriodo: "+this.fechaInicioPeriodo+
                " fechaFinPeriodo: "+this.fechaFinPeriodo+
                " estadoSolicitud "+this.estadoSolicitud+
                " idComplemento: "+this.idComplemento+
                " estadoComprobante: "+this.estadoComprobante+
                " tipoSolicitud:  "+this.tipoSolicitud+
                " uid: "+this.uid+
                "]";
    }
}
