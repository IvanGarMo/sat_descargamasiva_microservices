DELIMITER //
CREATE PROCEDURE TieneAccesoCliente(
	IN _IdCliente INT,
    IN _UidUserFirebase VARCHAR(100),
    OUT _TieneAcceso BIT
)
	BEGIN
		SET @_IdUsuario = -1;
        CALL ReturnIdUsuario(_uidUserFirebase, @_IdUsuario);
		IF EXISTS(SELECT 1 FROM Cliente_Usuario WHERE idCliente=_IdCliente AND idUsuario=@_IdUsuario)
			THEN SELECT 1 INTO _TieneAcceso;
        ELSE
			SELECT 0 INTO _TieneAcceso;
        END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE cargaClienteSolicitud(
	IN _idCliente BIGINT,
    OUT _rfc VARCHAR(13),
    OUT _nombre VARCHAR(100),
    OUT _apPaterno VARCHAR(100),
    OUT _apMaterno VARCHAR(100),
    OUT _cuentaConContrasena BIT, 
    OUT _cuentaConCertificado BIT
)
	BEGIN
		SELECT rfc, nombre, apPaterno, apMaterno, cuentaConContrasena, cuentaConCertificado
        FROM Cliente WHERE idCliente=_idCliente
        INTO _rfc, _nombre, _apPaterno, _apMaterno, _cuentaConContrasena, _cuentaConCertificado;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE ListaSolicitudes(
	IN _uidUserFirebase VARCHAR(200),
    IN _rfcEmisor VARCHAR(13),
    IN _rfcReceptor VARCHAR(13),
    IN _fechaInicioPeriodo DATE, 
    IN _fechaFinPeriodo DATE, 
    IN _estado INT
)
	BEGIN
		SET @_IdUsuario = -1;
        CALL ReturnIdUsuario(_uidUserFirebase, @_IdUsuario);
		SELECT idDescarga, rfcEmisor, rfcReceptor, fechaInicioPeriodo, fechaFinPeriodo, 
        descripcionEstado, IFNULL(noFacturas, 'Información no disponible') AS noFacturas
        FROM VistaGeneralSolicitud
        WHERE idUsuario=@_IdUsuario
        AND (_rfcEmisor='' 
			OR (rfcEmisor<>'' AND rfcEmisor LIKE CONCAT('%', _rfcEmisor, '%')))
        AND (_rfcReceptor='' 
			OR (rfcReceptor<>'' AND rfcReceptor LIKE CONCAT('%', _rfcReceptor, '%')))
        AND (_fechaInicioPeriodo='1000-01-01' OR (_fechaInicioPeriodo<>'1000-01-01' AND fechaInicioPeriodo>=_fechaInicioPeriodo))
        AND (_fechaFinPeriodo='1000-01-01' OR (_fechaFinPeriodo<>'1000-01-01' AND fechaFinPeriodo<=_fechaFinPeriodo))
        AND (_estado=-1 OR (_estado<>-1 AND idEstado=_estado));
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE TieneContrasena(
	IN _IdCliente INT, 
    OUT _TieneContrasena BIT,
    OUT _Mensaje VARCHAR(200)
)
	BEGIN
		SELECT 'Error: El cliente no cuenta con contraseña capturada' INTO _Mensaje;
		SELECT cuentaConContrasena FROM VistaClienteUsuario WHERE idCliente=_IdCliente INTO _TieneContrasena;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE TieneCertificado(
	IN _IdCliente INT,
    OUT _TieneCertificado BIT,
    OUT _Mensaje VARCHAR(200)
)
	BEGIN
		SELECT 'Error: El cliente no cuenta con certificado capturado' INTO _Mensaje;
        SELECT cuentaConCertificado FROM VistaClienteUsuario WHERE idCliente=_IdCliente INTO _TieneCertificado;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE CargaContrasena(
	IN _IdCliente INT, 
    OUT _Contrasena VARCHAR(300)
)
	BEGIN
		SELECT contrasena FROM Cliente_Contrasena WHERE idCliente=_IdCliente INTO _Contrasena;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE CargaCertificadoCliente(
	IN _IdCliente INT,
    OUT _certificado BLOB, 
    OUT _urlCertificado VARCHAR(100),
    OUT _certificadoNube BIT, 
    OUT _certificadoBaseDatos BIT
)
	BEGIN 
		SELECT C1.certificadoNube, 
        C1.certificadoBaseDatos, 
        C2.certificado, 
        C3.urlCertificado 
        INTO _certificadoNube, _certificadoBaseDatos, _certificado, _urlCertificado
        FROM Cliente C1
        LEFT JOIN Cliente_Certificado_Local C2 ON C1.idCliente=C2.idCliente
        LEFT JOIN Cliente_Certificado_Nube C3 ON C1.idCliente=C3.idCliente
        WHERE C1.idCliente = _IdCliente;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE getTexto(
	IN _idDescarga BIGINT,
    OUT _Mensaje VARCHAR(1000)
)
	BEGIN
    SELECT CONCAT_WS(' ', 'Error. La solicitud: ', _idDescarga, ' tiene los mismos parámetros. \n', 
    '¿Por qué estoy viendo esto?\n', 
    'Los sistemas del SAT prohíben la realización de solicitudes con parámetros duplicados (fecha de inicio y de final, RFC de emisor y RFC de receptor).')
    INTO _Mensaje;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE PuedeHacerSolicitud(
	IN _IdCliente INT,
    IN _fechaInicioPeriodo DATETIME,
    IN _fechaFinPeriodo DATETIME,
    IN _rfcEmisor VARCHAR(13),
    IN _rfcReceptor VARCHAR(13),
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(1000)
)
	BEGIN
    IF EXISTS(SELECT 1 FROM SolicitudDescarga WHERE rfcEmisor=_rfcEmisor AND rfcReceptor=_rfcReceptor
		AND fechaInicioPeriodo=_fechaInicioPeriodo AND fechaFinPeriodo=_fechaFinPeriodo)
	THEN 
		SET @_IdDescarga = -1;
		SELECT idDescarga FROM SolicitudDescarga WHERE rfcEmisor=_rfcEmisor AND rfcReceptor=_rfcReceptor
		AND fechaInicioPeriodo=_fechaInicioPeriodo AND fechaFinPeriodo=_fechaFinPeriodo INTO @_IdDescarga;
        CALL getTexto(@_IdDescarga, _Mensaje);
        SELECT 0 INTO _OpValida;
	ELSE 
		SELECT 1 INTO _OpValida;
	END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE GuardaSolicitud(
	IN _IdCliente BIGINT,
    IN _IdDescargaSat VARCHAR(100),
    IN _RfcSolicitante VARCHAR(13),
    IN _RfcEmisor VARCHAR(13),
    IN _RfcReceptor VARCHAR(13),
    IN _FechaInicioPeriodo DATE,
    IN _FechaFinPeriodo DATE,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(250)
)
	BEGIN
		INSERT INTO SolicitudDescarga(idCliente, idDescargaSat, rfcSolicitante, rfcEmisor, rfcReceptor, 
        fechaInicioPeriodo, fechaFinPeriodo)
        VALUES(_IdCliente, _IdDescargaSat, _RfcSolicitante, _RfcEmisor, _RfcReceptor, _FechaInicioPeriodo, 
        _FechaFinPeriodo);
        SELECT 1, CONCAT_WS(' ', 'Solicitud realizada exitosamente. Id: ', _IdDescargaSat) INTO _OpValida, _Mensaje;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE guardaEstadoSolicitud(
	IN _idDescarga BIGINT,
    IN _nuevoEstado INT
) 
BEGIN
	UPDATE SolicitudDescarga SET estado=_nuevoEstado WHERE idDescarga=_idDescarga;
END//
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE TieneAccesoDetalleSolicitud(
	IN _IdDescarga BIGINT,
    IN _uidUserFirebase VARCHAR(100),
    OUT _TieneAcceso BIT
)
	BEGIN
		SET @_IdUsuario = -1;
        CALL ReturnIdUsuario(_uidUserFirebase, @_IdUsuario);
		IF NOT EXISTS(
			SELECT 1 FROM SolicitudDescarga SD 
            JOIN Cliente_Usuario CU ON SD.idCliente=CU.idCliente
            WHERE SD.idDescarga=_IdDescarga AND CU.idUsuario=@_IdUsuario)
        THEN SELECT 0 INTO _TieneAcceso;
        ELSE
			SELECT 1 INTO _TieneAcceso;
        END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE CargaDetalleSolicitud(
	IN _idDescarga BIGINT,
    OUT _idDescargaSat VARCHAR(100), 
    OUT _idCliente BIGINT,
    OUT _nombreCliente VARCHAR(300),
    OUT _fechaInicioPeriodo DATE,
    OUT _fechaFinPeriodo DATE,
    OUT _rfcEmisor VARCHAR(13),
    OUT _rfcReceptor VARCHAR(13),
    OUT _rfcSolicitante VARCHAR(13),
    OUT _estado VARCHAR(15),
    OUT _noFacturas INT, 
    OUT _descargasPermitidas INT,
    OUT _estaLista BIT,
    OUT _urlPaquetes VARCHAR(300)
)
	BEGIN
		SELECT idDescargaSat, idCliente, nombreCliente, fechaInicioPeriodo, fechaFinPeriodo, rfcEmisor, rfcReceptor, 
        rfcSolicitante, estado, noFacturas, descargasPermitidas, 
        CASE estado WHEN 3 THEN 1 ELSE 0 END, #3 quiere decir que la solicitud ya está lista
        urlPaquetes
        FROM VistaGeneralSolicitud WHERE idDescarga=_idDescarga
        INTO _idDescargaSat, _idCliente, _nombreCliente, _fechaInicioPeriodo, _fechaFinPeriodo, _rfcEmisor, _rfcReceptor, 
        _rfcSolicitante, _estado, _noFacturas, _descargasPermitidas, _estaLista, _urlPaquetes;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE cargaUrlPaquetes(
	IN _idDescarga BIGINT,
    OUT _urlPaquetes VARCHAR(300)
)
	BEGIN
		SELECT urlPaquetes
        FROM VistaGeneralSolicitud WHERE idDescarga=_idDescarga
        INTO _urlPaquetes;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE guardaUrlPaquete(
	IN _idDescarga BIGINT,
    IN _urlPaquetes VARCHAR(500),
    OUT _opValida BIT,
    OUT _mensaje VARCHAR(200)
)
	BEGIN
		INSERT INTO PaquetesUrl(idDescarga, urlPaquetes) VALUES(_idDescarga, _urlPaquetes);
        SELECT 1, 'Operación realizada exitosamente' INTO _opValida, _mensaje;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE listaEstadoSolicitud()
	BEGIN
		SELECT * FROM EstadoSolicitud;
    END //
DELIMITER ;