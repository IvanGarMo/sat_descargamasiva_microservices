/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import com.sat.serviciodescargamasiva.satserviciodescarga.configuration.Mensajes;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.ResultadoSolicitudDescarga;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.Resultado;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author IvanGarMo
 */
@Service
public class RequestProviderImplementation implements RequestProvider {
    private Request request;
    private Mensajes mensajes;
    
    public RequestProviderImplementation() {
        this.request = new Request();
        this.mensajes = new Mensajes();
    }
    
    @Override
    public ResultadoSolicitudDescarga generate(X509Certificate certificate, PrivateKey privateKey, String rfcSolicitante, String rfcEmisor, String rfcReceptor, String fechaInicio, String fechaFinal, TipoSolicitud tipo) 
    throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        return this.request.generate(certificate, privateKey, rfcEmisor, rfcReceptor, rfcSolicitante, fechaInicio, fechaFinal);
    }

    @Override
    public void setType(TipoSolicitud tipo) {
        this.request.setTypeRequest(tipo);
    }

    @Override
    public String sendRequest(String token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private final String rfcRegExp = "[A-Za-z]{4}[0-9]{6}[A-Za-z0-9]{3}";
    
    @Override
    public Resultado validate(String rfcSolicitante, String rfcEmisor, String rfcReceptor, 
            String fechaInicio, String fechaFinal, TipoSolicitud tipo) {
        Resultado rs = new Resultado();
        rs.setOperacionCorrecta(true);
        
        if(rfcSolicitante.matches(rfcRegExp)) {
            rs.setOperacionCorrecta(false);
            rs.setMensaje(mensajes.getRfcSolicitanteNoValido());
        }
        else if(rfcEmisor.equalsIgnoreCase(rfcSolicitante) || rfcReceptor.equalsIgnoreCase(rfcSolicitante)) {
            rs.setOperacionCorrecta(false);
            rs.setMensaje(mensajes.getRfcSolicitanteNoIncluido());
        }
        else if(rfcEmisor.equalsIgnoreCase(rfcSolicitante) && !rfcReceptor.isBlank() 
                && !rfcSolicitante.matches(rfcRegExp)) {
            rs.setOperacionCorrecta(false);
            rs.setMensaje(mensajes.getRfcReceptorNoValido());
        }
        else if(rfcReceptor.equalsIgnoreCase(rfcSolicitante) && !rfcEmisor.isBlank()
                && !rfcEmisor.matches(rfcRegExp)) {
            rs.setOperacionCorrecta(false);
            rs.setMensaje(mensajes.getRfcEmisorNoValido());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dFechaInicial = sdf.parse(fechaInicio);
                Date dFechaFinal = sdf.parse(fechaFinal);   
                if(dFechaInicial.after(dFechaFinal)) {
                    rs.setOperacionCorrecta(false);
                    rs.setMensaje(mensajes.getFechaIncorrecta());
                }
            } catch(ParseException ex) {
                rs.setOperacionCorrecta(false);
                rs.setMensaje(mensajes.getFechaNoValida());
            }
        }
        return rs;
    }

    @Override
    public ResultadoSolicitudDescarga doRequest(X509Certificate certificate, PrivateKey privateKey, String rfcSolicitante, String rfcEmisor, String rfcReceptor, String fechaInicio, String fechaFinal, TipoSolicitud tipo) 
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException,
            IOException {
        //this.request.generate(certificate, privateKey, rfcSolicitante, rfcEmisor, rfcReceptor, fechaInicio, fechaFinal, tipo);
        String idRequest = this.request.sendRequest(rfcEmisor);
        return this.request.getResult(idRequest);
    }
}

class Request extends RequestBase {
    private String typeRequest;
    private boolean isGenerated;
    private Mensajes mensajes;
    
    public boolean isGenerated() {
        return this.isGenerated;
    }
    
    public void setGenerated(boolean isGenerated) {
        this.isGenerated = isGenerated;
    }
    
    public Request() {
        super();
        String url = new Urls().getUrlSolicitud();
        String SOAPAction = new Urls().getUrlSolicitudAction();
        this.setUrl(url);
        this.setAction(SOAPAction);
        this.mensajes = new Mensajes();
    }
       
    public ResultadoSolicitudDescarga doRequest(X509Certificate certificate, PrivateKey privateKey, 
            String rfcSolicitante, String rfcEmisor, String rfcReceptor,
            String fechaInicio, String fechaFinal,
            TipoSolicitud tipo, String token) 
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException,
            IOException
    {
        this.generate(certificate, privateKey, rfcEmisor, rfcReceptor, rfcSolicitante, fechaInicio, fechaFinal);
        this.setTypeRequest(tipo);
        String xml = this.sendRequest(token);
        return this.getResult(xml);
    }
    /**
     * Constructor of Request class
     *
     * @param url
     * @param SOAPAction
     */
    public String sendRequest(String token) throws IOException {
        return this.send(token);
    }
    
    public Request(String url, String SOAPAction) {
        super(url, SOAPAction);
    }

    public void setTypeRequest(TipoSolicitud tipoSolicitud) {
        this.typeRequest = tipoSolicitud.toString();
    }

    @Override
    public ResultadoSolicitudDescarga getResult(String xmlResponse) {
        System.out.println("xmlResponse de Request: "+xmlResponse);
        Document doc = convertStringToXMLDocument(xmlResponse);
        //Verify XML document is build correctly
        ResultadoSolicitudDescarga rds = new ResultadoSolicitudDescarga();
        rds.setOperacionCorrecta(false);
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName("SolicitaDescargaResult");
            if(nl != null && nl.getLength() >= 0) {
                String idSolicitud = nl
                    .item(0)
                    .getAttributes()
                    .getNamedItem("IdSolicitud").getTextContent();
                rds.setOperacionCorrecta(true);
                rds.setIdSolicitud(idSolicitud);   
            }
        }
        return rds;
    }

    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @param rfcEmisor
     * @param rfcReceptor
     * @param rfcSolicitante
     * @param fechaInicial
     * @param fechaFinal
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */    
    public ResultadoSolicitudDescarga generate(X509Certificate certificate,
                         PrivateKey privateKey,
                         String rfcEmisor,
                         String rfcReceptor,
                         String rfcSolicitante,
                         String fechaInicial,
                         String fechaFinal
    ) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        
        ResultadoSolicitudDescarga rg = new ResultadoSolicitudDescarga();
        rg.setOperacionCorrecta(true);
        
        fechaInicial = fechaInicial + "T00:00:00";
        fechaFinal = fechaFinal + "T23:59:59";

        String canonicalTimestamp = "<des:SolicitaDescarga xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\">" +
                "<des:solicitud RfcEmisor=\"" + rfcEmisor + "\" RfcReceptor=\"" + rfcReceptor + "\" RfcSolicitante=\"" + rfcSolicitante + "\" FechaInicial=\"" + fechaInicial + "\" FechaFinal=\"" + fechaFinal + "\" TipoSolicitud=\"" + this.typeRequest + "\">" +
                "</des:solicitud>" +
                "</des:SolicitaDescarga>";

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

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\" xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<s:Header/>" +
                "<s:Body>" +
                "<des:SolicitaDescarga>" +
                "<des:solicitud RfcEmisor=\"" + rfcEmisor + "\" RfcReceptor =\"" + rfcReceptor + "\" RfcSolicitante=\"" + rfcSolicitante + "\" FechaInicial=\"" + fechaInicial + "\" FechaFinal =\"" + fechaFinal + "\" TipoSolicitud=\"" + this.typeRequest + "\">" +
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
                "<X509Data>" +
                "<X509IssuerSerial>" +
                "<X509IssuerName>" + certificate.getIssuerX500Principal() + "</X509IssuerName>" +
                "<X509SerialNumber>" + certificate.getSerialNumber() + "</X509SerialNumber>" +
                "</X509IssuerSerial>" +
                "<X509Certificate>" + Base64.getEncoder().encodeToString(certificate.getEncoded()) + "</X509Certificate>" +
                "</X509Data>" +
                "</KeyInfo>" +
                "</Signature>" +
                "</des:solicitud>" +
                "</des:SolicitaDescarga>" +
                "</s:Body>" +
                "</s:Envelope>");
        rg.setMensaje(mensajes.getSolicitudGeneradaCorrectamente());
        rg.setOperacionCorrecta(true);
        
        this.isGenerated = true;
        return rg;
    }
}