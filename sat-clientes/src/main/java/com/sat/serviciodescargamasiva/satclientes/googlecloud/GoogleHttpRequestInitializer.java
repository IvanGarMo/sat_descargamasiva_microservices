/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.googlecloud;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Data
public class GoogleHttpRequestInitializer implements HttpRequestInitializer {
    private GoogleCredentials googleCredentials;
    private HttpCredentialsAdapter adapter;
    
    @Override
    public void initialize(HttpRequest hr) throws IOException {
//        googleCredentials = GoogleCredentials.getApplicationDefault()
//                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream("C:\\Users\\Han-S\\Desktop\\Nueva carpeta\\sat_descargamasiva_microservices\\sat-clientes\\src\\main\\resources\\descargamasivasat-1db51d58737e.json"))
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        adapter = new HttpCredentialsAdapter(googleCredentials);
        
        adapter.initialize(hr);
        hr.setConnectTimeout(60000);
        hr.setReadTimeout(60000);
    }
}
