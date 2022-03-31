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
    SELECT IdCliente, Rfc, Idusuario, Nombre, ApPaterno, ApMaterno, CuentaConContrasena,
    CuentaConCertificado
    FROM UsuariosCliente
    WHERE IdUsuario=@_IdUsuario
    AND (_Rfc='' OR Rfc LIKE CONCAT('%', _Rfc, '%'))
    AND (_Nombre='' OR Nombre OR Nombre LIKE CONCAT('%', _Nombre ,'%'))
    AND (_ApPat='' OR ApPaterno LIKE CONCAT('%', _ApPat ,'%'))
    AND (_ApMat='' OR ApMaterno LIKE CONCAT('%', _ApMat ,'%'));
    END //
DELIMITER ;
;
SELECT * FROM UsuariosCliente;
CALL listaClientes()
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
    OUT _Contrasena VARCHAR(100),
    OUT _CuentaConCertificado BIT,
    OUT _Certificado BLOB
)
	BEGIN
    SELECT  Rfc, IdUsuario, Nombre, ApPaterno, ApMaterno, CuentaConContrasena, Contrasena,
    CuentaConCertificado, Certificado
    FROM UsuariosCliente
    WHERE IdCliente=_IdCliente
    INTO _Rfc, _IdUsuario, _Nombre, _ApPaterno, _ApMaterno, _CuentaConContrasena, _Contrasena, 
    _CuentaConCertificado, _Certificado;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE guardaCliente(
	IN _UidUserFirebase VARCHAR(100),
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
	IF EXISTS(SELECT 1 FROM Clientes WHERE Rfc=_Rfc)
		THEN SELECT 0, 'Ya existe un cliente registrado con esos datos' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO Clientes(Rfc, Idusuario, Nombre, ApPaterno, ApMaterno, CuentaConContrasena, Contrasena)
		VALUES (_Rfc, @_IdUsuario, _Nombre, _ApPaterno, _ApMaterno, 0, null);
		SET @_IdClienteInsertado = -1;
		SELECT LAST_INSERT_ID() INTO @_IdClienteInsertado;
		INSERT INTO UsuariosClientes(IdUsuario, IdCliente) VALUES(@_IdUsuario, @_IdClienteInsertado);
		SELECT 1, 'Cliente registrado exitosamente' INTO _OpValida, _Mensaje;
    END IF;
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
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE IdUsuario=@_IdUsuario AND IdCliente=_IdCliente)
		THEN SELECT 0, 'El cliente no existe para ese usuario' INTO _OpValida, _Mensaje;
	ELSE
		UPDATE Clientes 
        SET Rfc=_Rfc, Nombre=_Nombre, ApPaterno=_ApPaterno, ApMaterno=_ApMaterno
        WHERE IdCliente=_IdCliente AND IdUsuario=@_IdUsuario;
        SELECT 1, 'Cliente registrado exitosamente' INTO _OpValida, _Mensaje;
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
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE IdUsuario=@_IdUsuario AND IdCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó eliminar un usuario inexistente' INTO _OpValida, _Mensaje;
    ELSE
		DELETE FROM UsuariosClientes WHERE IdCliente=_IdCliente AND IdUsuario=@_IdUsuario;
		DELETE FROM Clientes WHERE IdCliente=_IdCliente;
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
	CALL ConvierteUid(_UidUserFirebase, @_IdUsuario);
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE IdUsuario=@_IdUsuario AND IdCliente=_IdCliente)
		THEN SELECT 0, 'Se actualizar la contraseña de un usuario inexistente' INTO _OpValida, _Mensaje;
    ELSE
		UPDATE Clientes SET CuentaConContrasena=1, Contrasena=_Contrasena
        WHERE IdCliente=_IdCliente AND IdUsuario=@_IdUsuario;
        SELECT 1, 'Se ha guardado correctamente la contraseña del usuario' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEUDRE actualizaCertificado(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    IN _Certificado BLOB,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)
)
	BEGIN
    DECLARE _IdUsuario INT;
    SELECT IdUsuario INTO _IdUsuario FROM Usuarios WHERE UidUserFirebase=_UidUserFirebase;
    IF NOT EXISTS(SELECT 1 FROM UsuariosClientes WHERE IdUsuario=_IdUsuario AND IdCliente=_IdCliente)
		THEN SELECT 0, 'Se intentó eliminar un usuario inexistente' INTO _OpValida, _Mensaje;
    ELSE IF EXISTS(SELECT 1 FROM CertificadoClientes WHERE IdCliente=_IdCliente)
		THEN 
        UPDATE CertificadoClientes SET Certificado=_Certificado WHERE IdCliente=_IdCliente;
        SELECT 1, 'Se ha actualizado correctamente el certificado' INTO _OpValida, _Mensaje;
    ELSE 
		INSERT INTO CertificadoClientes(IdCliente, Certificado) VALUES(_IdCliente, _Certificado);
    END IF;
    END;
DELIMITER ;
;