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
	idDescargaSat BIGINT,
    idCliente BIGINT,
    fechaInicioPeriodo DATETIME,
    fechaFinPeriodo DATETIME,
    rfcEmisor VARCHAR(13),
    rfcReceptor VARCHAR(13),
    rfcSolicitante VARCHAR(13),
    estado INT,
    noFacturas INT,
    descargasPermitidas INT
)
;
SELECT * FROM SolicitudDescarga;
SELECT * FROM Usuarios;
SELECT * FROM Clientes;