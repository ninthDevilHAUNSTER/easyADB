
import base.EasyAdb;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class Main {
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());


    public static void main(String[] args) throws Exception {
        EasyAdb adb = new EasyAdb(true);

//        logger.info(adb.checkApplicationActive("shaobao"));
//        logger.info(adb.checkApplicationActive("com.hypergryph.arknights"));
        BufferedImage image = adb.getScreenshot();
        ImageIO.write(image, "PNG", new File("test.png"));
    }
}

