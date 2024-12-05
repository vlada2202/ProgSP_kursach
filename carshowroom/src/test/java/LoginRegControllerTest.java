import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carshowroom.controllers.LoginRegController;
import com.carshowroom.models.Users;
import com.carshowroom.models.enums.Role;
import com.carshowroom.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public class LoginRegControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginRegController loginRegController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginRegController).build();
    }
    // 1. Тест получения страницы регистрации
    @Test
    public void testReg_ShouldReturnRegistrationView() throws Exception {
        mockMvc.perform(get("/reg"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("role"));
    }

    // 2. Тест регистрации пользователя с уже существующим именем
    @Test
    public void testAddUser_UsernameAlreadyExists_ShouldReturnRegistrationView() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(new Users("admin", "password", Role.ADMIN)));
        when(userRepository.findByUsername("admin")).thenReturn(new Users("admin", "password", Role.ADMIN));

        mockMvc.perform(post("/reg")
                        .param("username", "admin")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Пользователь c таким именем уже существует существует!"));
    }
}
