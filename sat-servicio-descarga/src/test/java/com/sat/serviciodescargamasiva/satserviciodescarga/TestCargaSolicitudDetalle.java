/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.SolicitudDetalle;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesSolicitud;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author IvanGarMo
 */
@SpringBootTest
@Slf4j
public class TestCargaSolicitudDetalle {
    @Autowired
    private OperacionesSolicitud operacionesSolicitud;
    
    @Test
    public void pruebaCargaSolicitudDetalle() throws ParseException, JsonProcessingException {
        
        long[] arrayEnteros = {1L, 2L, 3L, 4L, 5L, 6L, 7L};
        
        for(int i=0; i<arrayEnteros.length; i++) {
            SolicitudDetalle solicitudDetalle = operacionesSolicitud.cargaDetalleSolicitud(arrayEnteros[i]);
            solicitudDetalle.printSolicitudDetalle();   
        }
    }
}
