package org.zy.blockchain.service;

import java.util.Set;

import org.zy.blockchain.entity.Block;
import org.zy.blockchain.entity.BlockChain;
import org.zy.blockchain.entity.Transaction;

public interface BlockChainService {
	
	/**
	 * 新增区块链
	 * @param block
	 * @param proof
	 * @return
	 */
	boolean addBlock(Block block, String proof);
	
	/**
	 * 新增交易
	 * @param transaction
	 */
	void addNewTransaction(Transaction transaction);
	
	/**
	 * 获取区块链
	 * @return
	 */
	BlockChain getBlockChain();
	
	/**
	 * 获取节点
	 * @return
	 */
	Set<String> getPeers();
	
	/**
	 * 设置节点
	 * @param peers
	 */
	void setPeers(Set<String> peers);
	
	/**
	 * 挖矿打包接口
	 * @return
	 */
	int mine();
	
	/**
	 * 共识机制，始终取最长的链
	 */
	void consensus();
}
