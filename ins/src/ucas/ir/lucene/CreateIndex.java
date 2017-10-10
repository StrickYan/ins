/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file src/ucas/ir/lucene/CreateIndex.java
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/18 21:22:39
 * @brief Create index by running as a java application.
 *
 **/

package ucas.ir.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ucas.ir.pojo.News;

public class CreateIndex {

	// public static String indexRoute = "D:/java/data/index/"; //索引存放位置
	// public static String jsonFileRoute = "D:/java/data/json/"; //json原数据文件存放位置

	public static String indexRoute = "/tmp/szunews/index/"; // 索引存放位置
	public static String jsonFileRoute = "/tmp/szunews/json/"; // json原数据文件存放位置

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();// start time

		// 第一步：创建分词器
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		Analyzer analyzer = new IKAnalyzer(true);

		// 第二步：创建indexWriter配置信息
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		// 第三步：设置索引的打开方式
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		// 第四步：设置索引的路径
		Directory directory = null;
		// 第五步:创建indexWriter,用于索引的增删改.
		IndexWriter indexWriter = null;

		try {
			File indexpath = new File(indexRoute);
			if (indexpath.exists() != true) {
				indexpath.mkdirs();
			}
			directory = FSDirectory.open(indexpath);
			if (IndexWriter.isLocked(directory)) {
				IndexWriter.unlock(directory);
			}
			indexWriter = new IndexWriter(directory, indexWriterConfig);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// 循环创建索引
		ArrayList<String> filenamelist = getfileName();
		Iterator<String> iter = filenamelist.iterator();

		while (iter.hasNext()) {
			String file_name = iter.next();
			// System.out.println("当前iter:" + file_name);
			News news = getNews(jsonFileRoute + file_name);
			Document doc = new Document();
			if (news != null) {
				// System.out.println(news.getTitle());

				doc.add(new StringField("news_id", news.getId(), Store.YES)); // 索引 不分词
				doc.add(new TextField("news_title", news.getTitle(), Store.YES)); // 索引 分词
				doc.add(new TextField("news_article", news.getArtical(), Store.YES)); // 索引 分词
				doc.add(new TextField("news_source", news.getSource(), Store.YES)); // 索引 分词
				doc.add(new StoredField("news_show", news.getShow())); // 不索引 只存储
				doc.add(new StoredField("news_posttime", news.getTime())); // 不索引 只存储
				doc.add(new StringField("sign", "123836", Store.YES)); // 索引 不分词 该字段目的是为了返回全部索引记录

				// doc.add(new TextField("news_keywords", news.getKeyword(), Store.YES));
				// doc.add(new TextField("news_total", news.getTotal(), Store.YES));
				// doc.add(new TextField("news_url", news.getURL(), Store.YES));
				// doc.add(new TextField("news_reply", news.getReply(), Store.YES));

				try {
					indexWriter.addDocument(doc);
					indexWriter.commit();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			indexWriter.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();// end time
		long Time = endTime - startTime;
		System.out.println("create index cost " + Time + " ms.");
		System.out.println("index create success!");

	}

	// 获取news目录下所有json文件的文件名,返回文件名数组
	public static ArrayList<String> getfileName() {
		ArrayList<String> arrlist = new ArrayList<String>();
		File dataPth = new File(jsonFileRoute);
		if (dataPth.exists()) {
			File[] allFiles = dataPth.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				arrlist.add(allFiles[i].getName().toString());
			}
		}

		System.out.println(arrlist.size());
		return arrlist;
	}

	// 把json文件解析为News对象,返回值为News对象
	public static News getNews(String path) {
		News news = new News();
		try {
			JsonParser jParser = new JsonParser();
			JsonObject jObject = (JsonObject) jParser.parse(new FileReader(path));
			String id = jObject.get("id").getAsString();
			String title = jObject.get("title").getAsString().trim();
			String time = jObject.get("date").getAsString().trim();
			String source = jObject.get("from").getAsString();
			String artical = jObject.get("content").getAsString();
			String uRL = jObject.get("id").getAsString();
			String show = jObject.get("click_in_content").getAsString();

			// String keyword = jObject.get("Keyword").getAsString();
			// String total = jObject.get("Total").getAsString();
			// String reply = jObject.get("Reply").getAsString();
			String keyword = "test";
			String total = "test";
			String reply = "test";

			news = new News(id, title, keyword, time, source, artical, total, uRL, reply, show);
			return news;
		} catch (Exception e) {
			System.out.println("get news error: " + e);
			return null;
		}
	}

}
