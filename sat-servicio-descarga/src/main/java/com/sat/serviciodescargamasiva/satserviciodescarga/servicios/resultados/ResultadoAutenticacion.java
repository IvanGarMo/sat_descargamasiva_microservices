/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados;

import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Data
public class ResultadoAutenticacion extends Resultado {
    private String token;
    private String dateTokenCreated;
    private String dateTokenExpired;
}
