package net.uncc.app.command_injection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/command_injection")
public class Command_injectionController {

    @GetMapping("/")
    public String command_inject() {
        return "command_injection/index";
    }

    @PostMapping("/output/")
    @ResponseBody
    public Object command_injected(@RequestParam String ip_address) {
        Map<String, String> response_data = new HashMap<String, String>();
        try {
            String output = "";
            String[] command = {"/bin/bash", "-c", "ping -c 3 " + ip_address};
            Process proc = Runtime.getRuntime().exec(command);
            proc.waitFor();

            String line = "";
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader errorStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            while ((line = inputStream.readLine()) != null) {
                output += line + "<br/>";
            }
            inputStream.close();
            while ((line = errorStream.readLine()) != null) {
                output += line + "<br/>";
            }
            errorStream.close();
            proc.waitFor();

            response_data.put("status", "success");
            response_data.put("msg", output);
            return response_data;
        } catch (Exception e) {
            e.printStackTrace();
            response_data.put("status", "error");
            response_data.put("msg", "No output found");
            return response_data;
        }
    }

}
