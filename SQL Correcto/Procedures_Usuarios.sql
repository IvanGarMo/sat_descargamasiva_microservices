USE SatDescargaMasiva;
;
DELIMITER //
USE satdescargamasiva;
;
DELIMITER //
CREATE PROCEDURE guardaUsuarioRegistrado(
	IN _uidUserFirebase VARCHAR(100),
    IN _idMedio INT
)
	BEGIN
    IF NOT EXISTS(SELECT 1 FROM Usuario WHERE uidUserFirebase=_uidUserFirebase)
    THEN
		IF(_idMedio = 1) #Solo con correo electrónico se pide que verifiquen el correo
        THEN INSERT INTO Usuario(uidUserFirebase, MedioAutenticacion, activo, correoVerificado)
			VALUES(_uidUserFirebase, _idMedio, 0, 0);
        ELSE 
			INSERT INTO Usuario(uidUserFirebase, MedioAutenticacion, activo, correoVerificado) 
				VALUES(_uidUserFirebase, _idMedio, 0, 1);
        END IF;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE existeUsuario(
	IN _UidUserFirebase VARCHAR(100),
    OUT _Existe BIT
)
BEGIN
	IF EXISTS(SELECT 1 FROM Usuario WHERE uidUserFirebase=_UidUserFirebase)
    THEN SELECT 1 INTO _Existe;
    ELSE SELECT 0 INTO _Existe;
    END IF;
END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE estaUsuarioActivo(
	IN _uidUserFirebase VARCHAR(100),
    OUT _idUsuario BIGINT,
    OUT _activo BIT,
    OUT _medioAutenticacion INT,
    OUT _correoVerificado BIT
)
	BEGIN
		SELECT activo, MedioAutenticacion, correoVerificado, idUsuario 
        FROM Usuario
        WHERE uidUserFirebase=_uidUserFirebase
        INTO _activo, _medioAutenticacion, _correoVerificado, _idUsuario;
    END//
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE registraCorreoActivado(
	IN _UidUserFirebase VARCHAR(100),
    IN _CorreoActivado BIT
)
	BEGIN
		UPDATE Usuario SET correoVerificado=_CorreoActivado WHERE uidUserFirebase=_UidUserFirebase;
	END//
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE TerminaRegistro(
	IN _UidUserFirebase VARCHAR(100),
    IN _Nombre VARCHAR(100),
    IN _ApPaterno VARCHAR(100),
    IN _ApMaterno VARCHAR(100),
    IN _Correo VARCHAR(100),
    IN _IdSuscripcion INT,
    IN _Organizacion VARCHAR(100),
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    IF EXISTS(SELECT 1 FROM Usuario WHERE uidUserFirebase=_UidUserFirebase)
    THEN 
		UPDATE Usuario SET nombre=_nombre, apPaterno=_ApPaterno, apMaterno=_ApMaterno, correo=_Correo, 
        organizacion=_Organizacion, activo=1, FechaHoraRegistro=NOW(), idSuscripcion=_IdSuscripcion
        WHERE uidUserFirebase=_UidUserFirebase;
        SELECT 1, 'Datos del usuario guardados exitosamente. Se le redirigirá en un momento' INTO _OpValida, _Mensaje;
    ELSE 
		SELECT 0, 'Se intentó activar un usuario inexistente' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE ActualizaUsuario(
	IN _UidUserFirebase VARCHAR(100),
    IN _Nombre VARCHAR(100),
    IN _ApPaterno VARCHAR(100),
    IN _ApMaterno VARCHAR(100),
    IN _Organizacion VARCHAR(100),
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
	IF EXISTS(SELECT 1 FROM Usuario WHERE idUsuario=@_IdUsuario)
    THEN
		UPDATE Usuario SET nombre=_Nombre, apPaterno=_ApPaterno, apMaterno=_ApMaterno, organizacion=_Organizacion
        WHERE IdUsuario=@_IdUsuario;
        SELECT 1, 'Datos del usuario actualizados exitosamente' INTO _OpValida, _Mensaje;
    ELSE
		SELECT 0, 'Se intentó actualizar un usuario inexistente' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE SalvaTelefono(
	IN _UidUserFirebase VARCHAR(100),
    IN _Telefono VARCHAR(13), 
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
	IF EXISTS(SELECT 1 FROM Usuario_Telefono WHERE idUsuario=@_IdUsuario AND telefono=_Telefono)
    THEN SELECT 0, 'El teléfono ya está registrado' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO Usuario_Telefono(idUsuario, telefono) VALUES(@_IdUsuario, _Telefono);
        SELECT 1, 'El teléfono ha sido registrado exitosamente' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE EliminaTelefono(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdTelefono INT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    DELETE FROM Usuario_Telefono WHERE idUsuario=@_IdUsuario AND idTelefono=_IdTelefono;
    SELECT 1, 'Teléfono eliminado exitosamente' INTO _OpValida, _Mensaje;
    END //
DELIMITER ; 
;
DELIMITER //
CREATE PROCEDURE CargaUsuario(
	IN _UidUserFirebase VARCHAR(100),
    OUT _IdUsuario INT,
    OUT _UidUserFirebaseOut VARCHAR(100),
    OUT _Nombre VARCHAR(100),
    OUT _ApPaterno VARCHAR(100),
    OUT _ApMaterno VARCHAR(100),
    OUT _Correo VARCHAR(100),
    OUT _IdSuscripcion INT,
    OUT _Organizacion VARCHAR(100),
    OUT _Activo BOOLEAN
)
	BEGIN
		SELECT idUsuario, uidUserFirebase, nombre, apPaterno, apMaterno, correo, 
        idSuscripcion, organizacion, activo
        FROM Usuario WHERE UidUserFirebase=_UidUserFirebase
        INTO _IdUsuario, _UidUserFirebaseOut, _Nombre, _ApPaterno, _ApMaterno, _Correo,
        _IdSuscripcion, _Organizacion, _Activo;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE listaTelefonoUsuario(
	IN _UidUserFirebase VARCHAR(100)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    SELECT *
    FROM Usuario_Telefono UT WHERE UT.idUsuario=@_IdUsuario;
	END //
DELIMITER ;