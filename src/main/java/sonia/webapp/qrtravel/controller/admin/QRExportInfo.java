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
package sonia.webapp.qrtravel.controller.admin;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@ToString
public class QRExportInfo
{
  public QRExportInfo( String ownerUid, String ownerName, String email, String l )
  {
    this.ownerUid = ownerUid;
    this.ownerName = ownerName;
    this.email = email;
    this.l = l;
  }
  
  public void incrementRoom()
  {
    roomCounter++;
  }
  
  @Getter
  private final String ownerUid;
  
  @Getter
  private final String ownerName;
  
  @Getter
  private final String email;
  
  @Getter
  private final String l;
  
  @Getter
  private int roomCounter;
}
