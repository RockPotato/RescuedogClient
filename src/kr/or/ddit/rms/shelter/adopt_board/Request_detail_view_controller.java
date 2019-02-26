package kr.or.ddit.rms.shelter.adopt_board;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.plaf.synth.SynthSplitPaneUI;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import kr.or.ddit.rms.main.Main;
import kr.or.ddit.rms.vo.Adopt_LogVO;
import kr.or.ddit.rms.vo.CustomVO;
import kr.or.ddit.rms.vo.RescuedogVO;
import kr.or.ddit.rms.vo.ShelterVO;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Request_detail_view_controller implements Initializable{

	@FXML AnchorPane request_Pane;
	@FXML JFXButton Close_Btn;
	@FXML JFXButton OK_Btn;
	@FXML JFXButton Cancel_Btn;
	
	@FXML Text Custom_ID_Lbl;	//CustomId 값
	
	@FXML Label Request_Name;	//요청자 이름
	@FXML Label Request_Addr;	//요청자주소
	@FXML Label Request_Email;	//요청자 emial
	
	
	@FXML Label Request_Tel;	//전화번호
	@FXML Label Request_ID;		//주민등록번호
	@FXML Label Request_Age;	//나이
	
	@FXML ImageView Dog_Img;
	
	@FXML Label Y1;
	@FXML Label Y2;
	public static String[] info ;
	
	private Request_view_controller rvc;
	
	public void newset(Request_view_controller rvc) {
		this.rvc = rvc;
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(Request_view_controller.H.getAccessibleText());
		info = Request_view_controller.H.getAccessibleText().split("/");
		
		Show_Detail_View();
		
		//X버튼 눌렀을떄
		Close_Btn.setOnAction(e->{
			Request_view_controller.request_view_dialog.close();
		});
		
		//승인 버튼 클릭
		OK_Btn.setOnAction(e->{
			recognize();
			rvc.show_rqList();
			Request_view_controller.request_view_dialog.close();
		});
		
		//거절 버튼 클릭
		Cancel_Btn.setOnAction(e->{
			unrecognize();
			Request_view_controller.request_view_dialog.close();
			rvc.show_rqList();
		});
		
		
	}
	
	private void Show_Detail_View() {
		
		try {
			CustomVO cvo = new CustomVO();
			cvo.setCustom_id(info[11]);
			
			List<CustomVO> custom_log = Main.ab_s.getSearchCustom(cvo);
			
			
			Custom_ID_Lbl.setText(info[11]);
			Request_Name.setText(info[1]);
			Request_Addr.setText(info[2]);
			Request_Email.setText(info[5]);
			Request_Tel.setText(custom_log.get(0).getCustom_tel());
			Request_ID.setText(info[4]);
			Request_Age.setText(info[3]);
			
			Image img = new Image("file:\\\\Sem-pc\\공유폴더\\Rescuedog\\Rescuedog\\"+info[8]);
			Dog_Img.setImage(img);
			Y1.setText(info[6]);
			Y2.setText(info[7]);
			
			if(!info[12].equals("승인 대기중")) {
				OK_Btn.setVisible(true);
				Cancel_Btn.setVisible(true);
			}
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void recognize() {
		
		//선택한 회원의 입양기록 상태 변경
		Adopt_LogVO avo = new Adopt_LogVO();
		avo.setCustom_id(info[11]);
		avo.setRd_num(info[9]);
		avo.setAdopt_check("승인 완료");
		
		//선택하지 않은 회원의 입양기록 상태 변경
		Adopt_LogVO avo2 = new Adopt_LogVO();
		avo2.setCustom_id(info[11]);
		avo2.setRd_num(info[9]);
		avo2.setAdopt_check("승인 거절");
		
		//선택한 유기견의 입양상태 변경 
		RescuedogVO rvo = new RescuedogVO();
		rvo.setRd_num(info[9]);
		rvo.setRd_check("입양 완료");
		
		try {
			Main.ab_s.Adopt_Request_Ok(avo);
			System.out.println("1");
			Main.ab_s.Adopt_Request_Cancel(avo2);
			System.out.println("2");
			Main.ab_s.updateRescuedogRd_check(rvo);
			System.out.println("3");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	private void unrecognize() {
		Adopt_LogVO avo = new Adopt_LogVO();
		avo.setCustom_id(info[11]);
		avo.setRd_num(info[9]);
		avo.setAdopt_check("승인 거절");
		try {
			Main.ab_s.Adopt_Request_Ok(avo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
