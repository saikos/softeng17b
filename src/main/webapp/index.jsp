<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="gr.ntua.ece.softeng17b.*" %>
<%@ page import="gr.ntua.ece.softeng17b.conf.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>JSP</title>
    </head>

    <body>

        <h2>
        	Configuration
    	</h2>
    	<div>
    		<%
    		Configuration conf = Configuration.getInstance();
    		for(String key : conf.propertyNames()) {
    		%>
    			<div>
    				<%= key %> : <%= conf.getProperty(key) %>
				</div>
    		<% } %>
    	</div>


    	<h2>
    		Calculation
    	</h2>

        <div>
        	<form method="get">
        		<input type="text" name="numbers" size="48" placeholder="Εισάγετε ακεραίους χωρισμένους με κόμμα">
        		<button type="submit">Υπολογισμός kokolala</button>
    		</form>
    	</div>

    	<%
    	String inputNumbers = request.getParameter("numbers");
    	if (inputNumbers != null) {    	
    		Numbers numbers = Numbers.parse(inputNumbers);
    	%>
	    	<hr>
	    	<div>
	    	Πλήθος: <%= numbers.count() %> αριθμοί.
	    	</div>

	    	<div>
	    	Άθροισμα: <%= numbers.sum() %>
	    	</div>

    	<%
    	}
    	%>

    </body>
</html>
