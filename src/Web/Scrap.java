package Web;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Scrap {
	public static void main(String args[]){
		String page = "http://www.infomoney.com.br/ultimas-noticias/pagina/" ; 
		String selector = ".cm-mg-5-b";
		Scrap2 test = new Scrap2();
		for (int i = 1; i<21;  i++)
			test.addWords(page + i, selector);
		List<String> blackList = test.getBlackList();
		test.getWords().forEach((k,v) -> {
			if ((v >5) && !blackList.contains(k)) 
				System.out.println(k + ": " + v);
			});
		System.out.println("Fim");
	}

	public static class Scrap2 {
		String[] bl = {"a","as","da","das","na","nas",
					   "e","de","em",
					   "o","os","do","dos","no","nos",
					   "um","que","por","se","para"};
		List<String> blackList = new ArrayList<>();
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		
		public Scrap2(){
			for (int i =0 ; i< bl.length; i++)
				blackList.add(bl[i]);
		}
		
		public HashMap<String, Integer> getWords() {return words;}
		public List<String> getBlackList() {return blackList;}
		
		public void addWords(String page, String selector) {
			Document doc = null;
			try {
				doc = Jsoup.connect(page).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements newsHeadlines = doc.select(selector);
			newsHeadlines.forEach((item) -> {
				String[] w = item.text().split(" ");
				for (int i =0; i< w.length; i++){
					String key = Normalizer
					           .normalize(w[i], Normalizer.Form.NFD)
					           .replaceAll("[^\\p{ASCII}]", "")
					           .toLowerCase(); //Normalize string to count
					if ( words.get(key) != null)
						words.replace(key, words.get(key) + 1);
					else
						words.put(key, 1);
				}
				//System.out.println(item.attr("href"));
			});
		}
	}
}