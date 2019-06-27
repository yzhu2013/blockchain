package org.zy.blockchain.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.zy.blockchain.entity.Block;
import org.zy.blockchain.entity.BlockChain;
import org.zy.blockchain.entity.Transaction;
import org.zy.blockchain.rest.RestResult;
import org.zy.blockchain.service.BlockChainService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("blockchain")
public class BlockChainController {
	
	@Autowired
	private BlockChainService blockChainService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 新增一条交易记录
	 * @param jsonObj
	 * @return
	 */
	@PostMapping(value = "/newtransaction")
	@ResponseBody
	public String newTransaction(@RequestBody JSONObject jsonObj) {
		String author = jsonObj.getString("author");
		String content = jsonObj.getString("content");
		
//		String timestamp = String.valueOf(System.currentTimeMillis());
		String timestamp =DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
		Transaction transaction = new Transaction(author, content, timestamp);
		blockChainService.addNewTransaction(transaction);
		return "success";
	}
	
	/**
	 * 打包生产区块
	 * @return
	 */
	@GetMapping("/mine")
	@ResponseBody
	public String mine() {
		int result = blockChainService.mine();
		if(result == 0) {
			return "No transaction to mine";
		}
		return "Block#" + result + " mined";
	}

	
	/**
	 * 在已经启动的节点上注册新节点
	 * @param request
	 * @param paramMap
	 * @return
	 */
	@PostMapping("/registerwith")
	@ResponseBody
	public RestResult registerWithExistingNode(HttpServletRequest request, @RequestBody Map<String,String> paramMap) {
		String nodeAddress = paramMap.get("nodeAddress");
		if(!StringUtils.hasText(nodeAddress)) {
			//TODO: Error Handle
		}
		
		String requestUrl = request.getRequestURL().toString();
		requestUrl = requestUrl.substring(0,requestUrl.indexOf(request.getRequestURI()));
		
		Map<String,String> paramsMap = new HashMap<String,String>();
		
		paramsMap.put("requestUrl", requestUrl);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		String param = JSON.toJSONString(paramsMap);
		
		HttpEntity<String> entity = new HttpEntity<String>(param, headers);
		
		ResponseEntity<JSONObject> response =  restTemplate.postForEntity(nodeAddress +"/registerNode", entity, JSONObject.class);
		
		Set<String> newpeers = new HashSet<String>();
		if(response.getStatusCode() == HttpStatus.OK) {
			JSONObject chainObj = response.getBody();
			JSONArray chainDump = chainObj.getJSONArray("chain");
			BlockChain blockchain = createBlockChainFromDump(chainDump);
			
			JSONArray peers = chainObj.getJSONArray("peers");
			for(Object peer : peers) {
				newpeers.add((String)peer);
			}
			newpeers.remove(requestUrl);
			blockChainService.setPeers(newpeers);
			return RestResult.restSuccess();
		}else {
			return RestResult.restFail();
		}
	}
	
	/**
	 * 从其他节点来初始化区块链
	 * @param chain
	 * @return
	 */
	public BlockChain createBlockChainFromDump(List<Object> chain) {
		for(Object object : chain) {
			Block block = JSON.parseObject(JSON.toJSONString(object), Block.class);
			String proof = block.getHash();
			boolean added = blockChainService.addBlock(block, proof);
			if(!added) {
				//TODO: chain dump is tampered
			}
		}
		return blockChainService.getBlockChain();
	}
	
	/**
	 * 注册新节点
	 * @param jsonObj
	 * @return
	 */
	@PostMapping("/registerNode")
	@ResponseBody
	public JSONObject registerNewNode(@RequestBody JSONObject jsonObj) {
		String requestUrl = jsonObj.getString("requestUrl");
		if(!StringUtils.hasText(requestUrl)) {
			//TODO: Error Handle
		}
		blockChainService.getPeers().add(requestUrl);
		return getChain();
	}
	
	
	@PostMapping("/addBlock")
	@ResponseBody
	public RestResult addBlock(@RequestBody JSONObject jsonObj) {
		Block block = JSON.parseObject(JSON.toJSONString(jsonObj), Block.class);
		String proof = block.getHash();
		boolean added = blockChainService.addBlock(block, proof);
		if(added) {
			return RestResult.restSuccess();
		}else {
			return RestResult.restFail();
		}
		
	}
	
	
	/*
	 * 获取区块链
	 */
	@GetMapping("/chain")
	@ResponseBody
	public JSONObject getChain() {
		blockChainService.consensus();
		BlockChain bc = blockChainService.getBlockChain();
		JSONObject jsonObj = new JSONObject();
		if(bc != null) {
			List<Block> chain = bc.getChain();
			jsonObj.put("length", chain.size());
			jsonObj.put("chain", chain);
			jsonObj.put("peers", blockChainService.getPeers());
		}
		return jsonObj;
	}

}
