package com.Clover.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.Clover.Model_moneykit.Report;
import com.Clover.Model_moneykit.User;
import com.Clover.Model_slonkit.Product;
import com.Clover.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Controller
public class LoginController {

	@Autowired
	ProductService service;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginpage(@ModelAttribute("user") User user, BindingResult br) {
		System.out.println("Get Login Page");
		ModelAndView mv = new ModelAndView("Login");
		return mv;
	}

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public ModelAndView userLogin(@ModelAttribute("user") User user) {
		// User validUser = userRepository.validateUser(user.getUsername(),
		// user.getPassword(), user.getProduct());
		// if (validUser == null) {
		// ModelAndView mv1 = new ModelAndView();
		// mv1.addObject("msg",
		// "Username/Password Might have Been Wrong or user not found with this
		// product. Try Again.");
		// mv1.setViewName("Login");
		// return mv1;
		// }
		// ModelAndView mv = new ModelAndView();
		// mv.setViewName("Home");
		// if (user.getProduct().equals("Slonkit")) {
		// List<Product> slonkitProds = (List<Product>) slonkit_repo.findAll();
		// mv.addObject("ls", slonkitProds);
		// } else if (user.getProduct().equals("Moneykit")) {
		// List<com.Clover.Model_moneykit.Product> moneykitProds =
		// (List<com.Clover.Model_moneykit.Product>) moneykit_repo
		// .findAll();
		// mv.addObject("ls", moneykitProds);
		// }
		// //return mv;
		try {
			return service.validateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mv1 = new ModelAndView();
			mv1.addObject("msg", "Internal server error");
			mv1.setViewName("Login");
			return mv1;
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(@ModelAttribute("user") User user) {
		System.out.println("Get Login Page");
		ModelAndView mv = new ModelAndView("Login");
		return mv;
	}

	@RequestMapping(params = "Success", method = RequestMethod.POST)
	public ResponseEntity<Object> buttoncheck(HttpServletResponse response, @ModelAttribute("report") Report report)
			throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		String filename = "Product.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<Object>(service.generatePDF(response, report), headers, HttpStatus.OK);
	}

	@PostMapping("/getPDF")
	public ResponseEntity<Object> generateReport(HttpServletResponse response, @ModelAttribute("report") Report report)
			throws Exception {
		ByteArrayOutputStream os;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		String filename = "Product.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<Object>(service.generatePDF(response, report), headers, HttpStatus.OK);
	}

	@PostMapping("/reportExcel")
	public ResponseEntity<Object> generateExcel(HttpServletResponse response, @ModelAttribute("report") Report report)
			throws Exception {
		ByteArrayOutputStream os;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/excel"));
		String filename = "Product.xls";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<Object>(service.generateExcelSheet(response, report), headers,
				HttpStatus.OK);
	}

	@PostMapping("/viewPDF")
	public void generateReports(HttpServletResponse response, @ModelAttribute("report") Report report)
			throws IOException, JRException {
		this.service.viewPDF(response, report);
		InputStream jrxmlInput = this.getClass().getClassLoader().getResource("report.jrxml").openStream();
		JasperDesign design = JRXmlLoader.load(jrxmlInput);
		JasperReport jasperReport = JasperCompileManager.compileReport(design);
		System.out.println("Report compiled");
		List<Product> prods = this.service.getProducts();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(prods);
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

}
