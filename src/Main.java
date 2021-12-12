
import base.EasyAdb;
import exception.OcrEngineNotSupportException;
import ocr_processor.BaiduOcr;
import ocr_processor.BaseOcr;
import ocr_processor.TesseractOcr;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;


public class Main {
    public static void processImg() throws IOException, OcrEngineNotSupportException, InterruptedException {
        BaseOcr ocr = new BaiduOcr();
        String dirStr = Paths.get("C:\\Users\\shaob\\Downloads\\霓虹深渊图鉴8.0\\word\\media").toString();
        if (dirStr != null) {
            File dir = new File(dirStr);
            if (dir.isDirectory()) {
                File[] dirs = dir.listFiles();
                assert dirs != null;
                for (File file : dirs) {
                    InputStream is = new FileInputStream(new File(String.valueOf(file)));
                    BufferedImage img = ImageIO.read(is);
                    String res = ocr.recognizeMultiText(img, "\t");
                    System.out.println(res);
                    Thread.sleep(1000);
                }
            }
        }
    }

    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());


    public static void main(String[] args) throws Exception {
        processImg();
    }
}

