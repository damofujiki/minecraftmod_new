package com.hinasch.unlsaga.villager.smith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.material.UnsagaMaterials;

public class MaterialTransform {

	public static UnsagaMaterials materials = Unsaga.materialManager;
	public static HashSet<MaterialTransform> store;
	public final HashMap<UnsagaMaterial,Integer> required;
	public final UnsagaMaterial transformto;
	public final int probability;
	
	protected MaterialTransform(UnsagaMaterial m1,UnsagaMaterial m2,UnsagaMaterial tr,int prob){
		this.required = new HashMap();
		if(m1==m2){
			this.required.put(m1, 2);
		}else{
			this.required.put(m1, 1);
			this.required.put(m2, 1);
		}
		
		this.probability = prob;
		this.transformto = tr;
		
	}
	
	public static Optional<UnsagaMaterial> drawTransformed(EntityPlayer ep,UnsagaMaterial input1,UnsagaMaterial input2,Random rand){
		Multiset<UnsagaMaterial> inputs = HashMultiset.create();
		inputs.add(input1);
		inputs.add(input2);
		UnsagaMaterial transformed = null;
		for(Iterator<MaterialTransform> ite = store.iterator();ite.hasNext();){
			MaterialTransform trans = ite.next();
			if(matchElement(inputs,trans.required)){
				Unsaga.debug(trans.transformto.getUnlocalizedName()+"になりそう");
				if(rand.nextInt(100)<=trans.probability){
					transformed = trans.transformto;
					if(transformed==Unsaga.materialManager.damascus){
						ep.addStat(Unsaga.achievements.damascus, 1);
					}
					if(transformed.isChildOwnedBy(Unsaga.materialManager.steels)){
						ep.addStat(Unsaga.achievements.steel, 1);
					}
				}
			}
		}
		if(transformed==null)return Optional.absent();
		return Optional.of(transformed);
	}
	
	protected static boolean matchElement(Multiset<UnsagaMaterial> input,HashMap<UnsagaMaterial,Integer> requiredmatter){
		int count;
		int match = 0;
		for(Iterator<UnsagaMaterial> ite=requiredmatter.keySet().iterator();ite.hasNext();){
			count = 0;
			UnsagaMaterial requiredmaterial = ite.next();
			int requiredcount = requiredmatter.get(requiredmaterial);

			if(requiredmaterial.hasSubMaterials()){
				for(Iterator<UnsagaMaterial> itera=requiredmaterial.getSubMaterials().values().iterator();itera.hasNext();){
					count += input.count(itera.next());
				}
			}else{
				count += input.count(requiredmaterial);
			}
			if(count>=requiredcount){
				match += 1;
			}
		}
		if(match>=requiredmatter.size()){
			return true;
		}
		return false;
	}
	

	
	static{
		store = new HashSet();
		store.add(new MaterialTransform(materials.iron,materials.categorywood,materials.steel1,85));
		store.add(new MaterialTransform(materials.steel1,materials.categorywood,materials.steel2,15));
		store.add(new MaterialTransform(materials.iron,materials.categorywood,materials.steel2,15));
		store.add(new MaterialTransform(materials.ironOre,materials.categorywood,materials.steel2,15));
		store.add(new MaterialTransform(materials.silver,materials.bestial,materials.fairieSilver,15));
		store.add(new MaterialTransform(materials.steel2,materials.debris2,materials.damascus,100));
		store.add(new MaterialTransform(materials.debris,materials.debris,materials.debris2,100));
		store.add(new MaterialTransform(materials.debris,materials.bestial,materials.debris2,100));
		store.add(new MaterialTransform(materials.categoryStone,materials.categorywood,materials.debris,15));
		store.add(new MaterialTransform(materials.categoryStone,materials.silver,materials.debris,15));
		store.add(new MaterialTransform(materials.categoryStone,materials.obsidian,materials.debris,15));
		store.add(new MaterialTransform(materials.copperOre,materials.categorywood,materials.copper,100));
		store.add(new MaterialTransform(materials.ironOre,materials.categorywood,materials.iron,100));
		store.add(new MaterialTransform(materials.meteorite,materials.bone,materials.meteoricIron,15));
		store.add(new MaterialTransform(materials.dragonHeart,materials.iron,materials.dragonHeart,100));
	}
}
