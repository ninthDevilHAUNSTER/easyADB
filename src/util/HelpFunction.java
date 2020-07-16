package util;

import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    public static byte @Nullable [] getImageBinary(BufferedImage bi, String imgType) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, imgType, baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
