package su.usatu.project26.util;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import su.usatu.project26.model.ReportData;

public class PDFUtil {
	
	public static boolean generateNewPDF(ReportData dataForPDF, String fontsPath, String imgPath, String savingPath) 
			throws IOException, IllegalStateException {
		
		boolean pdfGenerationStatus = false;

		try (PDDocument document = new PDDocument()) {
			final PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				
				// image
				PDImageXObject image = PDImageXObject.createFromFile(imgPath + "favicon.png", document);
				contentStream.drawImage(image, 25, 790);
				
				// text
				contentStream.beginText();

				PDFont timesRegular = PDType0Font.load(document, new File(fontsPath + "times.ttf"));
				PDFont timesBold = PDType0Font.load(document, new File(fontsPath + "timesbd.ttf"));
				PDFont timesItalic = PDType0Font.load(document, new File(fontsPath + "timesi.ttf"));

				contentStream.setFont(timesBold, 20);
				contentStream.setLeading(14 * 1.25f);
				
				contentStream.newLineAtOffset(0, 828);

				contentStream.newLineAtOffset(82, -20);
				String line1 = "Отчёт";
				contentStream.showText(line1);
				contentStream.newLine();

				contentStream.setFont(timesRegular, 14);

				String line2 = "по произведённому расчёту размера платы за электроснабжение";
				contentStream.showText(line2);
				contentStream.newLine();
			
				ZonedDateTime currDateObj = ZonedDateTime.now(ZoneId.of("Asia/Yekaterinburg"));
			    DateTimeFormatter dateFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			    String formattedDate = currDateObj.format(dateFormatObj);
			    
				// сбрасываем координаты для newLineAtOffset
			    contentStream.endText();
				contentStream.beginText();
				
				contentStream.newLineAtOffset(265, 25);
				contentStream.setFont(timesItalic, 14);
				String dateLine = "Дата формирования отчёта: " + formattedDate;
				contentStream.showText(dateLine);
				contentStream.newLine();

				contentStream.endText();
				
				// data to put
				
				
				
				// table
				// Build calculationTable
				Table calculationTable = Table.builder().addColumnsOfWidth(150, 150, 150).font(timesRegular).padding(2)
						.addRow(Row.builder()
								.add(TextCell.builder().text("№ точки учёта").borderWidth(1).build())
								.add(TextCell.builder().text("Предыдущие показания").borderWidth(1).build())
								.add(TextCell.builder().text("Текущие показания").borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("1").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.firstMeterPrevReadings).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.firstMeterCurrReadings).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("2").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.secondMeterPrevReadings).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.secondMeterCurrReadings).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("3").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.thirdMeterPrevReadings).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.thirdMeterCurrReadings).borderWidth(1).build())
								.build())
						.build();
				
				// Build consumptionTable
				Table consumptionTable = Table.builder().addColumnsOfWidth(110, 110, 110, 110).font(timesRegular).padding(2)
						.addRow(Row.builder()
								.add(TextCell.builder().text("Услуга").borderWidth(1).build())
								.add(TextCell.builder().text("Объём, кВт*ч").borderWidth(1).build())
								.add(TextCell.builder().text("Тариф, руб.").borderWidth(1).build())
								.add(TextCell.builder().text("Начислено, руб.").borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("1").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.consumptionByFirstMeter).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.firstRatePrice).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.firstMeterAmount).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("2").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.consumptionBySecondMeter).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.secondRatePrice).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.secondMeterAmount).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("3").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.consumptionByThirdMeter).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.thirdRatePrice).borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.thirdMeterAmount).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("").borderWidth(0).build())
								.add(TextCell.builder().text("").borderWidth(0).build())
								.add(TextCell.builder().text("Итого:").borderWidth(1).font(timesItalic).build())
								.add(TextCell.builder().text(dataForPDF.totalAmount).borderWidth(1).build())
								.build())
						.build();
				
				// Set up the drawer for calculationTable
				TableDrawer calculationTableDrawer = TableDrawer.builder().contentStream(contentStream).startX(25)
						.startY(page.getMediaBox().getUpperRightY() - 80).table(calculationTable).build();

				// Set up the drawer for consumptionTable
				TableDrawer consumptionTableDrawer = TableDrawer.builder().contentStream(contentStream).startX(25)
						.startY(page.getMediaBox().getUpperRightY() - 150).table(consumptionTable).build();

				// Draw the tables
				calculationTableDrawer.draw();
				consumptionTableDrawer.draw();
				
			}
			
			document.save(savingPath);
			document.close();
			pdfGenerationStatus = true;
		}
		
		return pdfGenerationStatus;

	}
}
