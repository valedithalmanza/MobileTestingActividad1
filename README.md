# Actividad 1 - Mobile Testing

**Automatización de una alarma en Android con Appium, Java, JUnit 5 y Gradle.**

Este proyecto corresponde a la **Actividad 1 de Mobile Testing**. Automatiza la creación de una alarma en la aplicación **Reloj de Google** y verifica que se haya registrado correctamente.

## Caso de prueba

| Campo | Descripción |
| --- | --- |
| ID | TC-MOB-001 |
| Aplicación | Reloj de Google (Google Clock) |
| Dispositivo utilizado | HONOR X6b (Android físico) |
| Objetivo | Crear una alarma y comprobar que aparece en la lista |
| Hora predeterminada | 06:15 a. m. |
| Resultado esperado | La cantidad de alarmas del horario solicitado aumenta en una unidad |

La prueba también acepta **hora, minutos y período AM/PM** como parámetros, sin modificar las clases Java.

## Tecnologías

- Java 21
- Appium Server 3 y driver UiAutomator2
- Selenium / Appium Java Client
- JUnit 5
- Gradle (incluye Gradle Wrapper)
- Android SDK / ADB
- IntelliJ IDEA

## Estructura del proyecto

```text
src/test/java/
├── activities/alarma/
│   └── AlarmasActivity.java   # Interacciones con Google Reloj
├── device/
│   └── Android.java           # Configuración de la sesión Appium
└── tests/
    └── CrearAlarmaTest.java   # Caso de prueba JUnit

build.gradle.kts                # Dependencias y parámetros de ejecución
gradlew.bat                     # Gradle Wrapper para Windows
```

## Requisitos previos

1. Tener instalado **JDK 21** y el **Android SDK** con `adb` disponible.
2. Instalar **Appium Server** y el driver **UiAutomator2**.
3. Conectar un dispositivo Android por USB, activar la **depuración USB** y aceptar la autorización en el teléfono. También puede utilizarse un emulador compatible.
4. Instalar **Reloj de Google** desde Google Play Store en el dispositivo de prueba.
5. Tener libre el puerto **4723** para Appium.

**Aplicación utilizada:**

```text
appPackage:  com.google.android.deskclock
appActivity: com.android.deskclock.DeskClock
```

> Los identificadores de elementos pueden variar según la versión de Reloj de Google. El proyecto se probó con un HONOR X6b.

## Cómo ejecutar la prueba (Windows / PowerShell)

### 1. Verificar la conexión del dispositivo

```powershell
adb devices
```

El dispositivo debe figurar con estado `device`. Si aparece `unauthorized`, aceptar la autorización de depuración USB en el teléfono.

### 2. Iniciar Appium

En otra terminal, ejecutar y dejar el servidor abierto:

```powershell
appium
```

El proyecto se conecta a `http://127.0.0.1:4723`.

### 3. Ejecutar el test

Desde la carpeta raíz del proyecto:

```powershell
.\gradlew.bat test --tests tests.CrearAlarmaTest
```

Este comando utiliza **06:15 AM** de forma predeterminada.

**Ejemplo: crear una alarma a las 09:30 PM**

```powershell
.\gradlew.bat test --tests tests.CrearAlarmaTest -Phora=09 -Pminutos=30 -Pperiodo=PM
```

**Ejemplo: crear una alarma a las 11:30 PM**

```powershell
.\gradlew.bat test --tests tests.CrearAlarmaTest -Phora=11 -Pminutos=30 -Pperiodo=PM
```

También se puede ejecutar `CrearAlarmaTest.crearAlarma()` desde IntelliJ IDEA.

## Validación y evidencias

El test:

1. Inicia Google Reloj mediante Appium.
2. Cuenta las alarmas visibles que coinciden con el horario solicitado.
3. Abre el formulario, introduce la hora y los minutos y selecciona AM/PM.
4. Guarda la alarma y espera a que aparezca en la lista.
5. Comprueba mediante JUnit que la cantidad de coincidencias aumentó en **una unidad**.
6. Genera la captura `build/evidencias/alarma-creada.png` cuando la prueba pasa.

La ejecución correcta muestra mensajes como:

```text
PRUEBA EXITOSA: Alarma creada correctamente
Cantidad antes: 0
Cantidad despues: 1
BUILD SUCCESSFUL
```

**Nota:** si se repite un horario ya creado, el conteo puede partir de un valor distinto de cero. Para obtener una evidencia clara con `0` antes y `1` después, eliminar previamente la alarma con ese horario. Las capturas y los reportes de `build/` se generan localmente y no se incluyen en GitHub por el archivo `.gitignore`.

## Autoría

**Valeria Edith Almanza Mamani**  
Actividad académica: Mobile Testing.
