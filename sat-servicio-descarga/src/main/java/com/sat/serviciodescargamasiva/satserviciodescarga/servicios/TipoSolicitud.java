/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

/**
 *
 * @author IvanGarMo
 */
    public enum TipoSolicitud {
        CFDI {
            @Override
            public String toString() {
                return "CFDI";
            }
        },
        RETENCIONES {
            @Override
            public String toString() {
                return "Retenciones";
            }
        }
    }
