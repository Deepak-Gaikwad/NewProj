package com.Clover.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.Clover.Model_moneykit.Report;
import com.Clover.Model_moneykit.User;
import com.Clover.Model_slonkit.Product;
import com.Clover.moneykit_repo.ProductRepoMoneyKit;
import com.Clover.moneykit_repo.UserRepository;
import com.Clover.slonkit_repo.ProductRepoSlonkit;
import com.Clover.slonkit_repo.SlonkitUserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@Service
public class ProductService {

	@Autowired
	UserRepository moneykitUserRepo;

	@Autowired
	SlonkitUserRepo slonkitUserRepo;

	@Autowired
	ProductRepoSlonkit slonkit_repo;

	@Autowired
	ProductRepoMoneyKit moneykit_repo;

	public ModelAndView validateUser(User user) throws NullPointerException, SQLException, Exception {
		String psw = this.encryptPassword(user.getPassword());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Home");
		if (user.getProduct().equals("Slonkit")) {
			mv.addObject("prod", "Slonkit");
			com.Clover.Model_slonkit.User validUser = slonkitUserRepo.validateUser(user.getUsername(), psw,
					user.getProduct());
			if (validUser == null) {
				this.userInvalid();
			}
			List<Product> slonkitProds = (List<Product>) slonkit_repo.findAll();
			mv.addObject("ls", slonkitProds);
		} else if (user.getProduct().equals("Moneykit")) {
			mv.addObject("prod", "Moneykit");
			User validUser = moneykitUserRepo.validateUser(user.getUsername(), psw, user.getProduct());
			if (validUser == null) {
				this.userInvalid();
			}
			List<com.Clover.Model_moneykit.Product> moneykitProds = (List<com.Clover.Model_moneykit.Product>) moneykit_repo
					.findAll();

			mv.addObject("ls", moneykitProds);
		}
		mv.addObject("report", new Report());
		return mv;
	}

	private String encryptPassword(String password) {
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(password.getBytes());
	}

	private ModelAndView userInvalid() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", "Username/Password Might have Been Wrong or user not found with this product. Try Again.");
		mv.setViewName("Login");
		return mv;
	}

	public List<Product> getProducts() {
		return this.slonkit_repo.findAll();
	}

	public byte[] generateExcelSheet(HttpServletResponse response, Report report) throws IOException, JRException {
		String outXlsName = "test.xls";
		JasperReport jasperReport = this.compileReport();
		String jsonString = "";
		if ("Slonkit".equals(report.getProdName())) {
			List<Product> prods = this.slonkit_repo.getProducts(report.getFromDate(), report.getToDate());
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(prods);
		} else if ("Moneykit".equals(report.getProdName())) {
			List<com.Clover.Model_moneykit.Product> prods = this.moneykit_repo.getProducts(report.getFromDate(), report.getToDate());
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(prods);
		}
		JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonString.getBytes()));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), jsonDataSource);
		JRXlsExporter xlsExporter = new JRXlsExporter();

		xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outXlsName));
		SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
		xlsReportConfiguration.setOnePagePerSheet(false);
		xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
		xlsReportConfiguration.setDetectCellType(false);
		xlsReportConfiguration.setWhitePageBackground(false);
		xlsExporter.setConfiguration(xlsReportConfiguration);
		xlsExporter.exportReport();
		return outXlsName.getBytes();
	}

	public void viewPDF(HttpServletResponse response, Report report) throws IOException, JRException {
		JasperReport jasperReport = this.compileReport();
		String jsonString = "";
		if ("Slonkit".equals(report.getProdName())) {
			List<Product> prods = this.slonkit_repo.getProducts(report.getFromDate(), report.getToDate());
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(prods);
		} else if ("Moneykit".equals(report.getProdName())) {
			List<com.Clover.Model_moneykit.Product> prods = this.moneykit_repo.getProducts(report.getFromDate(), report.getToDate());
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(prods);
		}
		JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonString.getBytes()));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), jsonDataSource);
		System.out.println(jasperPrint);
		JRPdfExporter pdfExporter = new JRPdfExporter();
		pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
		pdfExporter.exportReport();
		response.setContentType("application/pdf");
		response.setHeader("Content-Length", String.valueOf(pdfReportStream.size()));
		response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");
		OutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(pdfReportStream.toByteArray());
		responseOutputStream.close();
		pdfReportStream.close();
	}

	public byte[] generatePDF(HttpServletResponse response, Report report) throws Exception {
		ByteArrayOutputStream pdfReportStream = null;
		try {
			JasperReport jasperReport = this.compileReport();
			String jsonString = "";
			if ("Slonkit".equals(report.getProdName())) {
				List<Product> prods = this.slonkit_repo.getProducts(report.getFromDate(), report.getToDate());
				ObjectMapper mapper = new ObjectMapper();
				jsonString = mapper.writeValueAsString(prods);
			} else if ("Moneykit".equals(report.getProdName())) {
				List<com.Clover.Model_moneykit.Product> prods = this.moneykit_repo.getProducts(report.getFromDate(), report.getToDate());
				ObjectMapper mapper = new ObjectMapper();
				jsonString = mapper.writeValueAsString(prods);
			}
			JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonString.getBytes()));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), jsonDataSource);
			System.out.println(jasperPrint);
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			pdfReportStream = new ByteArrayOutputStream();
			pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
			pdfExporter.exportReport();
			return pdfReportStream.toByteArray();
		} catch (IOException | JRException e) {
			e.printStackTrace();
			throw new Exception("Failed");
		} finally {
			try {
				pdfReportStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private JasperReport compileReport() throws IOException, JRException {
		InputStream jrxmlInput = this.getClass().getClassLoader().getResource("report.jrxml").openStream();
		JasperDesign design = JRXmlLoader.load(jrxmlInput);
		return JasperCompileManager.compileReport(design);
	}
}
