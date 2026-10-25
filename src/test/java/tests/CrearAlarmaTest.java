
package tests;

import activities.alarma.AlarmasActivity;
import device.Android;

import io.appium.java_client.android.AndroidDriver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;

public class CrearAlarmaTest {

    private AndroidDriver driver;
    private AlarmasActivity alarmasActivity;

    @BeforeEach
    public void setUp() throws Exception {

        driver = Android.crearDriver();

        alarmasActivity = new AlarmasActivity(driver);
    }

    @Test
    public void crearAlarma() throws Exception {


        int hora = Integer.parseInt(
                System.getProperty("alarma.hora", "06")
        );

        int minutos = Integer.parseInt(
                System.getProperty("alarma.minutos", "15")
        );

        String periodo = System.getProperty(
                "alarma.periodo", "AM"
        ).trim().toUpperCase(Locale.ROOT);


        Assertions.assertTrue(
                hora >= 1 && hora <= 12,
                "La hora debe estar entre 1 y 12"
        );

        Assertions.assertTrue(
                minutos >= 0 && minutos <= 59,
                "Los minutos deben estar entre 0 y 59"
        );

        Assertions.assertTrue(
                periodo.equals("AM") || periodo.equals("PM"),
                "El periodo debe ser AM o PM"
        );

        System.out.println("=== INICIO DEL TEST ===");

        System.out.println(
                "Horario solicitado: "
                        + hora + ":"
                        + String.format("%02d", minutos)
                        + " " + periodo
        );


        int cantidadAntes = alarmasActivity
                .contarAlarmasVisibles(
                        hora, minutos, periodo
                );

        System.out.println(
                "Alarmas coincidentes antes: " + cantidadAntes
        );

        // EJECUTAR LAS ACCIONES
        alarmasActivity.agregarAlarma();

        alarmasActivity.cambiarModoTeclado();

        alarmasActivity.establecerHora(
                hora, minutos, periodo
        );

        alarmasActivity.guardarAlarma();

        // ESPERAR A QUE APAREZCA LA NUEVA ALARMA
        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(15)
        );

        wait.until(d ->
                alarmasActivity.contarAlarmasVisibles(
                        hora, minutos, periodo
                ) > cantidadAntes
        );

        // OBTENER EL RESULTADO REAL
        int cantidadDespues = alarmasActivity
                .contarAlarmasVisibles(
                        hora, minutos, periodo
                );

        // VERIFICACION CON JUNIT
        Assertions.assertEquals(
                cantidadAntes + 1,
                cantidadDespues,
                "ERROR: No se creo una nueva alarma"
        );

        // GUARDAR EVIDENCIA
        Path evidencia = Path.of(
                "build",
                "evidencias",
                "alarma-creada.png"
        );

        Files.createDirectories(
                evidencia.getParent()
        );

        Files.write(
                evidencia,
                driver.getScreenshotAs(OutputType.BYTES)
        );

        System.out.println(
                "PRUEBA EXITOSA: Alarma creada correctamente"
        );

        System.out.println(
                "Cantidad antes: " + cantidadAntes
        );

        System.out.println(
                "Cantidad despues: " + cantidadDespues
        );

        System.out.println("=== FIN DEL TEST ===");
    }

    @AfterEach
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
