package subham.loginregdemo.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import subham.loginregdemo.models.Book;


public interface BookRepository extends CrudRepository<Book, Long>{
    List<Book> findAll();
}

