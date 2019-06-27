package ort.zy.blockchain.client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.zy.blockchain.entity.Block;
import org.zy.blockchain.entity.Transaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 区块链客户端控制器
 */
@Controller
@RequestMapping("bcclient")
public class BlockchainClientController {
	
	@Value("${chain.address}")
	private String chainAddress;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/")
	public String index(Model model) {
		List<Transaction> transactions = fetchPosts();
		model.addAttribute("title", "block chain client test...");
		model.addAttribute("node_address", chainAddress);
		model.addAttribute("posts", transactions);
		return "/index";
	}
	
	
	@PostMapping("/submit")
	public void submit(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> params = new HashMap<String,String>();
		
		params.put("content", request.getParameter("content"));
		params.put("author",request.getParameter("author"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		String param = JSON.toJSONString(params);
		HttpEntity<String> entity = new HttpEntity<String>(param, headers);
		
		restTemplate.postForEntity(chainAddress +"/newtransaction", entity, String.class);
		
		try {
			response.sendRedirect("/bcclient/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Transaction> fetchPosts() {
		ResponseEntity<JSONObject> response = restTemplate.getForEntity(chainAddress + "/chain", JSONObject.class);
		List<Transaction> posts = new ArrayList<Transaction>();
		if(response.getStatusCode() == HttpStatus.OK) {
			JSONObject respBody = response.getBody();
			JSONArray chain = respBody.getJSONArray("chain");
			if(chain != null) {
				for(Object object : chain) {
					Block block = JSON.parseObject(JSON.toJSONString(object), Block.class);
//					Block block = (Block)object;
					List<Transaction> transactions = block.getTransactions();
					posts.addAll(transactions);
				}
			}
		}
		return posts;
	}
}
