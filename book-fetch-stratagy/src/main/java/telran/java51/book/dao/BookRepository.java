package telran.java51.book.dao;

import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import telran.java51.book.model.Author;
import telran.java51.book.model.Book;


/*
 * 
 * Это сильно упрощенный проект. Сделали специаль, чтобы посмотреть fetch-стратегии
 * 
 * В нем при запуске появляется ошибка ленивой инициализации, то есть система не может инициализировать прокси, так как нет сессии
 * 
 * Проблема в том, что в методе printAuthorsOfBook(), мы сначала находим книгу, потом получаем сет авторов, а потом начинаем распечатывать только их имена
 * 
 * Но проблема в том, что в качестве оптимизации на самом деле при нахождении книги и сета авторов, мы получаем не настоящий сет, а прокси, некую облегченную версию
 * 
 * Соответственно когда мы потом пытаемя у этого сета еще и получить getFullName(). То есть у нас есть прокси (некоеподобие ссылок) и мы у этих прокси пытается выдернуть имя, но  на тот момент сессия уже разорвана, поэтому получить доступ к именам автором не получается
 * 
 * Существуют три способа это исправить:
 * 
 * 1. PersistenceContext(type=PersistenceContextType.EXTENDED) к EntityManager em;
 * 2. В классе Book к аннотации @ManyToMany добавить параметр (fetch = FetchType.EAGER) Можно использовать в продакшене, когда во всех методах мне нужна бубедт именно жадная загрузка, чтобы не убивать производительность
 * 3. Добавить к методу //@Transactional(readOnly = true), чтобы сохранить сессию до момента получения имен. Использовать можно в продакшене
 * 3. (Самый тонкий вариант) Написать query и сделать query.getSingleResult(), а запросе пропсиать left join fetch b.authors. Этот способ не делает весь метод транзакционным, но именно делает жадный запрос только один раз
 */
@Repository
public class BookRepository {
	
	//@PersistenceContext(type=PersistenceContextType.EXTENDED) //Расширенный вариант, но это означает, что	EntityManager удерживает соединение, но получается, что так мы убиваем весь multi-threading. По факту в продакшене не используется
	//@PersistenceContext(type=PersistenceContextType.TRANSACTION)//Стоит по умолчанию
	@PersistenceContext
	EntityManager em;
	
	
	@Transactional
	public void addBooks() {
		
		Author markTwain = Author.builder().fullName("Mark Twain").build();
		
		em.persist(markTwain);
		
		Book pandp = Book.builder().isbn("978-014035073").author(markTwain).title("The Prince and the Pauper").build();
		
		em.persist(pandp);
		
		Author ilf = Author.builder().fullName("Iliya Ilf").build();
		
		Author petrov = Author.builder().fullName("Evgeniy Petrov").build();
		
		em.persist(ilf);
		em.persist(petrov);
		
		Book chairs12 = Book.builder().isbn("978-0810114845").author(ilf).author(petrov).title("12 chairs").build();
		
		em.persist(chairs12);
	}

	//@Transactional(readOnly = true)
	public void printAuthorsOfBook (String isbn) {
		
	//Book book = em.find(Book.class, isbn); //Этот вариант выдаст ошибку ленивой инициализации
		
		//Добавили позже...
		
		TypedQuery <Book> query = em.createQuery("select b from Book b left join fetch b.authors a where b.isbn=?1", Book.class);//важно в запрос добавить left join fetch b.authors
		
		query.setParameter(1, isbn);
		
		Book book = query.getSingleResult();
		
		//Добавили позже.
		
		Set <Author> authors = book.getAuthors();
		
		authors.forEach(x-> System.out.println(x.getFullName()));
	}
}
