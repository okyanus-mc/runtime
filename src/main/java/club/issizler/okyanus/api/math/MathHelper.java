// Source: net.minecraft.util.math.MathHelper
// Reduced for functions we need

package club.issizler.okyanus.api.math;

import net.minecraft.util.SystemUtil;

public class MathHelper {

    private static final float[] SINE_TABLE = SystemUtil.consume(new float[65536], (floats_1) -> {
        for (int int_1 = 0; int_1 < floats_1.length; ++int_1) {
            floats_1[int_1] = (float) Math.sin((double) int_1 * 3.141592653589793D * 2.0D / 65536.0D);
        }
    });

    public static float sqrt(double double_1) {
        return (float) Math.sqrt(double_1);
    }

    public static float sin(float float_1) {
        return SINE_TABLE[(int) (float_1 * 10430.378F) & '\uffff'];
    }

    public static float cos(float float_1) {
        return SINE_TABLE[(int) (float_1 * 10430.378F + 16384.0F) & '\uffff'];
    }

}
