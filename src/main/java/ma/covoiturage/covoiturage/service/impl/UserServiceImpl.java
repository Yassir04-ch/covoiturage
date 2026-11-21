package ma.covoiturage.covoiturage.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.ConducteurCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.UserResponse;
import ma.covoiturage.covoiturage.entity.Admin;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Passager;
import ma.covoiturage.covoiturage.entity.User;
import ma.covoiturage.covoiturage.exception.ConflictException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.UserMapper;
import ma.covoiturage.covoiturage.repository.UserRepository;
import ma.covoiturage.covoiturage.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void checkEmail(String email){
        if(userRepository.existsByEmail(email)){
            throw new ConflictException("Email est déja utilisé");
        }
    }

    @Transactional
     public UserResponse createPassger(UserCreateRequest request){
        checkEmail(request.getEmail());
        Passager passager = userRepository.save(userMapper.toPassager(request));
        return userMapper.toPassagerResponse(passager);
    }

    @Transactional
    public UserResponse createAdmin(UserCreateRequest request){
        checkEmail(request.getEmail());
        Admin admin = userRepository.save(userMapper.toAdmin(request));
        return userMapper.toAdminResponse(admin);
    }

    @Transactional
    public UserResponse createConducteur(ConducteurCreateRequest request){
        checkEmail(request.getEmail());
        Conducteur conducteur = userRepository.save(userMapper.toConducteur(request));
        return userMapper.toConducteurResponse(conducteur);
    }

    @Transactional
    public UserResponse getById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found"));
        if(user instanceof Passager passager){
            return userMapper.toPassagerResponse(passager);
        }
        if(user instanceof Conducteur conducteur){
            return userMapper.toConducteurResponse(conducteur);
        }
        if(user instanceof Admin admin){
            return userMapper.toAdminResponse(admin);
        }
        throw new IllegalStateException("Type utilisateur non reconnu");
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request , UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("Utilisateur not found"));
        if(request.getEmail() != null && userRepository.existsByEmailAndIdNot(request.getEmail() ,userId)){
            throw new ConflictException("Email est déja utilisé");
        }
        if(user instanceof Passager passager){
            userMapper.updateUser(request,passager);
            return userMapper.toPassagerResponse(passager);
        }
        if(user instanceof Conducteur conducteur){
            userMapper.updateUser(request,conducteur);
            return userMapper.toConducteurResponse(conducteur);
        }
        if(user instanceof Admin admin){
            userMapper.updateUser(request,admin);
            return userMapper.toAdminResponse(admin);
        }
        throw new IllegalStateException("type utilisateur non reconnu");
    }
}
