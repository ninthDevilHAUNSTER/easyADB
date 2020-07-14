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
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());

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
        this.device.executeShellCommand(command, this.receiver);
        System.out.println(this.receiver.getOutput());
    }



    private void createDevice() throws DeviceNotFoundException {
        AndroidDebugBridge.init(false);
        AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(
                "adb", false);
        waitForDevice(bridge);
        IDevice[] devices = bridge.getDevices();
        if (devices.length == 0) {
            // no device
            throw new DeviceNotFoundException("检测到 0 台 连接设备，初始化失败");
        }
        else if (devices.length == 1) {
            // one device
            this.device = devices[0];
        }
        else {
            // muti device
            for (int i = 0; i < devices.length; i++) {
                System.out.println("[ " + (i + 1) + " ]" + devices[i].getName());
            }
            Scanner scan = new Scanner(System.in);
            // 从键盘接收数据
            int i = 0;
            boolean FlAG = false;
            while (!FlAG) {
                System.out.print("发现多个设备，自动连接失败。请输入编号：,输入0退出当前程序");
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
        System.out.println("设备连接成功，当前设备名称\t" + this.device.getName());
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