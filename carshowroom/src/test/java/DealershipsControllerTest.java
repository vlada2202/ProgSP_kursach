import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carshowroom.controllers.DealershipsController;
import com.carshowroom.models.Dealerships;
import com.carshowroom.repositories.DealershipRepository;
import com.carshowroom.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class DealershipsControllerTest {


    @Mock
    private DealershipRepository dealershipRepository;

    @InjectMocks
    private DealershipsController dealershipsController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dealershipsController).build();
    }

    // 1. Тест получения информации об автосалоне по ID
    @Test
    public void testGetDealershipById_ShouldReturnDealershipView() throws Exception {
        Long dealershipId = 1L;
        Dealerships dealership = new Dealerships();
        when(dealershipRepository.getReferenceById(dealershipId)).thenReturn(dealership);

        mockMvc.perform(get("/dealerships/{id}", dealershipId))
                .andExpect(status().isOk())
                .andExpect(view().name("dealership"))
                .andExpect(model().attributeExists("dealership"));
    }

    // 2. Тест удаления дилерского центра
    @Test
    public void testDeleteDealership_ShouldRedirectToDealerships() throws Exception {
        Long dealershipId = 1L;
        mockMvc.perform(get("/dealerships/delete/{id}", dealershipId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dealerships"));

        verify(dealershipRepository).deleteById(dealershipId);
    }

    // 3. Тест на наличие ошибки при редактировании дилерского центра (не существующий ID)
    @Test
    public void testEditDealershipNonExistingId_ShouldRedirectToDealerships() throws Exception {
        Long dealershipId = 99L;
        when(dealershipRepository.existsById(dealershipId)).thenReturn(false);

        mockMvc.perform(get("/dealerships/edit/{id}", dealershipId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dealerships"));
    }

    // 4. Тест на отсутствие дилерского центра (редактирование по несуществующему ID)
    @Test
    public void testEditDealership_NonExistingId_ShouldRedirectToDealerships() throws Exception {
        Long dealershipId = 99L;
        when(dealershipRepository.existsById(dealershipId)).thenReturn(false);

        mockMvc.perform(get("/dealerships/edit/{id}", dealershipId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dealerships"));
    }
}