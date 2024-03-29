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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Entity(name = "room")
@NamedQueries(
  {
    @NamedQuery(name = "listRooms",
                query = "select a, upper(a.description) as orderName from room a order by orderName")
  })
@ToString
public class Room implements Serializable
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    Room.class.getName());

  public Room()
  {
  }

  public Room(String pin)
  {
    this.pin = pin;
  }

  public Room(String pin, int type, String description, String ownerUid,
    String domain)
  {
    this.pin = pin;
    this.description = description;
    this.ownerUid = ownerUid;
    this.domain = domain;
    for (RoomType t : Database.listRoomTypes())
    {
      if (t.getRtype() == type)
      {
        roomType = t;
      }
    }
  }

  @PrePersist
  public void prePersist()
  {
    creation = new Date();
    LOGGER.debug("prePersist " + this.getClass().getCanonicalName() 
      + ", " + this.toString());
  }

  @Override
  public boolean equals(Object obj)
  {
    boolean same = false;

    if (this == obj)
    {
      return true;
    }

    if ((obj != null) && (obj instanceof Room))
    {
      same = this.pin.equals(((Room) obj).pin);
    }

    return same;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;

    hash = 37 * hash + Objects.hashCode(this.pin);

    return hash;
  }

  public int getAttendeesCount()
  {
    int count = 0;

    if (attendees != null)
    {
      for (Attendee a : attendees)
      {
        if (Strings.isNullOrEmpty(a.getDeparture()))
        {
          count++;
        }
      }
    }

    return count;
  }

  public String getStatistics()
  {
    return Integer.toString(attendees.size()) + "/"
      + Integer.toString(getAttendeesCount());
  }

  @Id
  @Getter
  private String pin;

  @Getter
  private String description;

  @Getter
  @Column(name = "owner_uid")
  private @JsonIgnore
  String ownerUid;

  @Getter
  private String domain;

  @Getter
  @JsonIgnore
  @Temporal(TemporalType.TIMESTAMP)
  private Date creation;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "rtype", nullable = false)
  @Getter
  private RoomType roomType;

  @OneToMany(
    mappedBy = "pin",
    fetch = FetchType.EAGER
  )
  @Getter
  private @JsonIgnore
  List<Attendee> attendees;
}
