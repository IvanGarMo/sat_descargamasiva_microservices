######################################
DELIMITER //
CREATE PROCEDURE getSolicitudDescargaComplemento()
	BEGIN
		SELECT valor, descripcion FROM solicitud_descarga_complemento;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE getSolicitudDescargaEstadoComprobante()
	BEGIN
		SELECT valor, descripcion FROM solicitud_descarga_estado_comprobante;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE getSolicitudDescargaTipoSolicitud()
	BEGIN
		SELECT valor, descripcion FROM solicitud_descarga_tipo_solicitud;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE guardaRfcReceptorSolicitud(
	IN _idDescarga BIGINT, 
    IN _rfcReceptor VARCHAR(13)
)
	BEGIN
		INSERT INTO solicitud_descarga_rfc_receptor(rfcRecceptor, idDescarga) VALUES(_rfcReceptor, _idDescarga);
    END //
DELIMITER ;
;
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
    OUT _cuentaConCertificado BIT,
    OUT _cuentaConKey BIT
)
	BEGIN
		SELECT rfc, nombreCliente, apPaternoCliente, apMaternoCliente, 
        cuentaConContrasena, cuentaConCertificado, cuentaConKey
        FROM VistaClienteUsuario WHERE idCliente=_idCliente
        INTO _rfc, _nombre, _apPaterno, _apMaterno, _cuentaConContrasena, _cuentaConCertificado, _cuentaConKey;
    END //
DELIMITER ;
;

DELIMITER //
CREATE PROCEDURE estadoListaSolicitud()
	BEGIN
		SELECT idEstado, descripcionEstado FROM Solicitud_Descarga_Estado_Solicitud;
    END//
DELIMITER ;
;
USE SatDescargaMasiva;
SELECT * FROM Usuario;
SELECT * FROM VistaGeneralSolicitud;
CALL ListaSolicitudes('hXLtTCe2L0e0PvssJ0TVmS2SQ5o1', '', '', '', -1, -1, -1, -1,'');
SELECT * FROM VistaGeneralSolicitud;
DROP PROCEDURE ListaSolicitudes;

SELECT * FROM Solicitud_Descarga_Estado_Comprobante;
SELECT * FROM Solicitud_Descarga_Estado_Solicitud;
SELECT * FROM Solicitud_Descarga_Complemento;
SELECT * FROM Solicitud_Descarga;
SELECT * FROM Solicitud_Descarga_Tipo_Solicitud;
SELECT * FROM VistaGeneralSolicitud;

CALL ListaSolicitudes('hXLtTCe2L0e0PvssJ0TVmS2SQ5o1', '', '', '', -1, -1, -1, '-1','');

DELIMITER //
CREATE PROCEDURE ListaSolicitudes(
	IN _uidUserFirebase VARCHAR(200),
    IN _rfcSolicitante VARCHAR(13),
    IN _fechaInicioPeriodo VARCHAR(15), 
    IN _fechaFinPeriodo VARCHAR(15), 
    IN _estadoSolicitud INT, 
    IN _idComplemento INT, 
    IN _estadoComprobante INT, 
    IN _tipoSolicitud VARCHAR(3), 
    IN _uid VARCHAR(100)
)
	BEGIN
		SET @_IdUsuario = -1;
        CALL ReturnIdUsuario(_uidUserFirebase, @_IdUsuario);
		SELECT idDescarga, rfcSolicitante, fechaInicioPeriodo, fechaFinPeriodo, nombreCliente,
        descripcionEstado, IFNULL(noFacturas, 'Información no disponible') AS noFacturas,
        complemento, estadoComprobante, tipoSolicitud
        FROM VistaGeneralSolicitud
        WHERE idUsuario=@_IdUsuario
        AND (_fechaInicioPeriodo='' OR (_fechaInicioPeriodo<>'' 
			AND (fechaInicioPeriodo>=STR_TO_DATE(_fechaInicioPeriodo, '%d-%m-Y'))))
        AND (_fechaFinPeriodo='' OR (_fechaFinPeriodo<>'' 
			AND (fechaFinPeriodo<=STR_TO_DATE(_fechaFinPeriodo, '%d-%m-Y'))))
        AND (_estadoSolicitud-1 OR (_estadoSolicitud<>-1 AND idEstado=_estadoSolicitud))
        AND (_rfcSolicitante='' OR (_rfcSolicitante<>'' AND rfcSolicitante LIKE CONCAT('%', _rfcSolicitante,'%')))
        AND (_idComplemento=-1 OR (_idComplemento<>-1 AND idComplemento=_idComplemento))
        AND (_estadoComprobante=-1 OR (_estadoComprobante<>-1 AND idEstadoComprobante=_estadoComprobante))
        AND (_tipoSolicitud='-1' OR (_tipoSolicitud<>'-1' AND idTipoSolicitud=_tipoSolicitud))
        AND (_uid='' OR (_uid<>'' AND esUidSolicitado=1 AND uid=_uid));
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
    OUT _existeSolicitud BIT, 
    OUT _idDescarga BIGINT,
    OUT _complemento VARCHAR(15),
    OUT _estadoComprobante VARCHAR(5),
    OUT _tipoSolicitud VARCHAR(5), 
    OUT _receptores JSON
)
	BEGIN
    IF EXISTS(SELECT 1 FROM VistaGeneralSolicitud WHERE fechaInicioPeriodo=_fechaInicioPeriodo AND fechaFinPeriodo=_fechaFinPeriodo)
	THEN 
		SET @_idDescarga = -1;
		SELECT 1, idDescarga, complemento, estadoComprobante, tipoSolicitud 
        FROM VistaGeneralSolicitud
        WHERE fechaInicioPeriodo=_fechaInicio AND fechaFinPeriodo=_fechaFinPeriodo
        INTO _existeSolicitud, @_idDescarga, _complemento, _estadoComprobante, _tipoSolicitud;
        SELECT JSON_ARRAYAGG(JSON_OBJECT('rfcReceptor', rfcReceptor, 'idDescarga', idDescarga)) 
        FROM Solicitud_Descarga_Rfc_Receptor WHERE idDescarga=@_idDescarga
        INTO _receptores;
	ELSE 
		SELECT 0 INTO _existeSolicitud;
	END IF;
    END //
DELIMITER ;
;
SELECT * FROM Solicitud_Descarga;
SELECT * FROM Solicitud_Descarga_Rfc_Receptor;
DROP PROCEDURE GuardaSolicitud;
SELECT * FROM Solicitud_Desc;

SET @_idDescarga = -1;
CALL GuardaSolicitud('2922929292', 5, '01-01-2021', '01-02-2021', 'GAMI9809092A1', 'GAMI9809092A1', 
	0, 0, '', '-1', '-1', '-1', @_idDescarga);
SELECT @_idDescarga;
DELIMITER //
CREATE PROCEDURE GuardaSolicitud(
	IN _idDescargaSat VARCHAR(100),
    IN _idCliente BIGINT,
    IN _fechaInicioPeriodo VARCHAR(15),
    IN _fechaFinPeriodo VARCHAR(15),
    IN _rfcEmisor VARCHAR(13),
    IN _rfcSolicitante VARCHAR(13),
    IN _estado INT, 
    IN _esUidSolicitado BIT, 
    IN _uid VARCHAR(200),
    IN _idComplemento INT, 
    IN _estadoComprobante INT,
    IN _tipoSolicitud VARCHAR(3),
    OUT _idDescarga BIGINT
)
	BEGIN
		INSERT INTO Solicitud_Descarga(idDescargaSat, idCliente, fechaInicioPeriodo, fechaFinPeriodo,
				rfcEmisor, rfcSolicitante, estado, esUidSolicitado,  uid, idComplemento, estadoComprobante, 
                tipoSolicitud)
			VALUES (_idDescargaSat, _idCliente, STR_TO_DATE(_fechaInicioPeriodo, "%d-%m-%Y"), 
            STR_TO_DATE(_fechaFinPeriodo, "%d-%m-%Y"), _rfcEmisor, _rfcSolicitante, 
            _estado, _esUidSolicitado, _uid, _idComplemento, _estadoComprobante, _tipoSolicitud);
            SET _idDescarga = LAST_INSERT_ID();
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
SET @_idDescarga=7;
SET @_idDescargaSat = '';
SET @_idCliente=-1;
SET @_nombreCliente='';
SET @_fechaInicioPeriodo = '';
SET @_fechaFinPeriodo = '';
SET @_rfcSolicitante = '';
SET @_rfcReceptores = '';
SET @_noFacturas = '';
SET @_esUidSolicitado = 1;
SET @_uid = '';
SET @_idEstado = -1;
SET @_descripcionEstado = '';
SET @_complemento = '';
SET @_estadoComprobante = '';
SET @_tipoSolicitud = '';
CALL CargaDetalleSolicitud(6, @_idDescargaSat, @_idCliente, @_nombreCliente, @_fechaInicioPeriodo, 
	@_fechaFinPeriodo, @_rfcSolicitante, @_rfcReceptores, @_noFacturas, @_esUidSolicitado, @_uid,
    @_idEstado, @_descripcionEstado, @_complemento, @_estadoComprobante, @_tipoSolicitud);
SELECT  @_idDescarga, @_idDescargaSat, @_idCliente, @_nombreCliente, @_fechaInicioPeriodo, 
	@_fechaFinPeriodo, @_rfcSolicitante, @_rfcReceptores, @_noFacturas, @_esUidSolicitado, @_uid,
    @_idEstado, @_descripcionEstado, @_complemento, @_estadoComprobante, @_tipoSolicitud;

SELECT * FROM Solicitud_Descarga;
SELECT * FROM VistaGeneralSolicitud;
SELECT 1 FROM Solicitud_Descarga_Rfc_Receptor WHERE idDescarga=2;

DROP PROCEDURE CargaDetalleSolicitud;
DELIMITER //
CREATE PROCEDURE CargaDetalleSolicitud(
	IN _idDescarga BIGINT,
    OUT _idDescargaSat VARCHAR(100), 
    OUT _idCliente BIGINT,
    OUT _nombreCliente VARCHAR(300),
    OUT _fechaInicioPeriodo DATE,
    OUT _fechaFinPeriodo DATE,
    OUT _rfcSolicitante VARCHAR(13),
    OUT _rfcReceptores JSON,
    OUT _noFacturas VARCHAR(100), 
    OUT _esUidSolicitado BIT,
    OUT _uid VARCHAR(200), 
    OUT _idEstado INT, 
    OUT _descripcionEstado VARCHAR(100),
    OUT _complemento VARCHAR(100),
    OUT _estadoComprobante VARCHAR(100),
    OUT _tipoSolicitud VARCHAR(100)
)
	BEGIN
		SELECT idDescargaSat, idCliente, nombreCliente, fechaInicioPeriodo, fechaFinPeriodo, rfcSolicitante, 
        IFNULL(noFacturas, 'Aún no disponible'), esUidSolicitado, uid, idEstado, descripcionEstado,
        complemento, estadoComprobante, tipoSolicitud
        FROM VistaGeneralSolicitud 
        WHERE idDescarga=_idDescarga
        INTO _idDescargaSat, _idCliente, _nombreCliente, _fechaInicioPeriodo, _fechaFinPeriodo, _rfcSolicitante,
        _noFacturas, _esuidSolicitado, _uid, _idEstado, _descripcionEstado, _complemento, _estadoComprobante, 
        _tipoSolicitud;
        IF EXISTS(SELECT 1 FROM Solicitud_Descarga_Rfc_Receptor WHERE idDescarga=_idDescarga)
		THEN 
			SELECT JSON_ARRAYAGG(JSON_OBJECT('rfcReceptor', rfcReceptor, 'idDescarga', idDescarga))
			FROM Solicitud_Descarga_Rfc_Receptor WHERE idDescarga=_idDescarga
			INTO _rfcReceptores;
        END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE GuardaReceptor(
	IN _rfcReceptor VARCHAR(13), 
    IN _idDescarga BIGINT
)
	BEGIN
	INSERT INTO Solicitud_Descarga_Rfc_Receptor(rfcReceptor, idDescarga) VALUES(_rfcReceptor, _idDescarga);
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