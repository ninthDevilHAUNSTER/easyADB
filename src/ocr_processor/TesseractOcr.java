package ocr_processor;

import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.*;

public class TesseractOcr implements BaseOcr {
    public static Logger logger = Logger.getLogger(TesseractOcr.class.getName());
    private final String EOL = System.getProperty("line.separator");
    private final String tessPath = "C://Program Files//Tesseract-OCR";

    public static String consumeInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            System.out.println(s);
            sb.append(s);
        }
        return sb.toString();
    }

    private String preprocessCmdCommand(BufferedImage img) throws IOException, InterruptedException {
        String cmd = "D:\\Program Files\\Tesseract-OCR\\tesseract.exe D:\\java_box\\easyADB\\storage\\TEST.png 1.txt -l chi_sim";
        ProcessBuilder pb = new ProcessBuilder(cmd);
//        pb.redirectError(new File("stderr.txt"));
//        pb.redirectOutput(new File("stdout.txt"));
        try {
            Process p = pb.start();
            // Write to the standard input stream
//            OutputStream stdin = p.getOutputStream();
//            stdin.write(HelpFunction.getImageBinary(img, "png"));
            InputStream stdout = p.getInputStream();
            consumeInputStream(stdout);

            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)
                    System.err.println("fail!");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String recognizeSingleText(BufferedImage img) throws IOException, InterruptedException {
        preprocessCmdCommand(img);
        return null;
    }

    @Override
    public String recognizeMultiText(BufferedImage img) {
        return null;
    }

    @Override
    public int[] orientText(BufferedImage img, String text) {
        int[] result = new int[]{0, 0, 0, 0};
        return result;
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
