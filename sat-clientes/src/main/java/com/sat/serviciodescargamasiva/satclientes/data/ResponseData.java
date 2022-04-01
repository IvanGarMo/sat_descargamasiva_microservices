/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author IvanGarMo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
   private boolean opValida;
   private String mensaje;
   private int idCliente;
}
