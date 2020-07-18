package ocr_processor;

import exception.OcrEngineNotActiveException;
import exception.OcrEngineNotSupportException;
import net.sourceforge.yamlbeans.YamlException;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface BaseOcr {
    String getOcrName();

    String recognizeSingleText(BufferedImage img);

    String recognizeMultiText(BufferedImage img);

    int[] orientText(BufferedImage img, String text);


    boolean initOcr() throws OcrEngineNotSupportException;

    boolean isOcrActive() throws OcrEngineNotActiveException;

    void printOcrInfo();
}
