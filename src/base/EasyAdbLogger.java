package base;

import org.apache.log4j.Logger;

import java.util.Map;
/**
 * 还想封装一下的，这个log和Python的有点不太一样么。。。
 * 根本就不需要封装，不然函数位置可能会报的不对劲？
 */
public class EasyAdbLogger {
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());


    /**
     * @param buffer       content to output
     * @param mode         0: system.out 1: to logger 2(default):
     * @param logger_level 1:DEBUG 2:INFO 3:WARN 4:ERROR 5:FATAL
     *                     DEBUG：输出调试信息；指出细粒度信息事件对调试应用程序是非常有帮助的。
     *                     INFO： 输出提示信息；消息在粗粒度级别上突出强调应用程序的运行过程。
     *                     WARN： 输出警告信息；表明会出现潜在错误的情形。
     *                     ERROR：输出错误信息；指出虽然发生错误事件，但仍然不影响系统的继续运行。
     *                     FATAL： 输出致命错误；指出每个严重的错误事件将会导致应用程序的退出。
     */
    public static void print(String buffer, int mode, int logger_level) {
        assert mode >= 0 && mode <= 2;
        assert logger_level >= 1 && logger_level <= 5;
        if (mode == 0) {
            System.out.println(buffer);
        } else if (mode == 1) {
            handle_logger(buffer, logger_level);
        } else {
            System.out.println(buffer);
            handle_logger(buffer, logger_level);
        }
    }


    public static void print(String buffer) {
        handle_logger(buffer, 1);
    }

    public static void print(String buffer, int logger_level) {
            handle_logger(buffer, logger_level);
    }

    private static void handle_logger(String buffer, int logger_level) {
        switch (logger_level) {
            case 1 -> EasyAdbLogger.logger.debug(buffer);
            case 2 -> EasyAdbLogger.logger.info(buffer);
            case 3 -> EasyAdbLogger.logger.warn(buffer);
            case 4 -> EasyAdbLogger.logger.error(buffer);
            case 5 -> EasyAdbLogger.logger.fatal(buffer);
        }
    }
}
