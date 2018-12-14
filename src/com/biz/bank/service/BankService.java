package com.biz.bank.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biz.bank.vo.BankVO;

/*
 * BankService 클래스의 핵심부분
 * 1. findId() : bankList에서 계좌번호를 조회하는 부분
 * 가. 매개변수로 strId(String)값을 받고
 * 나. bankList를 순회(반복)하면서 
 * 다. bankList에 들어있는 vo의 strId값을 추출해서
 * 		(bankList.get(i).getStrId 또는 vo.getStrId()이용)
 * 라. 매개변수로 받은 strId와 일치하는 값이 있는지 검사한다.
 * 		if(vo.getStrId.equals(strId), if(strId.equals(vo.getStrId()))
 * 
 * 마. 만약 bankList에 찾고자 하는 id가 없을 경우
 * 		if(vo.getStrId().equals(strId) == false)
 * 	또는 if(vo.getStrId().equals(strId) != true) 라고 쓰이나
 * 	보편적 코드에서는 if(!vo.getStrId().equals(strId))
 * 
 * 바. findId()는 null을 return해서 값이 없음을 알리고
 * 사. 만약 bankList에 찾고자 하는 id가 있으면 
 * 아. findId()는 찾은 vo를 return해 준다.
 * 
 * 
 */
public class BankService {

	List<BankVO> bankList;
	String strFileName;
	String ioFoler;
	
	Scanner scan;
	
	// 멤버변수 영역에 있는 변수, 객체는 생성자에서 초기화한다.
	public BankService(String strFileName) {
		bankList = new ArrayList();
		this.strFileName =strFileName; 
		this.ioFoler = "src/com/biz/bank/iolist/";
		this.scan = new Scanner(System.in);
		
	}
	
	// 원장을 update
	public void bankBalanceWrit() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(strFileName);
			for(BankVO vo : bankList) {
				pw.println(vo.getStrId()+":"+vo.getIntBalance()+":"+vo.getStrLastDate());
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	// 입출금 내역을 저장하는 method
	public void bankIoWrite(String io, int intB,BankVO v) { //int intB = 입금액
		
		FileWriter fw;
		PrintWriter pw;
		String thisId = v.getStrId(); //계좌번호
		
		try {
			// 2번째 매개변수 true : 파일을 Append mode로 열어라 => 기존의 파일이 있으면 파일을 열고 없으면 만들어라?
			fw = new FileWriter(ioFoler+thisId,true);	// 기존 파일에 계속 추가해야하므로 FileWriter가 필요하다.
			pw = new PrintWriter(fw); 
			
			if(io.equals("I")) {
				pw.printf("%s:%s:%d:%d:%d:%s\n", 
						v.getStrLastDate(),
						"입금",
						intB,
						v.getStrId(),
						v.getIntBalance());
			} else {
				pw.printf("%s:%s:%d:%d:%d:%s\n", 
						v.getStrLastDate(),
						"출금",
						intB,
						v.getStrId(),
						v.getIntBalance());
				
			}
			
		
			
					
			
			
			pw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void bankMenu() {
		
		this.readFile();
		while(true) {
			
			System.out.println("=======================================");
			System.out.println("1.입금   2.출금   3. 계좌조회   0.종료");
			System.out.println("---------------------------------------");
			System.out.println("업무선택>> ");
			String sMenu = scan.nextLine();
			int intMenu = Integer.valueOf(sMenu);
			if(intMenu == 0) break;
			if(intMenu == 1) this.bankInput();
			if(intMenu == 2) this.bankOutput();
			
		}
	}
	
	
	public void bankInput() {
		
	//	Scanner scan = new Scanner(System.in); 생성자에서 초기화 했으므로 필요없음
		
		System.out.print("입금계좌번호>> ");
		String strN = scan.nextLine(); // 입력받은 값 
		
		BankVO b = findId(strN); // (findId(strN):계좌번호를 findId()에게 전달하는 코드)
		
		if( b==null ) {
			System.out.println("계좌번호 없음");
			return; // 메인에서 불렀기 때문에 메인으로 돌아가라
		}
		
		int iB = b.getIntBalance(); // 찾았으면 IntBalance값을 받아서 iB에 줘라
		System.out.print("입급액 >> ");
		String strB = scan.nextLine();
		int intB = Integer.valueOf(strB); 
		
		int lB = iB + intB;
		b.setIntBalance(lB);
		
		System.out.print("입금완료");
	
		this.bankBalanceWrit();
		this.bankIoWrite("I",intB,b); //b를 매개변수로 전달한다.
	}
	
	public void bankOutput() {
		
		//Scanner scan = new Scanner(System.in); 생성자에서 초기화했으므로 필요없음
		
		System.out.print("출금계좌번호>> ");
		String strN = scan.nextLine(); // 입력받은 값 
		
		BankVO b = findId(strN); // (findId(strN):계좌번호를 findId()에게 전달하는 코드)
		
		if( b==null ) {
			System.out.println("계좌번호 없음");
			return; // 메인에서 불렀기 때문에 메인으로 돌아가라
		}
		// 찾았으면 IntBalance값을 받아서 iB에 줘라
		int iB = b.getIntBalance();
		
		System.out.print("출금액 >> ");
		String strB = scan.nextLine();
		int intB = Integer.valueOf(strB); //문자형을 정수형으로 바꿔준다
		
		if(iB < intB) {
			System.out.println("잔액부족 출금불가!!");
			return;
	}
		int lB = iB - intB;
		b.setIntBalance(lB);

		System.out.println("출금완료");
		this.bankIoWrite("O",intB,b);
		this.bankBalanceWrit();
		
		
	}
	
	public void readFile() {
		// TODO 파일을 읽어 bankList에 저장하는 메서드
		
		FileReader fr;
		BufferedReader buffer;
		
		try {
			fr = new FileReader(this.strFileName);
			buffer = new BufferedReader(fr);
			
			while(true) {
				String reader = buffer.readLine();
				if(reader == null) break;
				//System.out.println(reader);
				
				String [] banks = reader.split(":");
				//System.out.println(banks[0]+"-");
				//System.out.println(banks[1]+"-");
				//System.out.println(banks[2]+"-");
				System.out.printf("%s-%s-%s\n",banks[0],banks[1],banks[2]);
				
				BankVO vo = new BankVO();
				vo.setStrId(banks[0]);
				vo.setIntBalance(Integer.valueOf(banks[1]));
				vo.setStrLastDate(banks[2]);
				bankList.add(vo);
				
			}
			buffer.close();
			fr.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public BankVO findId(String strId) {
		for(BankVO vo : bankList) { // vo 는 bankList의 한개 한개의 항목들이다.
		
			if(vo.getStrId().equals(strId)) {
				System.out.println(vo);
				
				return vo; // 찾았을 경우
		}
	
		}
		return null; //찾지 못했을 경우 (for문 밖에서 해줘야한다.)
		
		
	}
	
	
}
