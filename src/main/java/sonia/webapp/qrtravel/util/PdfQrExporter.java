package sonia.webapp.qrtravel.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class PdfQrExporter {
	/**
	 * Create a page with QR code and text
	 * @param pdf	Document  to put it in
	 * @param document	document object  ( to be able to close it outside)!
	 * @param page	Page to add the content to
	 * @param roomType	for example "Klausur" or "Hörsaal"
	 * @param roomName	name of the room
	 * @param url		base url for the web-Application including p=... parameter
	 * @param urlPrint	url to print, usually without p=..parameter
	 * @param pin		pin to show separately
	 * @param withSeat	true if seat number should be genereated
	 * @param seat		for example "1A"
	 * @throws IOException
	 */
	public static void createQrPage(PdfDocument pdf, Document document, PdfPage page, String roomType, String roomName,
			String url, String urlPrint, String pin, boolean withSeat, String seat) throws IOException {
		PdfFont helveticaBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD, true);
		PdfFont helvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA, true);
		PdfFont courier = PdfFontFactory.createFont(StandardFonts.COURIER, true);

		PdfCanvas canvas = new PdfCanvas(page);

		canvas.setFillColor(ColorConstants.WHITE);
		canvas.setStrokeColor(ColorConstants.LIGHT_GRAY);
		canvas.rectangle(0, 0, pdf.getDefaultPageSize().getWidth(), pdf.getDefaultPageSize().getHeight());
		canvas.fillStroke();

		Text text = new Text(roomType);
		text.setFontSize(withSeat ? 20f : 32.0f);
		Paragraph paragraph = new Paragraph(text);
		paragraph.setFont(helveticaBold);
		paragraph.setFontColor(new DeviceRgb(0, 0, 160));
		paragraph.setTextAlignment(TextAlignment.CENTER);
		document.add(paragraph);

		/////
		text = new Text(roomName);
		text.setFontSize(withSeat ? 18f : 28.0f);
		paragraph = new Paragraph(text);
		paragraph.setFont(helveticaBold);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		paragraph.setFontColor(ColorConstants.BLACK);
		document.add(paragraph);

		/////
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		BarcodeQRCode qrcode = new BarcodeQRCode(url, hints);

		Image image = new Image(qrcode.createFormXObject(pdf));

		float pageHeight = page.getCropBox().getHeight();
		float pageWidth = page.getCropBox().getWidth();

		float scale = withSeat ? 8.0f : 8.0f;
		image.scale(scale, scale);
		image.setStrokeColor(ColorConstants.BLACK);

		float imageWidth = image.getImageWidth() * scale;
		float x = (pageWidth - imageWidth) / 2 - 20;

		canvas.setFillColor(ColorConstants.BLACK);
		canvas.setStrokeColor(ColorConstants.BLACK);
		image.setRelativePosition(x, 0, 0, 0);
		document.add(image);

		image = new Image(ImageDataFactory.create(PdfQrExporter.class.getResource("/static/image/logo.png")));
		image.scale(0.5f, 0.5f);
		image.setFixedPosition(30, pageHeight - (image.getImageHeight() / 2) - 30);
		document.add(image);

		paragraph = new Paragraph();
		paragraph.setFont(helvetica);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		paragraph.setFontColor(ColorConstants.BLACK);
		if (!withSeat) {
			text = new Text("Registrierung unter:");
			text.setFontSize(withSeat ? 20f : 32.0f);
			text.setFont(helvetica);
			paragraph.add(text);
		}
		text = new Text(urlPrint);
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
		text = new Text(pin);
		text.setFontSize(32.0f);
		text.setFont(courier);
		paragraph.add(text);
		document.add(paragraph);
		if (withSeat) {
			text = new Text("Platz: ");
			text.setFontSize(20.0f);
			text.setFont(helvetica);
			paragraph = new Paragraph(text);
			paragraph.setFont(helvetica);
			paragraph.setTextAlignment(TextAlignment.CENTER);
			paragraph.setFontColor(ColorConstants.BLACK);
			text = new Text(seat);
			text.setFontSize(170.0f);
			text.setFont(helveticaBold);
			paragraph.add(text);
			document.add(paragraph);

		}

		document.flush();
	}

	/**
	 * generate a PDF document with QR code (one room only)
	 * @param writer	pdfWriter to write to
	 * @param roomType	for example "Klausur" or "Hörsaal"
	 * @param roomName	name of the room
	 * @param url		base url for the web-Application (without parameters)
	 * @param pin		pin for the room
	 * @throws IOException
	 */
	public static void generateDocument(PdfWriter writer, String roomType, String roomName, String url, String pin)
			throws IOException {
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setDefaultPageSize(PageSize.A4);
		Document document = new Document(pdf, PageSize.A4);
		PdfPage page = pdf.addNewPage();

		createQrPage(pdf, document, page, roomType, roomName, url + "&p=" + pin, url, pin, false, null);
		document.add(new AreaBreak());
		document.close();
	}

	/**
	 * generate a PDF document with QR codes for each seat (one page per seat)
	 * @param writer	pdfWriter to write to
	 * @param roomType	for example "Klausur" or "Hörsaal"
	 * @param roomName	name of the room
	 * @param url		base url for the web-Application (without parameters)
	 * @param pin		pin for the room
	 * @param rows		number of rows (with numbers)
	 * @param columns	number of columns (with letters)
	 * @throws IOException
	 */
	public static void generateDocument(PdfWriter writer, String roomType, String roomName, String url, String pin,
			int rows, int columns) throws IOException {
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setDefaultPageSize(PageSize.A4);
		Document document = new Document(pdf, PageSize.A4);

		for (int row = 1; row <= rows; row++) {
			for (int column = 0; column < columns; column++) {
				PdfPage page = pdf.addNewPage();
				String platz = "" + row + (char) ('A' + column);

				createQrPage(pdf, document, page, roomType, roomName, url + "?p=" + pin + "&l=" + platz, url, pin, true,
						platz);
				// document.add(new AreaBreak()); // seems unnecessary
			}
		}
		document.close();
	}

	/**
	 * for standalone invocation.
	 * @param args  destFile roomdescription PIN rows columns
	 * @throws IOException
	 */
	public static void main(String... args) throws IOException {
		if (args.length < 5) {
			System.out.println("5 Arguments: destFile roomdescription PIN rows columns");
		}
		generateDocument(new PdfWriter(args[0]), "Klausur", args[1], "https://qr.sonia.de/", args[2],
				Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		System.out.println("PDF just got created in " + args[0]);
		System.out.println("Maybe run\n" + "pdfnup --nup 2x2 --no-landscape " + args[0]);
	}
}
