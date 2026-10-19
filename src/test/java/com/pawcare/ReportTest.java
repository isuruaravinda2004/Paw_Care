package com.pawcare;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.pawcare.report.ReportService;
import com.pawcare.util.AppClock;
import net.sf.jasperreports.engine.*;
import java.nio.file.*;
class ReportTest extends DatabaseTest {
    @Test void compileFillAndExportJoinedReports() throws Exception {
        ReportService reports=new ReportService(); Path folder=Path.of("target/test-reports"); Files.createDirectories(folder);
        for(String name:new String[]{"appointments","payments","receipt"}) {
            JasperPrint report=reports.generate(name,AppClock.today(),AppClock.today().plusDays(7),1);
            assertFalse(report.getPages().isEmpty());
            StringBuilder text=new StringBuilder();
            for(JRPrintPage page:report.getPages()) for(JRPrintElement element:page.getElements()) if(element instanceof JRPrintText) text.append(((JRPrintText)element).getFullText()).append(" ");
            assertTrue(text.toString().contains("Nimali Perera"),text.toString()); assertTrue(text.toString().contains("Basic Bath"));
            Path pdf=folder.resolve(name+".pdf"); reports.exportPdf(report,pdf.toFile()); assertTrue(Files.size(pdf)>1000);
        }
    }
    @Test void emptyDateRangeStillProducesUsefulReport() throws Exception {
        JasperPrint report=new ReportService().generate("payments",AppClock.today().plusYears(1),AppClock.today().plusYears(2),null);
        assertFalse(report.getPages().isEmpty());
    }
}
