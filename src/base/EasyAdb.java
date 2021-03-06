package base;

import com.android.ddmlib.*;
import exception.*;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;
import ocr_processor.BaiduOcr;
import ocr_processor.BaseOcr;
import ocr_processor.TesseractOcr;
import org.jdesktop.swingx.util.OS;
import util.HelpFunction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class EasyAdb {
//    variables

    //    public
    public IDevice device;
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());

    private final CollectingOutputReceiver receiver = new CollectingOutputReceiver();

    public String getDeiceName() {
        assert device != null;
        return this.device.getName();
    }

    private String getAdbLocation() throws FileNotFoundException {
        try {
            YamlReader reader = new YamlReader(new FileReader(Paths.get("config", "config.yaml").toString()));
            Map ocr_config = reader.read(Map.class);
            Map ocr_info = (Map) ocr_config.get("device");
            File _path = new File((String) ocr_info.get("adb_path"));
            if (_path.exists()) {
                logger.info(String.format("adb_path 确认，采用config ADB中配置 %s", ocr_info.get("adb_path")));
                return (String) ocr_info.get("adb_path");
            } else {
                Path adb_path;
                if (OS.isWindows()) {
                    adb_path = Paths.get("ADB", "win", "adb.exe").toAbsolutePath();
                } else if (OS.isLinux()) {
                    throw new FileNotFoundException("adb指令未安装，请输入类似 'apt-get install adb '的指令安装");
//                adb_path = Paths.get("ADB", "linux").toAbsolutePath();
                } else if (OS.isMacOSX()) {
                    throw new FileNotFoundException("adb指令未安装，请输入类似 'brew cask install android-platform-tools' 的指令安装");
//                adb_path = Paths.get("ADB", "mac").toAbsolutePath();
                } else {
                    adb_path = Paths.get("ADB", "win", "adb.exe").toAbsolutePath();
                }
                logger.info(String.format("adb_path 确认，采用 插件自带 ADB 配置 %s", adb_path.toString()));
                return adb_path.toString();
            }
        } catch (YamlException e) {
            e.printStackTrace();
            return null;
        }
    }


    public EasyAdb() throws DeviceNotFoundException {
        try {
            // 复制config.yaml
            HelpFunction.copyConfigYaml();
            // 初始化adb
            initOcr();
            initDevice();
        } catch (YamlException | IOException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(String command) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        logger.debug(command);
        this.device.executeShellCommand(command, this.receiver);
        logger.debug(this.receiver.getOutput());
    }

    /**
     * @param X X坐标
     * @param Y Y坐标
     */
    public void simulateTap(int X, int Y) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        executeCommand(String.format("input tap %d %d", X, Y));
    }

    /**
     * @param X     X坐标
     * @param Y     Y坐标
     * @param biasX X的随机偏移，将返回 [X-bias,X+bias] 范围内的随机整数
     * @param biasY Y的随机偏移，将返回 [Y-bias,Y+bias] 范围内的随机整数
     */
    public void simulateTap(int X, int Y, int biasX, int biasY) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        int _biasX = (int) (Math.random() * biasX * 2) - biasX;
        int _biasY = (int) (Math.random() * biasY * 2) - biasY;
        executeCommand(String.format("input tap %d %d", X + _biasX, Y + _biasY));
    }

    /**
     * 模拟鼠标滑动
     * adb shell input swipe X1 Y1 X2 Y2
     *
     * @param X1 起始点X坐标
     * @param Y1 起始点Y坐标
     * @param X2 终点X坐标
     * @param Y2 终点Y坐标
     */
    public void simulateSwipe(int X1, int Y1, int X2, int Y2) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        executeCommand(String.format("input swipe %d %d %d %d", X1, Y1, X2, Y2));
    }

    public void closeApplication(String application_name) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        executeCommand(String.format("am force-stop %s", application_name));
    }

    public void openApplication(String application_name) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        executeCommand(String.format("am start -n %s", application_name));
    }

    public boolean checkApplicationActive(String application_name) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
//        dumpsys window windows | findstr \"Current\"
//        com.hypergryph.arknights/com.u8.sdk.U8UnityContext
        this.device.executeShellCommand("dumpsys window windows | grep \"Current\"", this.receiver);
        String output = this.receiver.getOutput();
        return output.contains(application_name);
    }

    public void restartApplication(String application_name) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        closeApplication(application_name);
        openApplication(application_name);
    }

    /**
     * @param X      起始X坐标
     * @param Y      起始Y坐标
     * @param width  截图的宽度（X+width为终点X坐标）
     * @param height 截图的长度（Y+width为终点Y坐标
     * @return BufferedImage类，也就是保存在内存中的图片
     */
    public BufferedImage getScreenshot(int X, int Y, int width, int height) throws AdbCommandRejectedException, IOException, TimeoutException, ScreenshotNullImageException {
        BufferedImage image = takeScreenshot();
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

    public BufferedImage getScreenshot() throws AdbCommandRejectedException, IOException, TimeoutException, ScreenshotNullImageException {
        return takeScreenshot();
    }

    private BufferedImage takeScreenshot() throws AdbCommandRejectedException, IOException, TimeoutException, ScreenshotNullImageException {
        // 这个RAW IMAGE 类真的看不懂它在干啥，之后能修改可以修改一下
        RawImage rawScreen = device.getScreenshot();
        if (rawScreen != null) {
            int width = rawScreen.width;
            int height = rawScreen.height;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int index = 0;
            int indexInc = rawScreen.bpp >> 3;
            for (int y = 0; y < rawScreen.height; y++) {
                for (int x = 0; x < rawScreen.width; x++, index += indexInc) {
                    int value = rawScreen.getARGB(index);
                    image.setRGB(x, y, value);
                }
            }
            return image;
        } else {
            throw new ScreenshotNullImageException();
        }
    }

    private void initOcr() {
        try {
            YamlReader reader = new YamlReader(new FileReader(Paths.get("config", "config.yaml").toString()));
            Map ocr_config = reader.read(Map.class);
            Map ocr_engine = (Map) ocr_config.get("ocr");
            //    private
            BaseOcr ocr;
            if (ocr_engine.get("engine") == "baidu") {
                ocr = new BaiduOcr();
            } else if (ocr_config.get("engine") == "tesseract") {
                ocr = new TesseractOcr();
            } else {
                ocr = new TesseractOcr();
            }
            ocr.isOcrActive();
        } catch (OcrEngineNotSupportException | OcrEngineNotActiveException | FileNotFoundException | YamlException e) {
            e.printStackTrace();
        }
    }

    private void initDevice() throws DeviceNotFoundException, FileNotFoundException, YamlException {
        String adb_location = getAdbLocation();
        AndroidDebugBridge.init(false);
        AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(
                adb_location, false);
        waitForDevice(bridge);
        IDevice[] devices = bridge.getDevices();
        if (devices.length == 0) {
            // no device
            logger.fatal("检测到 0 台 连接设备，初始化失败");
            throw new DeviceNotFoundException("检测到 0 台 连接设备，初始化失败");
        } else if (devices.length == 1) {
            // one device
            this.device = devices[0];
        } else {
            // muti device
            for (int i = 0; i < devices.length; i++) {
                System.out.println("[ " + (i + 1) + " ]" + devices[i].getName());
            }
            Scanner scan = new Scanner(System.in);
            // 从键盘接收数据
            int i = 0;
            boolean FlAG = false;
            while (!FlAG) {
                System.out.println("发现多个设备，自动连接失败。请输入编号：,输入0退出当前程序");
                if (scan.hasNextInt()) {
                    // 判断输入的是否是整数
                    i = scan.nextInt();
                    if (i >= 1 && i <= devices.length) {
                        // 接收整数
                        System.out.println("输入编号：" + i);
                        this.device = devices[i - 1];
                        FlAG = true;
                    } else if (i == 0) {
                        System.exit(0);
                    } else {
                        System.out.println("输入不正确！");
                    }
                } else {
                    // 输入错误的信息
                    System.out.println("输入不正确！");
                }
            }
        }
        logger.info("设备连接成功，当前设备名称\t" + this.device.getName());
    }

    private static void waitForDevice(AndroidDebugBridge bridge) {
        int count = 0;
        while (!bridge.hasInitialDeviceList()) {
            try {
                Thread.sleep(100);
                count++;
            } catch (InterruptedException ignored) {
            }
            if (count > 300) {
                logger.error("Time out");
                break;
            }
        }
    }

}