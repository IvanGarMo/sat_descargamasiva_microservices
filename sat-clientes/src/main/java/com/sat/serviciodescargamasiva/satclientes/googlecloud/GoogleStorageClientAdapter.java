/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.googlecloud;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.StorageObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author IvanGarMo
 */
@Component
@Data
public class GoogleStorageClientAdapter {
    @Autowired
    private Storage storage;
    public String BUCKET_CERTIFICADO = "certificados_sat";
    public String BUCKET_KEY = "key_sat";
    
    public boolean upload(MultipartFile file, String fileName, String bucketName) throws IOException {
        StorageObject storageObject = new StorageObject();
        storageObject.setName(fileName);
        InputStream targetStream = new ByteArrayInputStream(file.getBytes());
        storage.objects().insert(bucketName, storageObject,
                new AbstractInputStreamContent(fileName) {
            @Override
            public long getLength() throws IOException {
                return file.getSize();
            }

        @Override
            public boolean retrySupported() {
                return false;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return targetStream;
            }
        }).execute();
        return true;
    }
    
    public StorageObject download(String fileName, String bucketName) throws IOException {
        StorageObject storageObject = storage.objects().get(bucketName, fileName).execute();
        File file = new File("./"+fileName);
        FileOutputStream os = new FileOutputStream(file);
     
        storage.getRequestFactory()
                .buildGetRequest(new GenericUrl(storageObject.getMediaLink()))
                .execute()
                .download(os);
        storageObject.set("file", file);
        return storageObject;
    }
}
