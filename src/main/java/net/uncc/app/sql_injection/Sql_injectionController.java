package net.uncc.app.sql_injection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sql_injection")
public class Sql_injectionController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    private Object findDataFromDatabase(String queryString, Object param) {
    	try {
			return jdbcTemplate.queryForList(queryString);
		} catch (Exception e){
    		e.printStackTrace();
    		return new ArrayList<>();
		}
    }

	// ---------------------------------------------- Login as an Employee (1st tab) ----------------------------------------------
	@GetMapping("/")
	public ModelAndView sql_index() {
		String queryString = "SELECT * From Employees";
		List<Map> listOfemployee = (List<Map>) findDataFromDatabase(queryString, null);
		ModelAndView mav = new ModelAndView("sql_injection/index");
		mav.addObject("employeeList", listOfemployee);
		return mav;
	}

	@PostMapping("/")
	@ResponseBody
	public Map<String, String> sql_logged_in(@RequestParam String employee_username, @RequestParam String employee_password, HttpServletRequest request) {
		String queryString = "SELECT * From Employees where username = '" + employee_username + "' and password = '" + employee_password + "'";
		Object[] parameters = {employee_username, employee_password};
		List<Map> listOfemployee = (List<Map>) findDataFromDatabase(queryString, parameters);

		Map<String, String> response_data = new HashMap<String, String>();

		if (listOfemployee.size() == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("logged_in_employee", employee_username);

			response_data.put("status", "success");
			response_data.put("msg", "Successfully logged in as " + listOfemployee.get(0).get("first_name") + " " + listOfemployee.get(0).get("last_name"));
		} else {
			response_data.put("status", "success");
			response_data.put("msg", "No such customer found");
		}

		return response_data;
	}
	// ---------------------------------------------- Login as a Employee (1st tab) ----------------------------------------------

	// ---------------------------------------------- Reset data  (1st tab) ------------------------------------------------------
	@PostMapping("/reset")
	@ResponseBody
	public Map<String, String>  sql_reset(HttpServletRequest request) {
		Map<String, String> response_data = new HashMap<String, String>();
		HttpSession session = request.getSession();
		session.removeAttribute("logged_in_employee");

		response_data.put("status", "success");
		response_data.put("msg", "Successfully reset data ");

		return response_data;
	}
	// ---------------------------------------------- Reset data  (1st tab) ------------------------------------------------------

	// ---------------------------------------------- Update address(2nd tab) ----------------------------------------------------
	@GetMapping("/update")
	public String sql_update() {
		return "sql_injection/update";
	}

	@PostMapping("/update_address")
	@ResponseBody
	public String update_address(@RequestParam String updated_address, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String loggedInUser = (String) session.getAttribute("logged_in_employee");

		if (loggedInUser == null) {
			return  "{\"msg\":\"Please login as employee first.\"}";
		}

		try {
			int updatedEmpInfo = 0;
			// change 'updateQuery' with by applying '?' instead of direct parameter.
			String updateQuery = "UPDATE Employees SET address = '" + updated_address + "' WHERE username = '" + loggedInUser + "'";
			// change in 'jdbcTemplate.update' function by passing parameters so that dynamic input will not harm database.
			updatedEmpInfo = jdbcTemplate.update(updateQuery);
			if (updatedEmpInfo == 0) {
				return "{\"msg\":\"No rows updated.\"}";
			}
			// change to display only logged-in employee's data
			String selectQuery = "SELECT * From Employees";
			List<Map> employeeList = (List<Map>) findDataFromDatabase(selectQuery, null);
			return new ObjectMapper().writeValueAsString(employeeList);
		} catch (Exception e){
			e.printStackTrace();
			return "{\"msg\":\"No rows updated.\"}";
		}
    }
	// ---------------------------------------------- Update address(2nd tab) ----------------------------------------------------
}
