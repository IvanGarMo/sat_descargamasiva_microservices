/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satsuscripciones.jpa;

import com.sat.serviciodescargamasiva.satsuscripciones.data.Suscripcion;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author IvanGarMo
 */
public interface SuscripcionJpa extends CrudRepository<Suscripcion, Integer>{ }
