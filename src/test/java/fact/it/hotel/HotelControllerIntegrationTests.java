package fact.it.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fact.it.hotel.model.Hotel;
import fact.it.hotel.repository.HotelRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HotelControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel hotel1 = new Hotel("H01", "Sprinton Palace", "Geel", "Geelsebaan", 52);
    private Hotel hotel2 = new Hotel("H02", "Hotelleke aan zee", "Oostende", "Oostendsebaan", 12);
    //private Hotel hotel3 = new Hotel("H03", "De Scheve Brug", "Brugge", "Brugsebaan", 2);

    @BeforeEach
    public void beforeAllTests() {
        hotelRepository.deleteAll();
        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);
        //hotelRepository.save(hotel3);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        hotelRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenHotel_whenGetHotelByHotelCode_thenReturnJsonHotel() throws Exception {
        mockMvc.perform(get("/hotels/{hotelCode}", "H01"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotelCode", is("H01")))
                .andExpect(jsonPath("$.name", is("Sprinton Palace")))
                .andExpect(jsonPath("$.city", is("Geel")))
                .andExpect(jsonPath("$.street", is("Geelsebaan")))
                .andExpect(jsonPath("$.number", is(52)));
    }

    @Test
    public void whenGetHotels() throws Exception {
        mockMvc.perform(get("/hotels"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].hotelCode", is("H01")))
                .andExpect(jsonPath("$[0].name", is("Sprinton Palace")))
                .andExpect(jsonPath("$[0].city", is("Geel")))
                .andExpect(jsonPath("$[0].street", is("Geelsebaan")))
                .andExpect(jsonPath("$[0].number", is(52)))
                .andExpect(jsonPath("$[1].hotelCode", is("H02")))
                .andExpect(jsonPath("$[1].name", is("Hotelleke aan zee")))
                .andExpect(jsonPath("$[1].city", is("Oostende")))
                .andExpect(jsonPath("$[1].street", is("Oostendsebaan")))
                .andExpect(jsonPath("$[1].number", is(12)));
    }

    @Test
    public void whenPostHotel_thenReturnJsonHotel() throws Exception {
        Hotel hotel9 = new Hotel("H09", "Den Delper", "Geel", "GeelWest", 162);

        mockMvc.perform(post("/hotels")
                .content(mapper.writeValueAsString(hotel9))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotelCode", is("H09")))
                .andExpect(jsonPath("$.name", is("Den Delper")))
                .andExpect(jsonPath("$.city", is("Geel")))
                .andExpect(jsonPath("$.street", is("GeelWest")))
                .andExpect(jsonPath("$.number", is(162)));
    }

    @Test
    public void givenHotel_whenPutHotel_thenReturnJsonHotel() throws Exception {
        Hotel updatedHotel = new Hotel("H01", "New Hotel", "New City", "New Street", 333);

        mockMvc.perform(put("/hotels/{hotelCode}", "H01")
                .content(mapper.writeValueAsString(updatedHotel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotelCode", is("H01")))
                .andExpect(jsonPath("$.name", is("New Hotel")))
                .andExpect(jsonPath("$.city", is("New City")))
                .andExpect(jsonPath("$.street", is("New Street")))
                .andExpect(jsonPath("$.number", is(333)));
    }

    @Test
    public void givenHotel_whenDeleteHotel_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/hotels/{hotelCode}", "H01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoHotel_whenDeleteHotel_thenStatusNotFound() throws Exception {

        mockMvc.perform(delete("/hotels/{hotelCode}", "H06")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
