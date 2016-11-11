package com.cjon.bank.service;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;

public class BankWithdrawService implements BankService {

	@Override
	public void execute(Model model) {
				
		//넘겨받은 파라미터 설정/////////////////////////////
		HttpServletRequest request = (HttpServletRequest) model.asMap().get("request");
		String memberId = request.getParameter("memberId");
		String memberBalance = request.getParameter("memberBalance");
		/////넘겨받은 파라미터 설정///
		
		String checkTable = "st";
		DataSource dataSource = (DataSource)model.asMap().get("dataSource");
		Connection con =null;

		try {
			
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			
			/////////////////////////////여기까지 데이터베이스 환경설정//////////////////////////////

			
			
			////////////////////////////입금 로직 처리 부분!!!!!!!!!!!!!!///////////////////////
			BankDAO dao = new BankDAO(con);
			boolean result = dao.updateWithdraw(memberId,memberBalance,checkTable);
			
			if(result==true){
				con.commit();
				
			}else{
				con.rollback();
			}
			
			model.addAttribute("RESULT",result);
			con.close();

		} catch (Exception e) {	
			e.printStackTrace();
		}
		
	}////함수 종료
}
