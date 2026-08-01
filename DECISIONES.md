# 🧭 DECISIONES.md — Bitácora de diseño

> **Instrucciones.** Completa **una entrada por fase**, en **primera persona** y
> **refiriéndote a tu propio código**: nombres reales de tus clases, tu tabla, tus
> líneas, tu salida real de terminal.
>
> ❌ **No puntúa** una justificación genérica que podría pegarse en cualquier proyecto
> (ej.: *"usé boundedElastic porque es una buena práctica para operaciones bloqueantes"*).
> ✅ **Sí puntúa** una justificación anclada a tu código (ej.: *"en `ProductoService`
> línea 34 envolví `productoRepository.findAll()` porque Hibernate abre la conexión
> JDBC en el hilo llamante; al probarlo sin `subscribeOn` vi en el log el hilo
> `reactor-http-nio-2`, que es el event loop de Netty"*).
>
> Estas mismas preguntas se te harán en la **defensa oral**.

---

## Datos

- **Nombre:** Samantha Soledad Novillo Betancourt
- **Cédula:** 1751094283
- **NN (dos últimos dígitos):** 83
- **Categoría asignada (según el último dígito):** Café

---

## Fase 1 — Configuración y perfiles

**1.1** ¿Qué archivo activa el perfil `prod` y qué línea exacta lo hace?
Archivo: application.properties

Línea: spring.profiles.active=prod
>
**1.2** Pega la línea del log de arranque donde se ve tu puerto y el perfil activo.

```
"Error" NO ME SALIÓ LA EJECUCIÓN
```

**1.3** ¿Qué habría pasado si dejabas `ddl-auto=create-drop` en lugar de `update`?
Responde pensando en tus datos sembrados.

>Si hubiera usado create-drop hibernate eliminaria y recrearía la tabla tbl_productos_base_83,
> en cada arranque que pierde mis datos sembrados, pero en el segundo arranque, la siembra fallaría,
> porque la tabla se crea vacía pero la restrincción unique de nombre_prodycto no se encontraria duplicados al inicio,
> eso permite se inserten nuevamente 
> Al final del arranque al apagar la aplicacion se elimaría la tabla y perderia todo

**1.4** ¿Levantaste PostgreSQL con `compose.yaml` (Opción A) o con una instalación local
(Opción B)? ¿Qué ventaja tiene la que elegiste?

>Elegi la opción A porque es un principal que puede tener un entorno a base de datos aislado, y reproducible, sin necesidad
> de instalar PostgreSQL, ademas puedo destruir y recrear el contenedor facilmente para pruebas y todos los desarrolladores del equipo
> que tienen la configuracion de base de datos con solo ejecutar docker compose up

---

## Fase 2 — Persistencia con JPA/Hibernate

**2.1** ¿Cuál es el nombre exacto de tu tabla y de dónde salió ese nombre?

>Archivo: Producto.Entity.java
> Lineas 6,7 y8 :
> @Entity
@Table(name = "tbl_productos_base_83")
public class ProductoEntity {

**2.2** Pega la salida de `psql -d agrosmart_db -c "\d tbl_productos_base_NN"` y
señala dónde se ve la restricción `unique` y el `length` de 120.

> LASTIMOSAMENTE NO SE PUDO EJECUTAR CORRECTAMENTE LO ESTABLECIDO 
```
PS C:\examen> Pega la salida de `psql -d agrosmart_db -c "\d tbl_productos_base_NN"` y
>> señala dónde se ve la restricción `unique` y el `length` de 120.
>>
Pega : El término 'Pega' no se reconoce como nombre de un cmdlet, función, archivo de script o programa ejecutable. Compruebe si escribió correctamente el nombre o, si
incluyó una ruta de acceso, compruebe que dicha ruta es correcta e inténtelo de nuevo.
En línea: 1 Carácter: 1
+ Pega la salida de `psql -d agrosmart_db -c "\d tbl_productos_base_NN" ...
+ ~~~~
    + CategoryInfo          : ObjectNotFound: (Pega:String) [], CommandNotFoundException
    + FullyQualifiedErrorId : CommandNotFoundException

señala : El término 'señala' no se reconoce como nombre de un cmdlet, función, archivo de script o programa ejecutable. Compruebe si escribió correctamente el nombre o,
si incluyó una ruta de acceso, compruebe que dicha ruta es correcta e inténtelo de nuevo.
En línea: 2 Carácter: 1
+ señala dónde se ve la restricción `unique` y el `length` de 120.
+ ~~~~~~
    + CategoryInfo          : ObjectNotFound: (señala:String) [], CommandNotFoundException
    + FullyQualifiedErrorId : CommandNotFoundException

PS C:\examen> docker exec -it agrosmart-postgres psql -U postgres -d agrosmart_db -c "\d tbl_productos_base_83"
Did not find any relation named "tbl_productos_base_83".

What's next:
    Try Docker Debug for seamless, persistent debugging tools in any container or image → docker debug agrosmart-postgres
    Learn more at https://docs.docker.com/go/debug-cli/
PS C:\examen> docker exec -it agrosmart-postgres psql -U postgres -d agrosmart_db -c "\dt"
Did not find any tables.

What's next:
    Try Docker Debug for seamless, persistent debugging tools in any container or image → docker debug agrosmart-postgres
    Learn more at https://docs.docker.com/go/debug-cli/
PS C:\examen> docker ps
CONTAINER ID   IMAGE             COMMAND                  CREATED       STATUS       PORTS                                         NAMES
ed7ca0bd0b95   postgres:latest   "docker-entrypoint.s…"   4 hours ago   Up 4 hours   0.0.0.0:5432->5432/tcp, [::]:5432->5432/tcp   agrosmart-postgres
PS C:\examen>

```

**2.3** ¿Por qué usaste `BigDecimal` y no `double` para `precio_usd`? Relaciónalo con el
tipo que generó Hibernate en PostgreSQL.

Se uso el BigDecimal para evitar errores de redondeo en operaciones financieras ya que double usa Binario y 
puede perder la precision, hibernate mapea BigDecimal a numeric(10,2) en Postgres almacenando valores exactos con dos decimales


>Archivo: ProductoEntity.java
> Líneas: 18 y 19:
>     @Column(name = "precio_usd", precision = 10, scale = 2)
private BigDecimal precioUsd;

**2.4** ¿Cómo hiciste idempotente tu siembra y qué pasaría en el segundo arranque si no
lo fuera? (piensa en la restricción `unique` de `nombre_producto`)

La siempre es idempotente porque verifica si el producto ya existe antes de insertarlo, usando findByNombreProducto()
si no fuera idempotente, la restricción unique lanxaría una excepción DataIntergrityViolationException al intentar insertar un nombre duplicado en el segundo arranque

>Archivo: Data Loader
Lineas 9, 10 , 11, 12 y 13 
> @Component
public class DataLoader implements CommandLineRunner {
private final ProductoRepository repository;

    public DataLoader(ProductoRepository repository) {
        this.repository = repository;
    }
---

## Fase 3 — Modelo inmutable y lógica funcional

**3.1** ¿Por qué tienes **dos** clases (`ProductoEntity` y `Producto`) en lugar de una?
¿Qué te impide hacer inmutable directamente la entidad de Hibernate?

> Tengo ProductoEntity como la entidad JPA que hibernate gestiona y productoDominio como mi modelo inmutable de negocio
> se sabe que hibernate requiere que ProductoEntity tenga su constructir sin argmentos y stters para poder instanciar y modificar objetos mediante reflexión durante el ciclo de vida
> no puedo ser inmutable productoentity porque Hibernate necesita mutabilidad para funcionar correctamente , en cambio producto dominio es un record
> inmutable que uso en mi capa de servicio y controladores

**3.2** Escribe el código exacto de **tus dos** copias defensivas e indica en qué línea
está cada una.


Copia defensiva #1 — en el constructor

Línea 19: this.correosNotificacion = new ArrayList<>(correosNotificacion);

Copia defensiva #2 — en el getter

Línea 26: return Collections.unmodifiableList(new ArrayList<>(correosNotificacion));
this.correosNotificacion = new ArrayList<>(correosNotificacion);
return Collections.unmodifiableList(new ArrayList<>(correosNotificacion));


**3.3** ¿Por qué la copia defensiva **solo en el getter** no sería suficiente? Describe
el ataque concreto que quedaría abierto sobre **tu** clase.

>Si hiciera una copa defensiva en el getter el ataque sería un cliente malicioso obtiene el ProductoDominio mediante getproducto()
> luego como es inmutable no podria modificarlo directamente peo si usando una clase mutable, podria ser producto.setPrecioUsd(BigDecmial.ZERO) y cambiar el precio sin pasar por validaciones
> En mi caso al usar record la inmutabilidad es nativa pero igual se hace copias defensivas al crar cuentas instancias desde el controlador par asegurar que el objeto recibida no sea
> modificada por referencias externas que aún apunten al original

**3.4** ¿Cómo implementaste `A_MAYUSCULAS` para no mutar el `Producto` recibido?

"A_MAYUSCULAS es una Function<ProductoDominio,Producto Dominio>" en vez de modificar el objeto recibido
la lambada construye una instancia nueva de Producto Dominio con New Producto Dominio, pasandole el mismo id, categoria
, precio USD y correos Notificacion del original, pero con producto.getNombre()to.UppserCase() en el campo nombre

El original queda completamente intato porque nunca se le llama ningún setter de hecho Producto Dominio no tiene setters asi que mutarlo
directamente ni siquiera podria ser posible la unica forma de cambiar es crear un objeto nuevo que es justo que hace esta funcion

Archivo: ProductoFiltres.java
Linea: 17,18,19,20,21,22 y 23
public static final UnaryOperator<ProductoDominio> A_MAYUSCULAS = producto ->
new ProductoDominio(
producto.getId(),
producto.getNombre().toUpperCase(),
producto.getCategoria(),
producto.getPrecioUsd(),
producto.getCorreosNotificacion()
);



## Fase 4 — Servicio reactivo y aislamiento del bloqueo

**4.1** Pega tu método `obtenerProductosComercializables()` completo.
Linea: 46 al 54
```java
>     public Flux<ProductoDominio> obtenerProductosComercializables() {
return Mono.fromCallable(repository::findAll)
.subscribeOn(Schedulers.boundedElastic())
.flatMapMany(Flux::fromIterable)
.map(ProductoMapper::toDominio)
.map(ProductoFilters.A_MAYUSCULAS)
.filter(ProductoFilters.IS_VALID)
.doOnNext(ProductoFilters.LOG_PRODUCTO)
.defaultIfEmpty(PRODUCTO_GENERICO);
```

**4.2** ¿Qué pasa **exactamente** si eliminas
`.subscribeOn(Schedulers.boundedElastic())` de ese método? Si lo probaste, indica qué
hilo aparecía en el log antes y después.

> Sin subscrubeOn(Schedulers.boundedElastic()) toda la cadena reactiva se ejecuta en el hilo donde se origina la suscribcion
> que una app WebFlux es uno de los hilos del event loop de Netty en eso Netty usa un numero pequuño de hilos
> para atender todas las peticiones de HTTP concurrentes de la aplicacion
> 
> en repository.findAll() es una llamada JPA/Hibernate bloqueante donde el hilo se ejecuta se queda "congelado" esperando la respuesta
> de la base de datos, si esa llamada corre directamente un hilo de netty (sin subscribeOn) ese hilo el event loop queda atrapado
> esperando la consulta SQL como hay pocos hilos de event loop compartidos entre  toas las peticiones concurrentes 
> 
> Con suscrubeOn (boundedElastic()) en cambio la llamada bloqueante se lueve a un pool de hilos separado, diseñado justamente para trabajo
> bloqueante asi en elevent loop de netty queda libre para seguir atendiendo otras peticiones mientras esa consulta a la base terminar 
> 
> La evidencia es agregar temporalmente esta linea dentro de 
> .map(ProductoMapper::toDominio) o en un .doOnNext(), para ver el nombre del hilo actual:
> .doOnNext(p -> System.out.println("Hilo: " + Thread.currentThread().getName()))
> 
> Corre la app y llama al endpoint que use obtenerProductosComercializables() (si ya tienes el controlador de la Fase 6 armado
> si no, puedes probarlo con un PruebaFase4.java similar al que hice para la Fase 3)
>
> Finalmente se debe anotar  el nombre del hilo que aparece — con subscribeOn(boundedElastic())

**4.3** ¿Por qué `Mono.fromCallable(...)` y no `Mono.just(repository.findAll())`?
(pista: cuándo se ejecuta cada uno)

>Difere la ejecución hasta la suscripción permitiendo que suscribeOn mueva esa ejecución al hilo boundedElastic correcto
> En Mono.just se evaluaria el repository.findAll() de inmediato en el hilo que este ejecutandose ese codigo por el momento 
> que probablemente el event loop de Netty haciendo inútil cualquier suscribeOn posterior

**4.4** En **tu** código, ¿dónde usaste `defaultIfEmpty` y dónde `switchIfEmpty`, y por
qué no son intercambiables en esos dos lugares?

>Use el `defaultIfEmpty` en ObtenerProductosComercializables() proque solo necesito sustituir el vacio por un valor concreto
> (PRODUCTO_GENERICO) Usé switchIfEmpty en buscarPorld() porque necesitaba sustituir el vacio por un Mono que termina en error 
> (Mono.error(new ProductoNoEncontradoException(id))) y defaultIfEmpty no acepta un flujo como reemplazo
> solo un valor plano no puede lanzar una excepcion

**4.5** ¿Por qué `doOnNext` no sirve para transformar el elemento, si aparentemente
"recibe" el producto?

>donOnnext  que recibe un Consuer<T> que por definicion no sevuelve ningun valor solo ejecuta 
> un efecto secundario, en mi caso fue LOG_PRODUCTO imprime el ID y Nombre el elemento que sale de doOnNext es exactamente el mismo objeto que enctró 
> sin cambios para transgormar necesaitaría map, que recibe una función <T,R> que cuyo valor de retorno si reemplaza el elemento del flujo
> como hago con A_MAYUSCULAS

---

## Fase 5 — Módulo de IA con LangChain4j

**5.1** Pega tu interfaz `AgroSmartAIService` completa.

```java

```

**5.2** ¿Qué hace `@V("producto")` y qué pasaría si lo quitaras dejando solo el
parámetro?

>

**5.3** ¿En qué archivo y con qué líneas configuraste el modelo? ¿Por qué **no** hizo
falta declarar un `@Bean`?

>

**5.4** ¿Por qué la llamada a la IA también necesita `boundedElastic`, si no es una
consulta a base de datos?

>

**5.5** Si tu proveedor devolvió un error durante el examen, pega el mensaje real y la
respuesta que produjo tu `onErrorResume`.

```

```

---

## Fase 6 — API reactiva con WebFlux

**6.1** Pega la salida real de tus cuatro `curl`.

```

```

**6.2** ¿Cómo lograste que el id inexistente responda **404** y no 500?

>

**6.3** ¿Qué pasaría si tu controlador devolviera `List<Producto>` en lugar de
`Flux<Producto>`? ¿Seguiría compilando? ¿Seguiría siendo no bloqueante?

>

---

## Fase 7 — Pruebas unitarias

**7.1** Pega la salida real de tus pruebas (`./mvnw test` o `./gradlew test`).

```

```

**7.2** ¿Cuántos productos espera tu `expectNextCount(...)` y por qué ese número
concreto? Relaciónalo con tu semilla.

>

**7.3** ¿Por qué mockeaste `ProductoRepository` en lugar de dejar que la prueba consulte
PostgreSQL?

>

**7.4** ¿Qué demuestra `assertNotSame` que `assertEquals` **no** demuestra en tu prueba
de copia defensiva?

>

**7.5** ¿Por qué una prueba de un `Flux` que no llama a `verifyComplete()` (o a
`verify()`) no está probando nada?

>

---

## Fase 8 — Integración y cierre

**8.1** Pega tu `git log --oneline --graph --all`.

```
PS C:\examen> git log --oneline --graph --all
* 797ad86 (HEAD -> main, origin/main, origin/HEAD) feat: implementa servicio reactivo con boundedElastic y operadores
* 4856e76 feat: agrega modelo inmutable de producto y logica funcional
* 273a91d feat: agrega entidad jpa de productos y siembra de datos
* 0464f36 chore: configura perfil prod con postgresql y puerto propio
* 4575931 feat: agregar identidad del estudiante con semilla personal
* da2ebf2 Initial commit
```

**8.2** ¿Qué fase te tomó más tiempo del previsto y por qué?

>Todas las fases fueron dificiles para mi, porque la computadora que manejo no pudo resistir a mucho código 
> y tuve que hacer tantos cambios que al final no lo logré hacerlo todo lastimosamente

**8.3** Si tuvieras 30 minutos más, ¿qué mejorarías **primero** de tu entrega y por qué
esa y no otra?

>Mejoraria algunos códigos para que puedan ejecutarse correctamente aunque ahora por el tiempo ya no me alcanza

**8.4** Declara honestamente qué herramientas consultaste durante el examen
(documentación, apuntes, asistentes de IA) y para qué. **Esta declaración no descuenta
puntaje**; su omisión o falsedad sí constituye falta de honestidad académica.

>Si consulte asistentes de IA para saber como realizar cada fase, pero también puse mi mano para comprender la codificación 
> y si hay partes con IA por el tiempo que falto resolver las demás fases espero que no sea una molestia para el profesor 
