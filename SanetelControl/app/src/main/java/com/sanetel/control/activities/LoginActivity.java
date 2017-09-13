package com.sanetel.control.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.sanetel.control.R;
import com.sanetel.control.bean.ConstData;
import com.sanetel.control.bean.LoginAccount;
import com.sanetel.control.server.Server;
import com.sanetel.control.bean.ServerInfo;
import com.sanetel.control.utils.DevicesStatus;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via account/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText serverIPView;
    // private EditText serverPortView;
    private Switch rememberAccount;
    private LoginAccount loginAccount;

    // 服务器设置的view
    private View menu_more;
    private ImageView img_more_up;
    private boolean isShowMenu = false;

    // 服务器类型
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] serverTypes;
    private String selectedType;


    private String[] getServerTypes(Context ctx){
        // 在使用外部存储之前，你必须要先检查外部存储的当前状态，以判断是否可用。
        try {
            if (!DevicesStatus.isExternalStorageReadable()){
                throw new IOException(getString(R.string.error_external_storage));
            }
            byte[] buffer = new byte[1024];
            if (false) {
                // 内部存储
                FileInputStream fis = ctx.openFileInput("serverType.csv");
                if (fis != null) {
                    // TODO 读取文件内容获取天线类型
                    fis.read(buffer);
                    fis.close();
                }
                String s = new String(buffer);
                serverTypes = s.split(",");
            }else{
                // 外存储上放公共文件你可以使用getExternalStoragePublicDirectory()
                //  File file = newFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
                // 外置存储
                // Environment.getExternalStorageDirectory();
                // 创建应用私有文件的方法是Context.getExternalFilesDir(),
                // File file3 = new File(getExternalCacheDir().getAbsolutePath(), "getExternalCacheDir.txt");
                File fileServerType = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "serverType.csv");
                try {
                    // InputStream is = new FileInputStream(fileServerType);
                    // is.read(buffer);
                    // is.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(fileServerType), "GBK"));
                    // 读取直到最后一行
                    String line = "";
                    int count = 0, j=0;
                    while ((line = br.readLine()) != null){
                        // 把一行数据分割成多个字段
                        count++;
                        if (count > 0) { // 从第三行读取
                             /*String[] s = line.split(",");
                            for (int i = 0; i < s.length; i++) {
                                System.out.print(s[i] + "\t");
                                if(j == serverTypes.length) break;
                                serverTypes[j++] = s[i];
                            }
                            System.out.println();*/
                            serverTypes = line.split(",");
                        }

                    }
                    return serverTypes;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return serverTypes;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginAccount = new LoginAccount(this);
        String account = loginAccount.GetAccount();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        if (account != "")
        {
            mEmailView.setText(account);
        }
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        rememberAccount = (Switch) findViewById(R.id.login_switcher_auto_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        img_more_up = (ImageView)findViewById(R.id.imageView01);
        menu_more = findViewById(R.id.menuView01);
        serverIPView = (EditText)findViewById(R.id.editTextServerIP);
        // 端口固定 serverPortView = (EditText)findViewById(R.id.editTextServerPort);

        // 先获取信息
        getServerTypes(this);
        spinner = (Spinner) findViewById(R.id.spinnerServerType);

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,serverTypes);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);

    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            selectedType = serverTypes[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid account, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        serverIPView.setError(null);
        // serverPortView.setError(null);

        // Store values at the time of the login attempt.
        String account = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid account address.
        if (TextUtils.isEmpty(account)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(account)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        //获取服务器信息
        Server newSever=new Server(LoginActivity.this);
        ServerInfo serverInfo=newSever.GetServerInfo();
        String ip = serverInfo.GetIP();
        //读取输入的ip及port
        if("".equals(ip) && "".equals(serverIPView.getText().toString())) // &&"".equals(serverPortView.getText().toString())
        {
            if (ip.isEmpty()){
                serverIPView.setError(getString(R.string.error_server_ip_required));
                focusView = serverIPView;
                cancel = true;
            }
//            else if (serverInfo.GetPort().isEmpty()){
//                serverPortView.setError(getString(R.string.error_server_port_required));
//                focusView = serverPortView;
//                cancel = true;
//            }

            // 如果cancel为true，则显示设置服务器的按钮
            if (cancel){
                menu_more.setVisibility(View.VISIBLE);
                img_more_up.setImageResource(R.drawable.login_more);
                isShowMenu = true;
            }

        }  else  {
            if ("".equals(ip)) {
                ip = serverIPView.getText().toString();
            }
            if(rememberAccount.isChecked()){
                loginAccount.SaveAccount(account);
            }
            newSever.SetServerInfo(ip, ConstData.SERVER_PORT, spinner.getSelectedItem().toString());
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(account, password);
            mAuthTask.execute((Void) null);
            Intent intent = new Intent(this, SatelliteParametersActivity.class);
            startActivity(intent);
        }
    }
    private static final String[] accounts = {"admin","xwwt"};
    private boolean isEmailValid(String account) {
        for (String acc:accounts){
            if (account.equals(acc)) return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        return password.length() == 0 || (password.length() >= 4 && password.length() <= 10);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /*****
     *  自动补全逻辑
     *  LoaderManager
      */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only account addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary account addresses first. Note that there won't be
                // a primary account address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     *  ================================================================= 自动不全结束 =================================================================
     */


    /**
     *  弹出更多的设置信息
     */

    public void showMoreMenu(View view){
        if(isShowMenu)
        {
            menu_more.setVisibility(View.GONE);
            img_more_up.setImageResource(R.drawable.login_more_up);
            isShowMenu = false;
        }
        else{
            menu_more.setVisibility(View.VISIBLE);
            img_more_up.setImageResource(R.drawable.login_more);
            isShowMenu = true;
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String account, String password) {
            mEmail = account;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

