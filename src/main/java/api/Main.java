package api;

import api.CrptApi.*;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        CrptApi api = new CrptApi(TimeUnit.HOURS, 7);
        CrptApi api3 = new CrptApi(TimeUnit.SECONDS, 7);
        DocumentProductIntroRF documentProductIntroRF = new DocumentProductIntroRF(
                DocumentFormat.MANUAL,
                "String product_document",
                null,
                "@NonNull String signature",
                CrptApi.DocumentType.AGGREGATION_DOCUMENT,
                new Description(UUID.randomUUID().toString()),
                "@NonNull String doc_id",
                "@NonNull String doc_status",
                "@NonNull String doc_type",
        true,
        "@NonNull String owner_inn",
        "@NonNull String participant_inn",
        "@NonNull String producer_inn",
        LocalDate.now(),
        "@NonNull String production_type",
                new Product(null, null, null,
        "NonNull String owner_inn",
        "@NonNull String producer_in",
        LocalDate.now(),
        "@NonNull String tnved_code", null , null),
                LocalDate.now(), null);
        String signature = "JCYPPZXPHMHLSEDEGPPSWJWCDAUWGJ";
        Thread newThread = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread");
        Thread newThread1 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread1");
        Thread newThread2 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread2");
        Thread newThread3 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread3");
        Thread newThread4 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread4");
        Thread newThread5 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread5");
        Thread newThread6 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread6");
        Thread newThread7 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread7");
        Thread newThread8 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread8");
        Thread newThread9 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread9");
        Thread newThread10 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread10");
        Thread newThread11 = new Thread(() -> {
            try {
                api.requestToCreateProductIntroRFDocument(documentProductIntroRF, signature);
            } catch (IOException | LimitExceededException e) {
                e.printStackTrace();
            }
        }, "newThread11");

        newThread.start();
        newThread1.start();
        newThread2.start();
        newThread3.start();
        newThread4.start();
        newThread5.start();
        newThread6.start();
        newThread7.start();
        newThread8.start();
        newThread9.start();
        newThread10.start();
        newThread11.start();

        Thread.sleep(10_000);
        System.out.println("RequestCount  =   " + CrptApi.getRequestCount());
        System.out.println("RequestStartTime  =   " + CrptApi.getRequestStartTime());
        System.out.println("Token  =   " + CrptApi.getToken());
        System.out.println("LifetimeToken  =   " + CrptApi.getLifetimeToken());
//        try {
//            Thread newThread = new Thread(api::authorisation, "123");
//        } catch (Exception ex) {
//            System.out.println();
//        }
/*
        TimeUnit time = TimeUnit.SECONDS;
        long nanos = time.toNanos(1);
        System.out.println("nanos on second = " + nanos);
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime plusNanos = before.plusNanos(nanos);
        System.out.println("before = " + before);
        System.out.println("plusNanos = " + plusNanos);
        Thread.sleep(1500);
        System.out.println(plusNanos.isAfter(LocalDateTime.now()));
*/
        //System.out.println(localDateTime.minusNanos(LocalDateTime.now().to));
        //System.out.println(time.name());
//        ThreadGroup group = new ThreadGroup("группа A");
//
//        new ThreadAll(group, "Tок 1").start();
//        new ThreadAll(group, "Тик 2").start();
//        new ThreadAll(group, "Гор 3").start();
//        new ThreadAll(group, "Мор 4").start();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        group.interrupt();
    }
}
