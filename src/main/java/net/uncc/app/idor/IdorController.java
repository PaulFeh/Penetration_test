package net.uncc.app.idor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/idor")
public class IdorController {

	@Autowired
	ResourceLoader resourceLoader;

    Map<String, Map<String, String>> customerInfo = new HashMap<String, Map<String, String>>();
	
    private Map<String, Map<String, String>> read_customer_info_from_json() {   
    	try {
        	Resource resource=resourceLoader.getResource("classpath:files/customer.json");
			File file = resource.getFile();
			String text = new String(Files.readAllBytes(file.toPath()));
			ObjectMapper mapper = new ObjectMapper();
			customerInfo = mapper.readValue(text, Map.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return customerInfo;
    }

    // ---------------------------------------------- Login as a customer (1st tab: 1st operation) ----------------------------------------------
    @GetMapping("/")
    public String idor_login() {
        return "idor/index";
    }
    
    @PostMapping("/")
    @ResponseBody
    public Map<String, String> idor_logged_in(@RequestParam String customer_email, @RequestParam String customer_password, HttpServletRequest request) {
    	customerInfo = read_customer_info_from_json();
    	Map<String, String> response_data = new HashMap<String, String>();
    	
        if (customerInfo.containsKey(customer_email)) {
            if (customerInfo.get(customer_email).get("password").equals(customer_password)) {
            	HttpSession session = request.getSession();
            	
            	Map<String, String> curr_cust_Info = customerInfo.get(customer_email);
            	curr_cust_Info.put("email", customer_email);
            	session.setAttribute("logged_in_customer", curr_cust_Info);
            	
            	response_data.put("status", "success");
            	response_data.put("msg", "Successfully logged in as " + customerInfo.get(customer_email).get("name"));

            } else {
            	response_data.put("status", "success");
            	response_data.put("msg", "Please check your input");
            }
        } else {
        	response_data.put("status", "success");
        	response_data.put("msg", "No such customer found");
        }
        
        return response_data;
    }
	// ---------------------------------------------- Login as a customer (1st tab: 1st operation) ----------------------------------------------

	// ---------------------------------------------- Reset data  (1st tab: 2nd operation) ------------------------------------------------------
	@PostMapping("/reset")
	@ResponseBody
	public Map<String, String>  idor_reset(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("logged_in_customer");

		Map<String, String> response_data = new HashMap<String, String>();
		response_data.put("status", "success");
		response_data.put("msg", "Successfully reset data ");

		return response_data;
	}
	// ---------------------------------------------- Reset data  (1st tab: 2nd operation) ------------------------------------------------------

	// ---------------------------------------------- Get own profile (2nd tab: 1st operation) -----------------------------------
    @GetMapping("/profile")
    public ModelAndView idor_profile(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Map<String, String> session_customerInfo = (Map<String, String>) session.getAttribute("logged_in_customer");
		ModelAndView mav = new ModelAndView("idor/profile");
		if (session.getAttribute("logged_in_customer") == null) {
			return new ModelAndView("idor/index");
		}
		mav.addObject("logged_emp_id", session_customerInfo.get("id"));
		return mav;
    }

    @GetMapping("/own")
    @ResponseBody
    public String idor_profile_own(HttpServletRequest request) throws Exception {
    	HttpSession session = request.getSession();

		ObjectMapper objectMapper = new ObjectMapper();
    	if (session.getAttribute("logged_in_customer") == null) {
			Map<String, String> response_data = new HashMap<String, String>();
			response_data.put("status", "error");
			response_data.put("msg", "Please login as customer first");
			return  objectMapper.writeValueAsString(response_data);
		}

    	Map<String, String> customer_sessionInfo = (Map<String, String>) session.getAttribute("logged_in_customer");

    	// do changes here to restrict viewing all data of authenticated customer. You can delete data from 'session_customerInfo' dictionary which you do not want to display.

    	List list = new ArrayList<>();
    	String json = "";
    	try {
    		list.add(customer_sessionInfo);
    		json = objectMapper.writeValueAsString(list);

        } catch (JsonProcessingException e) {
    		json = "{\"status\":\"error\"}";
            e.printStackTrace();
        }
    	return json;
    }
	// ---------------------------------------------- Get own profile (2nd tab: 1st operation) -----------------------------------

	// ---------------------------------------------- Get other customer profile (2nd tab: 2nd operation) ------------------------
    @GetMapping("/info")
    @ResponseBody
    public String idor_performance_info(@RequestParam String requested_customer_id, HttpServletRequest request) throws Exception {
    	Map<String, String> requested_info = new HashMap<String, String>();
    	HttpSession session = request.getSession();
		String return_val = "";
    	// Do some changes so that only authenticated customer's profile can be viewed. You can authenticate logged-in customer id with entered customer id (customer_id)

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			if (session.getAttribute("logged_in_customer") == null) {
				requested_info.put("status", "error");
				requested_info.put("msg", "Please login as customer first");
				return objectMapper.writeValueAsString(requested_info);
			}

			for (Map.Entry<String, Map<String, String>> entry : customerInfo.entrySet()) {
				String key = entry.getKey();
				Map<String, String> value = entry.getValue();
				String each_customer_id = value.get("id");

				if (requested_customer_id.equals(each_customer_id)) {
					requested_info.put("msg", "You have successfully seen data");
					requested_info.put("name", value.get("name"));
					requested_info.put("email", key);
					requested_info.put("performance", value.get("performance"));
				}
			}
			return_val = objectMapper.writeValueAsString(requested_info);
		}
    	catch (JsonProcessingException e) {
			e.printStackTrace();
			return_val = "{\"status\":\"error\"}";
        }
		return return_val;
    }
	// ---------------------------------------------- Get other customer profile (2nd tab: 2nd operation) ------------------------

}
