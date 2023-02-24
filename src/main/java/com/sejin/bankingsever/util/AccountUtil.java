package com.sejin.bankingsever.util;

public class AccountUtil {

    public static String generateAccountNumber(Long userId) {
        final String defaultAccountNumber = "000000000";
        String paddedUserId = String.format("%09d", userId);

        return defaultAccountNumber.substring(0, defaultAccountNumber.length() - paddedUserId.length()) + paddedUserId;
    }

}
