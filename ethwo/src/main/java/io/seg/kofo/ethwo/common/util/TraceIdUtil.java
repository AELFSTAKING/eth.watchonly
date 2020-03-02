package io.seg.kofo.ethwo.common.util;

import io.seg.stability.trace.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;

import java.util.Random;


@Slf4j
public class TraceIdUtil {
    private static String getRandomTring(int pwd_len) {
        final int maxNum = 10;
        int i;
        int count = 0;
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < pwd_len) {
            i = Math.abs(r.nextInt(maxNum));
            pwd.append(String.valueOf(i));
            count++;
        }
        return pwd.toString();
    }

    private static long getRandomNum16() {
        return Long.parseLong(getRandomTring(16));
    }

    public static void startTrace() {
        try {
            TraceUtil.openNewTracer(getRandomNum16());
        } catch (Throwable e) {
            log.error("CEXTraceUtil open error: {}", e.getMessage(),e);
        }
    }

    public static void endTrace() {
        try {
            TraceUtil.closeSpan(TraceUtil.getSpan());
        } catch (Throwable e) {
            log.error("CEXTraceUtil close error: {}", e.getMessage(),e);
        }
    }

    public static String getTraceIdString() {
        try {
            Span span = TraceUtil.getSpan();
            return span.traceIdString();
        } catch (Throwable e) {
            log.error("CEXTraceUtil getSpan error: {}", e.getMessage(),e);
            return getRandomTring(16);
        }
    }

}
