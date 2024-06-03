package com.os.replay.util;

import java.util.concurrent.ConcurrentHashMap;

import com.os.replay.model.OpenFigiV3ResponseDataItem;

public class FigiCache {

	private static FigiCache cache = null;
	
	private ConcurrentHashMap<String, OpenFigiV3ResponseDataItem> sedolFigiMap = null;
	
	private FigiCache() {
		sedolFigiMap = new ConcurrentHashMap<String, OpenFigiV3ResponseDataItem>();
	}
	
	public static FigiCache getInstance() {
		if (cache == null) {
			cache = new FigiCache();
		}
		
		return cache;
	}
	
	public void addFigi(String sedol, OpenFigiV3ResponseDataItem figi) {
		sedolFigiMap.put(sedol, figi == null ? new OpenFigiV3ResponseDataItem() : figi);
	}
	
	public OpenFigiV3ResponseDataItem getFigi(String sedol) {
		return sedolFigiMap.get(sedol);
	}
}
