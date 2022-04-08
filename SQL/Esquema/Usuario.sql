USE SatDescargaMasiva;
;
CREATE TABLE Usuario(
	idUsuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    uidUserFirebase VARCHAR(100) NOT NULL,
    nombre VARCHAR(100),
    apPaterno VARCHAR(100),
    apMaterno VARCHAR(100),
    correo VARCHAR(100),
    idSuscripcion INT,
    organizacion VARCHAR(100),
    activo BIT, 
    correoVerificado BIT
)
;
ALTER TABLE Usuario ADD COLUMN correoVerificado BIT;
;
ALTER TABLE Usuario ADD COLUMN MedioAutenticacion INT;
;
CREATE TABLE MedioAutenticacion(
	idMedioAutenticacion BIGINT PRIMARY KEY AUTO_INCREMENT,
    descripcion VARCHAR(100)
)
;
INSERT INTO MedioAutenticacion(descripcion) VALUES('Correo y contraseña');
INSERT INTO MedioAutenticacion(descripcion) VALUES('Google');
;
ALTER TABLE Usuario ADD COLUMN FechaHoraRegistro DATETIME;
;
CREATE TABLE Usuario_Telefono (
	idTelefono BIGINT PRIMARY KEY AUTO_INCREMENT,
    idUsuario BIGINT,
    telefono VARCHAR(13) NOT NULL,
    FOREIGN KEY(idUsuario) REFERENCES Usuario(idUsuario)
)
;
DELIMITER //
CREATE PROCEDURE ReturnIdUsuario(
	IN _uidUsuarioFirebase VARCHAR(100),
    OUT _idUsuario BIGINT
)
	BEGIN 
		SELECT idUsuario
        FROM Usuario WHERE uidUserFirebase=_uidUsuarioFirebase
        INTO _idUsuario;
    END //
DELIMITER ;
;