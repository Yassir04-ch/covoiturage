package ma.covoiturage.covoiturage;

import ma.covoiturage.covoiturage.dto.request.ConducteurCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserCreateRequest;
import ma.covoiturage.covoiturage.dto.response.UserResponse;
import ma.covoiturage.covoiturage.entity.Admin;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Passager;
import ma.covoiturage.covoiturage.exception.ConflictException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.UserMapper;
import ma.covoiturage.covoiturage.repository.UserRepository;
import ma.covoiturage.covoiturage.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    UserCreateRequest request;
    ConducteurCreateRequest conducteurRequest;
    UserResponse response;
    Admin admin;
    Passager passager;
    Conducteur conducteur;
    UUID userId;

    @BeforeEach
    void setUp(){
        userId = UUID.randomUUID();
        request = UserCreateRequest.builder()
                .nom("cherqui")
                .prenom("yassir")
                .email("yassir@gmail.com")
                .telephone("06145654534")
                .build();
        conducteurRequest = ConducteurCreateRequest.builder()
                            .nom("conducteur1")
                            .prenom("test")
                            .email("conducteur@gmail.com")
                            .numeroPermis("JH135246")
                            .build();
        response = UserResponse.builder()
                .id(userId)
                .nom("cherqui")
                .prenom("yassir")
                .email("yassir@gmail.com")
                .telephone("06145654534")
                .build();
    }

    @Test
    void createPassagerTest(){
        passager = Passager.builder()
                .nom("yassir")
                .email("yassir@gmail.com")
                .build();
        response.setRole("PASSAGER");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toPassager(request)).thenReturn(passager);
        when(userMapper.toPassagerResponse(passager)).thenReturn(response);
        when(userRepository.save(passager)).thenReturn(passager);

        UserResponse saved = userServiceImpl.createPassger(request);
        assertNotNull(saved);
        assertEquals(userId, saved.getId());
        assertSame(passager.getEmail(),saved.getEmail());
        assertEquals("PASSAGER",saved.getRole());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(passager);
        verify(userMapper).toPassager(request);
        verify(userMapper).toPassagerResponse(passager);
    }

    @Test
    void createAdminTest() {
        admin = Admin.builder()
                .nom("yassir")
                .email("yassir@gmail.com")
                .build();
        response.setRole("ADMIN");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toAdmin(request)).thenReturn(admin);
        when(userRepository.save(admin)).thenReturn(admin);
        when(userMapper.toAdminResponse(admin)).thenReturn(response);

        UserResponse saved = userServiceImpl.createAdmin(request);

        assertNotNull(saved);
        assertEquals(userId, saved.getId());
        assertEquals("ADMIN", saved.getRole());
        assertEquals("yassir@gmail.com", saved.getEmail());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(admin);
        verify(userMapper).toAdmin(request);
        verify(userMapper).toAdminResponse(admin);
    }

    @Test
    void createConducteurTest() {
        conducteur = Conducteur.builder()
                .nom("conducteur1")
                .email("conducteur@gmail.com")
                .numeroPermis("JH135246")
                .build();
        response.setRole("CONDUCTEUR");
        response.setEmail("conducteur@gmail.com");
        when(userRepository.existsByEmail(conducteurRequest.getEmail())).thenReturn(false);
        when(userMapper.toConducteur(conducteurRequest)).thenReturn(conducteur);
        when(userRepository.save(conducteur)).thenReturn(conducteur);
        when(userMapper.toConducteurResponse(conducteur)).thenReturn(response);

        UserResponse saved = userServiceImpl.createConducteur(conducteurRequest);

        assertNotNull(saved);
        assertEquals(userId, saved.getId());
        assertEquals("CONDUCTEUR", saved.getRole());
        assertEquals("conducteur@gmail.com", saved.getEmail());

        verify(userRepository).existsByEmail(conducteurRequest.getEmail());
        verify(userRepository).save(conducteur);
        verify(userMapper).toConducteur(conducteurRequest);
        verify(userMapper).toConducteurResponse(conducteur);
    }

    @Test
    void createPassagerWithExistingEmailTest() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(ConflictException.class, () -> userServiceImpl.createPassger(request));
        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toPassager(any());
    }

    @Test
    void getByIdTest() {
        passager = Passager.builder()
                .nom("yassir")
                .email("yassir@gmail.com")
                .build();
        response.setRole("PASSAGER");
        when(userRepository.findById(userId)).thenReturn(Optional.of(passager));
        when(userMapper.toPassagerResponse(passager)).thenReturn(response);

        UserResponse saved = userServiceImpl.getById(userId);
        assertNotNull(saved);
        assertEquals(userId, saved.getId());
        assertEquals("PASSAGER", saved.getRole());
        verify(userRepository).findById(userId);
        verify(userMapper).toPassagerResponse(passager);
    }

    @Test
    void getByIdNotFoundTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getById(userId));
        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

}
