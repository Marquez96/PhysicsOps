package marquez.physicsops.Classes;

/**
 * Created by Carlos-Torre on 02/11/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

public class DBSQLite extends SQLiteOpenHelper{

    String tableUser = "CREATE TABLE IF NOT EXISTS User (\n" +
            "  idUser integer primary key autoincrement,"+
            "  userName VARCHAR(45) NULL,\n" +
            "  mail VARCHAR(45) NULL,\n" +
            "  name VARCHAR(45) NULL,\n" +
            "  surname VARCHAR(45) NULL,\n" +
            "  lastUpdate VARCHAR(45) NULL,\n" +
            "  password VARCHAR(45) NULL)\n";

    String tableSettings = "CREATE TABLE IF NOT EXISTS Settings (\n" +
            "  idSettings integer primary key autoincrement,\n"+
            "  online integer NULL,\n" +
            "  color VARCHAR NULL,\n" +
            "  idUser INT NOT NULL,\n" +
            "  CONSTRAINT fk_User_Settings\n" +
            "    FOREIGN KEY (idUser)\n" +
            "    REFERENCES Settings (idSettings)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION)\n" ;

    String tableFriends = "CREATE TABLE IF NOT EXISTS Friends (\n" +
            "  idFriends integer primary key autoincrement,"+
            "  idUser1 INT NOT NULL,\n" +
            "  idUser2 INT NOT NULL,\n" +
            "  state VARCHAR(45) NULL,\n" +
            "  CONSTRAINT fk_Friends_User1\n" +
            "    FOREIGN KEY (idUser1)\n" +
            "    REFERENCES User (idUser)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT fk_Friends_User2\n" +
            "    FOREIGN KEY (idUser2)\n" +
            "    REFERENCES User (idUser)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION)\n";

    String tableMessages = "CREATE TABLE IF NOT EXISTS Messages (\n" +
            "  idMessages integer primary key autoincrement,"+
            "  text VARCHAR(45) NULL,\n" +
            "  date DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
            "  idFriends INT NOT NULL,\n" +
            "  CONSTRAINT fk_Messages_Friends1\n" +
            "    FOREIGN KEY (idFriends)\n" +
            "    REFERENCES Friends (idFriends)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION)\n";

    String tableEvents = "CREATE TABLE IF NOT EXISTS Events (\n" +
            "  idEvents integer primary key autoincrement,"+
            "  title VARCHAR(45) NULL,\n" +
            "  type VARCHAR(45) NULL,\n" +
            "  duration VARCHAR(45) NULL,\n" +
            "  importance VARCHAR(45) NULL,\n" +
            "  date DEFAULT CURRENT_TIMESTAMP," +
            "  idUser INT NOT NULL,\n" +
            "  CONSTRAINT fk_Events_User1\n" +
            "    FOREIGN KEY (idUser)\n" +
            "    REFERENCES User (idUser)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION)\n";

    String tablePoint = "CREATE TABLE IF NOT EXISTS Point (\n" +
            "  idPoint integer primary key autoincrement,"+
            "  latitude REAL NULL,\n" +
            "  longitude VARCHAR(45) NULL,\n" +
            "  date DEFAULT CURRENT_TIMESTAMP,\n" +
            "  userName VARCHAR(45) NULL)";

    String query = "select * from User inner join Settings On (idUser=idUser) inner join Events On (idUsuario = User_idUser) where idUsuario = 1";
    //------------------------------------------------------------------------------------
    public DBSQLite(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableUser);
        db.execSQL(tableSettings);
        db.execSQL(tableFriends);
        db.execSQL(tableMessages);
        db.execSQL(tableEvents);
        db.execSQL(tablePoint);

        db.execSQL("insert into User(userName,mail,name,surname,lastUpdate, password) values(\"admin\",\"admin\",\"admin\",\"admin\",\"30/10/1900 00:00:00\",\"" + PasswordHash.hash("admin") + "\")");
        db.execSQL("insert into Settings(online,color,idUser) values(\"0\",\"green\",1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
        db.execSQL("drop table if exists Settings");
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Friends");
        db.execSQL("drop table if exists Messages");
        db.execSQL("drop table if exists Events");
        db.execSQL("drop table if exists Point");

        db.execSQL(tableUser);
        db.execSQL(tableSettings);
        db.execSQL(tableFriends);
        db.execSQL(tableMessages);
        db.execSQL(tableEvents);
        db.execSQL(tablePoint);

        db.execSQL("insert into User(userName,mail,name,surname,lastUpdate, password) values(\"admin\",\"admin\",\"admin\",\"admin\",\"30/10/1900 00:00:00\",\"" + PasswordHash.hash("admin") + "\")");
        db.execSQL("insert into Settings(online,color,idUser) values(\"0\",\"green\",1)");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.432284,\"28/05/2018 00:00:00\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.433284,\"28/05/2018 00:00:30\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.434284,\"28/05/2018 00:01:00\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.435284,\"28/05/2018 00:01:30\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.433284,\"28/05/2018 00:02:00\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.432434,\"28/05/2018 00:02:30\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.435284,\"28/05/2018 00:03:00\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.452284,\"28/05/2018 00:04:00\",\"admin\")");
        db.execSQL("insert into Point(latitude,longitude,date, userName) values(-3.54624, 40.454284,\"28/05/2018 00:04:30\",\"admin\")");


    }

    public void insertSQl(){

        SQLiteDatabase db = this.getWritableDatabase();
        String insertUser1 = "insert into User(userName,mail,name,surname,password) values(\"userName\",\"mail\",\"name\",\"surname\",\"password\")";
        /*String insertUser2 = "insert into User(userName,mail,name,surname,password) values(\"userName1\",\"mail1\",\"name1\",\"surname1\",\"password1\")";
        String insertSettings1 = "insert into Settings(color,online,idUser) values(\"red\",1,1)";
        String insertSettings2 = "insert into Settings(color,online,idUser) values(\"blue\",0,2)";
        String insertFriends = "insert into Friends(User_idUser1,User_idUser2) values(1,2)";
        String insertMessages = "insert into Messages(text,Friends_idFriends) values(\"i am the best\",1)";
        String insertEvent = "insert into Events(text,User_idUser) values (\"He hecho mucho ejercicio\",1)";
*/
        db.execSQL(insertUser1);
  /*      db.execSQL(insertUser2);
        db.execSQL(insertSettings1);
        db.execSQL(insertSettings2);
        db.execSQL(insertFriends);
        db.execSQL(insertMessages);
        db.execSQL(insertEvent);*/
    }

}
