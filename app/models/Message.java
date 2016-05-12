package models;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.*;
import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;
import com.avaje.ebean.annotation.*;



@Entity
public class Message extends Model {
  @Id
  public Long id;

  @Required(message = "必須項目です。")
  public String name;

  @Email(message = "メール形式で入力してください。")
  public String mail;

  @Pattern(value = "^[a-zA-Z0-9]+$", message = "半角英数字のみで入力してください。")
  public String message;

  public Date postdate;

  public static Finder<Long, Message> find = new Finder<Long, Message>(Long.class, Message.class);

  @Override
  public String toString(){
    return ("[id:" + id + ", name:" + name + ",mail:" + mail + ",message:" + message + ",date:" + postdate + "]");
  }


}




