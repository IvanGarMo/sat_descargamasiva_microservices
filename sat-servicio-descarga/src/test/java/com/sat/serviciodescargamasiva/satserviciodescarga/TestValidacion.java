/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesValidacionSolicitud;

/**
 *
 * @author IvanGarMo
 */
@SpringBootTest
@Slf4j
public class TestValidacion {
    @Autowired
    private OperacionesValidacionSolicitud validador;
    private Solicitud solicitud = new Solicitud();
    private ResponseData rd;
    
    @Test
    public void testValido() throws ParseException { //Debe ser válido
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("GAMI9809092A1");
        solicitud.setRfcReceptor("");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertTrue(rd.isOpValida());
    }
    
    @Test
    public void testValidoRfcReceptor() throws ParseException {
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("");
        solicitud.setRfcReceptor("GAMI9809092A1");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertTrue(rd.isOpValida());
    }
    
    @Test
    public void TestFallidoNingunoEspecificado() throws ParseException {
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("");
        solicitud.setRfcReceptor("");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertFalse(rd.isOpValida());
    }
    
    @Test
    public void TestValidoMinusculas() throws ParseException {
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("gami9809092a1");
        solicitud.setRfcReceptor("");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertTrue(rd.isOpValida());
    }
    
    @Test
    public void TestSolicitanteNoEncontrado() throws ParseException {
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("MONE711201Q44");
        solicitud.setRfcReceptor("");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertFalse(rd.isOpValida());
    }
    
    @Test
    public void TestFallaRfcNoValida() throws ParseException {
        solicitud.setRfcSolicitante("gam9809092A1");
        solicitud.setRfcEmisor("gam9809092A1");
        solicitud.setRfcReceptor("");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertFalse(rd.isOpValida());
    }
    
    @Test
    public void TestSolicitanteEmisorValidosReceptorNo() throws ParseException {
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("GAMI9809092A1");
        solicitud.setRfcReceptor("MON711201Q44");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2022-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertFalse(rd.isOpValida());
    }
    
    @Test
    public void TestFechaFinPosterior() throws ParseException {
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("GAMI9809092A1");
        solicitud.setRfcReceptor("MONE711201Q44");
        solicitud.setFechaInicioPeriodo("2021-01-01");
        solicitud.setFechaFinPeriodo("2020-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertFalse(rd.isOpValida());
    }
    
    
    @Test
    public void TestFechaInicioNoValida() throws ParseException {
        solicitud.setRfcSolicitante("GAMI9809092A1");
        solicitud.setRfcEmisor("GAMI9809092A1");
        solicitud.setRfcReceptor("MONE711201Q44");
        solicitud.setFechaInicioPeriodo("2003-01-01");
        solicitud.setFechaFinPeriodo("2020-01-01");
        ResponseData rd = validador.esValidaSolicitud(solicitud);
        assertFalse(rd.isOpValida());
    }
    
}
