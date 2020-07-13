import com.android.ddmlib.*;

import java.util.logging.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class EasyADB {
//    variables

    //    public
    public IDevice device;
    public static Logger logger = Logger.getLogger("EasyADB");
//SEVERE	严重
//WARNING	警告
//INFO	信息
//CONFIG	配置
//FINE	良好
//FINER	较好
//FINEST	最好
//ALL	开启所有级别日志记录
//OFF	关闭所有级别日志记录

    //    protected
    protected int DeviceId = 0;
    //    private
    private StringBuffer buffer;
    private CollectingOutputReceiver receiver = new CollectingOutputReceiver();

//    private static final String startActivity = "am start -n com.qkmoc.moc/com.qkmoc.moc.view.MainActivity";

//    functions

    public EasyADB(boolean do_init) {
        // 初始化adb
        if (do_init) {
            this.device = createDevice();
        }
    }

    public void executeCommand(String command) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        this.device.executeShellCommand(command, this.receiver);
        System.out.println(this.receiver.getOutput());
    }

    public void simulateTap(String command, int X, int Y) throws Exception {
//        TODO : ADD RANDOM METHOD
        this.device.executeShellCommand(
                command, this.receiver
        );
    }

    private static void resetLoggerInfo() {
        logger.setLevel(Level.ALL);

    }

    private static IDevice createDevice() {
//        TODO : MORE COMPLEX SITUATION
        IDevice device;
        AndroidDebugBridge.init(false);
        AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(
                "adb", false);
        waitForDevice(bridge);
        IDevice[] devices = bridge.getDevices();
        device = devices[0];
        return device;
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
                System.err.print("Time out");
                break;
            }
        }
    }
}