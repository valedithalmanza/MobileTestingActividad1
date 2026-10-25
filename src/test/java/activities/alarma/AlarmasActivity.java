package activities.alarma;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.StaleElementReferenceException;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class AlarmasActivity {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private static final String ID =
            "com.google.android.deskclock:id/";

    public AlarmasActivity(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(15)
        );
    }

    public void agregarAlarma() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id(ID + "fab")
                )
        ).click();

        System.out.println("Formulario de alarma abierto");
    }
    public void cambiarModoTeclado() {

        By campoHora = By.id(
                ID + "material_hour_text_input"
        );


        if (driver.findElements(campoHora).isEmpty()) {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id(
                                    ID + "material_timepicker_mode_button"
                            )
                    )
            ).click();

            System.out.println("Cambiando al modo teclado");

        } else {

            System.out.println(
                    "El selector ya estaba en modo teclado"
            );
        }


        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        campoHora
                )
        );

        System.out.println("Modo teclado verificado");
    }



    private WebElement obtenerCampo(String contenedor) {

        return driver.findElement(
                By.id(ID + contenedor)
        ).findElement(
                By.className("android.widget.EditText")
        );
    }


    public void establecerHora(
            int hora, int minutos, String periodo) {

        String horaTexto = String.format(
                "%02d", hora
        );

        String minutosTexto = String.format(
                "%02d", minutos
        );


        driver.findElement(
                By.id(ID + "material_hour_text_input")
        ).click();

        WebElement campoHora = obtenerCampo(
                "material_hour_text_input"
        );

        campoHora.click();
        campoHora.clear();
        campoHora.sendKeys(horaTexto);

        int horaReal = Integer.parseInt(
                campoHora.getText().trim()
        );

        if (horaReal != hora) {
            throw new IllegalStateException(
                    "La hora no se introdujo correctamente"
            );
        }

        driver.findElement(
                By.id(ID + "material_minute_text_input")
        ).click();

        WebElement campoMinutos = obtenerCampo(
                "material_minute_text_input"
        );

        campoMinutos.click();
        campoMinutos.clear();
        campoMinutos.sendKeys(minutosTexto);

        int minutosReales = Integer.parseInt(
                campoMinutos.getText().trim()
        );

        if (minutosReales != minutos) {
            throw new IllegalStateException(
                    "Los minutos no se introdujeron correctamente"
            );
        }


        String botonPeriodo;

        if (periodo.equalsIgnoreCase("AM")) {
            botonPeriodo = "material_clock_period_am_button";
        } else {
            botonPeriodo = "material_clock_period_pm_button";
        }

        WebElement boton = driver.findElement(
                By.id(ID + botonPeriodo)
        );

        boton.click();

        if (!Boolean.parseBoolean(
                boton.getAttribute("checked"))) {

            throw new IllegalStateException(
                    "No se selecciono correctamente " + periodo
            );
        }

        System.out.println(
                "Horario introducido: "
                        + horaTexto + ":"
                        + minutosTexto + " "
                        + periodo
        );
    }


    public void guardarAlarma() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id(ID + "material_timepicker_ok_button")
                )
        ).click();

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        By.id(ID + "material_timepicker_ok_button")
                )
        );

        System.out.println("Alarma guardada");
    }


    private String normalizar(String texto) {

        return texto
                .replaceAll("[\\s\\p{Z}]+", "")
                .toLowerCase(Locale.ROOT);
    }



    public int contarAlarmasVisibles(
            int hora, int minutos, String periodo) {

        String sufijo = periodo.equalsIgnoreCase("AM")
                ? "a.m." : "p.m.";

        String esperado = normalizar(
                String.format(
                        "%d:%02d%s",
                        hora, minutos, sufijo
                )
        );

        for (int intento = 0; intento < 4; intento++) {

            try {

                List<WebElement> alarmas = driver.findElements(
                        By.id(ID + "digital_clock")
                );

                int cantidad = 0;

                for (WebElement alarma : alarmas) {

                    String texto = alarma.getText();

                    String horaEncontrada = normalizar(texto);

                    System.out.println(
                            "Alarma encontrada: " + texto
                    );

                    if (horaEncontrada.equals(esperado)) {
                        cantidad++;
                    }
                }

                return cantidad;

            } catch (StaleElementReferenceException e) {

                System.out.println(
                        "Lista de alarmas actualizada. Reintentando..."
                );
            }
        }

        throw new IllegalStateException(
                "No se pudo leer la lista de alarmas"
        );
    }

}
