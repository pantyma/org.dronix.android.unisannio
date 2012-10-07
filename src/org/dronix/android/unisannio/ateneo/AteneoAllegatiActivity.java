package org.dronix.android.unisannio.ateneo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.dronix.android.unisannio.News;
import org.dronix.android.unisannio.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.Bundle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AteneoAllegatiActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allegati_list);
		
		Bundle b = getIntent().getExtras();
		News news = b.getParcelable("newsate");
		String UrlAllegati = news.getUrl();
		
		ListView listAllegati = (ListView) findViewById(R.id.allegatiList);
		
		//Prendo la lista degli allegati
		Document doc;
		try {
			doc = Jsoup.connect(UrlAllegati).timeout(10000).get();
			Elements newsItems = doc.select("div.meta > table > tbody > tr > td > p > select > option");
			
			int count =newsItems.size();
			List<String> items = new ArrayList<String>() ;
			//Log.i("ATENEO ALLEGATI ACTIVITY", "Il numero di allegati e` : "+ count);
			for (int i = 1; i < count;  i++) {
				
				Element allegato = newsItems.get(i);
				if (allegato != null) {
					items.add(allegato.text());
				}
			}
			
			ListAdapter lsAll = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
			listAllegati.setAdapter(lsAll);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
