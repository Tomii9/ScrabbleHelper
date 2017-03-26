package tomii;

import java.util.List;
import javax.sql.DataSource;

public interface WordDao {
	public void setDataSource(DataSource ds);
	public void create(String word);
	public Word getWord(String word);
	public List<Word> getWords(String regex);
	public void delete(String word);
}
