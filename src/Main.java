
import base.EasyAdb;
import org.apache.log4j.Logger;


public class Main {
    public static Logger logger = Logger.getLogger(EasyAdb.class.getName());



    public static void main(String[] args) throws Exception {
        EasyAdb adb = new EasyAdb(true);
//        logger.info(adb.checkApplicationActive("shaobao"));
//        logger.info(adb.checkApplicationActive("com.hypergryph.arknights"));

    }
}

