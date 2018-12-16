/*

 */
package cosmosquestsolver;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Base64;

//miscelanius functions
public class OtherThings {
    
    private static DecimalFormat intCommaFormat = new DecimalFormat("###,###,###,###");
    private static DecimalFormat twoDecimalFormat = new DecimalFormat("##.00");
    
    public static double fact(int num){
        double ans = 1;
        for (int i = 2; i <= num; i++){
            ans *= i;
        }
        return ans;
    }
    
    public static long nCr(int n, int r){
        long num = 1;
        for (int i = n; i >= n - r + 1; i --){
            num *= i;
        }
        return num/(long)fact(r);
    }

    public static String intToCommaString(long damage) {
        return intCommaFormat.format(damage);
    }
    
    public static String twoDecimalFormat(double num) {
        return twoDecimalFormat.format(num);
    }
    
    public static String encodeBase64(String str) throws UnsupportedEncodingException{//error handling?
        byte[] bytes = str.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static String decodeBase64(String code){
        byte[] decoded = Base64.getDecoder().decode(code);
        return new String(decoded);
    }
    
}
