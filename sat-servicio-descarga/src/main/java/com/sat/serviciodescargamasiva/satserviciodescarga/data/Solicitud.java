/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private long estadoSolicitud;
    private String estadoComprobante;
    private String tipoSolicitud;
    private String complemento;
    private boolean esUidSolicitado;
    private String uid;
    private int noFacturas;
    private int descargasPermitidas;
    
    @Override
    public String toString() {
        return "[ IdDescarga: "+this.idDescarga+" idDescargaSat "+this.idDescargaSat+
                " idCliente: "+this.idCliente+" fechaInicioPeriodo: "+this.fechaInicioPeriodo+
                " fechaFinPeriodo:  "+this.fechaFinPeriodo+" RfcSolicitante: "+rfcSolicitante+
                " rfcEmisor: "+this.rfcEmisor+" estadoSolicitud: "+this.estadoSolicitud+
                " cantidadReceptores: "+this.rfcReceptor.size()+
                " estadoComprobante: "+this.estadoComprobante+" tipoSolicitud:  "+this.tipoSolicitud+
                " esUidSolicitado: "+this.esUidSolicitado+
                " complemento: "+this.complemento+" Uid: "+this.uid+
                "]";
    }
    
    public Date getFechaInicioPeriodoDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.parse(fechaInicioPeriodo);
    }
    
    public Date getFechaFinPeriodoDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.parse(fechaInicioPeriodo);
    }
    
    public boolean esIgual(Solicitud solicitud) {
        if(!this.complemento.equals(solicitud.getComplemento())) {
            return false;
        }
        
        if(!this.estadoComprobante.equals(solicitud.getEstadoComprobante())) {
            return false;
        }
        
        if(!this.tipoSolicitud.equals(solicitud.getTipoSolicitud())) {
            return false;
        }
        
        if(this.rfcReceptor.size() != solicitud.rfcReceptor.size()) {
            return false;
        }
        
        for(String receptor : this.rfcReceptor) {
            if(!solicitud.getRfcReceptor().contains(receptor)) {
                return false;
            }
        }
        
        return true;
    }
}
