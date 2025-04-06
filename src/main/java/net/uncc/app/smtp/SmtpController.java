package net.uncc.app.smtp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/smtp_injection")

public class SmtpController {
	@Autowired
	ResourceLoader resourceLoader;

    @GetMapping("/")
    public String smtp_header_index() {
        return "smtp_injection/index";
    }
    
    @GetMapping("/form")
    @ResponseBody
    public String smtp_header_submit(@RequestParam String customer_firstName,@RequestParam String customer_email,@RequestParam String customer_comments) {
    	
    	String name = customer_firstName;
    	String email = customer_email;
    	String comment = customer_comments;
    	String to = "root@localhost";
    	String subject = "My Subject";

    	String headers = "From:" + name + "\\n" + " to:" + email + "\\n";
    	String[] split = headers.split("\\\\n");
    	String y="";
    	for (int i = 0; i < split.length; i++) {
			y += split[i];
			y += "<br>";
		}
    	System.out.println(y);
    	return y + " Message:" + comment;
    }
    

}







