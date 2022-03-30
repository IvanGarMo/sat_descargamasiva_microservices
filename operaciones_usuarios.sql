DELIMITER //
CREATE PROCEDURE SalvaUsuario(
	IN _UidUserFirebase VARCHAR(100),
    IN _Nombre VARCHAR(100),
    IN _ApPaterno VARCHAR(100),
    IN _ApMaterno VARCHAR(100),
    IN _Correo VARCHAR(100),
    IN _IdSuscripcion INT,
    IN _Organizacion VARCHAR(100),
    IN _Activo BIT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    IF EXISTS(SELECT 1 FROM Usuarios WHERE UidUserFirebase=_UidUserFirebase)
    THEN SELECT 0, 'El usuario ya existe' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO Usuarios(UidUserFirebase, Nombre, ApPaterno, ApMaterno, Correo, IdSuscripcion, Organizacion, Activo)
		VALUES (_UidUserFirebase, _Nombre, _ApPaterno, _ApMaterno, _Correo, _IdSuscripcion, _Organizacion, _Activo);
		SELECT 1, 'Usuario registrado exitosamente' INTO _OpValida, _Mensaje;
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
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    IF EXISTS(SELECT 1 FROM Usuarios WHERE UidUserFirebase=_UidUserFirebase)
    THEN
		UPDATE Usuarios SET Nombre=_Nombre, ApPaterno=_ApPaterno, ApMaterno=_ApMatenro
        WHERE UidUserFirebase=_UidUserFirebase;
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
    DECLARE _IdUsuario INT;
	SELECT IdUsuario FROM Usuarios WHERE UidUserFirebase=_UidUserFirebase INTO _IdUsuario;
    IF EXISTS(SELECT 1 FROM UsuariosTelefonos WHERE IdUsuario=_IdUsuario AND Telefono=_Telefono)
    THEN SELECT 0, 'El teléfono ya está registrado' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO UsuariosTelefonos(IdUsuario, Telefono) VALUES(_IdUsuario, _Telefono);
        SELECT 1, 'El teléfono ha sido registrado exitosamente' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE EliminaTelefono(
	IN _IdUsuario INT,
    IN _IdTelefono INT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    DELETE FROM UsuariosTelefonos WHERE IdUsuario=_IdUsuario AND IdTelefono=_IdTelefono;
    SELECT 1, 'Teléfono eliminado exitosamente' INTO _OpValida, _Mensaje;
    END //
DELIMITER ; 