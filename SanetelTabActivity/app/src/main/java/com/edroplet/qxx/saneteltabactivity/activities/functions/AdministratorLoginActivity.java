package com.edroplet.qxx.saneteltabactivity.activities.functions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.ImageUtil;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via account/password.
 */
public class AdministratorLoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private View mProgressView;
    ImageView thirdImageVIew;
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
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_login);

        mPasswordView = (EditText) findViewById(R.id.main_settings_administrator_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.pop_dialog_iv_third || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        thirdImageVIew = (ImageView) findViewById(R.id.pop_dialog_iv_third);
        if (thirdImageVIew != null){
            thirdImageVIew.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }
        mProgressView = findViewById(R.id.administrator_login_progress);

        /*
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
        */
        CustomTextView firstLine = (CustomTextView)findViewById(R.id.pop_dialog_tv_first);
        firstLine.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));

        CustomTextView secondLine = (CustomTextView) findViewById(R.id.pop_dialog_tv_second);

        secondLine.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));

        CustomTextView thirdStart = (CustomTextView) findViewById(R.id.pop_dialog_tv_third_start);
        String start = getString(R.string.main_settings_administrator_password_third_start);
        if (start != null && start.length() > 0) {
            thirdStart.setText(start);
            // thirdStart.setGravity(Gravity.CENTER|Gravity.END);
        } else {
            thirdStart.setVisibility(View.GONE);
        }

        thirdImageVIew.setImageBitmap(ImageUtil.textAsBitmap(this, getString(R.string.main_settings_administrator_password_third_button), ImageUtil.sp2px(this,32)));
        thirdImageVIew.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        thirdImageVIew.setMaxWidth(ImageUtil.dip2px(this, 90));
        findViewById(R.id.pop_dialog_tv_third_end).setVisibility(View.GONE);
    }

    //使用数组形式操作
    /*
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            selectedType = serverTypes[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    */
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
        mPasswordView.setError(null);
        // serverPortView.setError(null);

        // Store values at the time of the login attempt.
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //获取服务器信息
        //读取输入的ip及port
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mAuthTask = new UserLoginTask(password);
        mAuthTask.execute((Void) null);
        // Intent intent = new Intent(this, SatelliteParametersActivity.class);
        // startActivity(intent);

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
        // addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
    /*
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(AdministratorLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }
    */


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
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String password) {
            mEmail = "admin";
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
                mPasswordView.setError(getString(R.string.error_invalid_password));
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

