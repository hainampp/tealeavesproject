package com.example.tea_leaves_project.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/typetea")
public class TypeteaController {
    @GetMapping("/alltype")
    public ResponseEntity<?> alltype() {
        return null;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletetype(WebRequest webRequest,@PathVariable int id) {
        return null;
    }
}
