package com.thunder.eye.utils;

/**
 * @author ly
 */
public class SnowflakeIdGenerator {

    // 机器标识位数（0~31位，共32位）
    private final long workerIdBits = 5L;

    // 数据中心标识位数（32~47位，共16位）
    private final long datacenterIdBits = 5L;

    // 上次生成ID的时间戳
    private long lastTimestamp = -1L;

    // 工作机器ID（0~31）
    private final long workerId;

    // 数据中心ID（0~31）
    private final long datacenterId;

    // 序列号（0~4095）
    private long sequence = 0L;

    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        // 工作机器ID最大值
        long maxWorkerId = ~(-1L << workerIdBits);
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        // 数据中心ID最大值
        long maxDatacenterId = ~(-1L << datacenterIdBits);
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，此时抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行序列号递增
        // 序列号位数（48~63位，共16位）
        long sequenceBits = 16L;
        if (lastTimestamp == timestamp) {
            // 序列号掩码
            long sequenceMask = ~(-1L << sequenceBits);
            sequence = (sequence + 1) & sequenceMask;
            // 如果序列号已达到最大值，则等待下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，序列号重置为0
            sequence = 0L;
        }

        // 更新上次生成ID的时间戳
        lastTimestamp = timestamp;

        // 组装64位的ID
        // 起始的时间戳（2021-05-31 00:00:00）
        long twepoch = 1622496000000L;
        // 工作机器ID偏左移位数
        // 数据中心ID偏左移位数
        long datacenterIdShift = sequenceBits + workerIdBits;
        // 时间戳偏左移位数
        long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << sequenceBits) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {

        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(2, 2);
        for (int i = 0; i <10 ; i++) {
            System.out.println(idGenerator.nextId());
        }
    }
}
