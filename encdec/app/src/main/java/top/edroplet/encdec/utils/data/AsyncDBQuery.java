package top.edroplet.encdec.utils.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

/**
 * Created by xw on 2017/5/2.
 */

public class AsyncDBQuery extends AsyncQueryHandler {
    public AsyncDBQuery(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        // 更新mAdapter的Cursor
        // mAdapter.changeCursor(cursor);
    }
}
