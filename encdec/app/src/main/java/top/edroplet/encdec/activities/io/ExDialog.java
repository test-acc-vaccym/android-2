package top.edroplet.encdec.activities.io;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.edroplet.encdec.R;

public class ExDialog extends ListActivity  implements OnClickListener {


	final Map<String, String> MimeTypeMap = new HashMap<String, String>();
	final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{".3gp", "video/3gpp"},
			{".apk", "application/vnd.android.package-archive"},
			{".asf", "video/x-ms-asf"},
			{".avi", "video/x-msvideo"},
			{".bin", "application/octet-stream"},
			{".bmp", "image/bmp"},
			{".c", "text/plain"},
			{".class", "application/octet-stream"},
			{".conf", "text/plain"},
			{".cpp", "text/plain"},
			{".doc", "application/msword"},
			{".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
			{".xls", "application/vnd.ms-excel"},
			{".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
			{".exe", "application/octet-stream"},
			{".gif", "image/gif"},
			{".gtar", "application/x-gtar"},
			{".gz", "application/x-gzip"},
			{".h", "text/plain"},
			{".htm", "text/html"},
			{".html", "text/html"},
			{".jar", "application/java-archive"},
			{".java", "text/plain"},
			{".jpeg", "image/jpeg"},
			{".jpg", "image/jpeg"},
			{".js", "application/x-javascript"},
			{".log", "text/plain"},
			{".m3u", "audio/x-mpegurl"},
			{".m4a", "audio/mp4a-latm"},
			{".m4b", "audio/mp4a-latm"},
			{".m4p", "audio/mp4a-latm"},
			{".m4u", "video/vnd.mpegurl"},
			{".m4v", "video/x-m4v"},
			{".mov", "video/quicktime"},
			{".mp2", "audio/x-mpeg"},
			{".mp3", "audio/x-mpeg"},
			{".mp4", "video/mp4"},
			{".mpc", "application/vnd.mpohun.certificate"},
			{".mpe", "video/mpeg"},
			{".mpeg", "video/mpeg"},
			{".mpg", "video/mpeg"},
			{".mpg4", "video/mp4"},
			{".mpga", "audio/mpeg"},
			{".msg", "application/vnd.ms-outlook"},
			{".ogg", "audio/ogg"},
			{".pdf", "application/pdf"},
			{".png", "image/png"},
			{".pps", "application/vnd.ms-powerpoint"},
			{".ppt", "application/vnd.ms-powerpoint"},
			{".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
			{".prop", "text/plain"},
			{".rc", "text/plain"},
			{".rmvb", "audio/x-pn-realaudio"},
			{".rtf", "application/rtf"},
			{".sh", "text/plain"},
			{".tar", "application/x-tar"},
			{".tgz", "application/x-compressed"},
			{".txt", "text/plain"},
			{".wav", "audio/x-wav"},
			{".wma", "audio/x-ms-wma"},
			{".wmv", "audio/x-ms-wmv"},
			{".wps", "application/vnd.ms-works"},
			{".xml", "text/plain"},
			{".z", "application/x-compress"},
			{".zip", "application/x-zip-compressed"},
			{"", "*/*"}
	};
	// private LayoutInflater mInflater;
	public HashMap<Map<String, Object>, Boolean> ischeck = new HashMap<Map<String, Object>, Boolean>();
	private List<Map<String, Object>> mData;
	private String mDir = "/storage";
	private ListView listview;
	private Context  context;
	private List<Map<String, Object>> selectid = new ArrayList<Map<String, Object>>();
	private boolean isMulChoice = false;	// 是否多选
	private boolean isMultiDir = false;		// 是否可以选择多个目录里的文件
	private boolean showAll = false;		// 是否显示所有文件
	private MyAdapter  adapter;
	private RelativeLayout layout;
	private Button cancle,ok;
	private TextView txtcount;
	private String fileType;

	private String getMsgString(Object ...msgs){
		StringBuffer sb = new StringBuffer();
		for (Object msg : msgs){
			sb.append("{");
			sb.append(String.valueOf(msg));
			sb.append("} ");
		}
		return sb.toString();
	}

	public void logd(Object ...msgs){
		Log.d("exdlg", getMsgString(msgs));
	}

	public void loge(Object ...msgs){
		Log.e("exdlg", getMsgString(msgs));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		Bundle bl = intent.getExtras();
		String title = bl.getString("explorer_title");
		isMultiDir = bl.getBoolean("allow_multidir");
		showAll = bl.getBoolean("show_all");
		// logd(title);
		initMap();
		Uri uri = intent.getData();
		// logd(uri.toString());
		fileType = intent.getType();
		// logd(fileType);
		mDir = uri.getPath();

		setTitle(title);
		mData = getData();

		setContentView(R.layout.floatview);
        context = this;
        listview = (ListView)findViewById(android.R.id.list);
        layout = (RelativeLayout)findViewById(R.id.relative);
        txtcount = (TextView)findViewById(R.id.txtcount);
        cancle   = (Button)findViewById(R.id.cancle);
        ok   = (Button)findViewById(R.id.ok);

		cancle.setOnClickListener(this);
        ok.setOnClickListener(this);

		MyAdapter adapter = new MyAdapter(this, txtcount);
		setListAdapter(adapter);

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes();
		p.height = (int) ( d.getHeight() * 0.99 );
		p.width = (int) ( d.getWidth() * 0.95 );
		getWindow().setAttributes(p);

	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		File f = new File(mDir);
		File[] files = f.listFiles();
		boolean isRoot = f.getParent() == null;
		//ContentResolver resolver = getContentResolver();
		//ContentResolver对象的getType方法可返回形如content://的Uri的类型
		//如果是一张图片，返回结果为image/jpeg或image/png等

		if ( !mDir.equals("/storage") && !isRoot) {
			map = new HashMap<String, Object>();
			map.put("title", "Back to ../");
			map.put("info", f.getParent());
			map.put("img", R.drawable.ex_folder);
			map.put("type", "folder");
			list.add(map);
		}
		if ( files != null ) {
			for ( int i = 0; i < files.length; i++ ) {
				map = new HashMap<String, Object>();
				File fi = files[i];
				// 文件不可读就不显示
				if (!fi.canRead()){
					continue;
				}
				String fileType = getMIMEType(fi.getName());
				map.put("title", fi.getName());

				map.put("info", fi.getPath());
				if ( fi.isDirectory() ) {
					map.put("img", R.drawable.ex_folder);
					map.put("type", "folder");
				} else {
					// logd(String.valueOf(new File(fi.toString())));
					// logd(fi.getName());
					// String fileType = resolver.getType(Uri.fromFile(new File(fi.toString())));
					if (!fi.canWrite()){
						continue;
					}
					map.put("img", R.drawable.ex_doc);
					if ( fileType != null && ( fileType.startsWith("text") || fileType.equalsIgnoreCase("application/text") ) ){ //判断用户选择的是否为text
						map.put("type", "text");
					} else {
						if(!showAll){
							continue;
						}
						map.put("type", "unknow");
					}

					map.put("size", fi.length());
					map.put("date",fi.lastModified());
				}
				list.add(map);
			}
		}
		return list;
	}

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch ( v.getId() ) {
        case R.id.cancle:
            isMulChoice = false;
            selectid.clear();
            adapter = new MyAdapter(context, txtcount);
            listview.setAdapter(adapter);
            layout.setVisibility(View.INVISIBLE);
            break;
        case R.id.ok:
            isMulChoice = false;
			/*
			 for ( int i=0;i < selectid.size();i++ ) {
			 for ( int j=0;j < mData.size();j++ ) {
			 if ( selectid.get(i).equals(mData.get(j)) ) {
			 mData.remove(j);
			 }
			 }
			 }
			 selectid.clear();
			 adapter = new MyAdapter(context, txtcount);
			 listview.setAdapter(adapter);
			 layout.setVisibility(View.INVISIBLE);
			 */
			String path="";
			for ( int i=0;i < selectid.size();i++ ) {
                path += selectid.get(i).get("info") + ";";
				// Log.e("adapter",String.valueOf(i)+"\t"+path);
            }
			layout.setVisibility(View.INVISIBLE);
			finishWithResult(path);
            break;
        default:
            break;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作");
	}

	void finishWithResult(String path) {

		Log.d("adapter", path);
		ContentResolver resolver = getContentResolver();
		Bundle conData = new Bundle();
		conData.putString("results", "Thanks Vivi");
		Intent intent = new Intent();
		intent.putExtras(conData);
		Uri startDir = Uri.fromFile(new File(path));
		String fileType = resolver.getType(startDir);
		if (fileType != null)
			Log.d("adapter", fileType);
		// Log.d("adapter", startDir.toString());
		intent.setDataAndType(startDir,
				"vnd.android.cursor.dir/lysesoft.andexplorer.file");
		setResult(RESULT_OK, intent);
		finish();
	}

	private void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = getMIMEType(file.getName());
		intent.setDataAndType(Uri.fromFile(file), type);
		try {
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, "未知类型，不能打开", Toast.LENGTH_SHORT).show();
		}
	}

	private String getMIMEType(String fileName) {
		String type = "*/*";
		int dotIndex = fileName.lastIndexOf('.'); //fileName.indexOf('.');
		// logd(String.valueOf("index: "+dotIndex));
		if (dotIndex < 0) {
			return type;
		}
		String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
		// logd("end: "+end);
		if (end.length() == 0) {
			return type;
		}
		// logd(String.valueOf(MIME_MapTable.length));
		/*
		for(int i=0; i<MIME_MapTable.length; i++) {
			logd(end+", " + MIME_MapTable[i][0]);
			if(end.equalsIgnoreCase(MIME_MapTable[i][0])){
				type = MIME_MapTable[i][1] ;
				return type;
			}
		}*/
		String mResult = MimeTypeMap.get(end);
		if (mResult != null && mResult.length() > 0)
			type = mResult;
		// logd(type);
		return type;
	}

	private Map initMap() {
		for (int i = 0; i < MIME_MapTable.length; i++) {
			MimeTypeMap.put(MIME_MapTable[i][0], MIME_MapTable[i][1]);
		}
		return MimeTypeMap;
	}

	/*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("MyListView4-click", (String) mData.get(position).get("info"));
		if ( (Integer) mData.get(position).get("img") == R.drawable.ex_folder ) {
			mDir = (String) mData.get(position).get("info");
			mData = getData();
			MyAdapter adapter = new MyAdapter(this, txtcount);
			setListAdapter(adapter);
		} else {
			finishWithResult((String) mData.get(position).get("info"));
		}
	}
	*/
	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView info;
		public TextView type;
		public TextView date;
		public TextView size;
		public CheckBox checked;
	}

	public class MyAdapter extends BaseAdapter {
		public HashMap<Integer, Integer> visiblecheck;//用来记录是否显示checkBox
		private Context context;
        private LayoutInflater inflater=null;
        private HashMap<Integer, View> mView ;
        private TextView txtcount;

		public MyAdapter(Context context, TextView txtcount) {
			// this.mInflater = LayoutInflater.from(context);
			this.context = context;
            this.txtcount = txtcount;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = new HashMap<Integer, View>();
            visiblecheck = new HashMap<Integer, Integer>();
			if(!isMultiDir){
				ischeck.clear();
				selectid.clear();
				txtcount.setText("共选择了" + selectid.size() + "项");
			}
            if ( isMulChoice ) {
                for ( int i=0;i < mData.size();i++ ) {
					Map<String, Object> mi = mData.get(i);
					// loge(String.valueOf(mi));
					if ( (Integer) mi.get("img") == R.drawable.ex_folder ) {
                    	visiblecheck.put(i, CheckBox.INVISIBLE);
					} else {
						// logd((String)mi.get("type"));
						if ( (String) mi.get("type") == "text" ){
							if(ischeck.get(mi) == null){
								// loge("not: "+ String.valueOf(mi));
								ischeck.put(mi, false);
							}
							// loge("mi: " + String.valueOf(mi));
							visiblecheck.put(i, CheckBox.VISIBLE);
						}else{
							visiblecheck.put(i, CheckBox.INVISIBLE);
						}
					}
                }
            } else {
                for ( int i=0;i < mData.size();i++ ) {
                    ischeck.put(mData.get(i), false);
                    visiblecheck.put(i, CheckBox.INVISIBLE);
                }
            }
		}

		public int getCount() {
			return mData.size();
		}

		public Object getItem(int arg0) {
			return mData.get(arg0);
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if ( convertView == null ) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.listview, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
                holder.type = (TextView) convertView.findViewById(R.id.file_type);
                holder.date = (TextView) convertView.findViewById(R.id.file_date);
                holder.size = (TextView) convertView.findViewById(R.id.file_size);
				holder.checked = (CheckBox) convertView.findViewById(R.id.listviewCheckBox);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final Map<String, Object> mdp = mData.get(position);
			holder.img.setBackgroundResource((Integer) mdp.get("img"));
			holder.title.setText((String) mdp.get("title"));
			holder.info.setText((String) mdp.get("info"));
			String fileName = (String) mdp.get("title");
			final Integer iv = (Integer) mdp.get("img");
			if(iv == R.drawable.ex_folder) {
				holder.size.setText("文件夹");
				holder.size.setTextColor(Color.RED);
				holder.type.setVisibility(View.GONE);
				holder.date.setVisibility(View.GONE);
			} else {
				long fileSize = (long)mdp.get("size");
				if(fileSize > 1024*1024) {
					float size = fileSize /(1024f*1024f);
					holder.size.setText(new DecimalFormat("#.00").format(size) + "MB");
				} else if(fileSize >= 1024) {
					float size = fileSize/1024;
					holder.size.setText(new DecimalFormat("#.00").format(size) + "KB");
				} else {
					holder.size.setText(fileSize + "B");
				}
				int dot = fileName.lastIndexOf('.');
				if(dot > -1 && dot < (fileName.length() -1)) {
					holder.type.setText(fileName.substring(dot + 1) + "文件");
				}
				holder.date.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(mdp.get("date")));
			}
			if(visiblecheck.get(position) == View.VISIBLE){
				holder.checked.setVisibility(View.VISIBLE);
				holder.size.setVisibility(View.VISIBLE);
				holder.type.setVisibility(View.VISIBLE);
				holder.date.setVisibility(View.VISIBLE);
			}else {
				// 不是文件夹就显示为不可点击
				// logd("["+Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]",mdp);
				if (!((String)mdp.get("type")).equalsIgnoreCase("folder")){
					logd("["+Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]","getView",position,convertView,parent);
					convertView.findViewById(R.id.file).setEnabled(true);
				}
			}
			boolean check = false;
			if (ischeck.get(mdp) != null){
				check = ischeck.get(mdp);
			}
			holder.checked.setChecked(check);
			convertView.setOnLongClickListener(new Onlongclick());
			final CheckBox ceb = holder.checked;

			convertView.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						if ( iv != R.drawable.ex_folder ) {
							if ( mdp.get("type") == "text" ) {
								if ( isMulChoice ) {
									// logd("mdp: ", String.valueOf(mdp));
									if ( ceb.isChecked() ) {
										ceb.setChecked(false);
										ischeck.put(mdp,false);
										selectid.remove(mdp);
									} else {
										ceb.setChecked(true);
										ischeck.put(mdp,true);
										// logd((String) mData.get(position).get("info"));
										selectid.add(mdp);
									}
									txtcount.setText("共选择了" + selectid.size() + "项");
								} else {
									Toast.makeText(context, "选择了" + mdp.get("info"), Toast.LENGTH_SHORT).show();
									// logd((String) mdp.get("info"));
									finishWithResult((String) mdp.get("info"));
								}
							}
						} else {
							mDir = (String) mdp.get("info");
							mData = getData();
							MyAdapter adapter = new MyAdapter(context, txtcount);
							setListAdapter(adapter);
						}
                    }
				});
			return convertView;
		}

		class Onlongclick implements OnLongClickListener {

            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                isMulChoice = true;
                selectid.clear();
				ischeck.clear();
				if ( adapter == null ) {
					adapter = new MyAdapter(context, txtcount);
					listview.setAdapter(adapter);
				}
                layout.setVisibility(View.VISIBLE);
                for ( int i=0;i < mData.size();i++ ) {
					// Log.e("adapter",String.valueOf(adapter));
                    adapter.visiblecheck.put(i, CheckBox.VISIBLE);
                }
                adapter = new MyAdapter(context, txtcount);
                listview.setAdapter(adapter);
                return true;
            }
        }

	}
}

