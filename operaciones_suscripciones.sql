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
		IF (_IdSuscripcion<>-1) AND NOT EXISTS((SELECT 1 FROM Suscripciones WHERE IdSuscripcion=_IdSuscripcion))
			THEN SELECT 0, 'Se intentó seleccionar una suscripción inexistente' INTO _OpValida, _Mensaje;
		ELSEIF NOT EXISTS(SELECT 1 FROM Usuarios WHERE UidUserFirebase=_UidUserFirebase AND Activo=1)
			THEN SELECT 0, 'Se intentó actualizar datos de un usuario inexistente' INTO _OpValida, _Mensaje;
		ELSE
			UPDATE Usuarios SET IdSuscripcion=_IdSuscripcion WHERE UidUserFirebase=_UidUserFirebase;
            SELECT 1, 'Se actualizó exitosamente la suscripción del usuario' INTO _OpValida, _Mensaje;
        END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE cargaSuscripcionUsuario(
	IN _UidUserFirebase VARCHAR(100)
)
	BEGIN
		SELECT S.IdSuscripcion, S.Descripcion, S.LimiteInferiorDescargas, S.LimiteSuperiorDescargas, 
        S.CostoPorXml, S.Activo
        FROM Usuarios AS U
        JOIN Suscripciones AS S ON U.IdSuscripcion=S.IdSuscripcion
        WHERE U.UidUserFirebase=_UidUserFirebase;
    END //
DELIMITER ;