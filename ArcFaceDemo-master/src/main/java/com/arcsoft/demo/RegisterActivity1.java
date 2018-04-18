package com.arcsoft.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcsoft.demo.Activity.MainActivity;
import com.arcsoft.demo.Database.MyDatabaseHelper;
import com.arcsoft.demo.contact.ContactHttpClient;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity1 extends Activity {
    private static final int TIAOZHUAN = 5;
    private EditText et_name;
    private EditText pwd2;
    private Spinner spinner;
    private EditText classes;
    private Button zhuce;
    private Bitmap image;
    private String getUsername;
    private String getUserpassword;
    private String et_name1;
    private String pwd22;
    private String spinner2;
    private String classes1;

    private ImageView image1;
    private EditText register_username;
    private EditText register_userpassword;
    //数据库管理器Helper
    private MyDatabaseHelper dbHelper;
    //获取数据库
    private SQLiteDatabase db;
    private final String TAG = this.getClass().toString();
    private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE_OP = 2;
    private static final int REQUEST_CODE_OP = 3;
    public static final int UPDATE_BITEMAP = 4;
    private final static int REGISTER_SUCCESS_BACK = 0x0001;


    // 学长加的
    private String userName = null;
    private byte[] userFace = null;
    private String faceData = null;
    private final static int REGISTER_FAIL_BACK = 0x0000;
    private Activity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); /*set it to be no title*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,/*set it to be full screen*/
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);

        instance = this;// 学长改的

        et_name = (EditText) findViewById(R.id.name1);
        pwd2 = (EditText) findViewById(R.id.psw2);
        spinner = (Spinner) findViewById(R.id.spinner1);
        classes = (EditText) findViewById(R.id.classs);
        zhuce = (Button) findViewById(R.id.register_btn);
        image1 = (ImageView) findViewById(R.id.image1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(RegisterActivity1.this).setTitle("请选择").setIcon(android.R.drawable.ic_dialog_info).setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 1:
                                Toast.makeText(RegisterActivity1.this, "pai", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                ContentValues values = new ContentValues(1);
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                ((Application) (RegisterActivity1.this.getApplicationContext())).setCaptureImage(uri);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                break;
                            case 0:
                                Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                getImageByalbum.setType("image/jpeg");
                                Toast.makeText(RegisterActivity1.this, "打开图片", Toast.LENGTH_SHORT).show();
                                startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                break;
                            default:
                        }
                    }
                }).show();
            }
        });

        register_username = (EditText) findViewById(R.id.register_username);
        register_userpassword = (EditText) findViewById(R.id.register_userpassword);
        dbHelper = new MyDatabaseHelper(this, "user.db", null, 1);
        db = dbHelper.getReadableDatabase();

        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsername = register_username.getText().toString().trim();
                getUserpassword = register_userpassword.getText().toString().trim();
                et_name1 = et_name.getText().toString().trim();
                pwd22 = pwd2.getText().toString().trim();
                classes1 = classes.getText().toString().trim();
                spinner2 = spinner.getSelectedItem().toString().trim();
				
                saveDataToFile(RegisterActivity1.this,et_name1,"name");
                saveDataToFile(RegisterActivity1.this,getUsername,"numbers");


                if (!getUserpassword.equals(pwd22)) {
                    new AlertDialog.Builder(RegisterActivity1.this).setTitle("提示！").setMessage("两次密码不一致!").create().show();
                }
                if (getUsername.equals("") || getUserpassword.equals("")) {
                    new AlertDialog.Builder(RegisterActivity1.this).setTitle("提示！").setMessage("用户名或密码不能为空!").create().show();
                } else {
                    register();
                }
            }
        });
    }
    //联网操作
    public void httpp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter out = null;
                BufferedReader in = null;
                try {
                    URL realUrl = new URL("http://geek-team.xin/FaceIn/SRegister");
                    HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                    conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    conn.setRequestProperty("Accept-Charset", "UTF-8");
                    conn.setRequestProperty("contentType", "UTF-8");
                    // POST
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    out = new PrintWriter(conn.getOutputStream());
                    JSONObject object = new JSONObject();
                    object.put("name", et_name1);
                    object.put("id", getUsername);
                    object.put("password", getUserpassword);
                    object.put("sex", spinner2);
                    object.put("class", classes1);
                    if (image.getHeight() > 10) {
                        object.put("img", Bitmap2String(image));
                        Log.e(":::put image:::", "image=" + Bitmap2String(image));
                    } else {
                        Log.e(":::image:::", "image.Height=" + image.getHeight() + ":::image.Width=" + image.getWidth());
                    }
                    object.put("data", FaceDB.data);
                    Log.e(":::image:::data:::", FaceDB.data);
                    out.println(object.toString());
                    out.flush();
                    conn.connect();
                    // BufferedReader
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //读结果
                    String line = in.readLine();
                    JSONObject object1 = new JSONObject(line);
                    String result = object1.getString("result");
                    Log.e(":::返回result:::", "==" + result);
                    if (result.equals("true")) {
                        Message ms2 = Message.obtain();
                        ms2.what = TIAOZHUAN;
                        handler.sendMessage(ms2);
                    } else {
                        Toast.makeText(RegisterActivity1.this, result, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
    //图像转字节
    public byte[] Bitmap2Bytes(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // 图像转String
    public String Bitmap2String(Bitmap image) {
        return Base64.encodeToString(Bitmap2Bytes(image), Base64.DEFAULT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// 学长改
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Uri mPath = ((Application) (RegisterActivity1.this.getApplicationContext())).getCaptureImage();
            String file = getPath(mPath);
            Bitmap bmp = Application.decodeImage(file);
            startRegister(bmp, file);
        } else if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            String file = getPath(mPath);
            Bitmap bmp = Application.decodeImage(file);
            if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0) {
                Log.e(TAG, "error");
            } else {
                Log.i(TAG, "bmp [" + bmp.getWidth() + "," + bmp.getHeight() + "]");
            }
            startRegister(bmp, file);
        } else if (requestCode == REQUEST_CODE_OP) {
            Log.i(TAG, "RESULT =" + resultCode);
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            String path = bundle.getString("imagePath");
            Log.i(TAG, "path=" + path);
        } else {
            Log.e(TAG, "失败了::requestCode-->" + requestCode + "  resultCode-->" + resultCode);
        }

        if (resultCode == REGISTER_SUCCESS_BACK) {
            userName = data.getStringExtra("userName");
            userFace = data.getByteArrayExtra("userImage");
            faceData = data.getStringExtra("faceData");
            Log.e("Success backIntent-->", "" + userName + "====" + userFace + "=====" + faceData);

            et_name.setText(userName);
            image1.setImageBitmap(BitmapFactory.decodeByteArray(userFace, 0, userFace.length));
            image = ((BitmapDrawable) image1.getDrawable()).getBitmap();
        } else if (resultCode == REGISTER_FAIL_BACK) {
//            String failInfo = data.getStringExtra("failInfo");
            Log.e("Fail backIntent-->", "失败");
            new android.app.AlertDialog.Builder(instance).setTitle("Sorry").setMessage("您未选择照片").setPositiveButton("确定", null).show();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_BITEMAP:
                    image1 = (ImageView) findViewById(R.id.image1);
                    image1.setImageBitmap((Bitmap) msg.obj);
                    image = ((BitmapDrawable) image1.getDrawable()).getBitmap();
                    break;
                case TIAOZHUAN:
                    Log.e("REG","setpppp");
                    Toast.makeText(RegisterActivity1.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                    Log.e("REG","setpppp1");
                    DialogMaker.dismissProgressDialog();
                    Intent intent = new Intent(RegisterActivity1.this, MainActivity.class);
//                    getUsername = register_username.getText().toString().trim();
//                    getUserpassword = register_userpassword.getText().toString().trim();
                    intent.putExtra("username",getUsername);
                    intent.putExtra("userpassword",getUserpassword);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(this, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }


    private boolean isExist() {
        boolean flag = true;
        //遍历数据，判断注册是否重复
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("user"));
                String userpassword = cursor.getString(cursor.getColumnIndex("password"));
                if (username.equals(getUsername)) {
                    flag = false;
                }

            } while (cursor.moveToNext());
        }
        return flag;

    }

    //返回按钮
    public void register_back(View v) {
        this.finish();
    }

    /*Main*/

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * @param mBitmap
     */
    private void startRegister(Bitmap mBitmap, String file) {
        Intent it = new Intent(RegisterActivity1.this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", file);
        it.putExtras(bundle);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

    /**
     * ***************************************** 注册 **************************************
     */

    private void register() {
        if (!checkRegisterContentValid()) {
            return;
        }
        if (!NetworkUtil.isNetAvailable(RegisterActivity1.this)) {
            Toast.makeText(RegisterActivity1.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("register", "step2");
        DialogMaker.showProgressDialog(this, getString(R.string.registering), false);
        // 注册流程

        ContactHttpClient.getInstance().register(getUsername, et_name1, getUserpassword, new ContactHttpClient.ContactHttpCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Log.e("register", "step3");
                httpp();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(RegisterActivity1.this, getString(R.string.register_failed, String.valueOf(code), errorMsg), Toast.LENGTH_SHORT).show();
                Log.e("register", getString(R.string.register_failed, String.valueOf(code), errorMsg));
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    private boolean checkRegisterContentValid() {
        // 帐号检查
        if (getUsername.length() < 6 || getUsername.length() > 20) {
            Toast.makeText(this, "账号不得少于6位数", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 密码检查
        if (getUserpassword.length() < 6 || getUserpassword.length() > 20) {
            Toast.makeText(this, "密码不得少于6位数", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }
    private void saveDataToFile(Context context,String data,String fileName)
    {
        FileOutputStream fileOutputStream=null;
        BufferedWriter bufferedWriter=null;

        try {
            fileOutputStream=context.openFileOutput(fileName,Context.MODE_PRIVATE);
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(bufferedWriter!=null){
                    bufferedWriter.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
	


}
