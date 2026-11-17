package ma.covoiturage.covoiturage;

import ma.covoiturage.covoiturage.dto.request.ConducteurCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserCreateRequest;
import ma.covoiturage.covoiturage.dto.response.UserResponse;
import ma.covoiturage.covoiturage.entity.Admin;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Passager;
import ma.covoiturage.covoiturage.mapper.UserMapper;
import ma.covoiturage.covoiturage.repository.UserRepository;
import ma.covoiturage.covoiturage.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

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

        UserResponse saved = userService.createPassger(request);
        assertNotNull(saved);
        assertEquals(userId, saved.getId());
        assertSame(passager.getEmail(),saved.getEmail());
        assertEquals("PASSAGER",saved.getRole());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(passager);
    }


}
