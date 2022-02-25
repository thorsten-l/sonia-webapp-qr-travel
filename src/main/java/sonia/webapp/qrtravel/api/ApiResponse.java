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
package sonia.webapp.qrtravel.api;

import lombok.Getter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public class ApiResponse
{
  public final static int OK = 0;
  public final static int ERROR = 1;
  public final static int INVALID_CREDENTIALS = 2;
  public final static int UNKNOWN_ROOM = 3;
  public final static int ACCOUNT_BLOCKED = 4;
  public final static int PHONENUMBER_IS_MISSING = 5;

  public ApiResponse( int code, String message )
  {
    this.code = code;
    this.message = message;
  }
  
  @Getter
  private int code;
  
  @Getter
  private String message;
}
