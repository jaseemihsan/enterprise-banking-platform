package com.bank.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class ReferenceGenerator {

    private ReferenceGenerator() {
    }

    public static String generateReference() {

        String date =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        int random =
                ThreadLocalRandom.current()
                        .nextInt(1000,9999);

        return "TXN" + date + random;

    }

}
