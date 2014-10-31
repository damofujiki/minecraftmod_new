package com.hinasch.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class FileObject {

	//未完
	protected Optional<Character> currentPointer;
	protected File file;
	protected FileReader fileReader;
	protected BufferedWriter bWriter;
	protected BufferedReader bReader;
	protected FileWriter fileWriter;
	protected JsonWriter jsonWriter;
	protected JsonReader jsonReader;
	protected Gson gson;
	
	protected Optional<String> currentStr = Optional.of(new String());
	protected String temp = new String();
	public FileObject(String filename){
		this.file = new File(filename);
		
	}
	
	public FileObject(File path,String filename){
		this.file = new File(path,filename);
		
	}
	
	public void openForOutput(){
			try {
				fileWriter = new FileWriter(file);
				
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		
	}
	
	public void openForJsonOutput(){
		try {
			fileWriter = new FileWriter(file);
			jsonWriter = new JsonWriter(new BufferedWriter(fileWriter));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
	
	public void openForJsonInput(){
		if(file.exists()){
			try {

				fileReader = new FileReader(file);
				bReader= new BufferedReader(fileReader);
				jsonReader = new JsonReader(bReader);
				
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}else{
			System.out.println("file not found:"+file.toString());
		}

	}
	
	public void write(String str){
		if(fileWriter!=null){
			try {
				fileWriter.write(str);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}else{
			System.out.println("can't write,open file first.");
		}
	}
	
	public void writeJson(Object obj,Class clazz){
		
		this.gson = new Gson();
		System.out.println(gson.toJson(obj));
		gson.toJson(obj,clazz,jsonWriter);
		//gson.to
	}
	
	public <T> T readJson(Class clazz){
		this.gson = new Gson();
		return gson.fromJson(jsonReader, clazz);
	}
	public void readNext(){
		if(fileReader!=null){
			try {
				
				int c = fileReader.read();
				if(c==-1){
					this.currentPointer = Optional.absent();
					return;
				}
				this.currentPointer = Optional.of((char)c);
				
				
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			
		}else{
			System.out.println("can't read,open file first.");
		}
		
	}
	
	public void close(){
		if(fileWriter!=null){
			try {
				if(this.jsonWriter!=null){
					jsonWriter.close();
				}
				fileWriter.close();

			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		if(fileReader!=null){
			try {
				fileReader.close();
				bReader.close();
				if(this.jsonReader!=null){
					this.jsonReader.close();
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	public void openForInput(){
		if(file.exists()){
			try {

				fileReader = new FileReader(file);
				bReader= new BufferedReader(fileReader);
				
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}else{
			System.out.println("file not found:"+file.toString());
		}
	}
	
	public boolean exists(){
		return file.exists();
	}
	

	public Optional<String> read(){
		if(bReader!=null){
			try {
				String line = bReader.readLine();
				if(line==null){
					return Optional.absent();
				}
				return Optional.of(line);
				
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return Optional.absent();
	}
}
