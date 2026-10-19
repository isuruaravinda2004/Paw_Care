package com.pawcare.report;
import com.pawcare.util.DBConnection;
import net.sf.jasperreports.engine.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/** Compile trusted bundled JRXML, then fill its JOIN query using a JDBC connection. */
public class ReportService {
    public JasperPrint generate(String name, LocalDate from, LocalDate to, Integer paymentId) throws SQLException, JRException, IOException {
        if(!Set.of("appointments","payments","receipt").contains(name)) throw new IllegalArgumentException("Unknown report.");
        if(from==null || to==null || from.isAfter(to)) throw new IllegalArgumentException("Report start date must not be after end date.");
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("FROM_DATE",java.sql.Date.valueOf(from)); parameters.put("TO_DATE",java.sql.Date.valueOf(to));
        parameters.put("PAYMENT_ID",paymentId==null?0:paymentId);
        try(InputStream in=getClass().getResourceAsStream("/reports/"+name+".jrxml"); Connection c=DBConnection.getInstance().getConnection()) {
            if(in==null) throw new FileNotFoundException("Report template missing: "+name);
            JasperReport template=JasperCompileManager.compileReport(in);
            return JasperFillManager.fillReport(template,parameters,c);
        }
    }
    public void exportPdf(JasperPrint report, File file) throws JRException { JasperExportManager.exportReportToPdfFile(report,file.getAbsolutePath()); }
}
