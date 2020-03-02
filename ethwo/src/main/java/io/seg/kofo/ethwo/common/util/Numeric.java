package io.seg.kofo.ethwo.common.util;

import org.web3j.exceptions.MessageDecodingException;

import java.math.BigInteger;
import java.util.Arrays;

import static org.web3j.utils.Numeric.cleanHexPrefix;

/**
 * @author WalkerLiu
 * @date 2018/10/17
 */
public class Numeric {
    public static BigInteger decodeQuantity(String value) {
        if (!isValidHexQuantity(value)) {
            return new BigInteger(value);
        }
        try {
            return new BigInteger(value.substring(2), 16);
        } catch (NumberFormatException e) {
            throw new MessageDecodingException("Negative ", e);
        }
    }

    private static boolean isValidHexQuantity(String value) {
        if (value == null) {
            return false;
        }

        if (value.length() < 3) {
            return false;
        }

        if (!(value.startsWith("0x") || value.startsWith("0X")) ) {
            return false;
        }

        // If TestRpc resolves the following issue, we can reinstate this code
        // https://github.com/ethereumjs/testrpc/issues/220
        // if (value.length() > 3 && value.charAt(2) == '0') {
        //    return false;
        // }

        return true;
    }

    public static BigInteger toBigInt(byte[] value, int offset, int length) {
        return toBigInt((Arrays.copyOfRange(value, offset, offset + length)));
    }

    public static BigInteger toBigInt(byte[] value) {
        return new BigInteger(1, value);
    }

    public static BigInteger toBigInt(String hexValue) {
        String cleanValue = cleanHexPrefix(hexValue);
        return toBigIntNoPrefix(cleanValue);
    }

    public static BigInteger toBigIntNoPrefix(String hexValue) {
        return new BigInteger(hexValue, 16);
    }
}
