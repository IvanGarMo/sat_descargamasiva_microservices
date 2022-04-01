/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satsuscripciones.data;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idsuscripcion")
    private int idSuscripcion;
    @Column(name="descripcion")
    private String descripcion;
    @Column(name="limitesuperiordescargas")
    private long limiteSuperiorDescargas;
    @Column(name="limiteinferiordescargas")
    private long limiteInferiorDescargas;
    @Column(name="costoporxml")
    private BigDecimal costoPorXml;
    @Column(name="activo")
    private boolean activo;
    
    @Override
    public String toString() {
        return "[ IdSuscripcion"+this.idSuscripcion+" Descripcion: "+this.descripcion+" LimiteSuperiorDescargas "+
                this.limiteSuperiorDescargas+" LimiteInferiorDescargas "+this.limiteInferiorDescargas+"]";
    }
}
