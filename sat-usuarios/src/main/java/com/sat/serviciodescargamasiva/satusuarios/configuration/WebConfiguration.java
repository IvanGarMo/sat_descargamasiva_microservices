/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.configuration;

import com.sat.serviciodescargamasiva.satusuarios.interceptors.UsuarioActivoInterceptor;
import com.sat.serviciodescargamasiva.satusuarios.interceptors.UuidInterceptor;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author IvanGarMo
 */
@Configuration
@EnableWebMvc
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {
    
    @Bean
    public UuidInterceptor getUuidInterceptor() {
        return new UuidInterceptor();
    }
    
    @Bean
    public UsuarioActivoInterceptor getUsuarioActivoInterceptor() {
        return new UsuarioActivoInterceptor();
    }
    
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //Interceptor que no dejara hacer operaciones de actualizacion de datos ni numeros
        //telefónicos a menos que este activo el usuario
//        registry.addInterceptor(getUsuarioActivoInterceptor())
//                .excludePathPatterns("/registro/**")
//                .addPathPatterns(Arrays.asList("/telefonos", "/telefonos/**", "/usuarios", "/usuarios/**"));
//        //Interceptor que no dejará hacer operaciones en las que no venga el id del usuario
//        registry.addInterceptor(getUuidInterceptor())
//                .excludePathPatterns("/registro/nuevo")
//                .addPathPatterns("/registro/**");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
        registry.addMapping("/registro/activaUsuario").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
    }
}
