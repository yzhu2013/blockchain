package org.zy.blockchain.service.impl;

import org.springframework.stereotype.Service;
import org.zy.blockchain.entity.Block;
import org.zy.blockchain.service.BlockService;
import org.zy.blockchain.util.SHA256Utils;

import com.alibaba.fastjson.JSON;

@Service
public class BlockServiceImpl implements BlockService{

	public String computeHash(Block block) {
		
		String blockJson = JSON.toJSONString(block);
		return SHA256Utils.getSHA256(blockJson);
		
	}

}
