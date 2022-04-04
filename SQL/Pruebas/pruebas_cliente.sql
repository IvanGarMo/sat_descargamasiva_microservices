##Guarda cliente. Resultado esperado, el cliente ha sido guardado exitosamente
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'MONE711201Q44', 'Elda', 'Moreno', 'Núñez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Este debe indicar que el cliente ya se encuentra registrado en el despacho
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'MONE711201Q44', 'Elda', 'Moreno', 'Núñez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Este debe registrarlo exitosamente, en el mismo despacho
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'GAPL390605Q44', 'Leonel', 'García', 'Peña', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;


##Este debe fracasar, por la misma razón de que ya se encuentra registrado en el despacho
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'GAPL390605Q44', 'Leonel', 'García', 'Peña', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Voy a registrar un cliente en otro despacho. Debe ser exitoso
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3Qwddhmxñz922bnyZ043', 'LUJA730405S4X', 'Alejandro', 'Lucero', 'Jaramillo', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Voy a registrar el anterior cliente, en el despacho número 1. Debe de fracasar.
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 'LUJA730405S4X', 'Alejandro', 'Lucero', 'Jaramillo', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Voy a registrar un cliente del despacho 1 en el 3. Debe de fracasar
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3Qwddhmxñz922bnyZ043', 'MONE711201Q44', 'Elda', 'Moreno', 'Núñez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Voy a registrar un cliente del despacho 1 en el 2. Debe de fracasar
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'MONE711201Q44', 'Elda', 'Moreno', 'Núñez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Voy a registrar un cliente en el despacho 3. Debe ser exitoso
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3Qwddhmxñz922bnyZ043', 'DIJA710405L9Y', 'Joaquín', 'Diosdado', 'Martínez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Desactivar a Joaquín. Debe ser exitoso
SET @_Uid = 'W9uEjiv8s3Qwddhmxñz922bnyZ043';
SET @_idCliente = 8;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL desactivaCliente(@_Uid, @_idCliente, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

##No debo poder desactivarlo por segunda vez
SET @_Uid = 'W9uEjiv8s3Qwddhmxñz922bnyZ043';
SET @_idCliente = 8;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL desactivaCliente(@_Uid, @_idCliente, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

#Tampoco debo poder desactivar a un cliente inexistente
SET @_Uid = 'W9uEjdsadsazpl33xsrbnyZ043'; #Id del 2
SET @_idCliente = 5;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL desactivaCliente(@_Uid, @_idCliente, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

#Tampoco debo poder desactivar a un cliente inexistente
SET @_Uid = 'W9uEjiv8s3Qwddhmxñz922bnyZ043'; #Id del 3
SET @_idCliente = 5;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL desactivaCliente(@_Uid, @_idCliente, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

##Voy a registrar a Joaquín en el despacho 2. Como ya tengo su información, debe solamente activarlo.
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'DIJA710405L9Y', 'Joaquín', 'Diosdado', 'Martínez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Voy a registrar a Joaquín en el despacho 3. No debe de pasar.
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjiv8s3Qwddhmxñz922bnyZ043', 'DIJA710405L9Y', 'Joaquín', 'Diosdado', 'Martínez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

##Voy a capturar un chingo de clientes válidos para el despacho 2. Todos los siguientes están planeados para ser exitosos
SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'GACE980814M9Z', 'Edwin', 'García', 'Camarillo', 0, 1, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'BRER981107SSZ', 'Rafael', 'Briones', 'Elizalde', 0, 1, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'GOGK980217', 'Karina', 'Gónzalez', 'Gónzalez', 0, 1, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'AVPB991011LLL', 'Blanca', 'Ávila', 'P´´erez', 0, 1, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'GAMC980302AAA', 'Christian', 'García', 'Martínez', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

SET @_IdCliente = -1;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL guardaCliente('W9uEjdsadsazpl33xsrbnyZ043', 'VAFR980909MMM', 'Raúl', 'Varela', 'Fraire', 1, 0, @_IdCliente, @_OpValida,
@_Mensaje);
SELECT @_IdCliente, @_OpValida, @_Mensaje;

## 
CALL listaClientes('W9uEjdsadsazpl33xsrbnyZ043', '', '', '', 'M');
CALL listaClientes('W9uEjiv8s3QwddhpQVi6rbnyZ043', '', '', '', '');
CALL listaClientes('W9uEjiv8s3Qwddhmxñz922bnyZ043', '', '', '', '');
##
CALL listaMedioContacto();
CALL cargaMedioContactoCliente(6);
##
SET @_OpValida = false;
SET @_Mensaje = '';
CALL insertaMedioContactoCliente(1, 5, '8713297388', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL insertaMedioContactoCliente(1, 5, '8712215782', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL insertaMedioContactoCliente(1, 5, '8712215782', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL insertaMedioContactoCliente(2, 5, '8717148050', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL insertaMedioContactoCliente(3, 5, 'elda.mor.nu@gmail.com', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

##Inserción
SET @_OpValida = false;
SET @_Mensaje = '';
CALL insertaMedioContactoCliente(1, 6, '8712338281', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false;
SET @_Mensaje = '';
CALL insertaMedioContactoCliente(3, 6, 'lgp@gmail.com', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

##Elimina medio de contacto
SET @_OpValida = false;
SET @_Mensaje = '';
CALL eliminaMedioContactoCliente(6, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

##Actualiza medio de contacto
SET @_OpValida = false;
SET @_Mensaje = '';
CALL actualizaMedioContactoCliente(7, 'elda.mornu@gmail.com', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

SET @_OpValida = false;
SET @_Mensaje = '';
CALL actualizaMedioContactoCliente(9, 'lgpetrolero@gmail.com', @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SELECT * FROM Cliente_MedioContacto;


##Pruebas para ver si se puede realizar operación sobre un medio de contacto
SET @_OpValida = false;
CALL puedeRealizarOperacionMedioContacto(5, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(6, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(7, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(8, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(9, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(10, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(11, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(12, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(13, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = false;
CALL puedeRealizarOperacionMedioContacto(14, @_OpValida);
SELECT @_OpValida;

##Llamadas a desactivar
SET @_OpValida = false; # Este intento debe de fallar
SET @_Mensaje = '';
CALL desactivaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 10, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false; # Este intento debe de fallar también
SET @_Mensaje = '';
CALL desactivaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 11, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false; #Este intento va a tener exito
SET @_Mensaje = '';
CALL desactivaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 5, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;
SET @_OpValida = false; #Este intento va a tener exito
SET @_Mensaje = '';
CALL desactivaCliente('W9uEjiv8s3QwddhpQVi6rbnyZ043', 6, @_OpValida, @_Mensaje);
SELECT @_OpValida, @_Mensaje;

##Estas dos deben de dar falso
SET @_Opvalida = true;
CALL puedeRealizarOperacionMedioContacto(5, @_OpValida);
SELECT @_OpValida;
SET @_Opvalida = true;
CALL puedeRealizarOperacionMedioContacto(6, @_OpValida);
SELECT @_OpValida;

SELECT * FROM Cliente_Movimiento;
SELECT * FROM Cliente_Usuario;
#Llamadas a 

SELECT * FROM Usuario;
SELECT * FROM Cliente;

# Usuario 1: W9uEjiv8s3QwddhpQVi6rbnyZ043
# Usuario 2:  W9uEjiv8s3Qwddhmxñz922bnyZ043
#Usuario 3: W9uEjdsadsazpl33xsrbnyZ043
SELECT * FROM Cliente_Usuario;

desactivaCliente(
	IN _UidUserFirebase VARCHAR(100),
    IN _IdCliente INT,
    OUT _OpValida BIT,
    OUT _Mensaje VARCHAR(300)