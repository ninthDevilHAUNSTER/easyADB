
import base.EasyAdb;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;
import exception.DeviceNotFoundException;
import exception.OcrEngineNotActiveException;
import exception.ScreenshotNullImageException;
import net.sourceforge.yamlbeans.YamlException;
import ocr_processor.BaiduOcr;
import ocr_processor.BaseOcr;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Test {
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());

    public static void main(String[] args) throws Exception {
        BaseOcr ocr = new BaiduOcr();
        InputStream is = new FileInputStream(new File("storage/ORIENT_TEST.png"));
        BufferedImage img = ImageIO.read(is);
        logger.info(ocr.orientText(img, "TW-8"));

    }
}

