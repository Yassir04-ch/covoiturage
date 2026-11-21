package ma.covoiturage.covoiturage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.ConducteurCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.UserResponse;
import ma.covoiturage.covoiturage.service.UserService;
import ma.covoiturage.covoiturage.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs" , description = "Gestions des Utilisateurs")
public class UserController {
    private final UserService userService;

    @PostMapping("/passagers")
    public ResponseEntity<UserResponse> createPassager(@Valid @RequestBody UserCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createPassger(request));
    }

    @PostMapping("/admins")
    public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody UserCreateRequest request){
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAdmin(request));
    }

    @PostMapping("/conducteurs")
    public ResponseEntity<UserResponse> createConducteur(@Valid @RequestBody ConducteurCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createConducteur(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest request , @PathVariable UUID userId){
        return ResponseEntity.ok(userService.updateUser(request , userId));
    }
}
