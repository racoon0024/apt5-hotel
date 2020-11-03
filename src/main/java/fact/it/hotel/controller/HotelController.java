package fact.it.hotel.controller;

import fact.it.hotel.model.Hotel;
import fact.it.hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @PostConstruct
    public void fillDatabase() {
        hotelRepository.save(new Hotel("h1", "Sprinton Palace", "Geel", "Geelsebaan", 52));
        hotelRepository.save(new Hotel("h25", "Hotelleke aan zee", "Oostende", "Oostendsebaan", 12));
        hotelRepository.save(new Hotel("h29", "De Scheve Brug", "Brugge", "Brugsebaan", 2));
    }

    @GetMapping("/hotels")
    public List<Hotel> getAllHotels(){
        return hotelRepository.findAll();
    }

    @GetMapping("/hotels/{hotelCode}")
    public Hotel getHotelByHotelCode(@PathVariable String hotelCode){
        return hotelRepository.findHotelByHotelCode(hotelCode);
    }

    @PostMapping("/hotels")
    public Hotel addHotel(@RequestBody Hotel newHotel){
        hotelRepository.save((newHotel));
        return newHotel;
    }

    @PutMapping("/hotels/{hotelCode}")
    public Hotel replaceHotel(@RequestBody Hotel updateHotel, @PathVariable String hotelCode){
        Hotel hotel = hotelRepository.findHotelByHotelCode(hotelCode);
        hotel.setName(updateHotel.getName());
        hotel.setCity(updateHotel.getCity());
        hotel.setStreet(updateHotel.getStreet());
        hotel.setNumber(updateHotel.getNumber());
        hotelRepository.save(hotel);
        return hotel;
    }

    @DeleteMapping("/hotels/{hotelCode}")
    public void deleteHotel(@PathVariable String hotelCode){
        Hotel hotel = hotelRepository.findHotelByHotelCode(hotelCode);
        hotelRepository.delete(hotel);
    }

}
