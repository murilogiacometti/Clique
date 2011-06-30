<%@ page import="java.lang.management.*" %>
<%@ page import="java.util.*" %>

JVM Memory Monitor

<%
Iterator iter = ManagementFactory.getMemoryPoolMXBeans().iterator();

while (iter.hasNext())
{
MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();
%>

Memory MXBean
Heap Memory Usage<%=
ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()
%>
Non-Heap Memory
Usage<%=
ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()
%>
<%} %>
 
Memory Pool MXBeans
<%
iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
while (iter.hasNext())
{
MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();
%>

<%= item.getName() %>
Type<%= item.getType() %>
Usage<%= item.getUsage() %>
Peak Usage<%= item.getPeakUsage() %>
Collection Usage<%= item.getCollectionUsage() %>

 
<%
}

%>