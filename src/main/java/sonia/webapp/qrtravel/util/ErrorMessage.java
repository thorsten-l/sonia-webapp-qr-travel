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
package sonia.webapp.qrtravel.util;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@ToString
public class ErrorMessage
{
  
  public ErrorMessage()
  {
    this.title = "";
    this.message = "";
    this.processed = true;
  }
  
  public ErrorMessage( String title, String message )
  {
    this.title = title;
    this.message = message;
    this.processed = false;
  }
  
  public boolean isDone()
  {
    boolean value = processed;
    processed = true;
    return value;
  }

  @Getter
  private boolean processed;
  
  @Getter
  private final String title;
  
  @Getter
  private final String message;
}
