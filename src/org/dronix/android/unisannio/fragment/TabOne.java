/*******************************************************************************
 * Copyright 2012 Ivan Morgillo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.dronix.android.unisannio.fragment;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.dronix.android.unisannio.LazyAdapter;
import org.dronix.android.unisannio.News;
import org.dronix.android.unisannio.R;
import org.dronix.android.unisannio.R.id;
import org.dronix.android.unisannio.R.layout;
import org.dronix.android.unisannio.R.string;
import org.dronix.android.unisannio.ateneo.AteneoAllegatiActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class TabOne extends Fragment {
	private String URL = "http://www.unisannio.it/notizie/avvisi/index.php";
	private String URLAllegati = "http://www.unisannio.it/notizie/avvisi/";
	private ArrayList<News> news;
	private LazyAdapter mAdapter;
	private PullToRefreshListView mPullRefreshListView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tabone, container, false);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
	    
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {				
				mPullRefreshListView.setLastUpdatedLabel(DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));
				
				new NewsRetriever().execute();
			}
		});

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		news = new ArrayList<News>();
		news.add(new News("", getString(R.string.pull)));

		mAdapter = new LazyAdapter(getActivity(), news);

		actualListView.setAdapter(mAdapter);
		
		actualListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Log.i("TABONE CLICK ELEMENT", "Click ricevuto in posizione : "+position);
				Intent i = new Intent(getActivity(), AteneoAllegatiActivity.class);
				i.putExtra("newsate", news.get(--position));
				startActivity(i);
			}
		});
		
		return view;
	}

	public List<News> getNews() {
		List<News> newsList = new ArrayList<News>();

		try {
			Document doc = Jsoup.connect(URL).timeout(10000).get();
			Elements newsItems = doc.select("div.meta > table > tbody > tr");

			for (int i = 2; i < newsItems.size(); i++) {
				String date = null;
				Element dateElement = newsItems.get(i).select("p").first();
				if (dateElement != null) {
					date = dateElement.text();
				}

				String href =null;
				String body = null;
				Element bodyElement = newsItems.get(i).select("a").first();
				if (bodyElement != null) {
					href = URLAllegati.concat(bodyElement.attr("href"));
					body = bodyElement.text();
				}

				if (date != null && body != null && href != null)
					newsList.add(new News(href,date, body));
			}

		} catch (SocketException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * for (News n : newsList) { Log.i("NEWS", n.getDate() + " " +
		 * n.getBody()); }
		 */
		return newsList;
	}

	class NewsRetriever extends AsyncTask<Void, Void, List<News>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<News> doInBackground(Void... params) {
			return getNews();
		}

		@Override
		protected void onPostExecute(List<News> result) {
			if (result != null) {
				news.clear();
				news.addAll(result);
				mAdapter.notifyDataSetChanged();
			}
			mPullRefreshListView.onRefreshComplete();
		}
	}
}
