package org.aery.study.jdk10;

import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.Locale;

public class JEP314_Additional_Unicode_Language_Tag_Extensions {

    @Test
    void name() {
        // 使用 BCP 47 語言標籤來設置 Locale
        Locale locale = Locale.forLanguageTag("zh-Hant-TW");

        // 顯示 Locale 的相關資訊
        System.out.println("語言: " + locale.getDisplayLanguage());
        System.out.println("地區: " + locale.getDisplayCountry());

        // 使用 Locale 設置貨幣格式
        Currency currency = Currency.getInstance(locale);
        System.out.println("貨幣: " + currency.getCurrencyCode());

        // 設置每週的第一天
        Locale localeWithFirstDay = Locale.forLanguageTag("zh-Hant-TW-u-fw-kerker");
        System.out.println("每週的第一天: " + localeWithFirstDay.getUnicodeLocaleType("fw"));
    }

}
