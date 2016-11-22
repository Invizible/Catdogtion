package com.github.invizible.catdogtion.util;

import lombok.NonNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtils {

  private static final String MONEY_FORMAT = "#0.##";

  private MoneyUtils() {
    //hide this guy
  }

  public static String format(@NonNull BigDecimal value) {
    return new DecimalFormat(MONEY_FORMAT).format(value);
  }
}
