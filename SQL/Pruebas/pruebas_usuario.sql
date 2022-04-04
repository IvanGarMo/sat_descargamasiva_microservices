USE SatDescargaMasiva;
####################################################
#Esta operación debe ser correcta
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaUsuario('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'Iván de Jesús', 'García', 'Moreno', 'ivangarmo98@gmail.com', 
	1, 'Contadores Serna', 1, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 
 
#Esta operación también
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaUsuario('W9uEjdsadsazpl33xsrbnyZ043', 'José Guadalupe', 'Zuñiga', 'Martínez', 'jgzuniga@outlook.com', 
	1, 'Contadores Zuñiga', 1, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 

#Esta operación, no
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaUsuario('W9uEjdsadsazpl33xsrbnyZ043', 'José Guadalupe', 'Zuñiga', 'Martínez', 'jgzuniga@outlook.com', 
	1, 'Contadores Zuñiga', 1, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 

#Esta tampoco
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaUsuario('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'Iván de Jesús', 'García', 'Moreno', 'ivangarmo98@gmail.com', 
	1, 'Contadores Serna',  @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 

#Esta sí
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaUsuario('W9uEjiv8s3Qwddhmxñz922bnyZ043', 'Enrique', 'Diosdado', 'Martínez', 'ediosdado@contadores.com', 
	1, 'Diosdado y compañía',  @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 

SET @_IdUsuario = -1;
CALL ReturnIdUsuario('W9uEjiv8s3QwddhpQVi6rbnyZ043', @_IdUsuario); SELECT @_IdUsuario; #Deben ser 1
CALL ReturnIdUsuario('W9uEjdsadsazpl33xsrbnyZ043', @_IdUsuario); SELECT @_IdUsuario; #2
CALL ReturnIdUsuario('W9uEjiv8s3Qwddhmxñz922bnyZ043', @_IdUsuario); SELECT @_IdUsuario; #y 3, en ese orden

# Actualiza usuario
SET @_OpValida = false;
SET @_Mensaje = '';
CALL ActualizaUsuario('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'Iván de Jesús 2', 'García 2', 'Moreno 2', 'Contadores Serna 2',
	@_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; #Este debe ser válido

SET @_OpValida = false;
SET @_Mensaje = '';
CALL ActualizaUsuario('W9uEjiv8s3QwddddddyZ043', 'Iván de Jesús 2', 'García 2', 'Moreno 2', 'Contadores Serna 2',
	@_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; #Este no debe ser válido

SET @_OpValida = false;
SET @_Mensaje = '';
CALL ActualizaUsuario('W9uEjdsadsazpl33xsrbnyZ043', 'José Guadalupe 2', 'Zuñiga 3', 'Moreno 2', 'Contadores Zuñiga',
	@_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; #Este no debe ser válido

SET @_OpValida = false;
SET @_Mensaje = '';
CALL ActualizaUsuario('W9uEjiv8s3Qwddhmxñz922bnyZ043', 'Enrique 2', 'Diosdado 4', 'Martínez', 'Diosdado & Co',
	@_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; #Este no debe ser válido

#Salva teléfono
#### Los siguientes 3 deben quedar registrado con el teléfono 1
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaTelefono('W9uEjiv8s3QwddhpQVi6rbnyZ043', '8713297388', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaTelefono('W9uEjiv8s3QwddhpQVi6rbnyZ043', '8712215782', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaTelefono('W9uEjiv8s3QwddhpQVi6rbnyZ043', '8712338281', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 

### El siguiente debe quedar registrado con el usuario 2
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaTelefono('W9uEjdsadsazpl33xsrbnyZ043', '8712338281', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje; 

### Los siguientes 2 deben quedar registrados con el usuario 3
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaTelefono('W9uEjiv8s3Qwddhmxñz922bnyZ043', '8717155091', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL SalvaTelefono('W9uEjiv8s3Qwddhmxñz922bnyZ043', '8712441883', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
;
CALL listaTelefonoUsuario('W9uEjiv8s3QwddhpQVi6rbnyZ043');
CALL listaTelefonoUsuario('W9uEjdsadsazpl33xsrbnyZ043');
CALL listaTelefonoUsuario('W9uEjiv8s3Qwddhmxñz922bnyZ043');
;
### Elimina el teléfono num 4
SET @_OpValida = false;
SET @_Mensaje = '';
CALL EliminaTelefono('W9uEjdsadsazpl33xsrbnyZ043', 4, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;