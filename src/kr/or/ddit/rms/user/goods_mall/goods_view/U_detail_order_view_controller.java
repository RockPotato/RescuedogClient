package kr.or.ddit.rms.user.goods_mall.goods_view;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.layout.AnchorPane;
import kr.or.ddit.rms.main.Main;
import kr.or.ddit.rms.mainpage.login.Login_controller;
import kr.or.ddit.rms.user.goods_mall.cart_view.Cart_view_controller;
import kr.or.ddit.rms.vo.Buy_LogVO;
import kr.or.ddit.rms.vo.CardVO;
import kr.or.ddit.rms.vo.CustomVO;

public class U_detail_order_view_controller implements Initializable {

	@FXML AnchorPane Order_Main_Pane;

	@FXML Label Order_Amount_Lbl;		//총수량 라벨	
	@FXML JFXButton Order_Pay_Btn;		//결제 버튼
	@FXML Label Order_Message_Lbl;		//경고메시지 라벨
	
	@FXML Label Order_Name_Lbl;			//구매자 이름 라벨
	@FXML Label Order_Email_Lbl;		//구매자 이메일 라벨
	@FXML Label Order_Tel_Lbl;			//구매자 전화번호 라벨
	
	@FXML JFXTextField Order_Name_Txt;		//배송정보 이름 라벨
	@FXML JFXTextField Order_Addr_Txt;		//배송정보 주소 라벨
	@FXML JFXTextField Order_Tel_Txt;		//배송정보 연락처 라벨
	@FXML JFXTextField Order_Request_Txt;	//배송정보 요청사항 라벨
	
	@FXML Label Order_Price_Lbl2;			//결제정보 총금액 라벨
	@FXML Label Order_Point_Lbl;			//결제정보 포인트 라벨
	@FXML JFXComboBox Order_Selcard_Comb;	//결제정보 카드사 콤보박스
	@FXML JFXTextField Order_Card1_Txt;		//결제정보 카드번호1 텍스트
	@FXML JFXTextField Order_Card2_Txt;		//결제정보 카드번호2 텍스트
	@FXML JFXTextField Order_Card3_Txt;		//결제정보 카드번호3 텍스트
	@FXML JFXTextField Order_Card4_Txt;		//결제정보 카드번호4 텍스트
	@FXML JFXTextField Order_CVC_Txt;		//결제정보 CVC번호 텍스트
	@FXML JFXButton Close_Btn;				//창닫기 버튼
	
	@FXML Label Order_Price_Lbl;			//총금액 라벨

	String[] prod_info = null;
	String name = null;
	String price = null;
	String point = null;
	String qaun = null;
	int int_point = 0;
			
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		name = U_goods_detail_view_controller.name;	
		price = U_goods_detail_view_controller.price;
		point = U_goods_detail_view_controller.point;
		qaun = U_goods_detail_view_controller.qaun;
		
		prod_info = U_goods_detail_view_controller.prod_info;
		int_point = Integer.parseInt(point);
		
		
		//닫기버튼 클릭
		Close_Btn.setOnAction(e->{
			U_goods_detail_view_controller.goods_detail_view_dialog.close();
		});
		
		//구매 종류
		Order_Amount_Lbl.setText("(1개)");
		
		//구매자정보 세팅
		Order_Name_Lbl.setText(Login_controller.log_c.getCustom_name());
		Order_Email_Lbl.setText(Login_controller.log_c.getCustom_email());
		Order_Tel_Lbl.setText(Login_controller.log_c.getCustom_tel());
		
		//배송정보 세팅
		Order_Name_Txt.setText(Login_controller.log_c.getCustom_name());
		Order_Addr_Txt.setText(Login_controller.log_c.getCustom_addr());
		Order_Tel_Txt.setText(Login_controller.log_c.getCustom_tel());
		Order_Request_Txt.setPromptText("요청사항을 입력주세요");
		
		//결제정보 세팅
		Order_Price_Lbl2.setText(price);
		Order_Point_Lbl.setText(point);
		Order_Price_Lbl.setText(price);
				
		
		
		

		//콤보박스에 카드사 세팅
		try {
			List<CardVO> list = Main.gm_u.getCardAll();
			for(int i=0; i<list.size(); i++) {
				Order_Selcard_Comb.getItems().add(list.get(i).getCard_name());
			}
		} catch (RemoteException e2) {
			
		}
		
		//결제 버튼 클릭
		Order_Pay_Btn.setOnAction(e->{
			if(Order_Card1_Txt.getText().isEmpty() || Order_Card2_Txt.getText().isEmpty()
					|| Order_Card3_Txt.getText().isEmpty() || Order_Card4_Txt.getText().isEmpty()
					|| Order_CVC_Txt.getText().isEmpty()) {
				
					alertError("결제정보 입력 란이 비었습니다");
				return;
			}
			payment();
			U_goods_detail_view_controller.goods_detail_view_dialog.close();
			U_goods_view_controller.goods_view_dialog.close();
			alertTest("결제 완료!! \n총 구매금액 : " + Order_Price_Lbl.getText() + "\n적립 포인트 : " + point);
		});
		
		
	}


	private void payment() {
		try {
			String card_name = (String) Order_Selcard_Comb.getValue();
			
			String tmp = Main.gm_u.getBuy_num();
			if(tmp==null) {
				tmp = "0";
			}
			int buy_num = Integer.parseInt(tmp);	
			
			Buy_LogVO bvo = new Buy_LogVO();
			bvo.setProd_name(prod_info[0]);
			bvo.setCustom_id(Login_controller.log_c.getCustom_id());
			bvo.setPrice(price);
			bvo.setBuy_quan(qaun);
			bvo.setRefund_check("배송준비중");
			bvo.setCard_name(card_name);
			bvo.setProd_num(prod_info[4]);
			bvo.setBuy_num(buy_num + 1 + "");
			
			//구매기록 추가
			Main.gm_u.insertBuy_log(bvo);
			
			//로그인한 회원의 포인트 정보 가져오기
			CustomVO cvo = new CustomVO();
			cvo.setCustom_id(Login_controller.log_c.getCustom_id());
			List<CustomVO> sc = Main.gm_u.getSearchCustom(cvo);
			int my_point = Integer.parseInt(sc.get(0).getCustom_point());
			
			//포인트 적립
			CustomVO vo = new CustomVO();
			vo.setCustom_id(Login_controller.log_c.getCustom_id());
			vo.setCustom_point((int_point + my_point) +"");
			
			
			Main.gm_u.updatePoint(vo);
			System.out.println(int_point  + "원 포인트 적립  " + my_point + "원래 포인트");
			
			//id값을주고 장바구니 DB 삭제
			Main.gm_u.deleteAllCart(Login_controller.log_c.getCustom_id());
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	private void alertTest(String message) {
		Alert alertErorr = new Alert(AlertType.INFORMATION);
		alertErorr.setTitle("결제완료");
		alertErorr.setContentText(message);
		alertErorr.showAndWait();
	}
	
	private void alertError(String message) {
		Alert alertErorr = new Alert(AlertType.ERROR);
		alertErorr.setTitle("경고");
		alertErorr.setContentText(message);
		alertErorr.showAndWait();
	}

}
