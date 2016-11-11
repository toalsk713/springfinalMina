package com.cjon.bank.service;

import java.sql.Connection;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;


public class BankSelectAllMemberService implements BankService{

	@Override
	public void execute(Model model) {
		//모든 사람에 대한 정보를 가져오는 로직을 수행
		
		DataSource dataSource = (DataSource)model.asMap().get("dataSource");
		Connection con;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			BankDAO dao = new BankDAO(con);
			ArrayList<BankDTO> list = dao.selectAll();
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
