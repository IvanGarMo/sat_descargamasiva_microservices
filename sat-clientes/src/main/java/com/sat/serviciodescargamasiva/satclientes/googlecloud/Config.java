/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.googlecloud;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.api.services.storage.Storage;
import java.io.IOException;
import java.security.GeneralSecurityException;
//**
// *
// * @author IvanGarMo
// */
@Configuration
public class Config {
    
    @Bean
    public Storage configStorageClient() throws GeneralSecurityException, IOException {
        Storage storage = new Storage(GoogleNetHttpTransport.newTrustedTransport(), 
            new GsonFactory(), new GoogleHttpRequestInitializer());
        return storage;
    }
}
