USE SatDescargaMasiva;
;
##Este procedimiento revisa si se tienen permisos para acceder al cliente
DELIMITER //
CREATE PROCEDURE puedeAccederCliente(
	IN _uidUsuario VARCHAR(100),
    IN _idCliente BIGINT,
    OUT _opValida BIT,
    OUT _mensaje VARCHAR(250)
)
	BEGIN
    SET @_IdUsuario = -1;
    SELECT idUsuario FROM Usuario WHERE uidUserFirebase=_UidUsuario INTO @_IdUsuario;
    IF NOT EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idCliente=_IdCliente AND idUsuario=@_IdUsuario AND relacionActiva=1)
    THEN SELECT 0, 'Error: No se tienen los permisos para modificar este cliente' INTO _OpValida, _Mensaje;
    ELSE SELECT 1, '' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
##Este procedimiento es para registrar la activación, no está planeado para 
##ser invocado desde la aplicación
DELIMITER //
CREATE PROCEDURE activaCliente(
	IN _idCliente BIGINT, 
    IN _idUsuario BIGINT
)
	BEGIN
		#El 2 del insert viene de Cliente_Operacion
		INSERT INTO Cliente_Movimiento(idCliente, idOperacion, esDesactivacion, idUsuarioDesactiva, 
        esActivacion, idUsuarioActiva, fechaHoraMovimiento)
        VALUES(_idCliente, 2, 0, null, 1, _idUsuario, NOW());
    END //
DELIMITER ;
;
##Este procedimiento sirve para llevar el registro del creado de cliene, no 
##esta planeado para ser invocado desde la aplicación
DELIMITER //
CREATE PROCEDURE creaCliente(
	IN _idCliente BIGINT, 
    IN _idUsuario BIGINT
)
	BEGIN
		#3 viene de Cliente_Operacion
		INSERT INTO Cliente_Movimiento(idCliente, idOperacion, esDesactivacion, idUsuarioDesactiva, 
        esActivacion, idUsuarioActiva, fechaHoraMovimiento) 
        VALUES(_idCliente, 3, 0, null, 1, _idUsuario, NOW());
    END //
DELIMITER ;
;
##Este es al momento de registrar un cliente, como parte de su hecho llama a un registro
# Verificar si el cliente existe 
# -Si existe
# ---Ver si está activo
# ----Si esta activo con el mismo despacho, indicarlo. No se guardará.
# ----Si esta activo con otro despacho, indicarlo. No se guardará.
# ---Si no está activo
# ----Activarlo con usuario actual. Solo se modifica la tabla de relación.
# -Si no existe
# --Registrarlo como nuevo cliente en Cliente, registrar la relación, registrar movimiento
DELIMITER //
CREATE PROCEDURE guardaCliente(
	IN _uidUserFirebase VARCHAR(100),
	IN _rfc VARCHAR(13),
    IN _nombre VARCHAR(100),
    IN _apPaterno VARCHAR(100),
    IN _apMaterno VARCHAR(100),
    IN _certificadoNube BIT,
    IN _certificadoBaseDatos BIT,
    OUT _idCliente INT,
    OUT _opValida BIT,
    OUT _mensaje VARCHAR(300)
)
	BEGIN
		SET @_IdUsuario = -1; 
		CALL ReturnIdUsuario(_uidUserFirebase, @_IdUsuario);
		SET @_IdCliente = -1;
        IF EXISTS(SELECT 1 FROM Cliente WHERE Rfc=_Rfc) 
        THEN
			SELECT idCliente FROM Cliente WHERE Rfc=_Rfc INTO @_IdCliente;
			IF EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idCliente=@_IdCliente AND idUsuario=@_IdUsuario AND relacionActiva=1)
            THEN SELECT @_IdCliente, 0, 'Error: Este cliente ya se encuentra registrado' INTO _idCliente, _opValida, _mensaje;
            ELSEIF EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idCliente=@_idCliente AND relacionActiva=1) 
            THEN SELECT 0, 'Error: Este cliente ya se encuentra registrado en otro despacho' INTO _opValida, _mensaje;
            ELSE 
            CALL activaCliente(@_IdCliente, @_IdUsuario);
            INSERT INTO Cliente_Usuario(idCliente, idUsuario, relacionActiva) VALUES(@_idCliente, @_idUsuario, 1);
            SELECT 1, 'El cliente se ha registrado éxitosamente' INTO _opValida, _mensaje;
            END IF;
        ELSE 
			INSERT INTO Cliente(rfc, nombre, apPaterno, apMaterno, certificadoNube, certificadoBaseDatos)
            VALUES(_rfc, _nombre, _apPaterno, _apMaterno, _certificadoNube, _certificadoBaseDatos);
            SET @_IdCliente = -1;
            SELECT LAST_INSERT_ID() INTO @_IdCliente;
            INSERT INTO Cliente_Usuario(idCliente, idUsuario, relacionActiva) VALUES(@_idCliente, @_idUsuario, 1);
            CALL creaCliente(@_IdCliente, @_IdUsuario);
            SELECT @_IdCliente, 1, 'Cliente registrado con éxito' INTO _idCliente, _opValida, _mensaje;
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
    AND (_Nombre='' OR nombreCliente LIKE CONCAT('%', _Nombre ,'%'))
    AND (_ApPat='' OR apPaternoCliente LIKE CONCAT('%', _ApPat ,'%'))
    AND (_ApMat='' OR apMaternoCliente LIKE CONCAT('%', _ApMat ,'%'));
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE getCliente(
	IN _idCliente INT,
    OUT _Rfc VARCHAR(13),
    OUT _IdUsuario BIGINT,
    OUT _Nombre VARCHAR(100),
    OUT _ApPaterno VARCHAR(100),
    OUT _ApMaterno VARCHAR(100),
    OUT _CuentaConContrasena BIT,
    OUT _CuentaConCertificado BIT
)
	BEGIN
    SELECT V.rfc, -1, V.nombreCliente, V.apPaternoCliente, V.apMaternoCliente, V.cuentaConContrasena, 
    V.cuentaConCertificado
    FROM VistaClienteUsuario V
    WHERE V.idCliente=_idCliente
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
		UPDATE Cliente 
        SET rfc=_Rfc, nombre=_Nombre, apPaterno=_ApPaterno, apMaterno=_ApMaterno
        WHERE idCliente=_IdCliente;
        SELECT 1, 'Datos del cliente actualizados exitosamente' INTO _OpValida, _Mensaje;
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
USE SatDescargaMasiva;
SELECT * FROM Usuario;
DROP PROCEDURE getListaClientesIdNombre;
CALL getListaClientesIdNombre('hXLtTCe2L0e0PvssJ0TVmS2SQ5o1');
DELIMITER //
CREATE PROCEDURE getListaClientesIdNombre(
	IN _UidUserFirebase VARCHAR(100)
)
	BEGIN
    SET @_IdUsuario= -1; 
    CALL ReturnIdUsuario(_UidUserFirebase, @_IdUsuario);
    SELECT -1 AS idCliente, '-Seleccione un cliente-' AS nombre, 
    '' as cuentaConContrasena, 
    '' as cuentaConCertificado
    UNION
    SELECT idCliente, CONCAT_WS(' ', V.nombreCliente, v.apPaternoCliente, v.apMaternoCliente) AS nombre,
    CASE V.cuentaConContrasena WHEN 0 THEN 'No cuenta con contraseña' ELSE 'Cuenta con contraseña' END AS cuentaConContrasena,
    CASE V.cuentaConCertificado WHEN 0 THEN 'No cuenta con certificado' ELSE 'Cuenta con certificado' END AS cuentaConCertificado
    FROM VistaClienteUsuario V WHERE idUsuario=@_idUsuario;
    END//
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
			IF NOT EXISTS(SELECT 1 FROM Cliente_Contrasena WHERE idCliente=_IdCliente)
				THEN INSERT INTO Cliente_Contrasena(idCliente, cuentaConContrasena, contrasena)
				VALUES(_IdCliente, 1, _Contrasena);
                SELECT 1, 'Se ha guardado correctamente la contraseña del usuario' INTO _OpValida, _Mensaje;
			ELSE 
				UPDATE Cliente_Contrasena SET cuentaConContrasena=1, contrasena=_Contrasena
			WHERE idCliente=_IdCliente;
			SELECT 1, 'Se ha guardado correctamente la contraseña del usuario' INTO _OpValida, _Mensaje;
            END IF;
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
		SELECT 1, 'Se ha actualizado correctamente el certificado' INTO _OpValida, _Mensaje;
    END IF;
    END //
DELIMITER ;
;
CREATE VIEW MedioContactoClientes AS
	SELECT CMC.idMedioRegistrado, CMC.idCliente, CMC.idMedio, CMC.descripcion, 
    CMD.descripcion AS 'descripcionMedio'
    FROM Cliente_MedioContacto CMC 
    JOIN Cliente_MedioContacto_Descripcion CMD ON CMC.idMedio=CMD.idMedio
;
DELIMITER //
CREATE PROCEDURE puedeRealizarOperacionMedioContacto(
	IN _IdCliente BIGINT, 
    OUT _OpValida BIT
) 
	BEGIN
		IF EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idCliente=_IdCliente)
        THEN SELECT 1 INTO _OpValida;
		ELSE
			SELECT 0 INTO _OpValida;
        END IF;
    END//
DELIMITER ;
;
DELIMITER //
	CREATE PROCEDURE listaMedioContacto()
    BEGIN
		SELECT * FROM Cliente_MedioContacto_Descripcion WHERE activo=1;
    END //
DELIMITER ;
;
DELIMITER //
	CREATE PROCEDURE cargaMedioContactoCliente(
		IN _IdCliente INT
    )
    BEGIN
		SELECT * 
        FROM MedioContactoClientes WHERE idCliente=_IdCliente ORDER BY idMedio ASC;
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
CREATE PROCEDURE eliminaMedioContactoCliente(
	IN _idMedioRegistrado INT,
    OUT _OpValida BIT, 
    OUT _Mensaje VARCHAR(250)
)
BEGIN
	DELETE FROM Cliente_MedioContacto WHERE idMedioRegistrado=_idMedioRegistrado;
    SELECT 1, 'Medio de contacto eliminado exitosamente' INTO _OpValida, _Mensaje;
END//
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
		IF NOT EXISTS(SELECT 1 FROM Cliente_MedioContacto WHERE idCliente=_idCliente AND idMedio=_idMedio
        AND descripcion=_Descripcion)
        THEN 
			INSERT INTO Cliente_MedioContacto(idCliente, idMedio, descripcion)
			VALUES(_idCliente, _idMedio, _Descripcion);
			SELECT 1, 'Medio de contacto registrado exitosamente' INTO _OpValida, _Mensaje;
		ELSE
			SELECT 0, 'Este medio de contacto ya se encuentra registrado' INTO _OpValida, _Mensaje;
		END IF;
    END //
DELIMITER ;
;