package com.example;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class BookingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingManagementApplication.class, args);
	}
}

@RestController
class BookingRestController {

	@RequestMapping(value = "/bookings", method = RequestMethod.GET)
	Collection<Booking> bookings() {
		return bookingRepository.findAll();
	}

	@RequestMapping(value = "/bookings/{id}", method = RequestMethod.GET)
	Booking getBookingByOne(@PathVariable("id") Long id) {
		return bookingRepository.findOne(id);
	}

	@RequestMapping(value = "/bookings/delete/{id}", method = RequestMethod.DELETE)
	void deleteBooking(@PathVariable("id") Long id) {
		bookingRepository.delete(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	void addBooking(@RequestBody Booking booking) {
		bookingRepository.save(booking);
	}

	@RequestMapping(value = "/bookings/update/{id}", method = RequestMethod.PUT)
	public void updateById(@PathVariable("id") Long id, @RequestBody Booking booking) {

		Booking newBooking = bookingRepository.findById(id);
		newBooking.setBookingName(booking.getBookingName());

		bookingRepository.save(newBooking);
	}

	@Autowired
	BookingRepository bookingRepository;
}

@Entity
class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String bookingName;

	public Long getId() {
		return id;
	}

	public String getBookingName() {
		return bookingName;
	}

	public void setBookingName(String bookingName) {
		this.bookingName = bookingName;
	}

	@Override
	public String toString() {
		return "Booking [id=" + id + ", bookingName=" + bookingName + "]";
	}

}

@Component
class BookingCommandLineRunner implements CommandLineRunner {

	@Override
	public void run(String... arg0) throws Exception {

		for (Booking b : bookingRepository.findAll()) {
			System.out.println(b.toString());
		}
	}

	@Autowired
	BookingRepository bookingRepository;

}

interface BookingRepository extends JpaRepository<Booking, Long> {

	Collection<Booking> findByBookingName(String bookingName);

	Booking findById(Long id);
}
