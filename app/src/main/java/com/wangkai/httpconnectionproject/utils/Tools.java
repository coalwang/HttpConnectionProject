package com.wangkai.httpconnectionproject.utils;

import java.math.BigDecimal;

public class Tools {
    private final static long SIZE_KB = 1024;
    private final static long SIZE_MB = SIZE_KB * 1024;
    private final static long SIZE_GB = SIZE_MB * 1024;
    private final static long SIZE_TB = SIZE_GB * 1024;

    private final static long SIZE_KB_X = 1000;
    private final static long SIZE_MB_X = SIZE_KB * 1000;
    private final static long SIZE_GB_X = SIZE_MB * 1000;
    private final static long SIZE_TB_X = SIZE_GB * 1000;

    public final static int SIZE_UINT_DEFAULT = -1;
    public final static int SIZE_UINT_B = 0;
    public final static int SIZE_UINT_KB = 1;
    public final static int SIZE_UINT_MB = 2;
    public final static int SIZE_UINT_GB = 3;
    public final static int SIZE_UINT_TB = 4;
    public final static int SIZE_AUTO = 5;


    public static String getSizeStr(long size, int num, int unit) {
        if (size <= 0) return "0B";
        StringBuffer sb = new StringBuffer("");
        final Float s = getSize(size, num, unit);
        if (unit == SIZE_UINT_B) {
            sb.append(s.intValue()).append("B");
        } else if (unit == SIZE_UINT_KB) {
            sb.append(s.intValue()).append("KB");
        } else if (unit == SIZE_UINT_MB) {
            sb.append(s).append("MB");
        } else if (unit == SIZE_UINT_GB) {
            sb.append(s).append("GB");
        } else if (unit == SIZE_UINT_TB) {
            sb.append(s).append("TB");
        } else if (unit == SIZE_AUTO) {
            if (size < SIZE_KB / 2) {
                sb.append(s.intValue()).append("B");
            } else if (size < SIZE_MB / 2) {
                sb.append(s).append("KB");
            } else {
                sb.append(s).append("MB");
            }
        } else {
            if (size < SIZE_KB_X) {
                sb.append(s.intValue()).append("B");
            } else if (size < SIZE_MB_X) {
                sb.append(s.intValue()).append("KB");
            } else if (size < SIZE_GB_X) {
                sb.append(s.intValue()).append("MB");
            } else if (size < SIZE_TB_X) {
                sb.append(s).append("GB");
            } else {
                sb.append(s).append("TB");
            }
        }

        return sb.toString();
    }

    public static String getSizeStrNoB(long size, int num, int unit) {
        StringBuffer sb = new StringBuffer(size < 0 ? "-" : "");
        final Float s = getSize(size, num, unit);

        if (unit == SIZE_UINT_B) {
            sb.append(s.intValue()).append("B");
        } else if (unit == SIZE_UINT_KB) {
            sb.append(s.intValue()).append("K");
        } else if (unit == SIZE_UINT_MB) {
            sb.append(s).append("M");
        } else if (unit == SIZE_UINT_GB) {
            sb.append(s).append("G");
        } else if (unit == SIZE_UINT_TB) {
            sb.append(s).append("T");
        } else if (unit == SIZE_AUTO) {
            if (size < SIZE_KB / 2) {
                sb.append(s.intValue()).append("B");
            } else if (size < SIZE_MB / 2) {
                sb.append(s).append("K");
            } else {
                sb.append(s).append("M");
            }
        } else {
            if (size < SIZE_KB_X) {
                sb.append(s.intValue()).append("B");
            } else if (size < SIZE_MB_X) {
                sb.append(s.intValue()).append("K");
            } else if (size < SIZE_GB_X) {
                sb.append(s).append("M");
            } else if (size < SIZE_TB_X) {
                sb.append(s).append("G");
            } else {
                sb.append(s).append("T");
            }
        }
        return sb.toString();
    }

    public static float getSize(long size, int num, int unit) {
        double s = size;
        if (unit == SIZE_UINT_B) {
            return size;
        } else if (unit == SIZE_UINT_KB) {
            return formatDoubleNum((s /= SIZE_KB), num);
        } else if (unit == SIZE_UINT_MB) {
            return formatDoubleNum((s /= SIZE_MB), num);
        } else if (unit == SIZE_UINT_GB) {
            return formatDoubleNum((s /= SIZE_GB), num);
        } else if (unit == SIZE_UINT_TB) {
            return formatDoubleNum((s /= SIZE_TB), num);
        } else if (unit == SIZE_AUTO) {
            if (size < SIZE_KB / 2) {
                return size;
            } else if (size < SIZE_MB / 2) {
                return formatDoubleNum((s /= SIZE_KB), num);
            } else {
                return formatDoubleNum((s /= SIZE_MB), num);
            }
        } else {
            if (size < SIZE_KB_X) {
                return size;
            } else if (size < SIZE_MB_X) {
                return formatDoubleNum((s /= SIZE_KB), num);
            } else if (size < SIZE_GB_X) {
                return formatDoubleNum((s /= SIZE_MB), num);
            } else if (size < SIZE_TB_X) {
                return formatDoubleNum((s /= SIZE_GB), num);
            } else {
                return formatDoubleNum((s /= SIZE_TB), num);
            }
        }
    }

    /**
     * 获取小数点位数
     *
     * @param d   原来数据
     * @param num 小数点位数
     * @return
     */
    public static Float formatDoubleNum(double d, int num) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(num, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}
