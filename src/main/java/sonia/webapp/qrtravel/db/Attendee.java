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
package sonia.webapp.qrtravel.db;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Entity(name = "attendee")
@NamedQueries(
  {
    @NamedQuery(name = "lastAttendeeEntry",
                query = "select a from attendee a where a.cookieUUID = :uuid and a.pin = :pin order by a.id desc")
  })
@ToString
public class Attendee implements Serializable
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    Attendee.class.getName());

  public void setAttendeeData( String pin, AttendeeData data )
  {
    this.cookieUUID = data.getUuid();
    this.email = data.getMail();
    this.surname = data.getSurname();
    this.givenname = data.getGivenName();
    this.location = data.getLocation();
    this.phonenumber = data.getPhone();
    this.pin = pin;
    this.street = data.getStreet();
    this.city = data.getCity();
  }
  
  @PrePersist
  public void prePersist()
  {
    createdTimestamp = System.currentTimeMillis();
    LOGGER.debug("prePersist " + this.getClass().getCanonicalName());
  }

  /**
   * Method description
   *
   */
  @PreUpdate
  public void preUpdate()
  {
    updatedTimestamp = System.currentTimeMillis();
    LOGGER.debug("preUpdate " + this.getClass().getCanonicalName());
  }

  @Override
  public boolean equals(Object obj)
  {
    boolean same = false;

    if (this == obj)
    {
      return true;
    }

    if ((obj != null) && (obj instanceof Attendee))
    {
      same = this.id == ((Attendee) obj).id;
    }

    return same;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;

    hash = 37 * hash + Objects.hashCode(this.id);

    return hash;
  }

  @Getter
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private long id;

  @Getter
  @Setter
  private String pin;

  @Getter
  @Setter
  private String arrive;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private String phonenumber;

  @Getter
  @Setter
  private String surname;

  @Getter
  @Setter
  private String givenname;

  @Getter
  @Setter
  private String studentnumber;

  @Getter
  @Setter
  private String departure;

  @Getter
  @Setter
  private String location;

  @Getter
  @Setter
  @Column(name = "cookie_uuid")
  private String cookieUUID;

  @Getter
  private long createdTimestamp;
  
  @Getter
  private long updatedTimestamp;
  
  @Getter
  @Setter
  private String street;

  @Getter
  @Setter
  private String city;
}
