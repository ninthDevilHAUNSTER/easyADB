
import base.EasyAdb;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;
import exception.DeviceNotFoundException;
import exception.ScreenshotNullImageException;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Test {
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());

    public static void testScreenshot() throws DeviceNotFoundException, TimeoutException, AdbCommandRejectedException, ScreenshotNullImageException, IOException {

        EasyAdb adb = new EasyAdb(true);
        BufferedImage image;
//        image = adb.getScreenshot();
//        ImageIO.write(image, "PNG", new File("test.png"));
        image = adb.getScreenshot(200, 132, 500, 500);
        ImageIO.write(image, "PNG", new File("test2.png"));


    }

    public static void main(String[] args) throws Exception {
        testScreenshot();
    }
}

