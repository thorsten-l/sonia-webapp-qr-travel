/*
 * Copyright 2022 Thorsten Ludewig (t.ludewig@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sonia.webapp.qrtravel;
 
import sonia.commons.crypt.cipher.AesSimpleCipher;
 
/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public final class Cipher 
{
   
  /** secret key */
  private static final char[] KEY = Config.getInstance().getCipherKey().toCharArray();
  
  /** cipher */
  private static final AesSimpleCipher CIPHER = 
    AesSimpleCipher.builder().build(KEY);

  public static String encrypt(String value)
  {
    return CIPHER.encrypt(value);
  }

  public static String decrypt(String value)
  {
    return CIPHER.decrypt(value);
  }
}