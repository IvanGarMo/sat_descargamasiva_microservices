USE SatDescargaMasiva;
;
DELIMITER //
CREATE PROCEDURE cambiaSuscripcionUsuario(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdSuscripcion INT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(100)
)
	BEGIN
		IF (_IdSuscripcion<>-1) AND NOT EXISTS((SELECT 1 FROM Suscripciones WHERE idSuscripcion=_IdSuscripcion))
			THEN SELECT 0, 'Se intentó seleccionar una suscripción inexistente' INTO _OpValida, _Mensaje;
		ELSEIF NOT EXISTS(SELECT 1 FROM Usuarios WHERE uidUserFirebase=_UidUserFirebase AND activo=1)
			THEN SELECT 0, 'Se intentó actualizar datos de un usuario inexistente' INTO _OpValida, _Mensaje;
		ELSE
			UPDATE Usuarios SET idSuscripcion=_IdSuscripcion WHERE uidUserFirebase=_UidUserFirebase;
            SELECT 1, 'Se actualizó exitosamente la suscripción del usuario' INTO _OpValida, _Mensaje;
        END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE CargaSuscripcionCliente(
	IN _UidUserFirebase VARCHAR(100),
    OUT _IdSuscripcion INT
)
	BEGIN
    SELECT idSuscripcion FROM Usuarios WHERE uidUserFirebase=_UidUserFirebase INTO _IdSuscripcion;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE CambiaSuscripcionCliente(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdSuscripcion INT, 
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(255)
)
	BEGIN
    IF NOT EXISTS(SELECT 1 FROM Suscripciones WHERE idSuscripcion=_IdSuscripcion)
    THEN SELECT 0, 'Se intentó seleccionar una suscripción inexistente' INTO _OpValida, _Mensaje;
    ELSE
		UPDATE Usuarios SET idSuscripcion=_IdSuscripcion WHERE uidUserFirebase=_UidUserFirebase;
        SELECT 1, 'Se ha actualizado exitosamente la suscripción' INTO _OpValida, _Mensaje;
	END IF;
	END //
DELIMITER ;