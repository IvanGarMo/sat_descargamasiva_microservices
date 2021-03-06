/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.Resultado;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author IvanGarMo
 */
abstract class RequestBase {
    
    private String xml;
    private String url;
    private String SOAPAction;
    
    protected RequestBase() {
        this.xml = null;
        this.url = null;
        this.SOAPAction = null;
    }
    
    /**
     * Constructor of RequestBase class
     *
     * @param url
     * @param SOAPAction
     */
    protected RequestBase(String url, String SOAPAction) {
        this.xml = null;
        this.url = url;
        this.SOAPAction = SOAPAction;
    }

    protected void setUrl(String url) {
        this.url = url;
    }
    
    protected void setAction(String SOAPAction) {
        this.SOAPAction = SOAPAction;
    }
    
    protected void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * Get result of a previously obtained XML
     *
     * @param xmlResponse
     * @return
     */
    protected abstract Resultado getResult(String xmlResponse);

    /**
     * Create digest SHA1 from a String and returning a Base64 String
     *
     * @param sourceData
     * @return
     * @throws NoSuchAlgorithmException
     */
    protected String createDigest(String sourceData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(sourceData.getBytes());

        return Base64.getEncoder().encodeToString(digest.digest());
    }

    /**
     * Sign SHA1 with private key and a String and returning a Base64 String
     *
     * @param sourceData
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    protected String sign(String sourceData, PrivateKey privateKey) throws
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(sourceData.getBytes());

        return Base64.getEncoder().encodeToString(sig.sign());
    }

    /**
     * Create HttpURLConnection to send previously created XML
     *
     * @return
     * @throws IOException
     */
    protected String send(String authorization) throws IOException {
        URL url = new URL(this.url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set timeout as per needs
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(20000);

        // Set DoOutput to true if you want to use URLConnection for output.
        // Default is false
        conn.setDoOutput(true);

        // Set Headers
        conn.setRequestProperty("Accept-Charset", "UTF_8");
        conn.setRequestProperty("Content-type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction", SOAPAction);

        if (authorization != null)
            conn.setRequestProperty("Authorization", authorization);

        // Write XML
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(xml.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = conn.getInputStream();

        // Read XML
        byte[] res = new byte[2048];
        int i;
        StringBuilder response = new StringBuilder();
        while ((i = inputStream.read(res)) != -1) {
            response.append(new String(res, 0, i));
        }
        inputStream.close();

        return response.toString();
    }

    /**
     * Convert a String to XMl (Document Object)
     *
     * @param xmlString
     * @return
     */
    protected static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
