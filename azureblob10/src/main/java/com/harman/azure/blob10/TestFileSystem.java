package com.harman.azure.blob10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFileSystem {
    private static int BUFF_SIZE;

    public static void main(String[] args) throws IOException {
        System.out.println("starting");
//        BUFF_SIZE = Integer.valueOf(args[0]);
        BUFF_SIZE = 1024;
        AtomicInteger c = new AtomicInteger();
        AtomicInteger batchId = new AtomicInteger();
        Runnable runnable = () -> {
            try {
                runNio(c.getAndIncrement(), batchId.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        while (true) {
            batchId.getAndIncrement();
            ExecutorService executor = Executors.newFixedThreadPool(200);
            for (int i = 0; i < 5; i++) {
                executor.submit(runnable);
                //executor.shutdown();
                //new Thread(runnable).start();
            }
            executor.shutdown();
            try {
                executor.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        //  }
    }


    private static void runNio(int count, int batchId) throws IOException, InterruptedException {


        //  Stopwatch stopwatch = Stopwatch.createStarted();

        long startTime = System.currentTimeMillis();
        try (
                InputStream in1 = Files.newInputStream(Paths.get("c:/DELETE/temp/pgadmin4-4.2-x86.exe"));
                OutputStream out1 = Files.newOutputStream(Paths.get("c:/DELETE/temp/out-files/out-" + count));) {
            byte[] buffer = new byte[BUFF_SIZE];
            int len;
            while ((len = in1.read(buffer)) >= 0) {
                out1.write(buffer, 0, len);
                out1.flush();
            }
            long stopTime = System.currentTimeMillis();
            System.out.println(batchId + ":" + count + ":" + (stopTime - startTime));
            //stopwatch.stop();
            // System.out.println("Finished write [" + stopwatch.elapsed(TimeUnit.SECONDS) + " count " + count + "] seconds");
        }
    }



}
