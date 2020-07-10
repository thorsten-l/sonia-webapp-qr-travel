/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonia.webapp.qrtravel.ldap;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author th
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