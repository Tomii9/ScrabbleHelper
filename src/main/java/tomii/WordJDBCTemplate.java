package tomii;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class WordJDBCTemplate implements WordDao{

	private JdbcTemplate jdbcTemplateObject;
	private List<Word> words;
	
	public WordJDBCTemplate() {
		words = new ArrayList<Word>();
	}
	
	@Override
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	@Override
	public void create(String word) {
		String SQL = "insert into WORDS (word, uploader) values (?, ?)";
		jdbcTemplateObject.update(SQL, word, "admin");
		System.out.println("Created new word " + word);
	}

	@Override
	public Word getWord(String string) {
		String SQL = "select word, name from words w, users u where w.uploader = u.id and word = ?";
		Word word = jdbcTemplateObject.queryForObject(SQL, new Object[]{string}, new WordMapper());
		return word;
	}

	@Override
	public List<Word> getWords(String regex) {
		Pattern p = Pattern.compile(regex);
		List<Word> results = new ArrayList<Word>();
		for (Word word : words) {
			Matcher m = p.matcher(word.getWord());
			if (m.matches()) {
				results.add(word);
			}
		}
		return results;
	}

	@Override
	public void delete(String word) {
		String SQL = "Delete from WORDS where word = ?";
		jdbcTemplateObject.update(SQL, word);
		System.out.println("Deleted Word " + word );
		
	}
	
	public List <Word> refreshCache() {
		String SQL = "select word, name from words w, users u where w.uploader = u.id";
		words = jdbcTemplateObject.query(SQL, new WordMapper());
		return words;
	}

}
