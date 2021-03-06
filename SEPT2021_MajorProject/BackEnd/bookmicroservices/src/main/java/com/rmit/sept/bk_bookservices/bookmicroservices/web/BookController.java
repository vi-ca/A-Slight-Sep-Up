package com.rmit.sept.bk_bookservices.bookmicroservices.web;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rmit.sept.bk_bookservices.bookmicroservices.model.Book;
import com.rmit.sept.bk_bookservices.bookmicroservices.service.IPageService;
import com.rmit.sept.bk_bookservices.bookmicroservices.service.IService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/books")   
//Allows communication to database from frontend to backend
public class BookController implements Resource<Book>{

	//Connects to remote web service
    @Autowired
	private IService<Book> bookService;
	
	//Pager extension
	@Autowired
	private IPageService<Book> bookPageService;

	@Override
	public ResponseEntity<Page<Book>> findAll(Pageable pageable, String searchText) {
		return new ResponseEntity<>(bookPageService.findAll(pageable, searchText), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Page<Book>> findAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		return new ResponseEntity<>(bookPageService.findAll(
				PageRequest.of(
						pageNumber, pageSize,
						sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
				)
		), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Book> findById(Long id) {
		return new ResponseEntity<>(bookService.findById(id).get(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Book> save(Book book) {
		return new ResponseEntity<>(bookService.saveOrUpdate(book), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Book> update(Book book) {
		return new ResponseEntity<>(bookService.saveOrUpdate(book), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteById(Long id) {
		return new ResponseEntity<>(bookService.deleteById(id), HttpStatus.OK);
	}

}
