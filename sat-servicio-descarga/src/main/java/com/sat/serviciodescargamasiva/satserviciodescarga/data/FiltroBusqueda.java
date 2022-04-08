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
    private String rfcEmisor;
    private String rfcReceptor;
    private Date fechaInicioPeriodo;
    private Date fechaFinPeriodo;
    private int estado;
}
