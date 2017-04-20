/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file src/ucas/ir/action/SearchServlet.java
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/18 21:22:39
 * @brief
 *
 **/

package ucas.ir.action;

import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.gson.*;

import ucas.ir.pojo.News;
import ucas.ir.pojo.Page;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int totalNews = 0;
	private static final int perpageCount = 10;

	public SearchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// handle form parm 
		String query = request.getParameter("q"); // query str
		query = new String(query.getBytes("iso8859-1"), "UTF-8");
		query = query.trim();

		String pageNum = request.getParameter("p"); // page 
		int p = pageNum == null ? 1 : Integer.parseInt(pageNum);

		String sortMethod = request.getParameter("sort"); // sort type

		System.out.println("query str:" + query);
		System.out.println("page:" + p);
		System.out.println("sort:" + sortMethod);

		long startTime = System.currentTimeMillis();// start time

		// index save location
        //String indexPathStr = "D:/java/data/index/";
		String indexPathStr = "/tmp/szunews/index/";
		
		String rets = "";//api return json data
		boolean success = false; //query result
		
		if(query == null || "".equals(query)){
			query = "123836"; //没有输入关键词则展示全部索引记录
		}
		if(query != null && "".equals(query) != true) {
			ArrayList<News> newsList = getTopDoc(query, indexPathStr);
			System.out.println("newslist length:" + newsList.size());

			Page page = new Page(p, newsList.size() / perpageCount + 1, perpageCount, newsList.size(),
					perpageCount * (p - 1), perpageCount * p, true, p == 1 ? false : true);
			System.out.println(page.toString());

			// result sort by time
			if ("time".equals(sortMethod) || "123836".equals(query)) {
				Collections.sort(newsList, new SortByTime());
			}
			else if ("heat".equals(sortMethod)) {
				Collections.sort(newsList, new SortByHeat());
			}

			List<News> pagelist = null; // data show
			if (newsList.size() < perpageCount) {
				// only exist one page data
				pagelist = newsList.subList(0, newsList.size());
				success = true;
			} else if (perpageCount * (p - 1) > newsList.size()) {
				// Current page no results
				// request.getRequestDispatcher("error.jsp").forward(request, response);
				success = false;
			} else if (perpageCount * p > newsList.size()) {
				// Current page's result not full
				pagelist = newsList.subList(perpageCount * (p - 1), newsList.size());
				success = true;
			} else {
				// normal page
				pagelist = newsList.subList(perpageCount * (p - 1), perpageCount * p);
				success = true;
			}

			long endTime = System.currentTimeMillis();// end time
			System.out.println("query cost time:" + (endTime - startTime));

			// request.setAttribute("newslist", pagelist);
			// request.setAttribute("queryback", query);
			// request.setAttribute("totaln", totalNews);
			// request.setAttribute("total_page", newsList.size() / perpageCount + 1);
			// request.setAttribute("time", (double) (endTime - startTime) / 1000);
			// request.setAttribute("page", page);
			// request.setAttribute("sort", sortMethod);
			// request.getRequestDispatcher("result.jsp").forward(request, response);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", success); //query status
			map.put("queryback",query); //query str
			map.put("time",(double) (endTime - startTime) / 1000); //query time
			map.put("page",page); //result of page
			map.put("sort",sortMethod); //sort
			map.put("newslist",pagelist); //query result
			
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			rets = gson.toJson(map); //map to json

		}
		else {
			success = false;
		}
		
		if(success == false){
			rets = "";
		}
		
		// return data to api:(http://localhost:8080/ins/search?q=xxx&p=xxx&sort=xxx)
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(rets);
		out.flush();
		out.close();

	}

	public static ArrayList<News> getTopDoc(String key, String indexPathStr) {
		ArrayList<News> newsList = new ArrayList<News>();

		Directory directory = null;
		try {
			File indexPath = new File(indexPathStr);
			if (indexPath.exists() != true) {
				indexPath.mkdirs();
			}
			// set open index location 
			directory = FSDirectory.open(indexPath);
			// mkdir indexSearcher
			DirectoryReader dReader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(dReader);

			String[] fields = { "news_title", "news_article", "news_id", "news_source", "sign" };
			
			// set analyzer type
			//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			Analyzer analyzer = new IKAnalyzer(true); 
			
			MultiFieldQueryParser parser2 = new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzer);
			Query query2 = parser2.parse(key);

			QueryScorer scorer = new QueryScorer(query2, fields[0]);
			SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span>", "</span>");
			Highlighter highlighter = new Highlighter(fors, scorer);

			// return 999 results
			TopDocs topDocs = searcher.search(query2, 999);
			if (topDocs != null) {
				totalNews = topDocs.totalHits;
				System.out.println("query result number:" + totalNews);
				for (int i = 0; i < topDocs.scoreDocs.length; i++) {
					Document doc = searcher.doc(topDocs.scoreDocs[i].doc);

					TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(),
							topDocs.scoreDocs[i].doc, fields[0], analyzer);
					Fragmenter fragment = new SimpleSpanFragmenter(scorer);
					highlighter.setTextFragmenter(fragment);
					highlighter.setTextFragmenter(new SimpleFragmenter(100)); // summary's length

					String hl_title = highlighter.getBestFragment(tokenStream, doc.get("news_title"));

					tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), topDocs.scoreDocs[i].doc,
							fields[1], analyzer);
					String hl_summary = highlighter.getBestFragment(tokenStream, doc.get("news_article"));
					
					String article = "";
					try{
						article = doc.get("news_article").substring(0, 100);
					}
					catch (Exception e){
						article = "";
					}
					
					News news = new News(doc.get("news_id"), hl_title != null ? hl_title : doc.get("news_title"),
							doc.get("news_keywords"), doc.get("news_posttime"), doc.get("news_source"),
							hl_summary != null ? hl_summary : article, doc.get("news_total"),
							doc.get("news_url"), doc.get("news_reply"), doc.get("news_show"));
					newsList.add(news);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newsList;
	}

}
