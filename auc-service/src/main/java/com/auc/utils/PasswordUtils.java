package com.auc.utils;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 智慧办公的密码是shiro加密模式，暂时保持兼容
 * @author zhangqi
 */
public class PasswordUtils {

  /**
   * shiro的密码加密算法
   * @param source
   * @param salt
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static String getMd5Hash(String source, String salt) throws NoSuchAlgorithmException {
    return new String(Hex.encode(hash(Utf8.encode(source), Utf8.encode(salt), 1)));
  }

  protected static byte[] hash(byte[] bytes, byte[] salt, int hashIterations)
      throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    if (salt != null) {
      digest.reset();
      digest.update(salt);
    }

    byte[] hashed = digest.digest(bytes);
    int iterations = hashIterations - 1;

    for (int i = 0; i < iterations; ++i) {
      digest.reset();
      hashed = digest.digest(hashed);
    }

    return hashed;
  }
}
