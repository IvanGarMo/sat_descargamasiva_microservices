/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author IvanGarMo
 */
@ConfigurationProperties(prefix = "sat")
@Data
public class Urls {
    private String urlAutentica;
    private String urlAutenticaAction;
    private String urlSolicitud;
    private String urlSolicitudAction;
    private String urlVerificar;
    private String urlVerificarSolicitudAction;
    private String urlDescargarSolicitud;
    private String urlDescargarSolicitudAction;
}
