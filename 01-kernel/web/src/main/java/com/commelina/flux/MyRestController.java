package com.commelina.flux;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author panyao
 * @date 2017/11/8
 */
@RestController
@RequestMapping("/users")
public class MyRestController {

    private final PersonRepository repository;

    public MyRestController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{user}")
    public Mono<User> getUser(@PathVariable Long user) {
        // ...
        return null;
    }

    @GetMapping("/{user}/customers")
    Flux<Customer> getUserCustomers(@PathVariable Long user) {
        // ...
        return null;
    }

    @DeleteMapping("/{user}")
    public Mono<User> deleteUser(@PathVariable Long user) {



        // ...
        return null;
    }

}
