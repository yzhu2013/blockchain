package org.zy.blockchain.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zy.blockchain.entity.Block;
import org.zy.blockchain.entity.BlockChain;
import org.zy.blockchain.entity.Transaction;
import org.zy.blockchain.rest.RestResult;
import org.zy.blockchain.service.BlockChainService;
import org.zy.blockchain.service.BlockService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class BlockChainServiceImpl implements BlockChainService{
	
	@Autowired
	private BlockService blockService;
	
	private BlockChain blockChain;
	
	private Set<String> peers;
	
	@Autowired
	private RestTemplate restTemplate;
	

	@PostConstruct
	public void init() {
		blockChain = new BlockChain();
		//仅仅第一个启动节点是创建创世纪区块，需要新增判断是否为第一个启动节点
		createGenesisBlock();
		peers = new HashSet<String>();
	}
	
	
	public BlockChain getBlockChain() {
		return blockChain;
	}
	
	public Set<String> getPeers(){
		return peers;
	}
	
	public void setPeers(Set<String> peers) {
		this.peers = peers;
	}

	/**
	 * 第一个创世纪模块
	 */
	public void createGenesisBlock() {
		Block genesisBlock = new Block(0, new ArrayList<Transaction>(), String.valueOf(System.currentTimeMillis()), "0");
		genesisBlock.setHash(blockService.computeHash(genesisBlock));
		blockChain.getChain().add(genesisBlock);
	}
	
	public Block getLastBlock() {
		return blockChain.getChain().get(blockChain.getChain().size()-1);
	}
	
	public boolean addBlock(Block block, String proof){
		if(blockChain.getChain().size()>0) {
			String previousHash =  getLastBlock().getHash();
			//验证previousHash
			if(!previousHash.equals(block.getPreviousHash())) {
				return false; 
			}
			//判断区块有效性ֵ
			if(!isValidProof(block,proof)) {
				return false;
			}
		}
		
		block.setHash(proof);
		blockChain.getChain().add(block);
		return true;
	}
	
	/**
	 * 工作量证明
	 * @param block
	 * @return
	 */
	public String proofOfWork(Block block) {
		String computedHash = blockService.computeHash(block);
		while (!computedHash.startsWith(appendCharacter('0', blockChain.getDifficulty()))){
			block.setNonce(block.getNonce() + 1);
			computedHash = blockService.computeHash(block);
		}
		return computedHash;
	}
	
	
	public boolean isValidProof(Block block, String blockHash) {
//		return blockHash.startsWith(appendCharacter('0',  blockChain.getDifficulty())) && blockHash.equals(blockService.computeHash(block));
		return blockHash.startsWith(appendCharacter('0',  blockChain.getDifficulty()));
	}
	
	public void addNewTransaction(Transaction transaction) {
		blockChain.getUnconfirmedTransactions().add(transaction);
	}
	
	/**
	 * 判断区块链的有效性
	 * @param chain
	 * @return
	 */
	public boolean validateChain(List<Block> chain) {
		boolean result = true;
		String previousHash = "0";
		for(Block block : chain) {
			String blockHash = block.getHash();
			//�жϵ�ǰblock�Ϸ��Ժ�previous hash�Ƿ���ȷ
			if(!(isValidProof(block, blockHash) && previousHash.equals(block.getPreviousHash()))) {
				result = false;
				break;
			}
			previousHash = block.getHash();
		}
		return result;
	}
	
	/**
	 * 挖矿打包
	 */
	public int mine() {
		if(blockChain.getUnconfirmedTransactions().size() < 1) {
			return 0;
		}
		Block lastBlock = getLastBlock();
		List<Transaction> transactionToBlock = new ArrayList<Transaction>();
		transactionToBlock.addAll(blockChain.getUnconfirmedTransactions());
		
		Block newBlock = new Block(lastBlock.getIndex() + 1, transactionToBlock, String.valueOf(System.currentTimeMillis()), lastBlock.getHash());
		//工作量证明
		String proof = proofOfWork(newBlock);
		//新增区块
		if(addBlock(newBlock, proof)) {
			
		}
		blockChain.getUnconfirmedTransactions().clear();
		
		//
		announceNewBlock(newBlock);
		
		return newBlock.getIndex();
	}
	
	private String appendCharacter(char character, int length){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<length; i++) {
			sb.append(character);
		}
		return sb.toString();
	}

	/**
	 * 向其他节点声明新区块的产生
	 * @param block
	 */
	public void announceNewBlock(Block block) {
		
		for(String peer : peers) {
			String peerUrl = peer + "/blockchain/addBlock";
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

			String param = JSON.toJSONString(block);
			
			HttpEntity<String> entity = new HttpEntity<String>(param, headers);
			
			restTemplate.postForEntity(peerUrl, entity, JSONObject.class);
		}
	}
	
	/**
	 * 共识机制： 始终去最长链
	 */
	public void consensus() {
		int currentLen = blockChain.getChain().size();
		List<Block> longtestChain = null;
		for(String node : peers) {
			System.out.println(node + "/blockchain/chain");
				
			JSONObject resObj = restTemplate.getForObject(node + "/blockchain/chain", JSONObject.class);
			int length = resObj.getIntValue("length");
			JSONArray chain = resObj.getJSONArray("chain");
			List<Block> blockList = convertToJsonArrayToBlockList(chain);
			if(length > currentLen && validateChain(blockList)) {
				currentLen = length;
				longtestChain = blockList;
			}
			if(longtestChain != null) {
				blockChain.setChain(longtestChain);
			}
		}
	}
	
	private List<Block> convertToJsonArrayToBlockList(JSONArray jsonArr) {
		List<Block> blockList = new ArrayList<Block>();
		for(Object obj : jsonArr) {
			Block block = JSON.parseObject(JSON.toJSONString(obj), Block.class);
			blockList.add(block);
		}
		return blockList;
	}

}
