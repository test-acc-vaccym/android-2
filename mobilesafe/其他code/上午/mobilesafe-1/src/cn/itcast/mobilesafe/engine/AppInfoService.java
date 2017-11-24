package cn.itcast.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import cn.itcast.mobilesafe.domain.AppInfo;

public class AppInfoService {

	private Context context;
	private PackageManager pm;
	public AppInfoService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		pm = context.getPackageManager();
	}
	
	//�õ����е�Ӧ�ó�����Ϣ
	public List<AppInfo> getAppInfos(){
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(ApplicationInfo info:applicationInfos){
			AppInfo appInfo = new AppInfo();
			
			Drawable app_icon = info.loadIcon(pm);
			appInfo.setApp_icon(app_icon);
			
			String app_name = info.loadLabel(pm).toString();
			appInfo.setApp_name(app_name);
			
			String packageName = info.packageName;
			appInfo.setPackagename(packageName);
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				String app_version = packageInfo.versionName;
				appInfo.setApp_version(app_version);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean isUserApp = filterApp(info);
			appInfo.setUserApp(isUserApp);
			appInfos.add(appInfo);
		}
		return appInfos;
	}
	
	//�ж�Ӧ�ó����Ƿ����û�����
    public boolean filterApp(ApplicationInfo info) {
    	//ԭ����ϵͳӦ�ã��û��ֶ�����
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //�û��Լ���װ��Ӧ�ó���
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}
