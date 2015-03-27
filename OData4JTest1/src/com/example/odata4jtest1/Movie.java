package com.example.odata4jtest1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmEntitySet;

import android.database.Cursor;

import com.j256.ormlite.field.DatabaseField;

/*
 * This is the Movie class. It should have a constructor that takes an OEntity, and also one that
 * takes a Movie and turns it into an OEntity. This is to help the ExampleProvider with its management
 * of both the local SQLite Database on Android and also the outside Database.
 */

public class Movie {

	@DatabaseField(generatedId = true) //this is automatically generated
	public int id;
	@DatabaseField
	public String name;
	@DatabaseField
	public int yearReleased;
	@DatabaseField
	public String rating;
	
	Random rand = new Random();
	
	public Movie() {
		//ORMlite needs no-arg constructor
	}
	
	public Movie(String nameT, int yearReleasedT, String ratingT) {
		name = nameT;
		yearReleased = yearReleasedT;
		rating = ratingT;
	}
	
	public Movie(Movie mov) {
		name = mov.name;
		yearReleased = mov.yearReleased;
		rating = mov.rating;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(", ").append("name=").append(name);
		sb.append(", ").append("rating=").append(rating);
		sb.append(", ").append("year released=").append(yearReleased);
		return sb.toString();
	}
	
	private EdmEntitySet _entitySet; //private variables often have _
	public Movie(OEntity entity) {
		this._entitySet = entity.getEntitySet();
		if(entity.getEntitySet().getName().equals("Movies")){
			id = (Integer)entity.getProperty("MovieId").getValue();
			name = (String)entity.getProperty("MovieName").getValue();
			//rating = (String)entity.getProperty("MovieRating").getValue();
			//yearReleased = (Integer)entity.getProperty("YearReleased").getValue(); //may cause problems
			yearReleased = 0;
		}else{
			//there was an error
		}
	}
	
	public Movie(Cursor cursor) {
		id = cursor.getInt(cursor.getColumnIndex("MovieId"));
		name = cursor.getString(cursor.getColumnIndex("MovieName"));
		//yearReleased = cursor.getInt(cursor.getColumnIndex("YearReleased")); //may cause problems
		yearReleased = 0;
		//rating = cursor.getString(cursor.getColumnIndex("MovieRating"));
	}
	
	public OEntity toEntity(){ //not sure that this is working!
		List<OProperty<?>> propList = new ArrayList<OProperty<?>>();
		propList.add(OProperties.int32("MovieId", this.id));
		propList.add(OProperties.string("MovieName", this.name));
		propList.add(OProperties.string("MovieRating", this.rating));
		propList.add(OProperties.int32("YearReleased", this.yearReleased));
		
		OEntityKey key = OEntityKey.infer(this._entitySet, propList);
		OEntity entity = OEntities.create(this._entitySet, key, propList, null);
		
		return entity;
	}
	
	public Movie generateMovie() {
		Movie mov = new Movie();
		String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
				"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		
		mov.name = "";
		for(int i = 0; i < 4; i++) {
			mov.name = mov.name + letters[rand.nextInt(26)];
		}
		mov.name += " ";
		for(int i = 0; i < 4; i++) {
			mov.name = mov.name + letters[rand.nextInt(26)];
		}
		mov.name += " ";
		for(int i = 0; i < 4; i++) {
			mov.name = mov.name + letters[rand.nextInt(26)];
		}
		
		mov.yearReleased = rand.nextInt(80) + 1936;
		
		mov.rating = "";
		for(int i = 0; i < 2; i++) {
			mov.rating = mov.rating + letters[rand.nextInt(26)];
		}
		
		return mov;
	}
}
