package controllers;

import play.*;
import play.mvc.*;
import views.html.*;
import java.util.*;
import models.*;
import play.data.Form;
import static play.data.Form.*;
import java.util.Date;
import play.data.DynamicForm;



public class Application extends Controller {


	final static Form<Message> messageForm = Form.form(Message.class);


	//一覧表示
	public static Result index(){
		//dataSetに一覧を格納
		List<Message> dataSet = Message.find.all();
		return ok(index.render("ホーム画面", dataSet));
	}


	//新規フォーム
	public static Result add(){
		//add.scala.htmlをレンダリングして、引数の文字列を渡し、新しいフォームを作る
		return ok(add.render("新規メッセージ作成画面","投稿フォーム",new Form(Message.class)));
	}


	//投稿データ作成
	public static Result create() {
		//フォーム受け取り
		//bindFromRequest()はPOSTのパラメータを受け取るようのメソッド。GETではエラーになる
		Form<Message> myForm = messageForm.bindFromRequest();
		//エラーでないか判定
		if(myForm.hasErrors()){
			return ok(add.render("新規メッセージ作成画面","ERROR",myForm));
		}else{
			Message data = myForm.get();
			//データ登録
			Message message = new Message();
			message.name = data.name;
			message.mail = data.mail;
			message.message = data.message;
			message.postdate = new Date();
			//保存
			message.save();
			return redirect("/");
		}
	}


	//ID検索
	public static Result setItem() {
		return ok(item.render("投稿メッセージID検索画面","ID番号を入力。",new Form(Message.class)));
	}


	//編集
	public static Result edit(){
		//フォーム受け取り
		Form<Message> myForm = messageForm.bindFromRequest();
		//エラーでないか判定
		if(myForm.hasErrors()){
			return ok(item.render("投稿メッセージID検索画面","ERROR!!",myForm));
		}else{
			Message data = myForm.get();
			Message message = Message.find.byId(data.id);
			try{
				myForm = myForm.fill(message);
			}catch(Exception e){
				return ok(item.render("投稿メッセージID検索画面","ERROR",myForm));
			}finally{
				return ok(edit.render("投稿メッセージ編集画面","ID="+data.id+"の投稿を編集。", data.id, myForm));
			}
			
		}
	}



	//更新
	public  static Result update(){
		//フォーム受け取り
		Form<Message> myForm = messageForm.bindFromRequest();
		//エラーでないか判定
		if(myForm.hasErrors()){
			//idの数値のみ受け取り
			String param = "id";
			DynamicForm input = Form.form();
			input = input.bindFromRequest(param);
			String idString = input.data().get("id");
			Long id = Long.parseLong(idString);
			return ok(edit.render("投稿メッセージ編集画面","ERROR",id, myForm));
		}else{
			Message message = myForm.get();
			message.postdate = new Date();
			try{
				message.update();
			    flash();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				return redirect("/");
			}
		}
	}

	
	//正常処理
	public static Result delete() {
		return ok(delete.render("投稿投稿メッセージID削除画面 (指定したIDを削除する)","削除するID番号",new Form(Message.class)));
	}


	//異常処理
	public static Result remove(){
		//フォーム受け取り
		Form<Message> myForm = messageForm.bindFromRequest();
		//エラーでないか判定
		if(myForm.hasErrors()){
			return ok(delete.render("投稿メッセージID削除画面（指定したIDを削除する）","ERROR",myForm));
		}else{
			Message data = myForm.get();
			Long id = data.id;
			try{
				//データ削除
				Message.find.ref(id).delete();
				flash();
			}catch(Exception e){
				return ok(delete.render("投稿メッセージID削除画面（指定したIDを削除する）","ERROR",myForm));
			}finally{
				return redirect("/");
			}
			
		}
	}



	//投稿者検索
	public static Result find(){
		//フォームを受け取り
		String[] params = {"input"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String text = input.data().get("input");
		//検索
		List<Message> dataSet = Message.find.where().eq("name",text).findList();
		return ok(find.render("投稿者検索画面", new Form(String.class), dataSet));
	}


}



