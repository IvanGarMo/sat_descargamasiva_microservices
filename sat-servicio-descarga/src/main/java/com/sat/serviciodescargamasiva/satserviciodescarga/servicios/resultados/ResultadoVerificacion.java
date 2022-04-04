/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IvanGarMo
 */
public class ResultadoVerificacion extends Resultado {
    private List<String> idPaquetes;
    private int estadoSolicitud;
    private int noFacturas;
    
    public ResultadoVerificacion() {
        idPaquetes = new ArrayList<String>();
    }
    
    public void addPaquete(String idPaquete) {
        this.idPaquetes.add(idPaquete);
    }
}
