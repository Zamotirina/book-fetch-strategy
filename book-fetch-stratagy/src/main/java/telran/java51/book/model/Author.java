package telran.java51.book.model;

import java.util.Set;

import jakarta.persistence.Entity;
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
@EqualsAndHashCode
@Builder
@Entity
public class Author {

	@Id
	String fullName;
	@Singular
	@ManyToMany(mappedBy = "authors")
	Set <Book> books;
}
