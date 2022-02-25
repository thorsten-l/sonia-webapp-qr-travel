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

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.db.Database;
import sonia.webapp.qrtravel.db.Room;

@Controller
public class PdfQrExportController
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    PdfQrExportController.class.getName());

  private final static Config CONFIG = Config.getInstance();

  public void createQrPage( PdfDocument pdf, Document document, PdfPage page, Room room)
    throws IOException
  {
    LOGGER.debug("create qr page for room={}", room.toString());
    
    PdfFont helveticaBold = PdfFontFactory.createFont(
      StandardFonts.HELVETICA_BOLD, true);
    PdfFont helvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA, true);
    PdfFont courier = PdfFontFactory.createFont(StandardFonts.COURIER, true);

  
    PdfCanvas canvas = new PdfCanvas(page);

    canvas.setFillColor(ColorConstants.LIGHT_GRAY);
    canvas.setStrokeColor(ColorConstants.LIGHT_GRAY);
    canvas.rectangle(0, 0, pdf.getDefaultPageSize().getWidth(), pdf.
      getDefaultPageSize().getHeight());
    canvas.fillStroke();

    Text text = new Text(room.getRoomType().getDescription());
    text.setFontSize(32.0f);
    Paragraph paragraph = new Paragraph(text);
    paragraph.setFont(helveticaBold);
    paragraph.setFontColor(new DeviceRgb(0, 0, 160));
    paragraph.setTextAlignment(TextAlignment.CENTER);
    document.add(paragraph);

    /////
    text = new Text(room.getDescription().trim());
    text.setFontSize(28.0f);
    paragraph = new Paragraph(text);
    paragraph.setFont(helveticaBold);
    paragraph.setTextAlignment(TextAlignment.CENTER);
    paragraph.setFontColor(ColorConstants.BLACK);
    document.add(paragraph);

    /////
    Map<EncodeHintType, Object> hints = new HashMap<>();
    hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    BarcodeQRCode qrcode = new BarcodeQRCode(CONFIG.getWebServiceUrl() + "/?p="
      + room.getPin(), hints);

    Image image = new Image(qrcode.createFormXObject(pdf));

    float pageHeight = pdf.getDefaultPageSize().getHeight();
    float pageWidth = pdf.getDefaultPageSize().getWidth();

    float scale = 8.0f;
    image.scale(scale, scale);
    image.setStrokeColor(ColorConstants.BLACK);

    float imageHeight = image.getImageHeight() * scale;
    float imageWidth = image.getImageWidth() * scale;
    float x = (pageWidth - imageWidth) / 2;
    float y = (pageHeight - imageHeight) / 2 + 56;

    canvas.setFillColor(ColorConstants.WHITE);
    canvas.setStrokeColor(ColorConstants.BLACK);
    canvas.rectangle(x, y, imageWidth, imageHeight);
    canvas.fillStroke();

    canvas.setFillColor(ColorConstants.BLACK);
    canvas.setStrokeColor(ColorConstants.BLACK);
    image.setFixedPosition(x, y);
    document.add(image);

    image = new Image(ImageDataFactory.create(this.getClass().getResource(
      "/static/image/logo.png")));
    image.scale(0.5f, 0.5f);
    image.setFixedPosition(30, pageHeight - (image.getImageHeight() / 2) - 30);
    document.add(image);

    text = new Text("\n\n\n\n\n\n\n\n\nRegistration unter:\n");
    text.setFontSize(32.0f);
    text.setFont(helvetica);
    paragraph = new Paragraph(text);
    paragraph.setFont(helvetica);
    paragraph.setTextAlignment(TextAlignment.CENTER);
    paragraph.setFontColor(ColorConstants.BLACK);
    text = new Text(CONFIG.getWebServiceUrl() + "\n");
    text.setFontSize(32.0f);
    text.setFont(courier);
    paragraph.add(text);
    document.add(paragraph);

    text = new Text("PIN: ");
    text.setFontSize(32.0f);
    text.setFont(helvetica);
    paragraph = new Paragraph(text);
    paragraph.setFont(helvetica);
    paragraph.setTextAlignment(TextAlignment.CENTER);
    paragraph.setFontColor(ColorConstants.BLACK);
    text = new Text(room.getPin());
    text.setFontSize(32.0f);
    text.setFont(courier);
    paragraph.add(text);
    document.add(paragraph);
    document.flush();
  }

  @GetMapping(path = "/admin/pdfqrexport",
              produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public void httpGetPdfQrExport(
    @RequestParam(name = "owneruid", required = true) String ownerUid,
    HttpServletResponse response)
  {
    LOGGER.debug("httpGetPdfQrExport");
    response.setHeader("Content-Disposition",
      "attachment; filename=QrCodeExport.pdf");

    List<Room> rooms = Database.listRooms();

    LOGGER.debug("owner uid = {}", ownerUid);

    try
    {
      try (OutputStream out = response.getOutputStream())
      {
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        pdf.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdf, PageSize.A4);
        PdfPage page = pdf.addNewPage();

        boolean firstPage = true;

        for (Room room : rooms)
        {
          if (room.getOwnerUid().equals(ownerUid) || ownerUid.equals("-ALL-"))
          {
            if (firstPage)
            {
              firstPage = false;
            }
            else
            {
              page = pdf.addNewPage();
              document.add(new AreaBreak());
            }

            createQrPage( pdf, document, page, room);
          }
        }
        
        document.close();
        pdf.close();
      }
    }
    catch (IOException ex)
    {
      LOGGER.error("Write export error.", ex);
    }
  }
}
