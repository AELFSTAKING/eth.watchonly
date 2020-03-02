package io.seg.kofo.ethwo.common.enums;

import lombok.Getter;

/**
 * 监控指标枚举
 */
@Getter
public enum MonitorTarget {
    /**
     * 监控会每分钟读取一次日志数据
     * <p>
     * 各个数据说明
     * "target_name"  ----->  监控指标的名字
     * <p>
     * "method" ----->  监控方法，可以取的值有last,avg,sum,min,keyword,text
     * last: 执行一次日志读取时，获取指标的最新一条日志值
     * avg：执行一次日志读取，获取指标(可能有好几条日志产生)的平均值
     * sum：执行一次日志读取，获取指标(可能有好几条日志产生)的加权总和
     * min：执行一次日志读取，获取指标(可能有好几条日志产生)其中最小的一个值
     * keyword：执行一次日志读取，返回关键字在日志出现的次数
     * text：执行一次日志读取，直接返回文本值。
     * <p>
     * "compare" 和  "threshold" 共同构成报警阈值
     * "compare"支持>=, > ,< ,<=,!=,text
     * 例如 >= 和 1  ----->  日志值大于等于1就报警
     * 不需要报警时 "compare" 传空字符串，"threshold"传null
     */


    // 检查sender账户余额是否大于feeLimit
    CHECK_BALANCE("CHECK_BALANCE", "keyword", ">=", 1L),
    ;

    private String targetName;
    private String method;
    private String compare;
    private Long threshold;

    MonitorTarget(String targetName, String method, String compare, Long threshold) {
        this.targetName = targetName;
        this.method = method;
        this.compare = compare;
        this.threshold = threshold;
    }
}
