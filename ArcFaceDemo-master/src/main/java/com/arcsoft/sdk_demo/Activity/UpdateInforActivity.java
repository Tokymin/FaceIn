package com.arcsoft.sdk_demo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.sdk_demo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UpdateInforActivity extends AppCompatActivity {
    private ImageView mhead;//头像
    private TextView username;//昵称
    private EditText qianm, duihauk;//签名
    private TextView sex;//性别
    private LinearLayout linearLayout;
    private String mFilePath;
    private String qianming;
    private String name;
    private static int REQUEST_CAMERA = 1;
    private Bitmap photo;
    private Button back;
    private Button calcell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_infor);
        mhead = (ImageView) findViewById(R.id.toux);
        username = (TextView) findViewById(R.id.nichen);
        qianm = (EditText) findViewById(R.id.qianm);
        calcell=(Button)findViewById(R.id.calcell);
        sex = (TextView) findViewById(R.id.sex2);
        back=(Button) findViewById(R.id.back);
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "temp.png";// 指定路径

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateInforActivity.this).setTitle("请输入").setIcon(android.R.drawable.ic_dialog_info).setView(duihauk = new EditText(UpdateInforActivity.this)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        username.setText(duihauk.getText().toString());
                        name = username.getText().toString();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        calcell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qianming = qianm.getText().toString();
                Intent i=new Intent(UpdateInforActivity.this, SlindingActivity.class);
                i.putExtra("name",name);
                i.putExtra("qm",qianming);
                setResult(123,i);
                finish();
            }
        });
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateInforActivity.this).setTitle("请选择").setIcon(android.R.drawable.ic_dialog_info).setSingleChoiceItems(new String[]{"男", "女"}, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();
                                sex.setText("男");
                            case 1:
                                dialog.dismiss();
                                sex.setText("女");
                        }
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
        mhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateInforActivity.this).setTitle("请选择").setIcon(android.R.drawable.ic_dialog_info).setSingleChoiceItems(new String[]{"拍照", "相册"}, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();
                                openCamera();
                                break;
                            case 1:
                                dialog.dismiss();
                                Toast.makeText(UpdateInforActivity.this, "哈哈没想到吧我又回来啦", Toast.LENGTH_SHORT).show();
                                openAblam();
                                break;
                        }
                    }
                }).setNegativeButton("取消", null).show();
            }
        });

    }

    private void openCamera() {
        String state = Environment.getExternalStorageState();// 获取内存卡可用状态
        if (state.equals(Environment.MEDIA_MOUNTED)) {// 内存卡状态可用
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, 1);
        }
    }

    private void openAblam() {//打开相册，英语不好嘿嘿
        Intent albumIntent = new Intent(Intent.ACTION_PICK);//打开系统的相册
        albumIntent.setType("image/*");
        startActivityForResult(albumIntent, 0x1004);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 0x1004:
                    getClipPhotoByPickPicture(data.getData(), data.getExtras());
                    break;
                case 0x1006:
                    //接收裁剪好的图片信息并保存到本地文件夹
                    photo = data.getParcelableExtra("data");
                    mhead.setImageBitmap(photo);
                    // 图像保存到文件中
                    FileOutputStream foutput = null;
                    try {
                        File fileDir = new File(Environment.getExternalStorageDirectory() + "/Elephant/accountImg/");
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        File fileImg = new File(fileDir, "accountImg.jpg");
                        if (fileImg.exists()) {
                            fileImg.delete();
                        }
                        foutput = new FileOutputStream(fileImg);
                        photo.compress(Bitmap.CompressFormat.JPEG, 50, foutput); // 压缩图片

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if (null != foutput) {
                            try {
                                foutput.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case 1:
                    if (data.getData() != null || data.getExtras() != null) { // 防止没有返回结果
                        Uri uri = data.getData();
                        if (uri != null) {
                            this.photo = BitmapFactory.decodeFile(uri.getPath()); // 拿到图片
                        }
                        if (photo == null) {
                            Bundle bundle = data.getExtras();
                            if (bundle != null) {
                                photo = (Bitmap) bundle.get("data");
                                FileOutputStream fileOutputStream = null;
                                try {// 获取 SD 卡根目录 生成图片并
                                    String saveDir = Environment.getExternalStorageDirectory() + "/dhj_Photos";// 新建目录
                                    File dir = new File(saveDir);
                                    if (!dir.exists()) dir.mkdir();// 生成文件名
                                    SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
                                    String filename = "MT" + (t.format(new Date())) + ".jpg";// 新建文件
                                    File file = new File(saveDir, filename);// 打开文件输出流
                                    fileOutputStream = new FileOutputStream(file);// 生成图片文件
                                    this.photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);// 相片的完整路径
                                    this.mFilePath = file.getPath();
                                    mhead.setImageBitmap(this.photo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "找不到图片", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                default:
                    break;
            }
        }
    }

    private void getClipPhotoByPickPicture(Uri uri, Bundle bundle) {

        if (uri == null) {
        } else {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);// aspectX是宽高的比例，这里设置的是正方形（长宽比为1:1）
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 400); // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputY", 400); //我设置不能设太大，<640
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, 0x1006);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
