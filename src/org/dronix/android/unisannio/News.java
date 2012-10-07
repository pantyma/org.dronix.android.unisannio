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
package org.dronix.android.unisannio;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable{
	
	public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
		public News createFromParcel(Parcel in) {
			return new News(in);
		}

		public News[] newArray(int size) {
			return new News[size];
		}
	};
	
	private String urlAllegati;
	private String date;
	private String body;

	public News(String date, String body) {
		this.date = date;
		this.body = body;
	}
	public News(String id,String date, String body){
		this.urlAllegati = id;
		this.date = date;
		this.body = body;
	}

	public News(Parcel in) {
		readFromParcel(in);
	}
	public String getUrl() {
		return urlAllegati;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(urlAllegati);
		dest.writeString(date);
		dest.writeString(body);
	}
	
	private void readFromParcel(Parcel in) {
		urlAllegati = in.readString();
		date = in.readString();
		body = in.readString();
	}
}
