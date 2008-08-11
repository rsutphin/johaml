<%@tag%>
<%@attribute name="count" required="true"%>
<%@attribute name="name" required="true"%>
${count} ${name}${count != 1 ? 's' : ''}
