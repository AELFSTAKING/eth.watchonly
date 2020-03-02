package io.seg.kofo.ethwo.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MonitorLogUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger("eth-wo-monitor-info");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("eth-wo-monitor-error");

    public static final String MODULE = "eth-wo";

    /**
     * 监控日志的输出(收集时间间隔都统一为一分钟收集一次)
     *
     * @param target 指标名
     * @param type   指标的数据类型,可以是以下类型
     *               --text: 文本类型
     *               --float : 浮点类型
     *               --int : 整数类型
     *               <p>
     * @param <T>    example：
     *               如果需要统计1分钟内用户注册的数量,那么在每次用户注册成功后输出一条监控日志:
     *               MonitorLogUtils.logMonitorInfo("member_create", TargetInfoType.INT,1);
     */
    public static <T> void logMonitorInfo(String target, TargetInfoType type, T info) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date());
        LOGGER.info("Monitor,{},{},{},{},{}", MODULE, target, date, type, String.valueOf(info));
    }

    /**
     * 输出异常和错误信息
     *
     * @param keyWord          关键字
     * @param format,arguments 参数传入同普通日志打印
     */

    public static void logMonitorError(String keyWord, String format, Object... arguments) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date());
        LOGGER_ERROR.error(String.format("Monitor,%s,%s,%s", MODULE, date, keyWord));
        LOGGER_ERROR.error(format, arguments);
    }

    public enum TargetInfoType {
        /**
         * text
         */
        TEXT("text"),
        /**
         * float
         */
        FLOAT("float"),
        /**
         * int
         */
        INT("int"),
        /**
         * tps
         */
        TPS("tps");

        String type;

        TargetInfoType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}

