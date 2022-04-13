/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.data;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author IvanGarMo
 */
@Data
@Slf4j
public class SolicitudDetalle {
    private long idDescarga;
    private String idDescargaSat;
    private long idCliente;
    private String nombreCliente;
    private Date fechaInicioPeriodo;
    private Date fechaFinPeriodo;
    private String rfcSolicitante;
    private List<String> rfcReceptores;
    private String noFacturas;
    private boolean esUid;
    private String uid;
    private int idEstadoSolicitud;
    private String descripcionEstadoSolicitud;
    private String complemento;
    private String estadoComprobante;
    private String tipoSolicitud;
    
    public void printSolicitudDetalle() {
        log.info("idDescarga: "+this.idDescarga);
        log.info("idDescargaSat: "+this.idDescargaSat);
        log.info("idCliente: "+this.idCliente);
        log.info("nombreCliente: "+this.nombreCliente);
        log.info("fechaInicioPeriodo: "+this.fechaInicioPeriodo.toString());
        log.info("fechaFinPeriodo: "+this.fechaFinPeriodo.toString());
        log.info("rfcSolicitante: "+this.rfcSolicitante);
        log.info("Receptores ------------------------------");
        rfcReceptores.forEach(receptor -> {
            log.info("RFC del receptor: "+receptor);
        });
        log.info("noFacturas: "+this.noFacturas);
        log.info("esUid: "+this.esUid);
        log.info("uid: "+this.uid);
        log.info("idEstadoSolicitud: "+this.idEstadoSolicitud);
        log.info("descripcionEstadoSolicitud: "+this.descripcionEstadoSolicitud);
        log.info("complemento: "+this.complemento);
        log.info("estadoComprobante: "+this.estadoComprobante);
        log.info("tipoSolicitud: "+this.tipoSolicitud);
    }
}
