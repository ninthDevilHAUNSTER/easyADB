package util;

import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;

public class HelpFunction {

    public static String getPlatform() {
        String platform = System.getProperty("os.name");
        if (platform.contains("Win")) {
            return "win";
        } else if (platform.contains("Linux")) {
            return "linux";
        } else if (platform.contains("Mac")) {
            return "mac";
        } else {
            return "uk";
        }
    }

    /**
     * 图片转二进制数组
     *
     * @param bi      BufferedImage
     * @param imgType String
     * @return byte Image
     */
    public static byte @Nullable [] getBinaryImage(BufferedImage bi, String imgType) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, imgType, baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte @Nullable [] getBinaryImage(BufferedImage bi) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage getBufferedImage(String img) throws IOException {
        try {
            InputStream is = new FileInputStream(new File(img));
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean copyConfigYaml() {
        InputStream in = null;
        OutputStream out = null;
        File config_yaml = new File(Paths.get("config", "config.yaml").toString());
        if (config_yaml.exists()) {
            return false;
        } else {
            try {
                in = new FileInputStream(new File(Paths.get("config", "config-template.yaml").toString()));
                out = new FileOutputStream(config_yaml);
                byte[] buffer = new byte[10240];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
