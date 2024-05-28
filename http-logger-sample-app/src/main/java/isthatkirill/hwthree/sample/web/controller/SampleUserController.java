package isthatkirill.hwthree.sample.web.controller;

import isthatkirill.hwthree.sample.web.dto.SampleUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
