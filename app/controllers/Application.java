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


	//一覧表示
	public static Result index(){
		//dataSetに一覧を格納
		List<Message> dataSet = Message.find.all();
		return ok(index.render("ホーム画面", dataSet));
	}

	//CREATE関連
	public static Result add(){
		//add.scala.htmlをレンダリングして、引数の文字列を渡し、新しいフォームを作る
		return ok(add.render("新規メッセージ作成画面","投稿フォーム",new Form(Message.class)));
	}
	public static Result create() {
		//フォーム受け取り
		//bindFromRequest()はPOSTのパラメータを受け取るようのメソッド。GETではエラーになる
		System.out.println("create");
		Form<Message> myForm = form(Message.class).bindFromRequest();
		System.out.println("formをget");
		//エラーでないか判定
		if(!myForm.hasErrors()){
			Message data = myForm.get();
			System.out.println("formをモデルに格納");
			//データ登録
			Message message = new Message();
			message.name = data.name;
			message.mail = data.mail;
			message.message = data.message;
			message.postdate = new Date();
			//保存
			message.save();
			System.out.println("データを");
			return redirect("/");
		}else{
			return ok(add.render("新規メッセージ作成画面","ERROR",myForm));
		}
	}

	//UPDATE関連
	public static Result setItem() {
		return ok(item.render("投稿メッセージID検索画面","ID番号を入力。",new Form(Message.class)));
	}

	public static Result edit(){
		//フォーム受け取り
		Form<Message> myForm = form(Message.class).bindFromRequest();
		//エラーでないか判定
		if(!myForm.hasErrors()){
			Message data = myForm.get();
			Long id = data.id;
			Message message = Message.find.byId(id);
			try{
				myForm = myForm.fill(message);
			}catch(Exception e){
				return ok(item.render("投稿メッセージID検索画面","ERROR:IDの投稿が見つかりません。",myForm));
			}
			return ok(edit.render("投稿メッセージ編集画面","ID="+id+"の投稿を編集。", id, myForm));
		}else{
			return ok(item.render("投稿メッセージID検索画面","ERROR:入力に問題があります。",myForm));
		}
	}

	public  static Result update(){
		//フォーム受け取り
		Form<Message> myForm = form(Message.class).bindFromRequest();
		//エラーでないか判定
		if(!myForm.hasErrors()){
			Message message = myForm.get();
			message.postdate = new Date();
			message.update();
			flash();
			return redirect("/");
		}else{
			//idの数値のみ受け取り
			String[] params = {"id"};
			DynamicForm input = Form.form();
			input = input.bindFromRequest(params);
			String idString = input.data().get("id");
			Long id = Long.parseLong(idString);
			return ok(edit.render("投稿メッセージ編集画面","ERROR:再度入力してください",id, myForm));

		}
	}

	//DELETE関連
	//正常処理
	public static Result delete() {
		return ok(delete.render("投稿投稿メッセージID削除画面 (指定したIDを削除する)","削除するID番号",new Form(Message.class)));
	}

	//異常処理
	public static Result remove(){
		//フォーム受け取り
		Form<Message> myForm = form(Message.class).bindFromRequest();
		//エラーでないか判定
		if(!myForm.hasErrors()){

			Message data = myForm.get();
			Long id = data.id;
			try{
				//データ削除
				Message.find.ref(id).delete();
				flash();
			}catch(Exception e){
				return ok(delete.render("投稿メッセージID削除画面（指定したIDを削除する）","ERROR:そのIDは見つかりません。",myForm));
			}
			return redirect("/");
		}else{
			return ok(delete.render("投稿メッセージID削除画面（指定したIDを削除する）","ERROR:入力にエラーが発生しました。",myForm));
		}
	}





	//投稿者検索画面
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






   


  





