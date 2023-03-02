package com.wko.rabbitmq.util;

/**
 * ClassName: SleepUtils
 * Package: com.wko.rabbitmq.util
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 23:01
 * @Version 1.0
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000L * second);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
