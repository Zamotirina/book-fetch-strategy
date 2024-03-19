package telran.java51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.java51.book.dao.BookRepository;

/*
 * Посколькуэто упрощенный проект для конкретной задачи.
 * 
 * Мы в этом классе добавили implements CommandLineRunner
 * 
 * Методы в репозитории захаркодили
 * 
 * И тут же их запустили
 * 
 * То есть приложение запускается, выполняет два метода и останавливается, потому что у нас нет тут никаких серверов, баз данных и или еще чего-то
 */

@SpringBootApplication
public class BookFetchStratagyApplication implements CommandLineRunner {
	
	@Autowired
	BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookFetchStratagyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		bookRepository.addBooks();
		bookRepository.printAuthorsOfBook("978-0810114845");
		
	}

}
