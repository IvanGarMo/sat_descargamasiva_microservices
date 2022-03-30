/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jpa;

import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author IvanGarMo
 */
public interface JpaUsuario extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByUid(String uid);
}
