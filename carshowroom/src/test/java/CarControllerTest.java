import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carshowroom.controllers.CarsController;
import com.carshowroom.models.Cars;
import com.carshowroom.repositories.CarRepository;
import com.carshowroom.repositories.CommentRepository;
import com.carshowroom.repositories.DealershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CarControllerTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private DealershipRepository dealershipRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CarsController carsController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carsController).build();
    }

    // 1. Тест проверки получения информации о машине по ID
    @Test
    public void testCarDetails_ShouldReturnCarView() throws Exception {
        Long carId = 1L;
        Cars car = new Cars(); // Создайте экземпляр Cars с необходимыми данными
        when(carRepository.existsById(carId)).thenReturn(true);
        when(carRepository.getReferenceById(carId)).thenReturn(car);

        mockMvc.perform(get("/cars/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(view().name("car"))
                .andExpect(model().attributeExists("s"));
    }


    // 2. Тест получения информации о существующем автомобиле
    @Test
    public void testGetCar_ExistingId_ShouldReturnCarView() throws Exception {
        Long carId = 1L;
        Cars car = new Cars();
        when(carRepository.existsById(carId)).thenReturn(true);
        when(carRepository.getReferenceById(carId)).thenReturn(car);

        mockMvc.perform(get("/cars/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("s"))
                .andExpect(view().name("car"));
    }

    // 3. Тест получения информации о несуществующем автомобиле
    @Test
    public void testGetCar_NonExistingId_ShouldRedirectToCatalog() throws Exception {
        Long carId = 99L;
        when(carRepository.existsById(carId)).thenReturn(false);

        mockMvc.perform(get("/cars/{id}", carId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog"));
    }

    // 4. Тест удаления автомобиля
    @Test
    public void testDeleteCar_ShouldRedirectToCatalog() throws Exception {
        Long carId = 1L;

        mockMvc.perform(get("/cars/delete/{id}", carId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog/all"));

        verify(carRepository).deleteById(carId);
    }
}