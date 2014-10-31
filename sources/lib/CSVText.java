package com.hinasch.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CSVText {

	protected String text;
	public CSVText(){
		text = new String();
	}
	
	public void setText(String par1){
		this.text = par1;
	}
	
	public String getText(){
		return this.text;
	}
	
	public void add(String par1){
		if(text.equals("")){
			text = par1;
		}else{
			text = text + ","+par1;
		}
	}
	
	public void add(String par1,int par2){
		String addtext = par1+":"+String.valueOf(par2);
		if(text.equals("")){
			text = addtext;
		}else{
			text = text + ","+addtext;
		}
	}

	public String get(int par1){
		if(text.contains(",")){
			String texts[] = text.split(",");
			//System.out.println("texts:"+texts[par1]);
			if(texts[par1].contains(":")){
				String parts[] = texts[par1].split(":");
				//System.out.println("parts:"+parts[0]);//called
				return parts[0];
			}
			return texts[par1];
		}
		if(text.contains(":")){
			String parts[] = text.split(":");
			//System.out.println(parts[0]);
			return parts[0];
		}
		return text;
	}
	
	public int getInt(int par1){
		if(text.contains(",")){
			String texts[] = text.split(",");
			if(texts[par1].contains(":")){
				String parts[] = texts[par1].split(":");
				return Integer.parseInt(parts[1]);
			}
			return -1;
		}
		if(text.contains(":")){
			String parts[] = text.split(":");
			//System.out.println("parts:"+parts[0]);
			return Integer.parseInt(parts[1]);
		}
		return -1;
	}
	
	public int getInt(String par1){
		for(int i=0;i<this.length();i++){
			String str = this.get(i);

			if(str.equals(par1)){
				int var1 = this.getInt(i);
				return var1;
			}
		}
		return -1;
	}
	public void remove(String par1){
		CSVText newtext = new CSVText();
		if(this.length()==0){
			return ;
		}
		for(int i=0;i<this.length();i++){
			String str = this.get(i);
			if(!str.equals(par1)){
				if(this.getInt(i)==-1){
					newtext.add(str);
				}else{
					newtext.add(str,this.getInt(i));
				}
			}
		}
		this.text = newtext.text;
	}
	
	public int length(){
		if(text.contains(",")){
			String texts[] = text.split(",");
			return texts.length;
		}
		if(this.text.equals("")){
			return 0;
		}
		return 1;
	}
	
	public static String collectionToCSV(Collection<String> input){
		StringBuilder strbuilder = new StringBuilder();
		for(Iterator<String> ite=input.iterator();ite.hasNext();){
			if(ite.hasNext()){
				strbuilder.append(ite.next()).append(",");
			}else{
				strbuilder.append(ite.next());
			}
		}
		return new String(strbuilder);
	}
	
	public static <T> String exchangeCollectionToCSV(Collection<T> input){
		StringBuilder strbuilder = new StringBuilder();
		for(Iterator<T> ite=input.iterator();ite.hasNext();){
			if(ite.hasNext()){
				strbuilder.append(ite.next().toString()).append(",");
			}else{
				strbuilder.append(ite.next().toString());
			}
		}
		return new String(strbuilder);
	}
	
	
	public static String intListToCSV(List<Integer> input){
		StringBuilder strbuilder = new StringBuilder();
		for(Iterator<Integer> ite=input.iterator();ite.hasNext();){
			if(ite.hasNext()){
				strbuilder.append(ite.next()).append(",");
			}else{
				strbuilder.append(ite.next());
			}
		}
		return new String(strbuilder);
	}
	
	public static List<Integer> csvToIntList(String str){
		String[] strs = str.split(",");
		List<Integer> output = new ArrayList();
		if(strs.length==0){
			return null;
		}
		for(String s:strs){
			output.add(Integer.valueOf(s));
		}
		return output;
	}
	
	public static List<String> csvToStrList(String str){
		String[] strs = str.split(",");
		List<String> output = new ArrayList();
		if(strs.length==0){
			return null;
		}
		for(String s:strs){
			output.add(s);
		}
		return output;
	}
}
