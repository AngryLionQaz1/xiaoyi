/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: SX
 * Author:   萧毅
 * Date:     2019/2/25 17:26
 * Description:
 */
package com.snow.xiaoyi;


import java.util.concurrent.*;

public class SX {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task = new Task();
        Future<Integer> result = executor.submit(task);
        Task task1 = new Task();
        Future<Integer> result1 = executor.submit(task1);
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果"+result.get());
            System.out.println("task运行结果2"+result1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}
class Task implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算"+Thread.currentThread().getName());
        Thread.sleep(3000);
        int sum = 0;
        for(int i=0;i<100;i++)
            sum += i;
        return sum;
    }
}

