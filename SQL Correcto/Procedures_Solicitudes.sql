DELIMITER //
CREATE PROCEDURE TieneAccesoCliente(
	IN _IdCliente INT,
    IN _UidUserFirebase VARCHAR(100),
    OUT _TieneAcceso BIT
)
	BEGIN
		IF EXISTS(SELECT 1 FROM UsuariosCliente WHERE idCliente=_IdCiente AND uidUserFirebase=_UidUserFirebase)
			THEN SELECT 1 INTO _TieneAcceso;
        ELSE
			SELECT 0 INTO _TieneAcceso;
        END IF;
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
		SELECT cuentaConContrasena FROM Clientes WHERE idCliente=_IdCliente INTO _TieneContrasena;
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
        SELECT cuentaConCertificado FROM CertificadoClientes WHERE idCliente=_IdCliente INTO _TieneCertificado;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE CargaContrasenaCertificado(
	IN _IdCliente INT,
    OUT _Contrasena VARCHAR(200),
    OUT _Certificado BLOB
)
	BEGIN
		SELECT contrasena, certificado FROM UsuariosCliente WHERE idCliente=_IdCliente INTO _Contrasena, _Certificado;
    END
DELIMITER ;
;
CREATE PROCEDURE PuedeHacerSolicitud(
	IN _IdCliente INT,
    IN _UidUserFirebase VARCHAR(100),
    IN _fechaInicioPeriodo DATETIME,
    IN _fechaFinPeriodo DATETIME,
    IN _rfcEmisor VARCHAR(13),
    IN _rfcReceptor VARCHAR(13),
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(250)
)
	BEGIN
    IF EXISTS(SELECT 1 FROM SolicitudDescarga WHERE idCliente=_IdCliente AND )
    END
;
DELIMITER //
CREATE PROCEDURE GuardaSolicitud(
	IN _IdCliente BIGINT,
    IN _IdDescargaSat VARCHAR(100),
    IN _RfcSolicitante VARCHAR(13),
    IN _RfcEmisor VARCHAR(13),
    IN _RfcReceptor VARCHAR(13),
    IN _FechaInicioPeriodo VARCHAR(13),
    IN _FechaFinPeriodo VARCHAR(13),
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(250)
)
	BEGIN
		INSERT INTO SolicitudDescarga(idCliente, idDescargaSat, rfcSolicitante, rfcEmisor, rfcReceptor, 
        fechaInicioPeriodo, fechaFinPeriodo)
        VALUES(_IdCliente, _IdDescargaSat, _RfcSolicitante, _RfcEmisor, _RfcReceptor, _FechaInicioPeriodo, 
        _FechaFinPeriodo);
        SELECT 1, CONCAT_WS(' ', 'Solicitud realizada exitosamente. Id ', _IdSolicitudDescarga) INTO _OpValida, _Mensaje;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE TieneAccesoDetalleSolicitud(
	IN _IdDescarga BIGINT,
    IN _uidUserFirebase VARCHAR(100),
    OUT _TieneAcceso BIT
)
	BEGIN
		IF NOT EXISTS(SELECT 1 FROM SolicitudClienteUsuario WHERE idDescarga=_IdDescarga AND uidUserFirebase=_uidUserFirebase)
        THEN SELECT 0 INTO _TieneAcceso;
        ELSE
			SELECT 1 INTO _TieneAcceso;
        END IF;
    END //
DELIMITER ;
;
DELIMITER //
CREATE PROCEDURE CargaDetalleSolicitud(
	IN _IdDescarga BIGINT,
    OUT _IdDescargaSat VARCHAR(100), 
    OUT _IdCliente INT,
    OUT _FechaInicioPeriodo DATETIME,
    OUT _FechaFinPeriodo DATETIME,
    OUT _RfcEmisor VARCHAR(13),
    OUT _RfcReceptor VARCHAR(13),
    OUT _RfcSolicitante VARCHAR(13),
    OUT _Estado INT,
    OUT _NoFacturas INT, 
    OUT _DescargasPermitidas INT
)
	BEGIN
		SELECT idDescarga, idDescargaSat, idCliente, fechaInicioPeriodo, fechaFinPeriodo, rfcEmisor, rfcReceptor, 
        rfcSolicitante, estado, noFacturas, descargasPermitidas
        FROM SolicitudDescarga WHERE IdDescarga=_IdDescarga
        INTO _IdDescarga, _IdDescargaSat, _IdCliente, _FechaInicioPeriodo, _FechaFinPeriodo, _RfcEmisor, _RfcReceptor, 
        _RfcSolicitante, _Estado, _NoFacturas, _DescargasPermitidas;
    END //
DELIMITER ;

SELECT * FROM SolicitudDescarga;