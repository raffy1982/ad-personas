package com.kdcloud.lib.domain;

import java.util.LinkedList;
import java.util.List;
public class Index {
	
	static class Link {
		
		String href;
		
		public Link() {
		}

		public Link(String href) {
			super();
			this.href = href;
		}
		
	}
	
	static class Item {
		Link reference;
		Link metadata;
		
		public String toString(){
			return "ITEM -> reference: " + reference.href + " - metadata: " + metadata.href;
		}
	}
	
	List<Item> items = new LinkedList<Index.Item>();
	

	public void add(String referenceUrl, String metadataUrl) {
		Item item = new Item();
		item.reference = new Link(referenceUrl);
		item.metadata = new Link(metadataUrl);
		items.add(item);
	}
	
	public void addAll(Index other) {
		items.addAll(other.items);
	}
	
	public int size() {
		return items.size();
	}
	
	private String addBaseUrl(String baseUrl, String href) {
		if (href.charAt(0) == '/')
			return baseUrl + href;
		return baseUrl + "/" + href;
	}

	public void setReferencesBaseUrl(String baseUrl) {
		for (Item i : items) {
			if (i.reference != null)
				i.reference.href = addBaseUrl(baseUrl, i.reference.href);
		}
	}

	public void setMetadataBaseUrl(String baseUrl) {
		for (Item i : items) {
			if (i.metadata != null)
				i.metadata.href = addBaseUrl(baseUrl, i.metadata.href);
		}
	}
	
	public void setBaseUrl(String baseUrl) {
		setReferencesBaseUrl(baseUrl);
		setMetadataBaseUrl(baseUrl);
	}
	
	public List<String> getAllReferences() {
		LinkedList<String> list = new LinkedList<String>();
		for (Item i : items) {
			if (i.reference != null)
				list.add(i.reference.href);
		}
		return list;
	}
	
	public List<String> getAllMetadataUrl() {
		LinkedList<String> list = new LinkedList<String>();
		for (Item i : items) {
			if (i.metadata != null)
				list.add(i.metadata.href);
		}
		return list;
	}
	
	public String toString() {
		String str = "INDEX:\n";
		for(Item i : items){
			str += i.toString() + "\n";
		}
		return str;	
	}

}
