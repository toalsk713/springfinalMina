package com.cjon.bank.service;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;

public class BankSelectOneMemberService implements BankService {

	@Override
	public void execute(Model model) {
		
		//넘겨받은 파라미터 설정/////////////////////////////
		HttpServletRequest request = (HttpServletRequest) model.asMap().get("request");
		String keyword = request.getParameter("keyword");
		/////넘겨받은 파라미터 설정///		
		
		DataSource dataSource = (DataSource)model.asMap().get("dataSource");
		Connection con;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			BankDAO dao = new BankDAO(con);
			ArrayList<BankDTO> list = dao.selectOne(keyword);
			System.out.println("111111111111111111dfdfdfdfdfdf111111");
			if(list!=null){
				con.commit();
				model.addAttribute("RESULT",list);

			}else{
				con.rollback();
			}
			
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
