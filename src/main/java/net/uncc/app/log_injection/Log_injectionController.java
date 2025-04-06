package net.uncc.app.log_injection;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/log_injection")
public class Log_injectionController {

    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/")
    public String log_inject() {
        return "log_injection/index";
    }

    @PostMapping("/")
    @ResponseBody
    public Object log_injected(@RequestParam String log_value) {
        Map<String, String> response_data = new HashMap<String, String>();
        Logger logger = LogManager.getLogger(Log_injectionController.class);
        try {
            SimpleLayout layout = new SimpleLayout();
            FileAppender appender = new FileAppender(layout,"./logs/Custom_log_file.log",true);
            logger.removeAllAppenders();
            logger.addAppender(appender);
            logger.setLevel(Level.DEBUG);
            logger.setAdditivity(true);

            log_value = java.net.URLDecoder.decode(log_value, StandardCharsets.UTF_8.name());
            Integer parsed_log_value = Integer.parseInt(log_value);
            logger.info("Value to log: " + parsed_log_value);

            response_data.put("status", "success");
            response_data.put("msg", "Successfully logged without error");
            return response_data;
        } catch (Exception e) {
            logger.info("After exception: " + log_value);
            response_data.put("status", "error");
            response_data.put("msg", "Successfully logged error");
            return response_data;
        }
    }

    @GetMapping("/view")
    @ResponseBody
    public ModelAndView log_view(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("log_injection/view");
        String text = "";
        try {
            File file = new File("./logs/Custom_log_file.log");
            text = new String(Files.readAllBytes(file.toPath())).replace("\n", "</br>");
        } catch (IOException e) {
            e.printStackTrace();
            text = "No log found";
        }
        mav.addObject("log_data", text);
        return mav;
    }

}
