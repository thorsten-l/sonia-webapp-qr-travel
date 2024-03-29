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
package sonia.webapp.qrtravel;

import sonia.webapp.qrtravel.cronjob.CheckExpiredJob;
import com.google.common.base.Strings;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.IOException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import sonia.webapp.qrtravel.cronjob.CheckMaxDurationJob;
import sonia.webapp.qrtravel.cronjob.InfluxDbStatisticsJob;
import sonia.webapp.qrtravel.db.Database;
import sonia.webapp.qrtravel.util.PdfQrExporter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@SpringBootApplication
public class QrTravelApplication
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    QrTravelApplication.class.getName());

  public static void main(String[] args) throws SchedulerException, IOException
  {
    if (System.getProperty("app.home") == null)
    {
      System.setProperty("app.home", ".");
    }

    Options options = new Options();
    CmdLineParser parser = new CmdLineParser(options);

    try
    {
      parser.parseArgument(args);
    }
    catch (CmdLineException e)
    {
      System.out.println(e.getMessage());
      options.setHelp(true);
    }

    if (options.isPdfQrExport())
    {
      LOGGER.info("Writing output file: {}", options.getPdfOutputFilename());

      if (Strings.isNullOrEmpty(options.getPdfOutputFilename())
        || Strings.isNullOrEmpty(options.getPdfRoomPin())
        || Strings.isNullOrEmpty(options.getPdfRoomType())
        || Strings.isNullOrEmpty(options.getPdfRoomDescripton())
        || Strings.isNullOrEmpty(options.getPdfServiceUrl()))
      {
        System.out.println("\nERROR: PDF export parameter is missing");
        System.out.println("--------------------------------------");
        parser.printUsage(System.out);
      }
      else
      {
        if (options.getPdfSeatRows() == 0)
        {
          PdfQrExporter.generateDocument(
            new PdfWriter(options.getPdfOutputFilename()),
            options.getPdfRoomType(),
            options.getPdfRoomDescripton(),
            options.getPdfServiceUrl(),
            options.getPdfRoomPin());
        }
        else
        {
          PdfQrExporter.generateDocument(
            new PdfWriter(options.getPdfOutputFilename()),
            options.getPdfRoomType(),
            options.getPdfRoomDescripton(),
            options.getPdfServiceUrl(),
            options.getPdfRoomPin(),
            options.getPdfSeatRows(),
            options.getPdfSeatColumns()
          );
        }
      }
      System.exit(0);
    }

    if (options.isHelp())
    {
      System.out.println("QR-Travel usage:");
      parser.printUsage(System.out);
      System.exit(0);
    }

    if (options.isCreateSampleConfig())
    {
      Config.getInstance(false);
      Config.writeSampleConfig(options.isForce());
      System.exit(0);
    }

    if (!Strings.isNullOrEmpty(options.getEncrypt()))
    {
      Config.getInstance();
      System.out.println(options.getEncrypt() + " = " + Cipher.
        encrypt(options.getEncrypt()));
      System.exit(0);
    }

    if (options.isCheckConfig())
    {
      System.out.println(Config.getInstance().toString());
      System.exit(0);
    }

    Config config = Config.getInstance();
    Database.initialize();

    BuildProperties build = BuildProperties.getInstance();
    LOGGER.info("Project Name    : " + build.getProjectName());
    LOGGER.info("Project Version : " + build.getProjectVersion());
    LOGGER.info("Build Timestamp : " + build.getTimestamp());

    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();
    scheduler.start();

    if (config.isEnableCheckExpired())
    {
      JobDetail job = JobBuilder.newJob(CheckExpiredJob.class)
        .withIdentity("CheckExpiredJob", "group1")
        .build();

      CronTrigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("trigger1", "group1")
        .withSchedule(CronScheduleBuilder.cronSchedule(config.
          getCheckExpiredCron()))
        .build();

      scheduler.scheduleJob(job, trigger);
    }

    if (config.isEnableCheckMaxDuration())
    {
      JobDetail job = JobBuilder.newJob(CheckMaxDurationJob.class)
        .withIdentity("CheckMaxDurationJob", "group2")
        .build();

      CronTrigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("trigger2", "group2")
        .withSchedule(CronScheduleBuilder.cronSchedule(config.
          getCheckMaxDurationCron()))
        .build();

      scheduler.scheduleJob(job, trigger);
    }

    if (config.isInfluxDbForStatisticsEnabled())
    {
      JobDetail job = JobBuilder.newJob(InfluxDbStatisticsJob.class)
        .withIdentity("InfulxDbStatisticsJob", "group3")
        .build();

      CronTrigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("trigger3", "group3")
        .withSchedule(CronScheduleBuilder.cronSchedule(config.getInfluxDbCron())).
        build();

      scheduler.scheduleJob(job, trigger);
    }

    SpringApplication.run(QrTravelApplication.class, args);
  }
}
