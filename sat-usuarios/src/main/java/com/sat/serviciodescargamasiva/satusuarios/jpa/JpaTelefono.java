/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jpa;

import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author IvanGarMo
 */
public interface JpaTelefono extends CrudRepository<Telefono, Long>{
    List<Telefono> findByIdUsuario(long idUsuario);
}
