package net.uncc.app.ssrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ssrf")
public class SsrfController {

	@Autowired
	ResourceLoader resourceLoader;

    Map<String, Map<String, String>> productInfo = new HashMap<String, Map<String, String>>();
	
    private Map<String, Map<String, String>> read_product_info_from_json() {
    	try {
        	Resource resource = resourceLoader.getResource("classpath:files/product.json");
			File file = resource.getFile();
			String text = new String(Files.readAllBytes(file.toPath()));
			ObjectMapper mapper = new ObjectMapper();
			productInfo = mapper.readValue(text, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return productInfo;
    }

	@PostMapping("/product/item/")
	@ResponseBody
	public Object product_item_post(@RequestParam String id) {
    	try {
			productInfo = read_product_info_from_json();
			String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString((productInfo.get("product")).get(id));
			json = json.replace("\n", "</br>").replace(" ", "&nbsp;");
			return json;
		} catch (Exception x) {
			return "No results found";
		}
	}

	@PostMapping("/product/design/")
	@ResponseBody
	public Object product_design_post(@RequestParam String id) {
		try {
			productInfo = read_product_info_from_json();
			String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString((productInfo.get("product_design")).get(id));
			json = json.replace("\n", "</br>").replace(" ", "&nbsp;");
			return json;
		} catch (Exception x) {
			return "No results found";
		}
	}

	@GetMapping("/")
	public String product_search() {
		return "ssrf/index";
	}

	@PostMapping("/")
	@ResponseBody
	public Object product_search_post(@RequestParam String program, @RequestParam String parameter, @RequestParam String param_value) {
		Map<String, String> response_data = new HashMap<String, String>();
		String msg = "";
		try {
			URL obj = new URL("http://localhost:8081/ssrf/product/" + program + "/?" + parameter + "=" + param_value);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.flush();
			os.close();

			int responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				msg = response.toString().replace("{", "").replace("}", "");
			} else {
				msg = "Failed operation";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Failed operation";
		}
		response_data.put("status", "success");
		response_data.put("msg", msg);
		return response_data;
	}


}
