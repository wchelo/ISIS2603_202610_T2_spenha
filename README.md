# Back del proyecto TallerPruebas
---------------------------------------
1. Sobre @Transactional: En la Regla 1, usted debe restar dinero de una cuenta y
sumarlo a un bolsillo. Son dos operaciones de guardado distintas
(accountRepository.save y pocketRepository.save).


a. Pregunta: ¿Qué pasaría si el servidor se apaga justo después de restar el
dinero de la cuenta, pero antes de sumarlo al bolsillo?

La funcion hace rollback, osea que borra todo lo que hizo, esto debido al @Transactional, que justamente asegura esto.

b. Investigación: ¿Cómo ayuda la anotación @Transactional a evitar que ese
dinero se "pierda" en el limbo? (Explique el concepto de atomicidad).

Lo que sucede con el uso de @Transactional en el inicio de la funcion es asegurar que solo se den dos casos. O que se complete con éxito toda la funcion, o que si en caso que pase alguna clase de error de algun tipo, esta haga "rollback" lo que deja todos los valores usado en su estado original antes de ser usados.

-----------------------------------------
2. Sobre la Inyección de Dependencias:


a. En su código usó @Autowired para obtener el AccountRepository dentro de
PocketService.

Sí, se usó tanto para hacer @AutoWired a AccountRepository como para PocketRepository ya que ambos son repositorios que se piensan usar en el PocketService.


b. Pregunta: ¿Por qué es mejor
usar @Autowired en lugar de hacer AccountRepository repo = new
AccountRepository() manualmente dentro del servicio? ¿Qué ventaja nos
da esto a la hora de hacer pruebas?

Porque crea las instancias necesarias de manera automatica, lo que nos da ventajas como una mejor leigibilidad y practicidad.

-------------------------------------------
3. Lógica vs. Controlador:

a. Pregunta: Si quisiéramos validar que el monto de una transacción no sea
negativo, ¿Por qué se recomienda poner ese if en la clase Service y no
directamente en el Controller (API) o en la pantalla del celular (Frontend)?

Porque se trata de una regla de negocio demasiado importante o natural, en el sentido en que la necesidad de dicha regla es mayor y por eso se debe tratar de una forma distinta, perteneciendo mejor a un servicio.

----------------------------------------------
4. Pruebas Unitarias:

a. Investigación: Investigue qué es Test Driven Development (TDD) y explique
sus principios fundamentales (Red, Green, Refactor). ¿Qué ventajas tiene
utilizar esta metodología de desarrollo?

Que se puede manejar el codigo de una manera diferente, la cual es, poder crear codigo por partes y asi evitar escribir codigo innecesario, evitar mas errores al tener una prueba que se encargara de hacernos caer en cuenta en que momento al modificar nuestro codigo base dañamos algo, y asi tener un mejor metodo para manejar funcionalidades.


b. Proponga al menos tres (3) casos de prueba adicionales, más allá de los
ya descritos en la Parte A

(no entiendo si pide casos de prueba adicional para la regla 0 o para las otras reglas, asi que voy a entregar 3 de alguna de las otras dos reglas)

Para la regla 2 (mover dinero entre dos cuentas) por ejemplo, se tiene:
- el caso de exito
-el caso en el que la cuenta origen no existe
-el caso en el que la cuenta destino no existe
-el caso en el que la cuenta origen es igual a la cuenta destino
-el caso en el que los fondos son insuficientes en la cuenta origen


--------------------------------------------------
5. Tipos de Assert: En JUnit, la validación final se realiza mediante "Asserts".
Investigue y explique brevemente cuáles son y para qué sirven los tipos de
aserciones, dando un ejemplo de en qué caso usaría cada uno.

- assertEqual funciona de forma que se pasan dos parametros, el esperado siendo el primero y luego el real. Por ejemplo se puede usar para verificar los saldos finales de ambas cuentas al pasar cierto monto de plata. 

assertEqual(saldoA -1000, algo) (para la parte esperada se usa el saldo definido y se le resta el monto que cedio)(para el otro assertEqual() se sumaran los 1000 que sedio el primero saldo)

assertEqual(algo, accountList.get(0).getSaldo()) (para la parte real se ingresa a la lista donde se instanciaron las entidades de las cuentas, se seleccciona alguna, y luego se selecciona el valor del saldo)(cabe aclarar que esto se hace es despues de llamar la funcion para pasar el dinero, de forma que el saldo guardado en el real sea el saldo que inicialmente se le puso menos o mas el monto, dependiendo si recibe o cede el dinero)

- assertThrow(), se usa para las excepciones. Dependiendo de la condiciion que se puso se verificara con este assert, cabe aclarar que en los test no se crea la logica para saber si crea un exception o no, eso esta en service. Un ejemplo es si no existe una cuenta origen, para ello en el test se crea un id falso que se entregara como parametro origen y en el service, la parte de la logica, entrara en el if que tirara la excepcion de EntityNotFoundException. y luego el test funcionara (ya que la logica en service "falló").

- assertNotNull(), para este es bastante sencillo y diciente. Este, asegura que el parametro que se pase dentro del assert sea un valor no nulo. Como por ejemplo, si creo un bolsillo, y al final retorno una instancia creada, quiero saber si lo que se retorna como PocketEntity sea distinto a null, significando que efectivamente se creo el bolsillo.

- como estos asserts hay muchos mas pero para este taller esos primeros fueron los escenciales.




## Enlaces de interés

* [BookstoreBack](https://github.com/Uniandes-isis2603/bookstore-back) -> Repositorio de referencia para el Back

* [Jenkins](http://157.253.238.75:8080/jenkins-isis2603/) -> Autentíquese con sus credencias de GitHub
* [SonarQube](http://157.253.238.75:8080/sonar-isis2603/) -> No requiere autenticación
