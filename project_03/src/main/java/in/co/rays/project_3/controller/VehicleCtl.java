package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.VehicleDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.VehicleModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "VehicleCtl",urlPatterns = { "/ctl/VehicleCtl" })
public class VehicleCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CollegeCtl.class);

	protected void preload(HttpServletRequest request) {
		HashMap  map = new HashMap();
		map.put("Red", "Red");
		map.put("Blue", "Blue");
		map.put("Yellow", "Yellow");


		request.setAttribute("tlist", map);
	}
	
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("purchseDate"))) {
			request.setAttribute("purchseDate", PropertyReader.getValue("error.require", "purchseDate"));
			pass = false;
		}else if (!DataValidator.isDate(request.getParameter("purchseDate"))) {
			request.setAttribute("purchseDate", PropertyReader.getValue("error.date", "purchseDate"));
			pass = false;
		}else if (!DataValidator.isValidAge(request.getParameter("purchseDate"))) {
			
			request.setAttribute("purchseDate", "Age Must be greater then 18 year");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("number"))) {
			request.setAttribute("number", PropertyReader.getValue("error.require", "number"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("number"))) {
			request.setAttribute("number", "Number must contains alphabets only");
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("insuranceAmount"))) {
			request.setAttribute("insuranceAmount", PropertyReader.getValue("error.require", "insuranceAmount"));
			pass = false;
		
		}else if (DataValidator.isTooLong(request.getParameter("insuranceAmount"), 14)) {
		    request.setAttribute("insuranceAmount", "insuranceAmount too long not contaion more then 15 words");
		    pass = false;
			
		}
		if (DataValidator.isNull(request.getParameter("colour"))) {
			request.setAttribute("colour", PropertyReader.getValue("error.require", "colour"));
			pass = false;

		}
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		VehicleDTO dto = new VehicleDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setNumber(DataUtility.getString(request.getParameter("number")));
		dto.setPurchaseDate(DataUtility.getDate(request.getParameter("purchseDate")));
		
		dto.setInsuranceAmount(DataUtility.getInt(request.getParameter("insuranceAmount")));
		
		dto.setColour(DataUtility.getString(request.getParameter("colour")));

		populateBean(dto, request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));
		VehicleModelInt model = ModelFactory.getInstance().getVehicleModel();
		if (id > 0 || op != null) {
			VehicleDTO dto;
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

		VehicleModelInt model = ModelFactory.getInstance().getVehicleModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			VehicleDTO dto = (VehicleDTO) populateDTO(request);

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
				ServletUtility.setErrorMessage("VehicleName Already Exists", request);
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.Vehicle_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.Vehicle_LIST_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.VEHICLE_VIEW;
	}

}

