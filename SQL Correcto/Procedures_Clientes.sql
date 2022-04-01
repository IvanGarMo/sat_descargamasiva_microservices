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
		CALL ConvierteUid(_UidUserFirebase, @_IdUsuario);
	IF EXISTS(SELECT 1 FROM Clientes WHERE Rfc=_Rfc AND IdUsuario=@_IdUsuario)
		THEN SELECT 0, 'Ya existe un cliente registrado con esos datos' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO Clientes(rfc, idUsuario, nombre, apPaterno, apMaterno, cuentaConContrasena, contrasena)
		VALUES (_Rfc, @_IdUsuario, _Nombre, _ApPaterno, _ApMaterno, 0, null);
		SET @_IdClienteInsertado = -1;
		SELECT LAST_INSERT_ID() INTO @_IdClienteInsertado;
		INSERT INTO UsuariosClientes(idUsuario, idCliente) VALUES(@_IdUsuario, @_IdClienteInsertado);
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
    CALL ConvierteUid(_UidUserFirebase, @_IdUsuario);
    SELECT idCliente, rfc, idUsuario, nombre, apPaterno, apMaterno, cuentaConContrasena,
    cuentaConCertificado
    FROM UsuariosCliente
    WHERE IdUsuario=@_IdUsuario
    AND (_Rfc='' OR Rfc LIKE CONCAT('%', _Rfc, '%'))
    AND (_Nombre='' OR Nombre OR Nombre LIKE CONCAT('%', _Nombre ,'%'))
    AND (_ApPat='' OR ApPaterno LIKE CONCAT('%', _ApPat ,'%'))
    AND (_ApMat='' OR ApMaterno LIKE CONCAT('%', _ApMat ,'%'));
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE getCliente(
	IN _IdCliente INT,
    IN _UidUserFirebase VARCHAR(100),
    OUT _Rfc VARCHAR(13),
    OUT _IdUsuario BIGINT,
    OUT _Nombre VARCHAR(100),
    OUT _ApPaterno VARCHAR(100),
    OUT _ApMaterno VARCHAR(100),
    OUT _CuentaConContrasena BIT,
    OUT _CuentaConCertificado BIT
)
	BEGIN
    SELECT  rfc, idUsuario, nombre, apPaterno, apMaterno, cuentaConContrasena, cuentaConCertificado
    FROM UsuariosCliente
    WHERE idCliente=_IdCliente AND uidUserFirebase=_UidUserFirebase
    INTO _Rfc, _IdUsuario, _Nombre, _ApPaterno, _ApMaterno, _CuentaConContrasena, _CuentaConCertificado;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE actualizaCliente(
	IN _UidUserFirebase VARCHAR(100),
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
	CALL ConvierteUid(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'El cliente no existe para ese usuario' INTO _OpValida, _Mensaje;
	ELSE
		UPDATE Clientes 
        SET rfc=_Rfc, nombre=_Nombre, apPaterno=_ApPaterno, apMaterno=_ApMaterno
        WHERE idCliente=_IdCliente AND idUsuario=@_IdUsuario;
        SELECT 1, 'Cliente actualizado exitosamente' INTO _OpValida, _Mensaje;
	END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE eliminaCliente(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    SET @_IdUsuario= -1; 
	CALL ConvierteUid(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó eliminar un cliente inexistente' INTO _OpValida, _Mensaje;
    ELSE
		DELETE FROM UsuariosClientes WHERE idCliente=_IdCliente AND idUsuario=@_IdUsuario;
		DELETE FROM Clientes WHERE idCliente=_IdCliente;
        SELECT 1, 'Se ha eliminado correctamente el cliente' INTO _OpValida, _Mensaje;
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
	CALL ConvierteUid(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE idUsuario=@_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó actualizar la contraseña de un usuario inexistente' INTO _OpValida, _Mensaje;
    ELSE
		UPDATE Clientes SET cuentaConContrasena=1, contrasena=_Contrasena
        WHERE idCliente=_IdCliente AND idUsuario=@_IdUsuario;
        SELECT 1, 'Se ha guardado correctamente la contraseña del usuario' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE actualizaCertificado(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    IN _Certificado BLOB,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    DECLARE _IdUsuario INT;
    SELECT idUsuario INTO _IdUsuario FROM Usuarios WHERE uidUserFirebase=_UidUserFirebase;
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE idUsuario=_IdUsuario AND idCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó eliminar un usuario inexistente' INTO _OpValida, _Mensaje;
    ELSEIF EXISTS(SELECT 1 FROM CertificadoClientes WHERE idCliente=_IdCliente)
		THEN 
        UPDATE CertificadoClientes SET certificado=_Certificado WHERE idCliente=_IdCliente;
        SELECT 1, 'Se ha actualizado correctamente el certificado' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO CertificadoClientes(idCliente, cuentaConCertificado, certificado) VALUES(_IdCliente, 1, _Certificado);
        SELECT 1, 'Se ha guardado el certificado' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;