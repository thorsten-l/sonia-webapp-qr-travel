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
package sonia.webapp.qrtravel.ldap;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@ToString
public class LoginAttempt
{
  public LoginAttempt(String username)
  {
    this.username = username;
    this.creationTimestamp = System.currentTimeMillis();
    this.counter = 0;
  }

  public int incrementCounter()
  {
    return ++counter;
  }

  @Getter
  private String username;

  @Getter
  private long creationTimestamp;

  @Getter
  private int counter;
}
