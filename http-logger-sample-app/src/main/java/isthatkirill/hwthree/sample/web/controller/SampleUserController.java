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
        // some logic to add
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("header-for-example", "example")
                .body(sampleUser);
    }

    @GetMapping
    public ResponseEntity<SampleUser> getUser(@RequestParam(name = "age", required = false) Integer age,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "surname", required = false) String surname) {
        // some logic to get
        return ResponseEntity.ok(new SampleUser(age, name, surname));
    }

    @PatchMapping("/{name}")
    public ResponseEntity<SampleUser> updateUser(@RequestBody SampleUser sampleUser, @PathVariable String name) {
        // some logic to update user using his name
        return ResponseEntity.ok(sampleUser);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteUser(@PathVariable String name) {
        // some logic to delete user using his name
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/exception")
    public ResponseEntity<SampleUser> getWithException() {
        // some logic throwing an exception (f.e. IllegalStateException, RuntimeException etc)
        throw new IllegalStateException("Illegal user state");
    }



}
