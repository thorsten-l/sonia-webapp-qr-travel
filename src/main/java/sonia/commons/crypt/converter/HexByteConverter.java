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
package sonia.commons.crypt.converter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 * @since 1.1.0
 */
public class HexByteConverter implements ByteConverter
{
  private static final char[] DIGITS =
  {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
    'f'
  };

  //~--- methods --------------------------------------------------------------

  protected static int toDigit(char ch, int index)
    throws IllegalArgumentException
  {
    int digit = Character.digit(ch, 16);

    if (digit == -1)
    {
      throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch
        + " at index " + index);
    }

    return digit;
  }

  @Override
  public byte[] decode(String value)
  {
    char[] data = value.toCharArray();
    int len = data.length;

    if ((len & 0x01) != 0)
    {
      throw new IllegalArgumentException("Odd number of characters.");
    }

    byte[] out = new byte[len >> 1];

    // two characters form the hex value.
    for (int i = 0, j = 0; j < len; i++)
    {
      int f = toDigit(data[j], j) << 4;

      j++;
      f = f | toDigit(data[j], j);
      j++;
      out[i] = (byte) (f & 0xFF);
    }

    return out;
  }

  @Override
  public String encode(byte[] data)
  {
    int l = data.length;

    char[] out = new char[l << 1];

    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++)
    {
      out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
      out[j++] = DIGITS[0x0F & data[i]];
    }

    return new String(out);
  }
}
