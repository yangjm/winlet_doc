<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://www.aggrepoint.com/winlet/site/local" prefix="site"%>
<site:preload>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title>Winlet框架文档</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="/doc/winlet/winlet_local.min.css">
	<link rel="stylesheet" href="/doc/css/doc.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
	<script src="/doc/winlet/winlet_local_bootstrap.min.js"></script>
	<script src="/doc/js/doc.js"></script>
</head>

<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<span class="navbar-brand">Winlet框架</span>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<site:tree level="0" name="page0">
						<site:eval type="focus" name="isFocus" />

						<li <%if (isFocus.booleanValue()) {%> class="active" <%}%>><a href="<site:url/>"><site:name /></a></li>
					</site:tree>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<div class="container-fluid content">
		<div class="col-md-3 col-sm-4 hidden-xs hidden-print">
			<div data-winlet="/doc/toc/toc?area=content" data-preload-forced></div>
		</div>
		<div id="content" class="col-md-9 col-sm-8 col-xs-12">
			<site:area name="content" />
		</div>
	</div>

	<div class="footer">
	</div>
</body>
</html>
</site:preload>