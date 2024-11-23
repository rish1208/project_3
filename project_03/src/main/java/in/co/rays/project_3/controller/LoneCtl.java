package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.LoneDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.LoneModelInt;
import in.co.rays.project_3.model.ModelFactory;

import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "LoneCtl",urlPatterns = { "/ctl/LoneCtl" })
public class LoneCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CollegeCtl.class);

	protected void preload(HttpServletRequest request) {
		HashMap  map = new HashMap();
		map.put("SBI", "SBI");
		map.put("UBI", "UBI");
		map.put("PNB", "PNB");;

		request.setAttribute("tlist", map);
	}
	
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("loneStartDate"))) {
			request.setAttribute("loneStartDate", PropertyReader.getValue("error.require", "loneStartDate"));
			pass = false;
		}else if (!DataValidator.isDate(request.getParameter("loneStartDate"))) {
			request.setAttribute("loneStartDate", PropertyReader.getValue("error.date", "loneStartDate"));
			pass = false;
		}else if (!DataValidator.isValidAge(request.getParameter("loneStartDate"))) {
			
			request.setAttribute("loneStartDate", "Age Must be greater then 18 year");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("customerId"))) {
			request.setAttribute("customerId", PropertyReader.getValue("error.require", "customerId"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("customerId"))) {
			request.setAttribute("customerId", "customerId must contains alphabets only");
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("loneAmount"))) {
			request.setAttribute("loneAmount", PropertyReader.getValue("error.require", "loneAmount"));
			pass = false;
		
		}else if (DataValidator.isTooLong(request.getParameter("loneAmount"), 14)) {
		    request.setAttribute("loneAmount", "loneAmount too long not contaion more then 15 words");
		    pass = false;
			
	     }
		if (DataValidator.isNull(request.getParameter("interestRate"))) {
			request.setAttribute("interestRate", PropertyReader.getValue("error.require", "interestRate"));
			pass = false;
			
		}else if (DataValidator.isTooLong(request.getParameter("interestRate"), 14)) {
		    request.setAttribute("interestRate", "interestRate too long not contaion more then 15 words");
		    pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "mobile"));
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
			request.setAttribute("mobile", "Please Enter Valid Mobile Number");
			pass = false;
		}
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		LoneDTO dto = new LoneDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setLoneStartDate(DataUtility.getDate(request.getParameter("loneStartDate")));
		dto.setCustomerId(DataUtility.getString(request.getParameter("customerId")));
		dto.setLoneAmount(DataUtility.getInt(request.getParameter("loneAmount")));
		dto.setInterestRate(DataUtility.getInt(request.getParameter("interestRate")));
		dto.setMobile(DataUtility.getString(request.getParameter("mobile")));

		populateBean(dto, request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));
		LoneModelInt model = ModelFactory.getInstance().getLoneModel();
		if (id > 0 || op != null) {
			LoneDTO dto;
			try {
					dto = model.findByPK(id);
					ServletUtility.setDto(dto, request);

				} catch (ApplicationException e) {
					log.error(e);
					ServletUtility.handleException(e, request, response);
					return;
				
				}
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		LoneModelInt model = ModelFactory.getInstance().getLoneModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			LoneDTO dto = (LoneDTO) populateDTO(request);

			try {
				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setDto(dto, request);

					ServletUtility.setSuccessMessage("Record Successfully Updated", request);

				} else {
					System.out.println("college add" + dto + "id...." + id);
					// long pk
					model.add(dto);
					ServletUtility.setSuccessMessage("Record Successfully Saved", request);
				}
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("ProductName Already Exists", request);
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.LONE_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.LONE_LIST_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.LONE_VIEW;
	}

}
