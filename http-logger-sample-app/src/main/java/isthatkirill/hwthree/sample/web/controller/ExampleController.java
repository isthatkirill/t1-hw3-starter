package isthatkirill.hwthree.sample.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kirill Emelyanov
 */

@RestController
@RequestMapping("/test")
public class ExampleController {

    @GetMapping
    public String get() {
        return "get";
    }

}
