package com.svili.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 异步线程池
 * 
 * @author svili
 * @since 2016-09-12
 */
public final class AsyncPool {

	/** 异步定时任务线程池 */
	private static final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(5);

	/** 异步线程池 */
	private static final ExecutorService executorService = Executors.newFixedThreadPool(30);

	public static <T> Future<T> submit(Callable<T> task) {
		return executorService.submit(task);
	}

	public static Future<?> submit(Runnable task) {
		return executorService.submit(task);
	}

	public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return scheduledService.schedule(command, delay, unit);
	}

	public static <T> ScheduledFuture<T> schedule(Callable<T> callable, long delay, TimeUnit unit) {
		return scheduledService.schedule(callable, delay, unit);
	}

	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period,
			TimeUnit unit) {
		return scheduledService.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,
			TimeUnit unit) {
		return scheduledService.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	public static void close() {
		scheduledService.shutdown();
		executorService.shutdown();

		try {
			if (!scheduledService.awaitTermination(30, TimeUnit.SECONDS)) {
				scheduledService.shutdownNow();
			}

			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (Exception e) {
			scheduledService.shutdownNow();
			executorService.shutdownNow();

			LogUtil.error("Close AsyncPool error.", e);
		}
	}
}
