DELIMITER //
CREATE PROCEDURE activaCliente(
	IN _IdUsuario BIGINT,
    IN _Rfc VARCHAR(13), 
    OUT _OpValida BIT, 
    OUT _Mensaje VARCHAR(250)
)
	BEGIN
		IF EXISTS(SELECT 1 FROM Cliente WHERE rfc=_Rfc) # Si el cliente ya está registrado, veo si está activo con otro despacho
        THEN
			SET @_IdCliente = -1;
            SELECT idCliente FROM Cliente WHERE rfc=_Rfc INTO @_IdCliente;
			IF EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idCliente=@_IdCliente)
			THEN SELECT 1, 'Este cliente ya se encuentra registrado en otro despacho' INTO _OpValida, _Mensaje;
            ELSE 
				SELECT 0, '' INTO _OpValida, _Mensaje;
            END IF;
        ELSE
			SELECT 0, '' INTO _OpValida, _Mensaje;
        END IF;
    END
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE guardaCliente(
	IN _UidUserFirebase VARCHAR(100),
	IN _Rfc VARCHAR(13),
    IN _Nombre VARCHAR(100),
    IN _ApPaterno VARCHAR(100),
    IN _ApMaterno VARCHAR(100),
    OUT _IdCliente INT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
		SET @_IdUsuario= -1; 
		CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
        
        SET @_OpValida = false;
        SET @_Mensaje = '';
        CALL activaCliente(@_IdUsuario, @_Rfc, @_OpValida, @_Mensaje);
		IF EXISTS(SELECT 1 FROM Cliente WHERE Rfc=_Rfc)
			THEN SELECT 1, @_Mensaje INTO _OpValida, _Mensaje;
		ELSE 
			INSERT INTO Cliente(rfc, nombre, apPaterno, apMaterno)
			VALUES (_Rfc, _Nombre, _ApPaterno, _ApMaterno);
			SET @_IdClienteInsertado = -1;
			SELECT LAST_INSERT_ID() INTO @_IdClienteInsertado;
			INSERT INTO Cliente_Usuario(idCliente, idUsuario, relacionActiva) VALUES(@_IdClienteInsertado, @_IdUsuario, 1);
			SELECT 1, 'Cliente registrado exitosamente', @_IdClienteInsertado INTO _OpValida, _Mensaje, _IdCliente;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE listaClientes(
	IN _UidUserFirebase VARCHAR(100),
    IN _Rfc VARCHAR(13),
    IN _Nombre VARCHAR(100),
    IN _ApPat VARCHAR(100),
    IN _ApMat VARCHAR(100)
)
	BEGIN 
    SET @_IdUsuario= -1; 
    CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    SELECT V.idCliente, V.rfc, V.nombreCliente, V.apPaternoCliente, V.apMaternoCliente, V.cuentaConContrasena, V.cuentaConCertificado
    FROM VistaClienteUsuario V
    WHERE IdUsuario=@_IdUsuario
    AND (_Rfc='' OR rfc LIKE CONCAT('%', _Rfc, '%'))
    AND (_Nombre='' OR nombre LIKE CONCAT('%', _Nombre ,'%'))
    AND (_ApPat='' OR apPaterno LIKE CONCAT('%', _ApPat ,'%'))
    AND (_ApMat='' OR apMaterno LIKE CONCAT('%', _ApMat ,'%'));
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE getCliente(
	IN _IdCliente INT,
    OUT _Rfc VARCHAR(13),
    OUT _IdUsuario BIGINT,
    OUT _Nombre VARCHAR(100),
    OUT _ApPaterno VARCHAR(100),
    OUT _ApMaterno VARCHAR(100),
    OUT _CuentaConContrasena BIT,
    OUT _CuentaConCertificado BIT
)
	BEGIN
    SELECT V.rfc, V.nombreCliente, V.apPaternoCliente, V.apMaternoCliente, V.cuentaConContrasena, 
    V.cuentaConCertificado
    FROM VistaClienteUsuario V
    WHERE V.idCliente=_IdCliente
    INTO _Rfc, _IdUsuario, _Nombre, _ApPaterno, _ApMaterno, _CuentaConContrasena, _CuentaConCertificado;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE actualizaCliente(
	IN _IdCliente INT,
	IN _Rfc VARCHAR(13),
    IN _Nombre VARCHAR(100),
    IN _ApPaterno VARCHAR(100),
    IN _ApMaterno VARCHAR(100), 
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'El cliente no existe para ese usuario' INTO _OpValida, _Mensaje;
	ELSE
		UPDATE Cliente 
        SET rfc=_Rfc, nombre=_Nombre, apPaterno=_ApPaterno, apMaterno=_ApMaterno
        WHERE idCliente=_IdCliente;
        SELECT 1, 'Datos del cliente actualizados exitosamente' INTO _OpValida, _Mensaje;
	END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE desactivaCliente(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó eliminar un usuario inexistente' INTO _OpValida, _Mensaje;
    ELSE
		DELETE FROM Cliente_Usuario WHERE idCliente=_IdCliente AND idUsuario=@_IdUsuario;
		INSERT INTO Cliente_Movimiento(idCliente, idOperacion, esDesactivacion, idUsuarioDesactiva, 
			esActivacion, idUsuarioActiva, fechaHoraMovimiento)
		VALUES(_IdCliente, 1, 1, @_IdUsuario, 0, null, NOW());
        SELECT 1, 'Se ha eliminado correctamente el usuario' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE actualizaContrasena(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    IN _Contrasena VARCHAR(100),
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'Error: Se intentó actualizar la contraseña de un usuario inexistente' INTO _OpValida, _Mensaje;
    ELSE
		UPDATE Cliente_Contrasena SET cuentaConContrasena=1, contrasena=_Contrasena
        WHERE idCliente=_IdCliente;
        SELECT 1, 'Se ha guardado correctamente la contraseña del usuario' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE actualizaCertificadoBlob(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    IN _Certificado BLOB,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó actualizar el certificado de un cliente inexistente' INTO _OpValida, _Mensaje;
    ELSEIF EXISTS(SELECT 1 FROM Cliente_Certificado_Local WHERE idCliente=_IdCliente)
		THEN 
        UPDATE Cliente_Certificado_Local SET certificado=_Certificado WHERE idCliente=_IdCliente;
        SELECT 1, 'Se ha actualizado correctamente el certificado' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO Cliente_Certificado_Local(idCliente, cuentaConCertificado, certificado) 
			VALUES(_IdCliente, 1, _Certificado);
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE actualizaCertificadoNube(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    IN _Url VARCHAR(1500),
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó actualizar el certificado de un cliente inexistente' INTO _OpValida, _Mensaje;
    ELSEIF EXISTS(SELECT 1 FROM Cliente_Certificado_Nube WHERE idCliente=_IdCliente)
		THEN 
        UPDATE Cliente_Certificado_Nube SET urlCertificado=_Url WHERE idCliente=_IdCliente;
        SELECT 1, 'Se ha actualizado correctamente el certificado' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO Cliente_Certificado_Nube(idCliente, cuentaConCertificado, urlCertificado) 
			VALUES(_IdCliente, 1, _Url);
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
	CREATE PROCEDURE listaMedioContacto
    BEGIN
		SELECT * FROM Cliente_MedioContacto_Descripcion WHERE activo=1;
    END
DELIMITER ;
;
DELIMITER //
	CREATE PROCEDURE cargaMedioContactoCliente(
		IN _IdCliente INT
    )
    BEGIN
		SELECT idMedioRegistrado, idCliente, idMedio, descripcion
        FROM Cliente_MedioContacto WHERE idCliente=_IdCliente
        GROUP BY(idMedio);
    END //
DELIMITER ;
;
DELIMITER //
	CREATE PROCEDURE actualizaMedioContactoCliente(
		IN _IdMedio INT,
        IN _Descripcion VARCHAR(500),
        OUT _OpValida BIT,
        OUT _Mensaje VARCHAR(500)
    )
    BEGIN
		UPDATE Cliente_MedioContacto SET descripcion=_Descripcion
        WHERE idMedioRegistrado=_IdMedio;
        SELECT 1, 'Medio de contacto registrado éxitosamente' INTO _OpValida, _Mensaje;
    END //
DELIMITER ;
;
DELIMITER //
	CREATE PROCEDURE insertaMedioContactoCliente(
		IN _idMedio INT,
        IN _idCliente BIGINT, 
        IN _Descripcion VARCHAR(500),
        OUT _OpValida BIT, 
        OUT _Mensaje VARCHAR(500)
    )
    BEGIN
		INSERT INTO Cliente_MedioContacto(idCliente, idMedio, descripcion)
        VALUES(_idCliente, _idMedio, _Descripcion);
        SELECT 1, 'Medio de contacto registrado exitosamente' INTO _OpValida, _Mensaje;
    END //
DELIMITER ;