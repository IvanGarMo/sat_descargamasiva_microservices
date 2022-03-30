/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

/**
 *
 * @author elda_
 */
public interface Download {
    byte[] download(String token, String idPackages);
}
