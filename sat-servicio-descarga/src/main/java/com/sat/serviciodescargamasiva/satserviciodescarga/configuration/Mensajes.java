/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author IvanGarMo
 */
@ConfigurationProperties(prefix = "mensajes")
@Data
public class Mensajes {
    private String rfcEmisorNoValido;
    private String rfcReceptorNoValido;
    private String rfcSolicitanteNoValido;
    private String rfcSolicitanteNoIncluido;
    private String fechaNoValida;
    private String fechaIncorrecta;
    private String solicitudGeneradaCorrectamente;
}
