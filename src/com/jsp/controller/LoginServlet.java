package com.jsp.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jsp.dao.TankMasterDAO;
import com.jsp.dto.TankMasterVO;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("EUC-KR");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String lev = request.getParameter("lev");
		String url=null;
		
		TankMasterDAO tankDAO = TankMasterDAO.getInstance();
		int result = tankDAO.userCheck(id, pwd, lev);
		if(result == 2 || result == 3){
			TankMasterVO tankmaster = new TankMasterVO();
			tankmaster = tankDAO.getMember(id);
			
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", tankmaster);			
			session.setAttribute("result", result);			
			url = "index.jsp";				
		}else{
			url = "index.jsp";
			if(result == 1){
				request.setAttribute("message", "사용자 권한이 맞지 않습니다.");
			}else if(result == 0){			
				request.setAttribute("message", "비밀번호가 맞지 않습니다.");
			}else{			
				request.setAttribute("message", "왜 이러지.....");
			}
		}
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}
}
