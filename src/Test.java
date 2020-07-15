
import base.EasyAdb;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;
import com.baidu.aip.ocr.AipOcr;
import exception.DeviceNotFoundException;
import exception.OcrEngineNotActiveException;
import exception.ScreenshotNullImageException;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;
import ocr_processor.BaiduOcr;
import ocr_processor.BaseOcr;
import ocr_processor.TesseractOcr;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.jdesktop.swingx.util.OS;

public class Test {
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());

    private static void testOrientFunction() throws Exception {
        BaseOcr ocr = new TesseractOcr();
        InputStream is = new FileInputStream(new File("storage/ORIENT_TEST.png"));
        BufferedImage img = ImageIO.read(is);
        logger.info(ocr.recognizeSingleText(img));
    }

    private static void testAdbConnection() throws Exception {
        EasyAdb adb = new EasyAdb(true);
        adb.getScreenshot(132, 312, 231, 312);
    }


    public static void main(String[] args) throws Exception {
        testOrientFunction();
    }
}

