package OuterInterfaces;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.BlogVisitor2DBService;
import domain.TipMessage;
import domain.blog_page;
import net.sf.json.JSONObject;


public class SelectVisitor extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginID = request.getParameter("loginID");
		String pageIndex = request.getParameter("pageIndex");
		TipMessage msg = new TipMessage();
		if (pageIndex == null){
			msg.setMessageCode("-100");
			msg.setMessageDetail("页码值未传送");
			JSONObject obj = JSONObject.fromObject(msg);
			response.getWriter().write(obj.toString());
			return;
		}
		String temp = (String) this.getServletContext().getAttribute("loginID");
		if (temp == null){
			msg.setMessageCode("-200");
			msg.setMessageDetail("登录已过期或未登录");
			JSONObject obj = JSONObject.fromObject(msg);
			response.getWriter().write(obj.toString());
			return;
		}
		if(!temp.equals(loginID)){
			msg.setMessageCode("-200");
			msg.setMessageDetail("登录已过期或未登录");
			JSONObject obj = JSONObject.fromObject(msg);
			response.getWriter().write(obj.toString());
		}else{
			BlogVisitor2DBService service = new BlogVisitor2DBService();
			blog_page page;
			String pageContain = request.getParameter("pageContain");
			if (checkPageFrame(pageContain)){
				page = service.selectUserByList(Integer.parseInt(pageIndex), Integer.parseInt(pageContain), 5);
			}else{
				page = service.selectUserByList(Integer.parseInt(pageIndex), 10, 5);
			}
			if(page == null){
				msg.setMessageCode("-300");
				msg.setMessageDetail("用户列表获取失败");
				JSONObject obj = JSONObject.fromObject(msg);
				response.getWriter().write(obj.toString());
			}else{
				JSONObject obj = JSONObject.fromObject(page);
				response.getWriter().write(obj.toString());
			}
		}
	}
	private boolean checkPageFrame(String pageFrame){
		if (pageFrame == null){
			return false;
		}else{
			try{
				Integer.parseInt(pageFrame);
			}catch(Exception e){
				return false;
			}
			return true;
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
