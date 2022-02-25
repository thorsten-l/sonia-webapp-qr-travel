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
public final class AdminCipher 
{
   
  /** secret key */
  private char[] cipherKey;
  
  /** cipher */
  private AesSimpleCipher aesChipher;

  private static AdminCipher SINGLETON;
  
  private AdminCipher() {}
  
  public static synchronized AdminCipher getInstance()
  {
    if ( SINGLETON == null ) 
    {
      SINGLETON = new AdminCipher();
      SINGLETON.cipherKey = Config.getInstance().getAdminCipherKey().toCharArray();
      SINGLETON.aesChipher = AesSimpleCipher.builder().build(SINGLETON.cipherKey);
    }
    return SINGLETON;
  }
 
  public String encrypt(String value)
  {
    return aesChipher.encrypt(value);
  }

  public String decrypt(String value)
  {
    return aesChipher.decrypt(value);
  }
}