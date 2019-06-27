package org.zy.blockchain.entity;

import java.util.List;

public class Block {
	
	private int index;
	
	private String timestamp;
	
	private String previousHash;
	
	private String hash;
	
	private List<Transaction> transactions;
	
	private String nonce;
	
	public Block(int index, List<Transaction> transactions, String timestamp, String previousHash) {
		this(index, transactions, timestamp, previousHash, "0");
	}
	
	
	public Block(int index, List<Transaction> transactions, String timestamp, String previousHash, String nonce) {
		this.index = index;
		this.transactions = transactions;
		this.timestamp = timestamp;	
		this.previousHash = previousHash;
		this.nonce = nonce;
	}
	
	public Block(int index, List<Transaction> transactions, String timestamp, String previousHash, String nonce, String hash) {
		this.index = index;
		this.transactions = transactions;
		this.timestamp = timestamp;	
		this.previousHash = previousHash;
		this.nonce = nonce;
		this.hash = hash;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}
