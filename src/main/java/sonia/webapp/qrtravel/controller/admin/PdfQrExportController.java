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
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.db.Database;
import sonia.webapp.qrtravel.db.Room;

@Controller
public class PdfQrExportController
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    PdfQrExportController.class.getName());

  private final static Config CONFIG = Config.getInstance();

  public static void createQrPage(Document document, Room room)
    throws IOException
  {
    PdfFont helveticaBold = PdfFontFactory.createFont(
      StandardFonts.HELVETICA_BOLD, true);
    PdfFont helvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA, true);
    PdfFont courier = PdfFontFactory.createFont(StandardFonts.COURIER, true);

    PdfDocument pdf = document.getPdfDocument();
    PdfPage page = pdf.addNewPage();
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
    text = new Text(room.getDescription());
    text.setFontSize(32.0f);
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
    float y = (pageHeight - imageHeight) / 2 + 80;

    canvas.setFillColor(ColorConstants.WHITE);
    canvas.setStrokeColor(ColorConstants.BLACK);
    canvas.rectangle(x, y, imageWidth, imageHeight);
    canvas.fillStroke();

    canvas.setFillColor(ColorConstants.BLACK);
    canvas.setStrokeColor(ColorConstants.BLACK);
    image.setFixedPosition(x, y);
    document.add(image);

    /////
    image = new Image(ImageDataFactory.create("logo.png"));
    image.scale(0.5f, 0.5f);
    image.setFixedPosition(30, pageHeight - (image.getImageHeight() / 2) - 30);
    document.add(image);

    text = new Text("\n\n\n\n\n\n\n\nRegistration unter:\n");
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

    text = new Text("\nPIN: ");
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
    document.add(new AreaBreak());
  }

  @GetMapping(path = "/admin/pdfqrexport",
              produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public void httpGetPdfQrExport(HttpServletResponse response)
  {
    LOGGER.debug("httpGetPdfQrExport");
    response.setHeader("Content-Disposition",
      "attachment; filename=QrCodeExport.pdf");

    List<Room> rooms = Database.listRooms();
    
    try
    {
      try (OutputStream out = response.getOutputStream())
      {
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        pdf.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdf, PageSize.A4);
        createQrPage(document, rooms.get(1));
      }
    }
    catch (IOException ex)
    {
      LOGGER.error("Write export error.", ex);
    }
  }
}
