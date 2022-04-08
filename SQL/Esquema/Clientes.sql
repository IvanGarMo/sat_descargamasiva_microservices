USE SatDescargaMasiva;
;
CREATE TABLE Cliente(
	idCliente BIGINT PRIMARY KEY AUTO_INCREMENT,
    rfc CHAR(13),
    nombre VARCHAR(100),
    apPaterno VARCHAR(100),
    apMaterno VARCHAR(100),
    certificadoNube BIT,
    certificadoBaseDatos BIT
)
;
CREATE INDEX Rfc_Index ON Cliente(rfc);
;
CREATE TABLE Cliente_Usuario (
	idCliente BIGINT,
    idUsuario BIGINT,
    relacionActiva BIT,
    FOREIGN KEY(idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY(idUsuario) REFERENCES Usuario(idUsuario),
    PRIMARY KEY(idCliente, idUsuario)
)
;
CREATE TABLE Cliente_MedioContacto_Descripcion(
	idMedio BIGINT PRIMARY KEY AUTO_INCREMENT,
    descripcion VARCHAR(500), 
    activo BIT
)
;
INSERT INTO Cliente_MedioContacto_Descripcion(descripcion, activo) VALUE('Teléfono celular', 1);
INSERT INTO Cliente_MedioContacto_Descripcion(descripcion, activo) VALUE('Teléfono fijo', 1);
INSERT INTO Cliente_MedioContacto_Descripcion(descripcion, activo) VALUE('Correo electrónico', 1);
;
CREATE TABLE Cliente_MedioContacto(
	idMedioRegistrado BIGINT PRIMARY KEY AUTO_INCREMENT,
    idCliente BIGINT,
    idMedio BIGINT,
    descripcion VARCHAR(500),
    FOREIGN KEY(idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY(idMedio) REFERENCES Cliente_MedioContacto_Descripcion(idMedio)
)
;
CREATE TABLE Cliente_Contrasena (
	idContrasena BIGINT PRIMARY KEY AUTO_INCREMENT,
    idCliente BIGINT,
    cuentaConContrasena BIT,
    contrasena VARCHAR(200), 
    FOREIGN KEY(idCliente) REFERENCES Cliente(idCliente)
)
;
CREATE TABLE Cliente_Certificado_Local (
	idCertificado BIGINT PRIMARY KEY AUTO_INCREMENT,
    idCliente BIGINT,
    cuentaConCertificado BIT,
    certificado BLOB,
    FOREIGN KEY(idCliente) REFERENCES Cliente(idCliente)
)
;
CREATE TABLE Cliente_Certificado_Nube (
	idCertificado BIGINT PRIMARY KEY AUTO_INCREMENT,
    idCliente BIGINT, 
    cuentaConCertificado BIT,
    urlCertificado VARCHAR(1500), 
    FOREIGN KEY(idCliente) REFERENCES Cliente(idCliente)
)
;
CREATE TABLE Cliente_Operacion (
	idOperacion BIGINT PRIMARY KEY AUTO_INCREMENT,
    descripcionOperacion VARCHAR(300)
)
;
INSERT INTO Cliente_Operacion(descripcionOperacion) VALUES('Cliente desactivado');
INSERT INTO Cliente_Operacion(descripcionOperacion) VALUES('Cliente activado');
INSERT INTO Cliente_Operacion(descripcionOperacion) VALUES('Cliente creado');
;
CREATE TABLE Cliente_Movimiento (
	idMovimiento BIGINT PRIMARY KEY AUTO_INCREMENT,
    idCliente BIGINT,
    idOperacion BIGINT,
    esDesactivacion BIT,
    idUsuarioDesactiva BIGINT,
    esActivacion BIT,
    idUsuarioActiva BIGINT, 
    fechaHoraMovimiento TIMESTAMP
)
;
CREATE VIEW VistaClienteUsuario AS
	SELECT 
		C.idCliente, C.rfc, C.nombre AS nombreCliente, C.apPaterno AS apPaternoCliente, C.apMaterno as apMaternoCliente, 
		U.idUsuario, U.nombre AS nombreUsuario, U.apPaterno AS apPaternoUsuario, U.apMaterno AS apMaternoUsuario,
        IFNULL(CCO.cuentaConContrasena, 0) AS cuentaConContrasena,
        CASE WHEN C.certificadoNube = 1 THEN IFNULL(CCEN.cuentaConCertificado, 0)
			 WHEN C.certificadoBaseDatos = 1 THEN IFNULL(CCEL.cuentaConCertificado, 0)
             END AS cuentaConCertificado
    FROM Cliente C 
    JOIN Cliente_Usuario UC ON C.idCliente=UC.idCliente
    JOIN Usuario U ON UC.idUsuario=U.idUsuario
    LEFT JOIN Cliente_Contrasena CCO ON C.idCliente=CCO.idCliente
    LEFT JOIN Cliente_Certificado_Local CCEL ON C.idCliente=CCEL.idCliente
    LEFT JOIN Cliente_Certificado_Nube CCEN ON C.idCliente=CCEN.idCliente
;