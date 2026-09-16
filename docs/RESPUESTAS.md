# Respuestas del examen BiblioMobil

## Pregunta 1

Cuando llegue un backend REST cambiaría la capa `data`, agregando implementaciones como `LibroRepositorioRemoto` y `LectorRepositorioRemoto`, y también cambiaría `shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/di/AppModule.kt` para enlazar las interfaces `LibroRepository` y `LectorRepository` con esas implementaciones remotas. Permanecerían intactos los modelos de `domain/model`, las interfaces de `domain/repository`, los casos de uso de `domain/usecase`, los ViewModels y las Screens, siempre que el contrato no cambie, porque la regla de dependencia obliga a que las capas externas dependan del dominio y no al revés.

## Pregunta 2

`RegistrarLibroUseCase` recibe `anio` y `ejemplares` como `String` porque así puede distinguir el campo vacío, el texto no numérico y el número válido o fuera de rango con mensajes precisos. Si recibiera `Int`, alguien en Presentation o en el ViewModel tendría que convertir antes, trasladando parte de la validación fuera del caso de uso y perdiendo los estados originales escritos por el usuario.

## Pregunta 3

Si `LibroRepository` fuera `factory`, Koin crearía instancias diferentes; como el repositorio actual guarda datos en memoria, `RegistrarLibroUseCase` podría registrar usando el repository A y `ListarLibrosUseCase` listar usando el repository B, por lo que el libro registrado no aparecería después al listar. Con `single` ambos casos de uso comparten la misma instancia y el estado registrado se conserva.

## Salida real relevante de pruebas

Comando ejecutado:

```text
./gradlew :shared:testAndroidHostTest
```

Resultado Gradle:

```text
> Task :shared:testAndroidHostTest

BUILD SUCCESSFUL in 12s
33 actionable tasks: 1 executed, 32 up-to-date
```

Resultado del reporte XML de Gradle:

```text
ResultadoDeTest: tests=2, failures=0, errors=0
AppModuleTest: tests=2, failures=0, errors=0
LibroRepositorioEnMemoriaTest: tests=2, failures=0, errors=0
LibroViewModelTest: tests=8, failures=0, errors=0
RegistrarLectorUseCaseTest: tests=5, failures=0, errors=0
RegistrarLibroUseCaseTest: tests=6, failures=0, errors=0
LibroTest: tests=4, failures=0, errors=0
DetallePrestamoTest: tests=3, failures=0, errors=0

Total: 32 pruebas, 0 fallos, 0 errores
```
