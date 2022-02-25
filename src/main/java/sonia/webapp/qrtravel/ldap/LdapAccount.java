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

import com.unboundid.ldap.sdk.Entry;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@ToString
public class LdapAccount
{
  public LdapAccount(Entry entry)
  {
    this.dn = entry.getDN();
    this.uid = entry.getAttributeValue("uid");
    this.sn = entry.getAttributeValue("sn");
    this.givenName = entry.getAttributeValue("givenName");
    this.mail = entry.getAttributeValue("mail");
    this.soniaStudentNumber = entry.getAttributeValue("soniaStudentNumber");
    this.soniaChipcardBarcode = entry.getAttributeValue("soniaChipcardBarcode");
    this.jpegPhoto = null;
    this.locality = entry.getAttributeValue("l");

    // 
    this.ou = entry.getAttributeValue("ou");
    this.employeeType = entry.getAttributeValue("employeeType");

    // 
    byte[] imageData = entry.getAttributeValueBytes("jpegPhoto");
   
    if ((imageData != null) && (imageData.length > 0))
    {
      jpegPhoto = Base64.encodeBase64String(imageData);
    }
  }

  @Getter
  private final String dn;

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
  @ToString.Exclude
  private String jpegPhoto;
  
  @Getter
  private final String locality;
}
