package lk.ijse.dpcommunication.controller;

import net.sf.jasperreports.engine.JasperCompileManager;

public class ReportCompiler {
    public static void main(String[] args) {
        try {
            JasperCompileManager.compileReportToFile("src/main/resources/reports/DP_PAYLATER.jrxml");
        } catch (Exception e) {
            e.printStackTrace();
        }try {
            JasperCompileManager.compileReportToFile("src/main/resources/reports/dp_order.jrxml");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
