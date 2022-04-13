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
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcioN) VALUE("-1", "Todos");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("acreditamientoieps10", "Acreditamiento del IEPS");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("aerolineas", "Aerolíneas");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("certificadodedestruccion", "Certificado de destrucción");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("cfdiregistrofiscal", "CFDI Registro Fiscal");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("comercioexterior10", "Comercio Exterior (1.0)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("comercioexterior11", "Comercio Exterior (1.1)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("comprobante", "Comprobante");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("consumodecombustibles", "Consumo de Combustibles (1.0)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("consumodecombustibles11", "Consumo de Combustibles (1.1)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("detallista", "Sector de ventas al detalle");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("divisas", "Compra venta de divisas");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("donat11", "Donatarias (1.1)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ecc11", "Estado de Cuenta Combustible (1.1)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ecc12", "Estado de Cuenta Combustible (1.2)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("gastoshidrocarburos10", "Hidrocarburos (1.0)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("iedu", "Instituciones Educativas Privadas");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("implocal", "Impuestos locales");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ine11", "INE");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ingresoshidrocarburos", "Ingreso hidrocarburos");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("leyendasfisc", "Leyendas Fiscales");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("nomina11", "Nómina (1.1)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("nomina12", "Nómina (1.2)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("notariospublicos", "Notarios públicos");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("obrasarteantiguedades", "Obras de arte plástica y antiguedades");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("pagoenespecie", "Pago en especie");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("pagos10", "Pagos (1.0)");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("pfic", "Persona física integrante de coordinado");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("renovacionysustitucionvehiculos", "Renovación y sustitución de vehículos");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("servicioparcialconstruccion", "Servicios parciales de construcción");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("spei", "SPEI de tercero a tercero");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("terceros11", "Terceros");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("turistapasajeroextranjero", "Turista pasajero extranjero");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("valesdedespensa", "Vales de despensa");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("vehiculousado", "Vehículo usado");
INSERT INTO Solicitud_Descarga_Complemento(valor, descripcion) VALUE("ventavehiculos11", "Venta de vehículos");
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
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(-1, "Todas");
INSERT INTO Solicitud_Descarga_Estado_Solicitud(idEstado, descripcionEstado) VALUES(0, "Información no disponible");
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
USE SatDescargaMasiva;
SELECT * FROM Solicitud_Descarga;
SELECT * FROM Solicitud_Descarga_Estado_Solicitud;
SELECT * FROM Solicitud_Descarga_Estado_Solicitud;
SELECT * FROM VistaGeneralSolicitud;
SELECT * FROM Solicitud_Descarga_Tipo_Solicitud;
DROP VIEW VistaGeneralSolicitud;
SELECT * FROM Solicitud_Descarga_Estado_Comprobante;
SELECT * FROM Cliente_Usuario;
SELECT * FROM Solicitud_Descarga;
SELECT * FROM Solicitud_Descarga_Rfc_Receptor;
;
DROP VIEW VistaGeneralSolicitud;

SELECT * FROM Solicitud_Descarga;
SELECT * FROM VistaGeneralSolicitud;
SELECT * FROM Cliente;

SELECT * FROM Solicitud_Descarga_Rfc_Receptor;

CREATE VIEW VistaGeneralSolicitud AS
SELECT SD.idDescarga, SD.idDescargaSat, SD.idCliente, CU.idUsuario, SD.fechaInicioPeriodo, SD.fechaFinPeriodo,
	SD.rfcSolicitante, SD.rfcEmisor, 
    SD.noFacturas,
    SD.esUidSolicitado, SD.uid,
    SDES.idEstado, IFNULL(SDES.descripcionEstado, 'No disponible') AS descripcionEstado, 
    CONCAT_WS(' ', C.nombre, C.apPaterno, C.apMaterno) AS nombreCliente,
    IFNULL(idComplemento, -1) AS idComplemento,
    IFNULL(SDC.descripcion, 'Ninguno') as complemento, SDEC.valor AS idEstadoComprobante,
    SDEC.descripcion AS estadoComprobante,
    SDTS.valor AS idTipoSolicitud,
    SDTS.descripcion AS tipoSolicitud
FROM Solicitud_Descarga SD
JOIN Cliente C ON SD.idCliente=C.idCliente
JOIN Cliente_Usuario CU ON C.idCliente=CU.idCliente
LEFT JOIN Solicitud_Descarga_Complemento SDC ON SD.idComplemento=SDC.id
LEFT JOIN Solicitud_Descarga_Estado_Comprobante SDEC ON SD.estadoComprobante=SDEC.valor
LEFT JOIN Solicitud_Descarga_Tipo_Solicitud SDTS ON SD.tipoSolicitud=SDTS.valor
LEFT JOIN Solicitud_Descarga_Estado_Solicitud SDES ON SD.estado=SDES.idEstado
;