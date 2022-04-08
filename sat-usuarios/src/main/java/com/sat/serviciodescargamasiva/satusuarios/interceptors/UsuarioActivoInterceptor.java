/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.interceptors;

import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesRegistro;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author IvanGarMo
 */
@Slf4j
public class UsuarioActivoInterceptor implements HandlerInterceptor {
    @Autowired
    private OperacionesRegistro operacionesRegistro;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String uuid = request.getHeader("uuid");
        
        if(uuid == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        
        Usuario usuario = operacionesRegistro.estaUsuarioActivo(uuid);
        if(usuario.getCorreoConfirmado() && usuario.getActivo()) {
            return true;
        }
        else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
