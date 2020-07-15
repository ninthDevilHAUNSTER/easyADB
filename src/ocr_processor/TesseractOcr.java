package ocr_processor;

import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.util.OS;

import javax.imageio.ImageIO;

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

        List<String> lcommand = new ArrayList<String>();
//        lcommand.add(Paths.get(tessPath,"tesseract").toString());
//        lcommand.add(Paths.get("storage", "TEST.png").toString());
//        lcommand.add("3");
//        lcommand.add("-l");
//        lcommand.add("chi_sim");
//        lcommand.add("--psm");
//        lcommand.add("12");
//        lcommand.add("dir.exe");
        ProcessBuilder pb = new ProcessBuilder("cmd.exe /c tesseract");
        System.out.println(lcommand);
        pb.redirectErrorStream(true);
        pb.directory(new File(System.getProperty("user.dir")));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        // 标准输入流（必须写在 waitFor 之前）
        String inStr = consumeInputStream(process.getInputStream());
        // 标准错误流（必须写在 waitFor 之前）
        String errStr = consumeInputStream(process.getErrorStream());
        int w = process.waitFor();
//        if (w == 0)// 0代表正常退出
//        {
//            byte[] b = new byte[1024];
//            int readbytes = -1;
//            StringBuffer sb = new StringBuffer();
//            try (InputStream in = process.getInputStream()) {
//                while ((readbytes = in.read(b)) != -1) {
//                    sb.append(new String(b, 0, readbytes));
//                }
//                in.close();
//                logger.info(sb.toString());
//            } catch (IOException e2) {
//                e2.printStackTrace();
//            }
//        } else {
//            String msg = switch (w) {
//                case 1 -> "Errors accessing files. There may be spaces in your image's filename.";
//                case 29 -> "Cannot recognize the image or its selected region.";
//                case 31 -> "Unsupported image format.";
//                default -> "Errors occurred.";
//            };
//            throw new RuntimeException(msg);
//        }
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
