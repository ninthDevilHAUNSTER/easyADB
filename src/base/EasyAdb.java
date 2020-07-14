package base;

import com.android.ddmlib.*;
import exception.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

public class EasyAdb {
//    variables

    //    public
    public IDevice device;
    public static EasyAdbLogger logger = new EasyAdbLogger();

    //    protected
    protected int DeviceId = 0;
    //    private
    private StringBuffer buffer;
    private CollectingOutputReceiver receiver = new CollectingOutputReceiver();


    public EasyAdb(boolean do_init) throws DeviceNotFoundException {
        // 初始化adb
        if (do_init) {
            createDevice();
        }
    }

    public void executeCommand(String command) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        EasyAdbLogger.logger.debug(command);
        this.device.executeShellCommand(command, this.receiver);
        EasyAdbLogger.logger.debug(this.receiver.getOutput());
    }

    /**
     * @param X X坐标
     * @param Y Y坐标
     */
    public void simulateTap(int X, int Y) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        executeCommand("input tap " + String.valueOf(X) + " " + String.valueOf(Y));
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
        executeCommand("input tap " + String.valueOf(X + _biasX) + " " + String.valueOf(Y + _biasY));
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


    private void createDevice() throws DeviceNotFoundException {
        AndroidDebugBridge.init(false);
        AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(
                "adb", false);
        waitForDevice(bridge);
        IDevice[] devices = bridge.getDevices();
        if (devices.length == 0) {
            // no device
            EasyAdbLogger.print("检测到 0 台 连接设备，初始化失败", 2, 4);
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
                EasyAdbLogger.print("发现多个设备，自动连接失败。请输入编号：,输入0退出当前程序");
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
                        EasyAdbLogger.print("输入不正确！");
                    }
                } else {
                    // 输入错误的信息
                    EasyAdbLogger.print("输入不正确！");
                }
            }
        }
        EasyAdbLogger.logger.info("设备连接成功，当前设备名称\t" + this.device.getName());
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
                EasyAdbLogger.logger.error("Time out");
                break;
            }
        }
    }
}