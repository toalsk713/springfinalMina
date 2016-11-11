package com.cjon.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.dto.AccountDTO;

public class BankDAO {

	private Connection con;
	
	public BankDAO(Connection con){
		this.con = con;
	}
	
	public ArrayList<BankDTO> selectAll() {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BankDTO> list = new ArrayList<BankDTO>();
		
		try {
			
			String sql = "select * from bank_member_tb";
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("444444444444");

			while(rs.next()){
				System.out.println("333333333333");
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setMemberName(rs.getString("member_name"));
				dto.setMemberAccount(rs.getString("member_account"));;
				dto.setMemberBalance(rs.getInt("member_balance"));
				list.add(dto);
				System.out.println("여기서확인 member_id"+ rs.getString("member_id"));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{//finally start
			
			try {//try catch start
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}//try catch end
		}//finally end
		System.out.println(list.get(0).getMemberAccount());
		return list;	
		
	}	//////selectAll end////////////////////////////////////////////////////////

	
	public ArrayList<BankDTO> selectOne(String keyword) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BankDTO> list = new ArrayList<BankDTO>();
		
		try {
			
			String sql = "select * from bank_member_tb where member_id = ?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rs = pstmt.executeQuery();

			while(rs.next()){
				System.out.println("333333333333");
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setMemberName(rs.getString("member_name"));
				dto.setMemberAccount(rs.getString("member_account"));;
				dto.setMemberBalance(rs.getInt("member_balance"));
				list.add(dto);
				System.out.println("여기서확인 member_id"+ rs.getString("member_id"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{//finally start
			
			try {//try catch start
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}//try catch end
		}//finally end
		System.out.println(list.get(0).getMemberAccount());
		return list;	
		
	}///select one////////////////////////////////////////////////////
	
	
	
	
	
	//입금하기 메소드//////////////////////////////////////////////////////////////////
	public boolean updateDeposit(String memberId, String memberBalance,String checkTable ) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			String sql = "update bank_member_tb set member_balance=member_balance+? where member_id=?";
			
			pstmt = con.prepareStatement(sql);
			
			//입금액 파싱//////////////////////////////
			int money = Integer.parseInt(memberBalance);
			
			pstmt.setInt(1, money);
			pstmt.setString(2, memberId);
			int count = pstmt.executeUpdate();
			
			if (count==1){
				///여기는 statement테이블 처리 부분	
				boolean check =false;
				//check가 st면 statement테이블에 입력
				if(checkTable.equals("st")){
					check = updateStatement(memberId,"입금",money);
					if(check==true) result = true;
					else result = false;
				
				}else{
					result = true;
				}
				//////////./////////////////////////////////////////
				
				System.out.println("BookDAO에서 입금 되었습니다.");
				
			}else{
				result = false;
				System.out.println("BookDAO에서 입금이 제대로 되지 않았습니다. ");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{//finally start
			
			try {//try catch start
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}//try catch end
		}//finally end
		return result;	
	}////////////////////deposit(입금하기) end /////////////////////////////////////////////

	public boolean updateWithdraw(String memberId, String memberBalance, String checkTable) {
		PreparedStatement pstmt = null;
		boolean result = false;
		
		try {
			
			String sql = "update bank_member_tb set member_balance=member_balance-? where member_id=?";
			
			pstmt = con.prepareStatement(sql);
			int money = Integer.parseInt(memberBalance);
			pstmt.setInt(1, money);
			pstmt.setString(2, memberId);
			int count = pstmt.executeUpdate();
			
			if (count==1){
				
				
				String sql1 = "select member_balance from bank_member_tb where member_id=?";
				PreparedStatement pstmt1 = null;
				ResultSet rs1 = null;
				pstmt1 = con.prepareStatement(sql1);
				pstmt1.setString(1, memberId);
				rs1 = pstmt1.executeQuery();
				
				while(rs1.next()){
					if(rs1.getInt("member_balance")<0){
						System.out.println("출금이 불가능합니다 BankDAO 에서,...");
						result= false;
					}else{
						
						///여기는 statement테이블 처리 부분	
						boolean check =false;
						//check가 st면 statement테이블에 입력
						if(checkTable.equals("st")){
							check = updateStatement(memberId,"출금",money);
							System.out.println("출금이 되었습니닷!!");
							if(check==true) result = true;
							else result = false;
						
						}else{
							result = true;
						}
						//////////./////////////////////////////////////////

						System.out.println("출금 되었습니다. BankDao에서...");
					}
				}
								
			}else{
				result = false;
				System.out.println("BookDAO에서 입금이 제대로 되지 않았습니다. ");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{//finally start
			
			try {//try catch start
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}//try catch end
		}//finally end
		return result;	
	}

	public boolean updateTransfer(String sendid, String sendrec, String transferBalance) {
		boolean result1 = updateWithdraw(sendid,transferBalance,"tr");
		boolean result2 = updateDeposit(sendrec,transferBalance,"tr");
		boolean result3 = updateTransfertb(sendid,sendrec,Integer.parseInt(transferBalance));
		boolean finalresult = false;
		
		if(result1==true&&result2==true&&result3==true){
			finalresult = true;
		}else{
			finalresult =false;
		}
		return finalresult;
	}
	
	//statement테이블 고치기
	public boolean updateStatement(String memberId, String kind, int Balance) {
		
		PreparedStatement pstmt = null;
		boolean result = false;
		
		try {
			///여기는 statement테이블 처리 부분
			String sql = "insert into bank_statement_tb(MEMBER_ID,KIND,MONEY,date) values(?,?,?,now())";
			pstmt =con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, kind);
			pstmt.setInt(3, Balance);
			
			int count1 = pstmt.executeUpdate();
			if(count1==1) result = true;
			else result = false;
			System.out.println("BookDAO에서 updateStatement 수정 되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
		}finally{//finally start
			
			try {//try catch start
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}//try catch end
		}//finally end
		return result;	
	}

	
	//transfertb테이블 수정하기!!!!
	public boolean updateTransfertb(String sendId, String receiveId, int balance) {
		
		PreparedStatement pstmt = null;
		boolean result = false;
		
		try {
			///여기는 statement테이블 처리 부분
			String sql = "insert into bank_transfer_history_tb(SEND_MEMBER_ID,RECEIVE_MEMBER_ID,TRANSFER_MONEY,date) values(?,?,?,now())";
			pstmt =con.prepareStatement(sql);
			pstmt.setString(1, sendId);
			pstmt.setString(2, receiveId);
			pstmt.setInt(3, balance);
			
			int count1 = pstmt.executeUpdate();
			if(count1==1) result = true;
			else result = false;
			System.out.println("BookDAO에서 updateTransfertb 수정 되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
		}finally{//finally start
			
			try {//try catch start
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}//try catch end
		}//finally end
		return result;	
	}

	
	public ArrayList<AccountDTO> selectAccount(String memberId) {
		  PreparedStatement pstmt=null;
		   ResultSet rs=null;
		   ArrayList<AccountDTO> list=new ArrayList<AccountDTO>();
		    
		   try{
		    String sql="select kind, member_id, money, date from bank_statement_tb where MEMBER_ID = ?";

		    pstmt= con.prepareStatement(sql);
		    pstmt.setString(1, memberId);
		    rs=pstmt.executeQuery();
		    
		    while(rs.next()){
		     AccountDTO dto=new AccountDTO();
		     
		     System.out.println(rs.getString("kind"));
		     
		     if( rs.getString("kind").equals("deposit") ) {
		      dto.setState("입금");
		     }
		     if( rs.getString("kind").equals("withdraw") ) {
		      dto.setState("출금");
		     }

		     dto.setSendid(""); //본인
		     dto.setReceiveid(""); //본인
		     dto.setMoney(rs.getInt("money"));
		     dto.setDate(rs.getString("date"));
		     
		     list.add(dto);
		    }
		   }catch(Exception e){
		    System.out.println(e);
		   }finally{
		    try {
		     rs.close();
		     pstmt.close();
		    } catch (SQLException e) {
		     e.printStackTrace();
		    }
		   }
		   return list;
		 }
		 
		 public ArrayList<AccountDTO> selectAccountTransfer(String memberId) {
		  PreparedStatement pstmt=null;
		   ResultSet rs=null;
		   ArrayList<AccountDTO> list=new ArrayList<AccountDTO>();
		    
		   try{
		    String sql="select send_member_id, receive_member_id, transfer_money, date from bank_transfer_history_tb where SEND_MEMBER_ID = ? or RECEIVE_MEMBER_ID = ?";

		    pstmt= con.prepareStatement(sql);
		    pstmt.setString(1, memberId);
		    pstmt.setString(2, memberId);
		    rs=pstmt.executeQuery();
		    
		    while(rs.next()){
		     AccountDTO dto=new AccountDTO();
		     
		     System.out.println(rs.getString("send_member_id"));
		     System.out.println(memberId);
		     
		     // 당사자가 보냈을 때
		     if( rs.getString("send_member_id").equals(memberId) ) {
		      dto.setState("이체입금");
		      dto.setSendid(memberId); //보낸사람
		      dto.setReceiveid(rs.getString("receive_member_id"));
		     }
		     // 당사자가 받았을 때
		     if( rs.getString("receive_member_id").equals(memberId) ) { 
		      dto.setState("이체출금");
		      dto.setSendid(rs.getString("send_member_id")); 
		      dto.setReceiveid(memberId); //받은사람
		     }

		     dto.setMoney(rs.getInt("transfer_money"));
		     dto.setDate(rs.getString("date"));

		     list.add(dto);
		    }
		   }catch(Exception e){
		    System.out.println(e);
		   }finally{
		    try {
		     rs.close();
		     pstmt.close();
		    } catch (SQLException e) {
		     e.printStackTrace();
		    }
		   }
		   return list;
		 }



}