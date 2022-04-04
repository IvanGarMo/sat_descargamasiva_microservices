/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.ResultadoAutenticacion;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author IvanGarMo
 */
@Service
public class AuthenticationProviderImplementation implements AuthenticationProvider {

    private Authentication auth;
    
    public AuthenticationProviderImplementation() {
        auth = new Authentication();
    }
    
    @Override
    public X509Certificate getCertificate(InputStream file, char[] key) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(file, key);
        String alias = ks.aliases().nextElement();

        return (X509Certificate) ks.getCertificate(alias);
    }

    @Override
    public PrivateKey getPrivateKey(InputStream file, char[] key) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
                KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(file, key);
        String alias = ks.aliases().nextElement();

        return (PrivateKey) ks.getKey(alias, key);
    }

    @Override
    public void generate(X509Certificate certificate, PrivateKey privateKey) 
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        this.auth.generate(certificate, privateKey);
    }

    @Override
    public String getToken() throws IOException {
        return this.auth.sendRequest(null);
    }
    
}

class Authentication extends RequestBase {
    private boolean isGenerated;
    
    public boolean isGenerated() {
        return this.isGenerated;
    }
    
    public void setIsGenerated(boolean isGenerated) {
        this.isGenerated = isGenerated;
    }
    
    public Authentication() {
        super();
        String url = new Urls().getUrlAutentica();
        String SOAPAction = new Urls().getUrlAutenticaAction();
        this.setUrl(url);
        this.setAction(SOAPAction);
    }
    
    public ResultadoAutenticacion doAuthentication(X509Certificate certificate, PrivateKey privateKey) 
            throws 
            NoSuchAlgorithmException, SignatureException, InvalidKeyException,
            IOException, CertificateEncodingException {
        this.generate(certificate, privateKey);
        String respuesta = this.send(null);
        return getResult(respuesta);
    }
    
    private final String xmlVacio = 
            "El servidor no regresó una respuesta. Por favor, verifique que esté usando las llaves correctas";
    
    @Override
    protected ResultadoAutenticacion getResult(String xmlResponse) {
        ResultadoAutenticacion auth = new ResultadoAutenticacion();
        Document doc = convertStringToXMLDocument(xmlResponse);
        System.out.println("Respuesta XML de token de inicio: "+xmlResponse);
        
        //Verify XML document is build correctly
        if (doc != null) {
            NodeList nodes = doc.getElementsByTagName("AutenticaResult");
            if(nodes != null && nodes.getLength()==1) {
                String token = nodes.item(0).getTextContent();
                auth.setOperacionCorrecta(true);
                auth.setToken(doc.getElementsByTagName("AutenticaResult").item(0).getTextContent());
                auth.setDateTokenCreated(doc.getElementsByTagName("u:Created").item(0).getTextContent());
                auth.setDateTokenExpired(doc.getElementsByTagName("u:Expires").item(0).getTextContent());
                return auth;
            }            
        }
        auth.setOperacionCorrecta(false);
        auth.setToken(null);
        auth.setMensaje(xmlVacio);
        return auth;
    }

    public String sendRequest(String token) throws IOException {
        return this.send(null);
    }
    
    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */    
    public void generate(X509Certificate certificate, PrivateKey privateKey)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        
        this.isGenerated = true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendarNow = Calendar.getInstance();

        String created = simpleDateFormat.format(calendarNow.getTime());
        calendarNow.add(Calendar.SECOND, 300); // Add 300 seconds which equals 5 minutes
        String expires = simpleDateFormat.format(calendarNow.getTime());
        String uuid = "uuid-" + UUID.randomUUID().toString() + "-1";

        String canonicalTimestamp = "<u:Timestamp xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" u:Id=\"_0\">" +
                "<u:Created>" + created + "</u:Created>" +
                "<u:Expires>" + expires + "</u:Expires>" +
                "</u:Timestamp>";

        String digest = createDigest(canonicalTimestamp);

        String canonicalSignedInfo = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>";

        String signature = sign(canonicalSignedInfo, privateKey);

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                "<s:Header>" +
                "<o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                "<u:Timestamp u:Id=\"_0\">" +
                "<u:Created>" + created + "</u:Created>" +
                "<u:Expires>" + expires + "</u:Expires>" +
                "</u:Timestamp>" +
                "<o:BinarySecurityToken u:Id=\"" + uuid + "\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">" +
                Base64.getEncoder().encodeToString(certificate.getEncoded()) +
                "</o:BinarySecurityToken>" +
                "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>" +
                "<SignatureValue>" + signature + "</SignatureValue>" +
                "<KeyInfo>" +
                "<o:SecurityTokenReference>" +
                "<o:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#" + uuid + "\"/>" +
                "</o:SecurityTokenReference>" +
                "</KeyInfo>" +
                "</Signature>" +
                "</o:Security>" +
                "</s:Header>" +
                "<s:Body>" +
                "<Autentica xmlns=\"http://DescargaMasivaTerceros.gob.mx\"/>" +
                "</s:Body>" +
                "</s:Envelope>");
    }
}
