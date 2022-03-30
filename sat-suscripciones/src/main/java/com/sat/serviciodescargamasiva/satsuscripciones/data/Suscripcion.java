/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satsuscripciones.data;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Entity
@Table(name="Suscripciones")
@Data
public class Suscripcion {
    private int idSuscripcion;
    private String descripcion;
    private long limiteSuperiorDescargas;
    private long limiteInferiorDescargas;
    private BigDecimal costoPorXml;
    private boolean activo;
}
