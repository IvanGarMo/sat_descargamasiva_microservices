/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.ResultadoVerificacion;
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
public class VerifyRequestImplementation implements VerifyRequest {
    private VerifierRequest verifyRequest;
    
    public VerifyRequestImplementation() {
        this.verifyRequest = new VerifierRequest();
    }
    
    @Override
    public void generate(X509Certificate certificate, PrivateKey privateKey, String idRequest, String rfcSolicitante) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        this.verifyRequest.generate(certificate, privateKey, idRequest, rfcSolicitante);
    }

    @Override
    public ResultadoVerificacion verifyRequest(String token, String idRequest) {
        //String cadRespuesta = this.verifyRequest.send(token);
        String cadRespuesta = "";
        return this.verifyRequest.getResult(cadRespuesta);
    }
    
    @Override
    public ResultadoVerificacion doVerify(X509Certificate certificate, PrivateKey privateKey, String idRequest, 
            String rfcSolicitante, String token)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
//        this.verifyRequest.generate(certificate, privateKey, idRequest, rfcSolicitante);
//        this.verifyRequest.send(token);
//        ResultadoVerificacion resultadoVerificacion = this.verifyRequest.getResult(idRequest);
//        return resultadoVerificacion;
    return new ResultadoVerificacion();
    }
    
}

class VerifierRequest extends RequestBase {

    public String sendRequest(String token) throws IOException {
        return this.send(token);
    }

    @Override
    protected ResultadoVerificacion getResult(String xmlResponse) {
        System.out.println("Respuesta de verificación: " + xmlResponse);
        Document doc = convertStringToXMLDocument(xmlResponse);
        ResultadoVerificacion rv = new ResultadoVerificacion();
        rv.setOperacionCorrecta(false);

        //Verify XML document is build correctly
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName("VerificaSolicitudDescargaResult");
            if (nl != null && nl.getLength() > 0) {
                int stateRequest = Integer.parseInt(nl.item(0).getAttributes().getNamedItem("EstadoSolicitud")
                        .getTextContent());
                //String mensaje = desc.consultaEstatusCodigo(stateRequest).getMensaje();
                String mensaje = "";
                rv.setOperacionCorrecta(true);
                rv.setMensaje(mensaje);
            }
        }

        return rv;
    }

    public void generate(X509Certificate certificate, PrivateKey privateKey, String idRequest, String rfcSolicitante)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        String canonicalTimestamp = "<des:VerificaSolicitudDescarga xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\">"
                + "<des:solicitud IdSolicitud=\"" + idRequest + "\" RfcSolicitante=\"" + rfcSolicitante + ">"
                + "</des:solicitud>"
                + "</des:VerificaSolicitudDescarga>";

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
                + "<des:VerificaSolicitudDescarga>"
                + "<des:solicitud IdSolicitud=\"" + idRequest + "\" RfcSolicitante=\"" + rfcSolicitante + "\">"
                + "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">"
                + "<SignedInfo>"
                + "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>"
                + "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>"
                + "<Reference URI=\"#_0\">"
                + "<Transforms>"
                + "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></Transforms>"
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
                + "</des:solicitud>"
                + "</des:VerificaSolicitudDescarga>"
                + "</s:Body>"
                + "</s:Envelope>");
    }
}
