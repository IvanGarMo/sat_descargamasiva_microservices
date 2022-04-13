/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes;
import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author IvanGarMo
 */
@SpringBootTest
@Slf4j
public class GuardaArchivos {
    @Autowired
    private OperacionesKey operacionesKey;
    
    void guardaCertificadoCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Leonel");
        cliente.setApPaterno("García");
        cliente.setApMaterno("Peña");
        cliente.setRfc("GAPL390605QAA");
        
        
    }
    
}
