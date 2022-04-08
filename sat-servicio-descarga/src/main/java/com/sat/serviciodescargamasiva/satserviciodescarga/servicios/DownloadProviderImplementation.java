/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.Resultado;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author IvanGarMo
 */
@Service
public class DownloadProviderImplementation implements Download {

    private Downloader download;

    public DownloadProviderImplementation() {
        this.download = new Downloader();
    }

    @Override
    public void generate(X509Certificate certificate, PrivateKey privateKey, String rfcSolicitante, String idPackage)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        this.download.generate(certificate, privateKey, rfcSolicitante, idPackage);
    }

    @Override
    public Resultado getResult(String xmlResponse)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        Resultado result = this.download.getResult(xmlResponse);
        return result;
    }

    @Override
    public Resultado doDownload(X509Certificate certificate, PrivateKey privateKey, String rfc, String idPaquete, String token)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException, IOException {
        Resultado result;
        this.download.generate(certificate, privateKey, rfc, idPaquete);
        String xml = this.download.send(token);
        result = this.download.getResult(xml);
        return result;
    }

}

class Downloader extends RequestBase {

    public Downloader() {
        super();
        String url = new Urls().getUrlDescargarSolicitud();
        String SOAPAction = new Urls().getUrlDescargarSolicitudAction();
        this.setUrl(url);
        this.setAction(SOAPAction);
    }

    /**
     * Constructor of Download class
     *
     * @param url
     * @param SOAPAction
     */
    public Downloader(String url, String SOAPAction) {
        super(url, SOAPAction);
    }

    public String sendRequest(String token) throws IOException {
        return this.send(token);
    }

    public Resultado doDownload(X509Certificate certificate, PrivateKey privateKey,
            String rfc, String idPaquete, String token)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException,
            IOException {
        this.generate(certificate, privateKey, rfc, idPaquete);
        String xml = this.send(token);
        return getResult(xml);
    }

    @Override
    protected Resultado getResult(String xmlResponse) {
        Document doc = convertStringToXMLDocument(xmlResponse);
        System.out.println("Documentos de respuesta: " + xmlResponse);
        Resultado res = new Resultado();
        res.setOperacionCorrecta(true);
        //Verify XML document is build correctly
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName("Paquete");
            if (nl != null && nl.getLength() > 0) {
                String paquete = nl.item(0).getTextContent();
                res.setOperacionCorrecta(true);
                res.setMensaje(paquete);
            }
        }
        return res;
    }

    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @param rfcSolicitante
     * @param idPackage
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */
    public void generate(X509Certificate certificate, PrivateKey privateKey, String rfcSolicitante, String idPackage)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        String canonicalTimestamp = "<des:PeticionDescargaMasivaTercerosEntrada xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\">"
                + "<des:peticionDescarga IdPaquete=\"" + idPackage + "\" RfcSolicitante=\"" + rfcSolicitante + "></des:peticionDescarga>"
                + "</des:PeticionDescargaMasivaTercerosEntrada>";

        String digest = createDigest(canonicalTimestamp);

        String canonicalSignedInfo = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">"
                + "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod>"
                + "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>"
                + "<Reference URI=\"#_0\">"
                + "<Transforms>"
                + "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>"
                + "</Transforms>"
                + "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>"
                + "<DigestValue>" + digest + "</DigestValue>"
                + "</Reference>"
                + "</SignedInfo>";

        String signature = sign(canonicalSignedInfo, privateKey);

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\" xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\">"
                + "<s:Header/>"
                + "<s:Body>"
                + "<des:PeticionDescargaMasivaTercerosEntrada>"
                + "<des:peticionDescarga IdPaquete=\"" + idPackage + "\" RfcSolicitante=\"" + rfcSolicitante + "\">"
                + "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">"
                + "<SignedInfo>"
                + "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>"
                + "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>"
                + "<Reference URI=\"#_0\">"
                + "<Transforms>"
                + "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>"
                + "</Transforms>"
                + "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>"
                + "<DigestValue>" + digest + "</DigestValue>"
                + "</Reference>"
                + "</SignedInfo>"
                + "<SignatureValue>" + signature + "</SignatureValue>"
                + "<KeyInfo>"
                + "<X509Data>"
                + "<X509IssuerSerial>"
                + "<X509IssuerName>" + certificate.getIssuerX500Principal() + "</X509IssuerName>"
                + "<X509SerialNumber>" + certificate.getSerialNumber() + "</X509SerialNumber>"
                + "</X509IssuerSerial>"
                + "<X509Certificate>" + Base64.getEncoder().encodeToString(certificate.getEncoded()) + "</X509Certificate>"
                + "</X509Data>"
                + "</KeyInfo>"
                + "</Signature>"
                + "</des:peticionDescarga>"
                + "</des:PeticionDescargaMasivaTercerosEntrada>"
                + "</s:Body>"
                + "</s:Envelope>");
    }
}
