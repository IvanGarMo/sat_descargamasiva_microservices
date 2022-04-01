CREATE DATABASE SatDescargaMasiva;
USE SatDescargaMasiva;
;
CREATE TABLE Usuarios(
	idUsuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    uidUserFirebase VARCHAR(100) NOT NULL,
    nombre VARCHAR(100),
    apPaterno VARCHAR(100),
    apMaterno VARCHAR(100),
    correo VARCHAR(100),
    idSuscripcion INT,
    organizacion VARCHAR(100),
    activo BIT
)
;
CREATE TABLE UsuariosTelefonos(
	idTelefono BIGINT PRIMARY KEY AUTO_INCREMENT,
    idUsuario BIGINT,
    telefono VARCHAR(13) NOT NULL,
    FOREIGN KEY(idUsuario) REFERENCES Usuarios(idUsuario)
)
;
CREATE TABLE Clientes(
	idCliente BIGINT PRIMARY KEY AUTO_INCREMENT,
    rfc CHAR(13),
    idUsuario INT,
    nombre VARCHAR(100),
    apPaterno VARCHAR(100),
    apMaterno VARCHAR(100),
    cuentaConContrasena BIT,
    contrasena VARCHAR(200)
)
;
CREATE TABLE CertificadoClientes(
	idCertificado BIGINT PRIMARY KEY AUTO_INCREMENT,
    cuentaConCertificado BIT,
    idCliente BIGINT, 
    certificado BLOB,
    FOREIGN KEY(idCliente) REFERENCES Clientes(idCliente)
)
;
CREATE TABLE UsuariosClientes(
	idUsuario BIGINT,
    idCliente BIGINT,
    FOREIGN KEY(idUsuario) REFERENCES Usuarios(idUsuario),
    FOREIGN KEY(idCliente) REFERENCES Clientes(idCliente)
)
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
 INSERT INTO Suscripciones(descripcion, limiteInferiorDescargas, limiteSuperiorDescargas, costoPorXml, activo)
	VALUES('Suscripcion 1', 1, 1000, 0.50, 1);
INSERT INTO Suscripciones(descripcion, limiteInferiorDescargas, limiteSuperiorDescargas, costoPorXml, activo)
	VALUES('Suscripcion 2', 1001, 10000, 0.30, 1);
INSERT INTO Suscripciones(descripcion, limiteInferiorDescargas, limiteSuperiorDescargas, costoPorXml, activo)
	VALUES('Suscripcion 3', 10001, 10000, 0.10, 1);
;
CREATE VIEW UsuariosCliente AS
SELECT U.uidUserFirebase, U.idUsuario, 
	CONCAT_WS(' ', u.Nombre, u.ApPaterno, u.ApMaterno) AS nombreUsuario, 
	U.correo, 
	C.idCliente, C.rfc, C.nombre, C.apPaterno, C.apMaterno, 
	CONCAT_WS(' ', C.nombre, C.apPaterno, C.apMaterno) AS nombreCliente, 
	C.cuentaConContrasena, C.contrasena, 
	IFNULL(CC.cuentaConCertificado, 0) AS cuentaConCertificado, 
	CC.certificado
FROM Usuarios AS U 
JOIN UsuariosClientes AS UC ON U.IdUsuario=UC.IdUsuario
JOIN Clientes AS C ON UC.IdCliente = C.IdCliente
LEFT JOIN CertificadoClientes AS CC ON C.IdCliente=CC.IdCliente
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