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

import lombok.Getter;
import lombok.Setter;
import org.kohsuke.args4j.Option;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public class Options
{
  
  @Setter
  @Getter
  @Option( name="--help", aliases="-h", usage="Display this help", required = false )
  private boolean help = false;
  
  
  @Getter
  @Option( name="--encrypt", aliases="-e", usage="Encrypt a password", required = false )
  private String encrypt;

  @Getter
  @Option( name="--create-sample-config", usage="Create a sample configuration file", required = false )
  private boolean createSampleConfig;

  @Getter
  @Option( name="--force", usage="Force overriding existing configuration file", required = false )
  private boolean force;

  @Getter
  @Option( name="--check-config", aliases="-c", usage="Check config file", required = false )
  private boolean checkConfig;
  
  @Setter
  @Getter
  @Option( name="--pdf-qr-export", aliases="-p", usage="PDF: create multiple qr code pages for a room with different seat numbers", required = false )
  private boolean pdfQrExport = false;

  @Setter
  @Getter
  @Option( name="--pdf-output", aliases="-po", usage="PDF: output file name", required = false )
  private String pdfOutputFilename;

  @Setter
  @Getter
  @Option( name="--pdf-room-type", aliases="-pt", usage="PDF: room type", required = false )
  private String pdfRoomType;

  @Setter
  @Getter
  @Option( name="--pdf-room-description", aliases="-pd", usage="PDF: room description", required = false )
  private String pdfRoomDescripton;

  @Setter
  @Getter
  @Option( name="--pdf-room-pin", aliases="-pp", usage="PDF: room pin", required = false )
  private String pdfRoomPin;

  @Setter
  @Getter
  @Option( name="--pdf-service-url", aliases="-pu", usage="PDF: service url", required = false )
  private String pdfServiceUrl;

  @Setter
  @Getter
  @Option( name="--pdf-seat-rows", aliases="-pr", usage="PDF: seat rows", required = false )
  private int pdfSeatRows;

  @Setter
  @Getter
  @Option( name="--pdf-seat-columns", aliases="-pc", usage="PDF: seat columns", required = false )
  private int pdfSeatColumns;

}
