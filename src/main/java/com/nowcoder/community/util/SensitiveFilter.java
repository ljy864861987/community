package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

	private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

	//替换符
	private static final String REPLACEMENT = "***";

	//根节点
	private TrieNode rootNode = new TrieNode();

	@PostConstruct
	public void init() {
		try (
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		) {
			String keyWord;
			while ((keyWord = reader.readLine()) != null) {
				//添加到前缀树
				this.addKeyWord(keyWord);
			}
		} catch (IOException e) {
			logger.error("加载敏感词文件失败:" + e.getMessage());
		}
	}

	//将一个敏感词添加到前缀树中
	private void addKeyWord(String keyWord) {
		TrieNode tempNode = rootNode;
		for (int i = 0; i < keyWord.length(); i++) {
			char c = keyWord.charAt(i);
			TrieNode subNode = tempNode.getSubNode(c);

			if (subNode == null) {
				subNode = new TrieNode();
				tempNode.addSubNode(c, subNode);
			}

			//指向子节点，进入下一轮循环
			tempNode = subNode;

			//设置结束标志
			if (i == keyWord.length() - 1) {
				tempNode.setKeyWordEnd(true);
			}
		}
	}

	/**
	 * 过滤敏感词
	 *
	 * @param text 待过滤的文本
	 * @return 过滤后的文本
	 */
	public String filter(String text) {
		if (StringUtils.isBlank(text)) {
			return null;
		}
		//指针1
		TrieNode tempNode = rootNode;
		//指针2
		int begin = 0;
		//指针3
		int position = 0;
		//结果
		StringBuilder sb = new StringBuilder();
		while (begin < text.length()) {
			if (position >= text.length()) {
				tempNode = rootNode;
				sb.append(text.charAt(begin));
				position = ++begin;
				continue;
			}
			char c = text.charAt(position);
			//跳过符号
			if (isSymbol(c)) {
				//若指针1处于根节点，将此符号计入结果
				if (tempNode == rootNode) {
					sb.append(c);
					begin++;
				}
				position++;
				continue;
			}
			tempNode = tempNode.getSubNode(c);
			if (tempNode == null) {
				tempNode = rootNode;
				sb.append(text.charAt(begin));
				position = ++begin;
			} else if (tempNode.isKeyWordEnd()) {
				sb.append(REPLACEMENT);
				begin = ++position;
				tempNode = rootNode;
			} else {
				position++;
			}
		}
		return sb.toString();
	}

	private boolean isSymbol(Character c) {
		//0x2E80~0x9FFF 是东亚文字范围
		return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
	}

	//前缀树
	private class TrieNode {

		//关键词结束标识
		private boolean isKeyWordEnd = false;

		//子节点(Key是下级字符，Value是下级节点)
		Map<Character, TrieNode> subNodes = new HashMap<>();

		public boolean isKeyWordEnd() {
			return isKeyWordEnd;
		}

		public void setKeyWordEnd(boolean keyWordEnd) {
			isKeyWordEnd = keyWordEnd;
		}

		//添加子节点
		public void addSubNode(Character c, TrieNode node) {
			subNodes.put(c, node);
		}

		//获取子节点
		public TrieNode getSubNode(Character c) {
			return subNodes.get(c);
		}
	}

}
