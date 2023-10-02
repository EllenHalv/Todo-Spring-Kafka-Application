package com.ellencodes.gui;

import org.apache.commons.text.StringEscapeUtils;


public class ConvertHtmlToString {
    public static String convertHtmlToPlainText(String html) {
        // Unescape HTML entities
        String unescapedHtml = StringEscapeUtils.unescapeHtml4(html);

        // Remove HTML tags using regex
        String plainText = unescapedHtml.replaceAll("<[^>]*>", "");

        return plainText.trim();
    }
}
