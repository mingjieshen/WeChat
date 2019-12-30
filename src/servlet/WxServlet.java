package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.WxService;

/**
 * Servlet implementation class WxServlet
 */
@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WxServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
			timestamp	时间戳
			nonce	随机数
			echostr	随机字符串
		 */
		String signature=request.getParameter("signature"); 
		String timestamp=request.getParameter("timestamp"); 
		String nonce=request.getParameter("nonce"); 
		String echostr=request.getParameter("echostr");
		if(WxService.check(timestamp,nonce,signature)) {
			PrintWriter out = response.getWriter();
			//原样返回echostr参数
			out.print(echostr);
			out.flush();
			out.close();
			System.out.println("接入成功");
		}else {
			System.out.println("接入失败");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		//处理消息和事件推送
		Map<String,String> requestMap=WxService.parseRequest(request.getInputStream());
		System.out.println(requestMap);
		//准备回复的数据包
		String respXml="<xml>\r\n" + 
				"  <ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\r\n" + 
				"  <FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName>\r\n" + 
				"  <CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime>\r\n" + 
				"  <MsgType><![CDATA[text]]></MsgType>\r\n" + 
				"  <Content><![CDATA[哈哈]]></Content>\r\n" + 
				"  <MsgId>1234567890123456</MsgId>\r\n" + 
				"</xml>";
		PrintWriter out=response.getWriter();
		out.print(respXml);
		out.flush();
		out.close();
	}

}
