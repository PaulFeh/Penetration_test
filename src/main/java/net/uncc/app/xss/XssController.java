package net.uncc.app.xss;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang.StringEscapeUtils.*;

@Controller
@RequestMapping("/xss")
public class XssController {

    @GetMapping("/")
    public String xss_index() {
        return "xss/index";
    }

    @GetMapping("/body_xss")
    @ResponseBody
    public String body_xss(@RequestParam String body_tagVal) throws Exception {
        return body_tagVal;
    }

    @GetMapping("/textarea_xss")
    @ResponseBody
    public Object textarea_xss(@RequestParam String textarea_tagVal) throws Exception {
        return textarea_tagVal;
    }

    @GetMapping("/js_xss")
    @ResponseBody
    public Object js_xss(@RequestParam String js_tagVal) throws Exception {
        return js_tagVal;
    }

}
