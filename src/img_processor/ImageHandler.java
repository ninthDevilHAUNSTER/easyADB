package img_processor;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;
import exception.ScreenshotNullImageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    /**
     * @param X      起始X坐标
     * @param Y      起始Y坐标
     * @param width  截图的宽度（X+width为终点X坐标）
     * @param height 截图的长度（Y+width为终点Y坐标
     * @return BufferedImage类，也就是保存在内存中的图片
     */
    public BufferedImage getScreenshot(BufferedImage image, int X, int Y, int width, int height) {
        //判断x、y方向是否超过图像最大范围
        if ((X + width) >= image.getWidth()) {
            width = image.getWidth() - X;
        }
        if ((Y + height) >= image.getHeight()) {
            height = image.getHeight() - Y;
        }
        BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x + X, y + Y);
                resultImage.setRGB(x, y, rgb);
            }
        }
        return resultImage;
    }

    public boolean compareImageWithPHash(BufferedImage img1, BufferedImage img2) {
        ImagePHash p = new ImagePHash();
        return p.imgCheck(img1, img2, 10);
    }

    private boolean compareImageWithSSMI(BufferedImage img1, BufferedImage img2) {
        // 网上没找到JAVA的代码，不会要我自己写吧
        return false;
    }

    private boolean compareImageWithColorHistogram(BufferedImage img1, BufferedImage img2) {
        ImageColorHistogram is = new ImageColorHistogram();
        double rate = is.colorHistogramSimilarity(img1, img2);
        return rate > 0.95;
    }

}
