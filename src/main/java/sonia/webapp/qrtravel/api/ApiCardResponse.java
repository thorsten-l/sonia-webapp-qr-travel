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
import lombok.Setter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public class ApiCardResponse
{

  public ApiCardResponse( int code, String message )
  {
    this.code = code;
    this.message = message;
  }
  
  @Getter
  private final int code;
  
  @Getter
  private final String message;
  
  @Getter
  @Setter
  private String mail;

  @Getter
  @Setter
  private String uid;

  @Getter
  @Setter
  private String sn;

  @Getter
  @Setter
  private String givenName;

  @Getter
  @Setter
  private String soniaStudentNumber;

  @Getter
  @Setter
  private String soniaChipcardBarcode;
  
  @Getter
  @Setter
  private String ou;
  
  @Getter
  @Setter
  private String employeeType;
  
  @Getter
  @Setter
  private String phoneNumber;
  
  @Getter
  @Setter
  private String jpegPhoto;  
}
