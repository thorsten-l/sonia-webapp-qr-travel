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
package sonia.webapp.qrtravel.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sonia.webapp.qrtravel.db.Database;
import sonia.webapp.qrtravel.db.Room;

/**
 * 
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@RestController
public class RoomController
{
  @GetMapping( path= "/room", produces = MediaType.APPLICATION_JSON_VALUE)
  public Room room(
    @RequestParam(name = "p", required = true) String pin )
  {
    return Database.findRoom(pin);
  }
}
