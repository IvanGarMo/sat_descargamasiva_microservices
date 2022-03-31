CREATE DATABASE SatDescargaMasiva;
USE SatDescargaMasiva;
;
CREATE TABLE Usuarios(
	IdUsuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    UidUserFirebase VARCHAR(100) NOT NULL,
    Nombre VARCHAR(100),
    ApPaterno VARCHAR(100),
    ApMaterno VARCHAR(100),
    Correo VARCHAR(100),
    IdSuscripcion INT,
    Organizacion VARCHAR(100),
    Activo BIT
)
;
CREATE TABLE UsuariosTelefonos(
	IdTelefono BIGINT PRIMARY KEY AUTO_INCREMENT,
    IdUsuario BIGINT,
    Telefono VARCHAR(13) NOT NULL,
    FOREIGN KEY(IdUsuario) REFERENCES Usuarios(IdUsuario)
)
;
CREATE TABLE Clientes(
	IdCliente BIGINT PRIMARY KEY AUTO_INCREMENT,
    Rfc CHAR(13),
    IdUsuario INT,
    Nombre VARCHAR(100),
    ApPaterno VARCHAR(100),
    ApMaterno VARCHAR(100)
)
;
ALTER TABLE Clientes ADD CuentaConContrasena BIT;
ALTER TABLE Clientes ADD Contrasena VARCHAR(100);
;
CREATE TABLE CertificadoClientes(
	IdCertificado BIGINT PRIMARY KEY AUTO_INCREMENT,
    CuentaConCertificado BIT,
    IdCliente BIGINT, 
    Certificado BLOB,
    FOREIGN KEY(IdCliente) REFERENCES Clientes(IdCliente)
)
;
CREATE TABLE UsuariosClientes(
	IdUsuario BIGINT,
    IdCliente BIGINT,
    FOREIGN KEY(IdUsuario) REFERENCES Usuarios(IdUsuario),
    FOREIGN KEY(IdCliente) REFERENCES Clientes(IdCliente)
)
;
 CREATE TABLE Suscripciones (
	IdSuscripcion INT AUTO_INCREMENT PRIMARY KEY,
	Descripcion VARCHAR(255),
	LimiteInferiorDescargas BIGINT,
	LimiteSuperiorDescargas BIGINT,
	CostoPorXml DECIMAL(6, 4),
	Activo BIT
 )
 ;
 INSERT INTO Suscripciones(Descripcion, LimiteInferiorDescargas, LimiteSuperiorDescargas, CostoPorXml, Activo)
	VALUES('Suscripcion 1', 1, 1000, 0.50, 1);
INSERT INTO Suscripciones(Descripcion, LimiteInferiorDescargas, LimiteSuperiorDescargas, CostoPorXml, Activo)
	VALUES('Suscripcion 2', 1001, 10000, 0.30, 1);
INSERT INTO Suscripciones(Descripcion, LimiteInferiorDescargas, LimiteSuperiorDescargas, CostoPorXml, Activo)
	VALUES('Suscripcion 3', 10001, 10000, 0.10, 1);
 ;
CREATE VIEW UsuariosCliente AS
SELECT U.UidUserFirebase, U.IdUsuario, CONCAT_WS(' ', U.Nombre, U.ApPaterno, U.ApMaterno) AS NombreUsuario, U.Correo, 
	C.IdCliente, C.Rfc, C.Nombre, C.ApPaterno, C.ApMaterno, 
    CONCAT_WS(' ', C.Nombre, C.ApPaterno, C.ApMaterno) AS NombreCliente, 
    C.CuentaConContrasena, C.Contrasena, 
    IFNULL(CC.CuentaConCertificado, 0) AS CuentaConCertificado, 
    CC.Certificado
FROM Usuarios AS U 
JOIN UsuariosClientes AS UC ON U.IdUsuario=UC.IdUsuario
JOIN Clientes AS C ON UC.IdCliente = C.IdCliente
LEFT JOIN CertificadoClientes AS CC ON C.IdCliente=CC.IdCliente;
;