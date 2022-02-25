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
package sonia.webapp.qrtravel.cronjob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.db.Database;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public class CheckMaxDurationJob implements Job
{
  private final static Logger LOGGER = LoggerFactory.getLogger(CheckMaxDurationJob.class.getName());

  private final static Config CONFIG = Config.getInstance();

  private final static long MILLIS_PER_MINUTE = 1000 * 60;

  @Override
  public void execute(JobExecutionContext jec) throws JobExecutionException
  {
    long maxDurationTimestamp = System.currentTimeMillis() - (CONFIG.getMaxDurationInMinutes() * MILLIS_PER_MINUTE);
    LOGGER.debug("Check max duration job started");
    LOGGER.debug("Current time millis = {}, expirationTimestamp={}", System.currentTimeMillis(),
      maxDurationTimestamp);
    Database.departureMaxDurationAttendeeEntries(maxDurationTimestamp);
  }
}
