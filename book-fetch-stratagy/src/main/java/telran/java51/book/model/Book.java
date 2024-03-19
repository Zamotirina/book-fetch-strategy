package telran.java51.book.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;


@AllArgsConstructor
@NoArgsConstructor
@Getter
//@Setter
@EqualsAndHashCode(of="isbn")
@Builder
@Entity
public class Book {

	@Id
	String isbn;
	String title;
	@Singular
	@ManyToMany//(fetch = FetchType.EAGER)// Это жадная загрузка, то есть мы говорим системе сразу выгружать не прокси, а авторов. Можно использовать в продакшене, когда во всех методах мне нужна бубедт именно жадная загрузка
	//@ManyToMany(fetch = FetchType.LAZY)//Стоит по умолчанию
	Set <Author> authors;
	
}
