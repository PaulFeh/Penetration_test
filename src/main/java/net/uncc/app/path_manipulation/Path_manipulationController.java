package net.uncc.app.path_manipulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/path_manipulation")
public class Path_manipulationController {

	@Autowired
	ResourceLoader resourceLoader;

    @GetMapping("/")
    public String path_manipulation_index() {
        return "path_manipulation/index";
    }

    @GetMapping("/viewFile")
    @ResponseBody
    public Map<String, String> view_file(@RequestParam String file_name) throws Exception {
		Map<String, String> response_data = new HashMap<String, String>();

		try {
			Resource resource = resourceLoader.getResource("classpath:files/" + file_name);
			File file = resource.getFile();
			String text = new String(Files.readAllBytes(file.toPath()));

			response_data.put("status", "success");
			response_data.put("msg", text);
			return response_data;
		} catch (IOException e) {
			e.printStackTrace();
			response_data.put("status", "error");
			response_data.put("msg", "No output found");
			return response_data;
		}
    }

}
