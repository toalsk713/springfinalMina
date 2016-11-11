package com.cjon.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.AccountDTO;

public class BankTransferTableService implements BankService {

	@Override
	public void execute(Model model) {
		
		//넘겨받은 파라미터 설정/////////////////////////////
		HttpServletRequest request = (HttpServletRequest) model.asMap().get("request");
		String keyword = request.getParameter("keyword");
		/////넘겨받은 파라미터 설정///		
		
		DataSource dataSource = (DataSource) model.asMap().get("dataSource");
		  Connection con = null;
		  try {
		   con = dataSource.getConnection();
		   con.setAutoCommit(false);
		   BankDAO dao = new BankDAO(con);
		   ArrayList<AccountDTO> list = dao.selectAccount(keyword);
		   
		   ArrayList<AccountDTO> list1 = dao.selectAccountTransfer(keyword);
		   for(int i=0;i<list1.size();i++){
		    list.add(list1.get(i));
		   }
		   
		   
		   if( list != null ) {
		    con.commit();
		   } else {
		    con.rollback();
		   }

		   model.addAttribute("RESULT", list);
		   con.close();
		   
		  } catch (SQLException e) {
		   e.printStackTrace();
		  } finally {
		   try {
		    con.close();
		   } catch (Exception e2) {
		    // TODO: handle exception
		   }
		  }
	}	
}
