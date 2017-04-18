<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="ucas.ir.pojo.*,java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	List<News> arrlist = (List<News>) request.getAttribute("newslist");
	String queryback = (String) request.getAttribute("queryback");
	String sort = (String) request.getAttribute("sort");
	int totalnews = (Integer) request.getAttribute("totaln");
	int total_page = (Integer) request.getAttribute("total_page");
	double time = Double.parseDouble(request.getAttribute("time").toString());
	Page pageInfo = (Page) request.getAttribute("page");
	int p = 1, i;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Inspiration</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="css/lib/bootstrap-3.3.7-dist/css/bootstrap.min.css">
<link rel="stylesheet" href="css/main.css">
<script type="text/javascript">
	window.onload = function() {
		document.getElementById("query").value ="<%=queryback%>";
	}
</script>
</head>
<body>
    <div id="logo">
    	<a href="http://localhost/ins/">
    		<img alt="logo" src="images/Inspiration_logo.png">
    	</a>
    </div>

	<form action="search" method="get">
	    <div id="search_box" class="container">       
	    	<div class="row">
				<div class="col-xs-12">
					<div class="input-group">
						<input type="text" class="form-control" id="query" name="q" value="" autocomplete="off">
						<span class="input-group-btn">
							<button class="btn btn-default" type="submit" id="btn_submit">Go</button>
						</span>
					</div>
					<button type="button" class="btn btn-default btn-sm" id="sort_select">
		                <span class="glyphicon glyphicon-sort-by-attributes-alt"></span>
	                </button>
				</div>
			</div>
		</div>
		<div id="sort_pick">
			<label class="checkbox-inline">
				<input type="radio" name="sort" value="relevancy" checked> 相关度
			</label>
			<label class="checkbox-inline">
				<input type="radio" name="sort" value="time"> 时间
			</label>
	    </div>
	</form>
	<div id="page">
	    <div class="result">
	    	Page <%=pageInfo.getPage()%> of about <%=totalnews%> results (<%=time%>s) 
	    </div>
		<%
			if (arrlist.size() > 0) {
				Iterator<News> iter = arrlist.iterator();
				News news;
				while (iter.hasNext()) {
					news = iter.next();
		%>
					<div class="result">
					    <div class="title">
					        <a href="http://www.szu.edu.cn/board/view.asp?id=<%=news.getId()%>"><%=news.getTitle()%></a>
					    </div>
                        <div class="content">
	                        <p><%=news.getArtical().length() > 200 ? news.getArtical().substring(0, 200) : news.getArtical()%>...</p>
                        </div>
                        <div class="url">www.szu.edu.cn</div>
					</div>
		<%
			    }
			}
		%>
	    <div id="page_controller" class="result">
	    <div class="row">
		    <div class="col-xs-4"> 
                <a href="search?q=<%=queryback%>&p=<%=pageInfo.getPage() - 1 == 0 ? 1 : pageInfo.getPage() - 1%>&sort=<%=sort%>">
		            <span class="glyphicon glyphicon-chevron-left"></span> 
		        </a>
		    </div>
		    <div class="col-xs-4">
		        <a href="">Page <%=pageInfo.getPage()%></a>
		    </div>
		    <div class="col-xs-4">
		        <a href="search?q=<%=queryback%>&p=<%=pageInfo.getPage() + 1%>&sort=<%=sort%>">
		            <span class="glyphicon glyphicon-chevron-right"></span>
		        </a>
		    </div>
		    
	    </div>
	    </div>
	</div>

	<div class="footerinfo">
		<p>&copy2017 Inspiration</p>
	</div>
	
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="css/lib/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
	<script src="js/main.js"></script>
</body>
</html>