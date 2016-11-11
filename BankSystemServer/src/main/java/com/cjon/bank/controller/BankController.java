package com.cjon.bank.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.service.BankDepositService;
import com.cjon.bank.service.BankSelectAllMemberService;
import com.cjon.bank.service.BankSelectOneMemberService;
import com.cjon.bank.service.BankService;
import com.cjon.bank.service.BankTransferService;
import com.cjon.bank.service.BankTransferTableService;
import com.cjon.bank.service.BankWithdrawService;

@Controller
public class BankController {
	
	private DataSource dataSource;
	private BankService service;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

	@RequestMapping(value="/selectAllMember")
	public void selectAllMember(HttpServletRequest request, HttpServletResponse response, Model model){
		
		//입력
		  String callback = request.getParameter("callback");
		  //로직
		  
		  service = new BankSelectAllMemberService();
		  model.addAttribute("dataSource", dataSource);
		  service.execute(model);
		  System.out.println("111111111111111111111111111");
		  
		  //로직 처리후 리턴된 변수 받기/////////////////////////////////////////////////
		  ArrayList<BankDTO> list =  (ArrayList<BankDTO>) model.asMap().get("RESULT");
		  System.out.println("222222222222222222222222");

		  ObjectMapper om = new ObjectMapper();
		  
		  try {
		   String json =  om.defaultPrettyPrintingWriter().writeValueAsString(list);
		   response.setContentType("text/plain; charset=utf8");
		   response.getWriter().println(callback + "("+json+")");
		  System.out.println(json);
		  } catch (JsonGenerationException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (JsonMappingException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }				
	}
	
	///아이디 검색하기!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!////////////////
	@RequestMapping(value="/selectOnemember")
	public void selectOnemember(HttpServletRequest request, HttpServletResponse response, Model model){
		
		//입력
		  String callback = request.getParameter("callback");
		  //로직
		  
		  service = new BankSelectOneMemberService();
		  model.addAttribute("dataSource", dataSource);
		  model.addAttribute("request",request);
		  service.execute(model);
		  System.out.println("한명검색으로 들어왔습니닷11");
		  
		  //로직 처리후 리턴된 변수 받기/////////////////////////////////////////////////
		  ArrayList<BankDTO> list =  (ArrayList<BankDTO>) model.asMap().get("RESULT");
		  System.out.println("222222222222222222222222");

		  ObjectMapper om = new ObjectMapper();
		  
		  try {
		   String json =  om.defaultPrettyPrintingWriter().writeValueAsString(list);
		   response.setContentType("text/plain; charset=utf8");
		   response.getWriter().println(callback + "("+json+")");
		  System.out.println(json);
		  } catch (JsonGenerationException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (JsonMappingException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }				
	}
	
	
	//입금하기
	@RequestMapping(value="/deposit")
	public void deposit(HttpServletRequest request, HttpServletResponse response, Model model){
		
		///클라이언트에서 넘겨받기//////////////////////////////
		String callback = request.getParameter("callback");
		service = new BankDepositService();
		model.addAttribute("dataSource",dataSource);
		model.addAttribute("request",request);
		service.execute(model);
		//////클라이언트에서 넘겨받기//////////////////////////////
		
		//결과처리 ///////////////////////////////////////
		boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(callback+"("+result+")");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//deposit (입금하기) 메소드 end//////////////////////////////////////////////
	

	//출금하기
	@RequestMapping(value="/withdraw")
	public void withdraw(HttpServletRequest request, HttpServletResponse response, Model model){
		
		///클라이언트에서 넘겨받기//////////////////////////////
		String callback = request.getParameter("callback");
		service = new BankWithdrawService();
		model.addAttribute("dataSource",dataSource);
		model.addAttribute("request",request);
		service.execute(model);
		//////클라이언트에서 넘겨받기//////////////////////////////
		
		//결과처리 ///////////////////////////////////////
		boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(callback+"("+result+")");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//withdraw end/////////////////////////////////////////////
	
	//이체하기
	@RequestMapping(value="/transfer")
	public void transfer(HttpServletRequest request, HttpServletResponse response, Model model){
		
		///클라이언트에서 넘겨받기//////////////////////////////
		String callback = request.getParameter("callback");
		service = new BankTransferService();
		model.addAttribute("dataSource",dataSource);
		model.addAttribute("request",request);
		service.execute(model);
		//////클라이언트에서 넘겨받기//////////////////////////////
		
		//결과처리 ///////////////////////////////////////
		boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(callback+"("+result+")");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//withdraw end/////////////////////////////////////////////
	
	
	
	//transferTable
	@RequestMapping(value="/transferTable")
	public void updatetransferTable(HttpServletRequest request, HttpServletResponse response, Model model){
		
		///클라이언트에서 넘겨받기//////////////////////////////
		String callback = request.getParameter("callback");
		service = new BankTransferTableService();
		model.addAttribute("dataSource",dataSource);
		model.addAttribute("request",request);
		service.execute(model);
		//////클라이언트에서 넘겨받기//////////////////////////////
		
		  //로직 처리후 리턴된 변수 받기/////////////////////////////////////////////////
		  ArrayList<BankDTO> list =  (ArrayList<BankDTO>) model.asMap().get("RESULT");
		  System.out.println("222222222222222222222222");

		  ObjectMapper om = new ObjectMapper();
		  
		  try {
		   String json =  om.defaultPrettyPrintingWriter().writeValueAsString(list);
		   System.out.println(json);
		   
		   response.setContentType("text/plain; charset=utf8");
		   response.getWriter().println(callback + "("+json+")");
		  System.out.println(json);
		  } catch (JsonGenerationException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (JsonMappingException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }				
	}//withdraw end/////////////////////////////////////////////
	
}