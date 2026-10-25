
package device;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.time.Duration;

public class Android {

    public static AndroidDriver crearDriver() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options();

        options.setDeviceName("HONOR X6b");

        String udid = System.getenv("ANDROID_UDID");

        if (udid != null && !udid.isBlank()) {
            options.setUdid(udid);
        }

        options.setAppPackage("com.google.android.deskclock");
        options.setAppActivity("com.android.deskclock.DeskClock");
        options.setNoReset(true);

        AndroidDriver driver = new AndroidDriver(
                new URL("http://127.0.0.1:4723/"),
                options
        );

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(5));

        driver.activateApp("com.google.android.deskclock");

        System.out.println(
                "Aplicacion abierta: " + driver.getCurrentPackage()
        );

        return driver;
    }
}
