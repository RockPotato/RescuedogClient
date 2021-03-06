package kr.or.ddit.rms.mainpage.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import aesExam.Aes256;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kr.or.ddit.rms.main.Main;
import kr.or.ddit.rms.mainpage.main_page.ChangeScene;
import kr.or.ddit.rms.mainpage.main_page.Main_page_controller;
import kr.or.ddit.rms.mainpage.sign_up.Signup_User_Controller;
import kr.or.ddit.rms.vo.AdminVO;
import kr.or.ddit.rms.vo.CustomVO;
import kr.or.ddit.rms.vo.MemberVO;
import kr.or.ddit.rms.vo.ShelterVO;

public class Login_controller implements Initializable{
	//fx:controller="kr.or.ddit.rms.mainpage.login.main_page_controller"
	public static MemberVO log;
	public static CustomVO log_c;
	public static ShelterVO log_s;
	public static AdminVO log_a;
	
	@FXML JFXButton MainPage_SignUp_Btn;
	@FXML JFXButton MainPage_FindId_Btn;
	@FXML JFXButton MainPage_FindPw_Btn;
	@FXML JFXButton MainPage_Login_Btn;
	@FXML JFXButton MainPage_exit_Btn;
	
	@FXML JFXTextField MainPage_Id_Txt;
	@FXML JFXPasswordField MainPage_Pw_Txt;
	
	public static Stage dialog;
	//public static Stage findpw_dialog;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		//로그인 버튼 클릭
		MainPage_Login_Btn.setOnAction(e->login());
		MainPage_Pw_Txt.setOnAction(e->login());
		
		//회원가입 클릭
		MainPage_SignUp_Btn.setOnAction(e->{
			//새창 띄우기
			dialog = new Stage(StageStyle.UTILITY);
			
			dialog.initModality(Modality.APPLICATION_MODAL);
			//dialog.initOwner();
			dialog.setTitle("회원가입");
			
			Parent parent = null;
			try {
				parent = FXMLLoader.load(Signup_User_Controller.class.getResource("signup_choice.fxml"));
			} catch(IOException ee) {
				ee.printStackTrace();
			}
			Scene scene = new Scene(parent);
			
			dialog.setScene(scene);
			dialog.setResizable(false);//크기고정
			dialog.show();
		});
		
		//ID찾기 클릭
		MainPage_FindId_Btn.setOnAction(e->{
			
			//새창 띄우기
			dialog = new Stage(StageStyle.UTILITY);
			
			dialog.initModality(Modality.APPLICATION_MODAL);
			//dialog.initOwner();
			dialog.setTitle("ID 찾기");
			
			Parent parent = null;
			try {
				parent = FXMLLoader.load(getClass().getResource("find_id.fxml"));
			} catch(IOException ee) {
				ee.printStackTrace();
			}
			
			Scene scene = new Scene(parent);
			
			dialog.setScene(scene);
			dialog.setResizable(false);//크기고정
			dialog.show();
			
		});
		
		
		//PW찾기 클릭
		MainPage_FindPw_Btn.setOnAction(e->{
			
			dialog = new Stage(StageStyle.UTILITY);
			
			dialog.initModality(Modality.APPLICATION_MODAL);
			//dialog.initOwner();
			dialog.setTitle("PW 찾기");
			
			Parent parent = null;
			try {
				parent = FXMLLoader.load(getClass().getResource("find_pw.fxml"));
			} catch(IOException ee) {
				ee.printStackTrace();
			}
			
			Scene scene = new Scene(parent);
			
			dialog.setScene(scene);
			dialog.setResizable(false);//크기고정
			dialog.show();
		});
		
		MainPage_exit_Btn.setOnAction(e->{
			System.exit(0);
		});
		
		
	} //initialize 
	
	private void login() {
		try {
			
			//ID, PW 미입력시 경고창 출력
			if(MainPage_Id_Txt.getText().isEmpty() != false) {
				alertTest("ID를 입력 해주세요!!");
				MainPage_Id_Txt.requestFocus();
				return;
			} else if(MainPage_Pw_Txt.getText().isEmpty() != false) {
				alertTest("PW를 입력 해주세요!!");
				MainPage_Pw_Txt.requestFocus();
				return;
			}
			
			
			String id = MainPage_Id_Txt.getText();
			String pw = MainPage_Pw_Txt.getText();
			
			//vo.setMem_pw(pw);
			
			MemberVO vo = new MemberVO();
			vo.setMem_id(id);
			MemberVO searchMember = Main.li.getSearchMember(vo);
			if(searchMember==null) {
				alertTest("없는 ID입니다.");
				return;
			}
			if(!searchMember.getMem_author().equals("3")) {
				String key = "RescuedogManagement"; 
				Aes256 aes256;
				String decText = null;
				try {
					aes256 = new Aes256(key);
					decText = aes256.aesDecode(searchMember.getMem_pw());
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (InvalidAlgorithmParameterException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				}
				if(!pw.equals(decText)) {
					alertTest("PW가 틀렸습니다");
					return;
				} 
			}else {
				if(!pw.equals(searchMember.getMem_pw())) {
					alertTest("관리자 PW가 틀렸습니다");
					return;
				} 
			}
			
			//일반회원 log 기록 추가
			CustomVO cvo = new CustomVO();
			cvo.setCustom_id(searchMember.getMem_id());
			CustomVO searchCustom = Main.li.getSearchCustom(cvo);
			
			ShelterVO svo = new ShelterVO();
			svo.setShel_id(searchMember.getMem_id());
			ShelterVO seachShelter = Main.li.getSearchShelter(svo);
			
			AdminVO avo = new AdminVO();
			avo.setAdmin_id(searchMember.getMem_id());
			AdminVO searchAdmin = Main.li.getSearchAdmin(avo);
			
			
			//화면전환
			switch(searchMember.getMem_author()) {
				case "1":
					log = searchMember;
					log_c = searchCustom;
					gotoFirstScene();
					break;
				case "2":
					log = searchMember;
					log_s = seachShelter;
					gotoFirstScene();
					break;
				case "3":
					log = searchMember;
					log_a = searchAdmin;
					gotoFirstScene();
					break;
				default :
					break;
			}
			
		}catch (RemoteException e) {
			System.out.println("아이디 없을때 뜨는 오류");
		} 
	}

	private void gotoFirstScene() {
		Class sc = Main_page_controller.class;
		ChangeScene.ChangeView(sc, "main_page.fxml", true);
	}

	//에러메시지 메서드
	public void errMsg(String headerText, String msg) {
		Alert errAlert = new Alert(AlertType.ERROR);
		errAlert.setTitle("Error");
		errAlert.setHeaderText(headerText);
		errAlert.setContentText(msg);
		errAlert.showAndWait();
		
	}
	
	
	//알림창
	public void infoMsg(String headerText, String msg) {
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("info");
		infoAlert.setHeaderText(headerText);
		infoAlert.setContentText(msg);
		infoAlert.showAndWait();
	}
	
	public void alertTest(String message){
	      Alert alertErorr = new Alert(AlertType.ERROR);
	      alertErorr.setTitle("ERROR");
	      alertErorr.setContentText(message);
	      alertErorr.showAndWait();
	}
}
