package org.zy.blockchain.service;

import org.zy.blockchain.entity.Block;

public interface BlockService {
	
	String computeHash(Block block);
	
	

}
