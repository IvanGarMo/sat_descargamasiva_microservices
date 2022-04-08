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
CREATE TABLE SolicitudDescarga(
	idDescarga BIGINT PRIMARY KEY AUTO_INCREMENT,
	idDescargaSat VARCHAR(100),
    idCliente BIGINT,
    fechaInicioPeriodo DATE,
    fechaFinPeriodo DATE,
    rfcEmisor VARCHAR(13),
    rfcReceptor VARCHAR(13),
    rfcSolicitante VARCHAR(13),
    estado INT,
    noFacturas INT,
    descargasPermitidas INT
)
;
CREATE TABLE EstadoSolicitud(
	idEstado BIGINT PRIMARY KEY,
    descripcionEstado VARCHAR(100)
)
;
INSERT INTO EstadoSolicitud(idEstado, descripcionEstado) VALUES(1, 'Aceptada');
INSERT INTO EstadoSolicitud(idEstado, descripcionEstado) VALUES(2, 'En proceso');
INSERT INTO EstadoSolicitud(idEstado, descripcionEstado) VALUES(3, 'Terminada');
INSERT INTO EstadoSolicitud(idEstado, descripcionEstado) VALUES(4, 'Error');
INSERT INTO EstadoSolicitud(idEstado, descripcionEstado) VALUES(5, 'Rechazada');
INSERT INTO EstadoSolicitud(idEstado, descripcionEstado) VALUES(6, 'Vencida');
;
CREATE TABLE PaquetesUrl(
	idRegistro BIGINT PRIMARY KEY AUTO_INCREMENT,
    idDescarga BIGINT, 
    urlPaquetes VARCHAR(300)
)
;
SELECT * FROM SolicitudDescarga;
SELECT * FROM EstadoSolicitud;
SELECT * FROM PaquetesUrl;
SELECT * FROM Cliente;
SELECT * FROM Cliente_Usuario;

SELECT * FROM VistaGeneralSolicitud;
SELECT * FROM EstadoSolicitud;
DROP VIEW VistaGeneralSolicitud;
SELECT * FROM SolicitudDescarga;

DROP VIEW VistaGeneralSolicitud;
CREATE VIEW VistaGeneralSolicitud AS
SELECT SD.idDescarga, SD.idDescargaSat, SD.idCliente, SD.fechaInicioPeriodo, SD.fechaFinPeriodo,
	SD.rfcEmisor, SD.rfcReceptor, SD.rfcSolicitante, SD.noFacturas, SD.descargasPermitidas,
    ES.idEstado, IFNULL(ES.descripcionEstado, 'No disponible') AS descripcionEstado, 
    CONCAT_WS(' ', C.nombre, C.apPaterno, C.apMaterno) AS nombreCliente, 
    PU.urlPaquetes, CU.idUsuario
FROM SolicitudDescarga SD
LEFT JOIN EstadoSolicitud ES ON SD.estado=ES.idEstado
JOIN Cliente C ON SD.idCliente=C.idCliente
JOIN Cliente_Usuario CU ON C.idCliente=CU.idCliente
LEFT JOIN PaquetesUrl PU ON PU.idDescarga=SD.idDescarga
;
SELECT * FROM SolicitudDescarga;
SELECT * FROM Usuarios;
SELECT * FROM Clientes;