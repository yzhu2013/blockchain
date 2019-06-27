package org.zy.blockchain.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.zy.blockchain.service.BlockService;

public class BlockChain {
	
//	private BlockService blockService;
	
	private List<Transaction> unconfirmedTransactions = new ArrayList<Transaction>();
	
	private List<Block> chain = new ArrayList<Block>();
	
	private int difficulty = 2;
	
//	private List<String> peers;
//	
//	public List<String> getPeers() {
//		return peers;
//	}
//
//	public void setPeers(List<String> peers) {
//		this.peers = peers;
//	}
	
//	public BlockService getBlockService() {
//		return blockService;
//	}
//
//	public void setBlockService(BlockService blockService) {
//		this.blockService = blockService;
//	}

	public List<Transaction> getUnconfirmedTransactions() {
		return unconfirmedTransactions;
	}

	public void setUnconfirmedTransactions(List<Transaction> unconfirmedTransactions) {
		this.unconfirmedTransactions = unconfirmedTransactions;
	}

	public List<Block> getChain() {
		return chain;
	}

	public void setChain(List<Block> chain) {
		this.chain = chain;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

//	public void createGenesisBlock() {
//		Block genesisBlock = new Block(0, new ArrayList<Transaction>(), String.valueOf(System.currentTimeMillis()), "0");
//		genesisBlock.setHash(blockService.computeHash(genesisBlock));
//		chain.add(genesisBlock);
//	}
//	
//	public Block getLastBlock() {
//		return chain.get(chain.size()-1);
//	}
//	
//	public boolean addBlock(Block block, String proof){
//		String previousHash =  getLastBlock().getHash();
//		//����previous hash
//		if(!previousHash.equals(block.getPreviousHash())) {
//			return false; 
//		}
//		//����block��hashֵ
//		if(!isValidProof(block,proof)) {
//			return false;
//		}
//		
//		block.setHash(proof);
//		chain.add(block);
//		return true;
//	}
//	
//	public String proofOfWork(Block block) {
//		String computedHash = blockService.computeHash(block);
//		while (!computedHash.startsWith(appendCharacter('0', difficulty))){
//			block.setNonce(block.getNonce() + 1);
//			computedHash = blockService.computeHash(block);
//		}
//		return computedHash;
//	}
//	
//	public boolean isValidProof(Block block, String blockHash) {
//		return blockHash.startsWith(appendCharacter('0', difficulty)) && blockHash.equals(blockService.computeHash(block));
//	}
//	
//	public void addNewTransaction(Transaction transaction) {
//		unconfirmedTransactions.add(transaction);
//	}
//	
//	public boolean validateChain(List<Block> chain) {
//		boolean result = true;
//		String previousHash = "0";
//		for(Block block : chain) {
//			String blockHash = block.getHash();
//			//�жϵ�ǰblock�Ϸ��Ժ�previous hash�Ƿ���ȷ
//			if(!(isValidProof(block, blockHash) && previousHash.equals(block.getPreviousHash()))) {
//				result = false;
//				break;
//			}
//			previousHash = block.getHash();
//		}
//		return result;
//	}
//	
//	public int mine() {
//		if(unconfirmedTransactions.size() < 1) {
//			return 0;
//		}
//		Block lastBlock = getLastBlock();
//		List<Transaction> transactionToBlock = new ArrayList<Transaction>();
//		transactionToBlock.addAll(unconfirmedTransactions);
//		
//		Block newBlock = new Block(lastBlock.getIndex() + 1, transactionToBlock, String.valueOf(System.currentTimeMillis()), lastBlock.getHash());
//		String proof = blockService.computeHash(newBlock);
//		addBlock(newBlock, proof);
//		unconfirmedTransactions.clear();
//		//TODO: announcement .... 
//		
//		return newBlock.getIndex();
//	}
//	
//	private String appendCharacter(char character, int length){
//		StringBuffer sb = new StringBuffer();
//		for(int i=0; i<length; i++) {
//			sb.append(character);
//		}
//		return sb.toString();
//	}

}
