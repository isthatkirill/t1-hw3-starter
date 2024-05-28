package isthatkirill.hwthree.sample.web.controller;

import isthatkirill.hwthree.sample.web.dto.SampleUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kirill Emelyanov
 */

@RestController
@RequestMapping("/sample/user")
public class SampleUserController {

    @PostMapping
    public ResponseEntity<SampleUser> addUser(@RequestBody SampleUser sampleUser) {
        // some logic
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("header-for-example", "example")
                .body(sampleUser);
    }

    @GetMapping
    public String error() {
        throw new RuntimeException("error");

    }


}
