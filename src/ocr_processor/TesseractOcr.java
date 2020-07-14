package ocr_processor;

import base.EasyAdb;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;

public class TesseractOcr implements BaseOcr {
    public static Logger logger = Logger.getLogger(TesseractOcr.class.getName());

    @Override
    public void recognizeText(BufferedImage img) {

    }

    @Override
    public boolean initOcr() {
        return false;
    }

    @Override
    public boolean isOcrActive() {
        return false;
    }

    @Override
    public void printOcrInfo() {
        logger.info("Tesseract Ocr");
    }

}
