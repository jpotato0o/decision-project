<%@page import="java.text.DecimalFormat"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.majorhelper.decision.InfluenceDiagram"%>
<%@page import="com.majorhelper.decision.InfluenceDiagram.Decision"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%!
    public static double roundTwoDecimals(double d) {
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    return Double.valueOf(twoDForm.format(d));
    }
%>
    <% Map<String, String[]> paramMap = request.getParameterMap();
       InfluenceDiagram id = new InfluenceDiagram(paramMap);
       List<Decision> u = id.expectedUtilities("major", "total_value"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">
	google.load('visualization', '1', {packages:['table']});
	google.setOnLoadCallback(drawTable);
	
	function drawTable() {
	    // Create and populate the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Major');
        data.addColumn('number', 'Score');
        data.addRows([
        <%
        for (int i = 0; i < u.size(); i++) {
            Decision d = u.get(i);
            String major = InfluenceDiagram.majorMap.get(d.decision);
            out.print("[");
            out.print("'" + major + "'");
            out.print(", ");
            //out.print(roundTwoDecimals(d.utility));
            out.print(Math.round(d.utility));
            out.print("]");
            if (i < u.size()-1) {
                out.print(",");
            }
        }
        %>]);

	    var table = new google.visualization.Table(document.getElementById('table_div'));
	    table.draw(data, {showRowNumber: true});   
	}
	
</script>
<script src="js/icons.js" type="text/javascript"></script>
<title>majorhelper.com Results</title>
</head>
<body>
<div id="wrapper">

<div id="title">
<a href="./"><img id="banner" src="images/banner.gif"></a><br>
<div id="title_nav">
<a href="./" onMouseOver="hiLite('home')" onMouseOut="unHiLite('home')"><img name="homeIcon" src="images/home_no_highlight.gif"></a>
<a href="./help.html" onMouseOver="hiLite('help')" onMouseOut="unHiLite('help')"><img name="helpIcon" src="images/help_no_highlight.gif"></a>
<a href="./about.html" onMouseOver="hiLite('about')" onMouseOut="unHiLite('about')"><img name="aboutIcon" src="images/about_no_highlight.gif"></a>
<script type="text/javascript">
curPageHiLite(getCurPage());
</script>
</div><!-- #title_nav -->
</div><!-- #title -->

<div id="chart_table_wrapper">
<p> The table below shows majors and their corresponding scores. The majors with the highest scores matched your preferences the closest. These majors are at the top of the table.</p>
<div id="table_div" style="width: 100%;"></div>
</div>

</div><!-- #wrapper-->
</body>
</html>
