package com.hinasch.lib.library;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.base.Optional;
import com.hinasch.lib.PairID;
import com.hinasch.lib.library.LibraryBook.EnumSelector;

public class LibraryShelf {

	public HashSet<LibraryBook> libSet = new HashSet();

	public LibraryShelf(){
		this.libSet = new HashSet();
	}
	public void addShelf(LibraryBook newBook){
		libSet.add(newBook);
	}
	public Optional<LibraryBook> find(Object object){
		LibraryBook returnBook = null;
		ItemStack is = null;
		Class _class = null;
		
		
		if(object instanceof ItemStack){
			is = (ItemStack)object;
			_class = is.getItem().getClass();
		}
		if(object instanceof Block){
			is = new ItemStack((Block)object,1);
			_class = ((Block)object).getClass();
		}
		if(object instanceof Item){
			is = new ItemStack((Item)object,1);
			_class = ((Item)object).getClass();
		}

		if(object instanceof String){
			returnBook = findAsOreKey((String)object);
		}
		if(is!=null){
			returnBook = findAsItemStack(is);
		}
		
		if(returnBook!=null){
			return Optional.of(returnBook);
		}
		
		returnBook = this.findDetailedly(is, _class);
		
		
		
		if(returnBook!=null){
			return Optional.of(returnBook);
		}
		
		if(is!=null){
			if(is.getItem() instanceof ItemArmor){
				ItemArmor armor = (ItemArmor)is.getItem();
				returnBook = this.findAsArmorMaterial(armor.getArmorMaterial());
			}
			if(is.getItem() instanceof ItemSword){
				ItemSword tool = (ItemSword)is.getItem();
				returnBook = this.findAsToolMaterial(tool.getToolMaterialName());
			}
			if(is.getItem() instanceof ItemTool){
				ItemTool tool = (ItemTool)is.getItem();
				returnBook = this.findAsToolMaterial(tool.getToolMaterialName());
			}
			if(is.getItem() instanceof ItemHoe){
				ItemHoe tool = (ItemHoe)is.getItem();
				returnBook = this.findAsToolMaterial(tool.getToolMaterialName());
			}
		}
		
		if(returnBook!=null){
			return Optional.of(returnBook);
		}

		return Optional.absent();
		
	}
	
	protected LibraryBook findDetailedly(ItemStack is,Class _class){
		//Unsaga.debug("詳細に調べます");
		LibraryBook returnBook = null;
		int oreid = OreDictionary.getOreID(is);
		String orekey = OreDictionary.getOreName(oreid);
		//Unsaga.debug(orekey);
		if(!orekey.equals("Unknown")){
			returnBook = this.findAsOreKey(orekey);
		}
		if(_class!=null && returnBook==null){
			returnBook = this.findAsClass(_class);
		}
		
		return returnBook;
	}


	
	protected LibraryBook findAsItemStack(ItemStack is){
		for(LibraryBook book:libSet){
			if(book.getKey()==EnumSelector.ITEMSTACK){
				if(book.isAllMetadata){
					if(book.idAndMeta.isPresent() && is.getItem()==book.idAndMeta.get().getItemObject()){
						return book;
					}
				}else{
					PairID pair = book.idAndMeta.isPresent() ? book.idAndMeta.get() : null;
					if(pair!=null && is.getItem()==pair.getItemObject() && is.getItemDamage()==pair.getMeta()){
						return book;
					}
				}
			}
		}
		return null;
	}
	
	protected LibraryBook findAsClass(Class _class){
		for(LibraryBook book:libSet){
			if(book.getKey()==EnumSelector._CLASS){
				if(book.classstore.isPresent() && book.classstore.get()==_class){
					return book;
				}
			}
		}
		return null;
	}
	
	protected LibraryBook findAsOreKey(String str){
		for(LibraryBook book:libSet){
			if(book.getKey()==EnumSelector.STRING){
				//Unsaga.debug(book.orekey.get()+":"+str);
				if(book.orekey.isPresent() && book.orekey.get().equals(str)){
					
					return book;
				}
			}
		}
		return null;
	}
	
	protected LibraryBook findAsToolMaterial(String tool){
		for(LibraryBook book:libSet){
			if(book.getKey()==EnumSelector.TOOLMATERIAL){
				if(book.enumtool.isPresent() && book.enumtool.get().toString().equals(tool)){
					return book;
				}
			}
		}
		return null;
	}
	
	protected LibraryBook findAsArmorMaterial(ArmorMaterial armor){
		for(LibraryBook book:libSet){
			if(book.getKey()==EnumSelector.TOOLMATERIAL){
				if(book.enumarmor.isPresent() && book.enumarmor.get()==armor){
					return book;
				}
			}
		}
		return null;
	}
//	public Optional<Library> findFromClass(Class input){
//		Optional<Library> info = Optional.absent();
//		for(Iterator<Library> ite=libSet.iterator();ite.hasNext();){
//			Library currentpage = ite.next();
//			if(currentpage.key==EnumSelector._CLASS){
//				if(currentpage.classstore.get()==input){
//					info = Optional.of(currentpage);
//				}
//			}
//
//		}
//		return info;
//	}
//	
//	public Optional<Library> findEnumInfo(String input){
//		Optional<Library> info = Optional.absent();
//		for(Iterator<Library> ite=libSet.iterator();ite.hasNext();){
//			Library currentpage = ite.next();
//			if(currentpage.key==EnumSelector.TOOLMATERIAL){
//				if(currentpage.enumtool.get().toString().equals(input)){
//					info = Optional.of(currentpage);
//				}
//			}
//			if(currentpage.key==EnumSelector.ARMORMATERIAL){
//				if(currentpage.enumarmor.get().toString().equals(input)){
//					info = Optional.of(currentpage);
//				}
//			}
//		}
//		return info;
//	}
//	
//	public Optional<Library> findInfo(ItemStack input){
//		Optional<Library> info = Optional.absent();
//		Item item = (Item)input.getItem();
//		for(Iterator<Library> ite=libSet.iterator();ite.hasNext();){
//			Library currentpage = ite.next();
//			if(currentpage.key==EnumSelector.ITEMSTACK){
//				if(currentpage.isAllMetadata){
//					if(currentpage.idmeta.get().id==Item.getIdFromItem(item)){
//						info = Optional.of(currentpage);
//					}
//				}else{
//					if(currentpage.idmeta.get().id==Item.getIdFromItem(item) && currentpage.idmeta.get().metadata==input.getItemDamage()){
//						info = Optional.of(currentpage);
//					}
//				}
//
//			}
//			if(currentpage.key==EnumSelector.STRING && !info.isPresent()){
//				String baseoreid = OreDictionary.getOreName(OreDictionary.getOreID(input));
//				//Unsaga.debug(baseoreid);
//				if(baseoreid.equals(currentpage.orekey.get())){
//					info = Optional.of(currentpage);
//				}
//				
//			}
//
//		}
//		return info;
//	}
}
