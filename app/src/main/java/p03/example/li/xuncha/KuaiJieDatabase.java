package p03.example.li.xuncha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KuaiJieDatabase extends SQLiteOpenHelper {

    public static final String CREATE_USERDATABASE = "create table KuaiJieDatabase ("
            + "id integer primary key autoincrement, "
            + "t1 text, "
            + "t2 text, "
            + "t3 text, "
            + "t4 text, "
            + "flag text,"
            + "status text, "
            + "provider text,"
            + "userId text) ";

    private Context mContext;

    public KuaiJieDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
