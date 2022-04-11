/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesAutorizacion;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author IvanGarMo
 */
@SpringBootTest
@Slf4j
public class TestExisteSolicitudIgual {
    @Autowired
    private OperacionesAutorizacion authService;
    private Solicitud solicitud;
    
    @Test
    public void operacionNoExiste() {
        solicitud = new Solicitud();
        solicitud.setIdCliente(1);
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        solicitud.setRfcEmisor("GAPL390605Q44");
        solicitud.setRfcReceptor("GAMI9809092A1");
        solicitud.setRfcSolicitante("GAPL390605Q44");
        log.info("Se supone que aquí esta el error");
        ResponseData rd = authService.puedeHacerSolicitud(solicitud);
        log.info("Aquí esto nod debe aparecer");
        assertTrue(rd.isOpValida());
    }
    
    @Test
    public void operacionExiste() {
        solicitud = new Solicitud();
        solicitud.setIdCliente(1);
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        solicitud.setRfcEmisor("GAPL390605Q44");
        solicitud.setRfcReceptor("");
        solicitud.setRfcSolicitante("GAPL390605Q44");
        ResponseData rd = authService.puedeHacerSolicitud(solicitud);
        assertFalse(rd.isOpValida());
    }
}
