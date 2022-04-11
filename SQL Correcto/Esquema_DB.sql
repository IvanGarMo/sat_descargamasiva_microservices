CREATE DATABASE SatDescargaMasiva;
USE SatDescargaMasiva;
;
;
 CREATE TABLE Suscripciones (
	idSuscripcion INT AUTO_INCREMENT PRIMARY KEY,
	descripcion VARCHAR(255),
	limiteInferiorDescargas BIGINT,
	limiteSuperiorDescargas BIGINT,
	costoPorXml DECIMAL(6, 4),
	activo BIT
 )
 ;
 CREATE TABLE Urls(
	IdUrl INT PRIMARY KEY AUTO_INCREMENT,
    DescripcionUrl VARCHAR(500),
    Url VARCHAR(500)
 )
 ;
 INSERT INTO Urls(DescripcionUrl, Url) VALUES("urlAutentica", "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/Autenticacion/Autenticacion.svc");
 INSERT INTO Urls(Descripcion, Url) VALUES()
 ;
 INSERT INTO Suscripciones(descripcion, limiteInferiorDescargas, limiteSuperiorDescargas, costoPorXml, activo)
	VALUES('Suscripcion 1', 1, 1000, 0.50, 1);
INSERT INTO Suscripciones(descripcion, limiteInferiorDescargas, limiteSuperiorDescargas, costoPorXml, activo)
	VALUES('Suscripcion 2', 1001, 10000, 0.30, 1);
INSERT INTO Suscripciones(descripcion, limiteInferiorDescargas, limiteSuperiorDescargas, costoPorXml, activo)
	VALUES('Suscripcion 3', 10001, 10000, 0.10, 1);
;
;
CREATE TABLE Solicitud_Descarga_Tipo_Solicitud(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    valor VARCHAR(5),
    descripcion VARCHAR(100)
)
;
INSERT INTO Solicitud_Descarga_Tipo_Solicitud(valor, descripcion) VALUES("-1", "Todos");
INSERT INTO Solicitud_Descarga_Tipo_Solicitud(valor, descripcion) VALUES("I", "Ingreso");
INSERT INTO Solicitud_Descarga_Tipo_Solicitud(valor, descripcion) VALUES("E", "Egreso");
INSERT INTO Solicitud_Descarga_Tipo_Solicitud(valor, descripcion) VALUES("T", "Traslado");
INSERT INTO Solicitud_Descarga_Tipo_Solicitud(valor, descripcion) VALUES("N", "Nómina");
INSERT INTO Solicitud_Descarga_Tipo_Solicitud(valor, descripcion) VALUES("P", "Pago");
;
CREATE TABLE Solicitud_Descarga_Estado_Comprobante(
	id BIGINT PRIMARY KEY AUTO_INCREMENT, 
    valor VARCHAR(5),
    descripcion VARCHAR(100)
)
;
INSERT INTO Solicitud_Descarga_Estado_Comprobante(valor, descripcion) VALUES("-1", "Todos");
INSERT INTO Solicitud_Descarga_Estado_Comprobante(valor, descripcion) VALUES("0", "Cancelado");
INSERT INTO Solicitud_Descarga_Estado_Comprobante(valor, descripcion) VALUES("1", "Vigente");
;
CREATE TABLE Solicitud_Descarga_Complemento(
	id BIGINT PRIMARY KEY AUTO_INCREMENT, 
    valor VARCHAR(200), 
    descripcion VARCHAR(200)
)
;
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("acreditamientoieps10", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("aerolineas", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("certificadodedestruccion", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("cfdiregistrofiscal", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("comercioexterior10", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("comercioexterior11", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("comprobante", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("consumodecombustibles", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("consumodecombustibles11", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("detallista", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("divisas", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("donat11", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ecc11", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ecc12", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("gastoshidrocarburos10", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("iedu", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("implocal", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ine11", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ingresoshidrocarburos", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("leyendasfisc", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("nomina11", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("nomina12", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("notariospublicos", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("obrasarteantiguedades", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("pagoenespecie", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("pagos10", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("pfic", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("renovacionysustitucionvehiculos", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("servicioparcialconstruccion", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("spei", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("terceros11", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("turistapasajeroextranjero", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("valesdedespensa", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("vehiculousado", "");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ventavehiculos11", "");
;
CREATE TABLE Solicitud_Descarga(
	idDescarga BIGINT PRIMARY KEY AUTO_INCREMENT,
	idDescargaSat VARCHAR(100),
    idCliente BIGINT,
    fechaInicioPeriodo DATE,
    fechaFinPeriodo DATE,
    rfcEmisor VARCHAR(13),
    rfcSolicitante VARCHAR(13),
    estado INT,
    noFacturas INT,
    descargasPermitidas INT,
    esUidSolicitado BIT,
    uid VARCHAR(200),
    idComplemento INT,
    estadoComprobante INT, 
    tipoSolicitud VARCHAR(3)
)
;
CREATE TABLE Solicitud_Descarga_Rfc_Receptor (
	id BIGINT PRIMARY KEY AUTO_INCREMENT, 
    rfcReceptor VARCHAR(13),
    idDescarga BIGINT,
    FOREIGN KEY(idDescarga) REFERENCES Solicitud_Descarga(idDescarga)
)
;
CREATE TABLE Solicitud_Descarga_Estado_Solicitud(
	idEstado BIGINT PRIMARY KEY,
    descripcionEstado VARCHAR(100)
)
;
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(1, 'Aceptada');
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(2, 'En proceso');
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(3, 'Terminada');
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(4, 'Error');
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(5, 'Rechazada');
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(6, 'Vencida');
;
CREATE TABLE Solicitud_Descarga_Paquetes_Url(
	idRegistro BIGINT PRIMARY KEY AUTO_INCREMENT,
    idDescarga BIGINT, 
    urlPaquetes VARCHAR(300)
)
;
CREATE VIEW VistaGeneralSolicitud AS
SELECT SD.idDescarga, SD.idCliente, SD.fechaInicioPeriodo, SD.fechaFinPeriodo,
	SD.rfcSolicitante, SD.noFacturas,
    SD.esUidSolicitado, SD.uid,
    SDES.idEstado, IFNULL(SDES.descripcionEstado, 'No disponible') AS descripcionEstado, 
    CONCAT_WS(' ', C.nombre, C.apPaterno, C.apMaterno) AS nombreCliente,
    IFNULL(idComplemento, -1) AS idComplemento,
    IFNULL(SDC.descripcion, 'Ninguno') as complemento, SDEC.descripcion AS estadoComprobante,
    SDTS.descripcion AS tipoSolicitud
FROM Solicitud_Descarga SD
JOIN Cliente C ON SD.idCliente=C.idCliente
JOIN Cliente_Usuario CU ON C.idCliente=CU.idCliente
LEFT JOIN Solicitud_Descarga_Complemento SDC ON SD.idComplemento=SDC.id
LEFT JOIN Solicitud_Descarga_Estado_Comprobante SDEC ON SD.estadoComprobante=SDEC.valor
LEFT JOIN Solicitud_Descarga_Tipo_Solicitud SDTS ON SD.tipoSolicitud=SDTS.valor
LEFT JOIN Solicitud_Descarga_Estado_Solicitud SDES ON SD.estado=SDES.idEstado
;