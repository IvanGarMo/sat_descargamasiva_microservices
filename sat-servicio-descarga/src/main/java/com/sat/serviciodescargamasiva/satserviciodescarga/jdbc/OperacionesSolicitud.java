/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.FiltroBusqueda;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.SolicitudDetalle;
/**
 *
 * @author IvanGarMo
 */
public interface OperacionesSolicitud {
    Object listaSolicitudes(String uuid, FiltroBusqueda filtro);
    Object listaEstados();
    ResponseData guardaSolicitud(Solicitud solicitud);
    SolicitudDetalle cargaDetalleSolicitud(long idDescarga);
    String cargaUrlPaquetes(long idDescarga);
    ResponseData guardaUrlPaquete(long idDescarga, String urlPaquete);
    void guardaEstadoSolicitud(long idDescarga, int nuevoEstado);
    void guardaReceptorSolicitudDescarga(long idDescarga, String rfcReceptor);
    
    //Nuevos cambios en SAT 4.2
    Object getComplementos();
    Object getSolicitudDescargaEstado();
    Object getSolicitudDescargaTipoSolicitud();
}
