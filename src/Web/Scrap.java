package Web;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Scrap {
	public static void main(String args[]){
		String page = "http://www.infomoney.com.br/ultimas-noticias/pagina/" ; 
		String selector = ".cm-mg-5-b";
		CountWords test = new CountWords();
		for (int i = 1; i<41;  i++)
			test.addWords(page + i, selector);
		
		System.out.println("**** Todas as palavras *****");
		test.getWords().forEach((item) -> { System.out.println(item.key + ": " + item.value);});
		System.out.println("**** Top *****");
		test.top(10).forEach((item) -> { System.out.println(item.key + ": " + item.value);});
		System.out.println("Fim");
	}

	static class WordData{
		String key;
		Integer value;
		public WordData(String _key, Integer _value){
			key = _key;
			value = _value;
		}
		
		 /*Comparator for sorting the list by Student Name*/
	    public final static Comparator<WordData> ValueComparator = new Comparator<WordData>() {
	    	public int compare(WordData w1, WordData w2) {
	    		//Descending order
	    		return  w2.value.compareTo(w1.value);
	    	}
	    };

	    /*Comparator for sorting the list by roll no*/
	    public final Comparator<WordData> KeyComparator = new Comparator<WordData>() {
	    	public int compare(WordData w1, WordData w2) {
	    		//Descending order
	    		return  w2.key.compareTo(w1.key);
	    	}
		};
	    
		/*@Override
	    public String toString() {
	        return "[ rollno=" + rollno + ", name=" + studentname + ", age=" + studentage + "]";
	    }*/
	}
	
	public static class CountWords {

		String[] blackList = {"a","as","da","das","na","nas",
					   "e","de","em",
					   "o","os","do","dos","no","nos",
					   "um","que","por","se","para","com","diz","mais","apos"};
		List<WordData> words = new ArrayList<>();
				
		public CountWords(){}		
		
		public List<WordData> getWords() {return words;}
		
		//return top max from words
		public List<WordData> top(int max){
			Collections.sort(words, WordData.ValueComparator);
			return words.subList(0, max);
		}
		
		public void addItem(String key){
			
			//check blacklist
			for (int i =0; i<blackList.length;i++)
				if (key.compareTo(blackList[i]) == 0)
					return;
			
			//find item
			WordData wd = getWordDataByKey(key);
			if (wd != null)
				wd.value = wd.value +1;
			else
				words.add(new WordData(key, 1));
		}
		
		public WordData getWordDataByKey(String key){
			for (int i=0; i< words.size();i++)	
				if (key.compareTo(words.get(i).key) == 0)
					return words.get(i);
			return null;
		}
		
		public void addWords(String page, String selector) {
			Document doc = null;
			try {
				doc = Jsoup.connect(page).get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Elements newsHeadlines = doc.select(selector);
			newsHeadlines.forEach((item) -> {
				String[] w = item.text().split(" ");
				for (int i =0; i< w.length; i++)
					addItem(normalizeString(w[i]));
			});
		}
		
		private String normalizeString(String s){
			return Normalizer.normalize(s, Normalizer.Form.NFD)
			          .replaceAll("[^\\p{ASCII}]", "")
			          .toLowerCase(); //Normalize string to count
		}
	}
}