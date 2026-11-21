package ma.covoiturage.covoiturage.service;

import ma.covoiturage.covoiturage.dto.request.ConducteurCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse createPassger(UserCreateRequest request);

    UserResponse createAdmin(UserCreateRequest request);

    UserResponse createConducteur(ConducteurCreateRequest request);

    UserResponse getById(UUID id);

    UserResponse updateUser(UserUpdateRequest request , UUID userId);
}
