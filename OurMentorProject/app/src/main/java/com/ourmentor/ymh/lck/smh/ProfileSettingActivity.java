package com.ourmentor.ymh.lck.smh;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.io.UnsupportedEncodingException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.UnknownHostException;
        import java.util.concurrent.TimeUnit;

        import de.hdodenhof.circleimageview.CircleImageView;
        import okhttp3.Headers;
        import okhttp3.MediaType;
        import okhttp3.MultipartBody;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

public class ProfileSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText profileSetting_statsMsg_editText;
    private ImageView profileSetting_ok, profileSetting_cancel;
    private ImageView profileSetting_modify;
    private EditText profileSetting_hashTag01_editText, profileSetting_hashTag02_editText, profileSetting_hashTag03_editText;
    private TextView profileSetting_name_textView;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;
    private CircleImageView mPhotoImageView;
    private File bgfile ;

    private String name, pic, userMsg, hashTag01, hashTag02, hashTag03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        Intent intent = getIntent();
        pic =intent.getStringExtra("pic");
        name = intent.getStringExtra("name");
        userMsg=intent.getStringExtra("userMsg");
        hashTag01=intent.getStringExtra("hashTag01");
        hashTag02=intent.getStringExtra("hashTag02");
        hashTag03=intent.getStringExtra("hashTag03");



        profileSetting_name_textView = (TextView)findViewById(R.id.profileSetting_name_textView);
        profileSetting_name_textView.setText(name);
        profileSetting_statsMsg_editText = (EditText)findViewById(R.id.profileSetting_statsMsg_editText);
        profileSetting_statsMsg_editText.setText(userMsg);
        profileSetting_ok = (ImageView)findViewById(R.id.profileSetting_ok);

        mPhotoImageView = (CircleImageView)findViewById(R.id.profileSetting_myImg);
        Glide.with(getApplicationContext()).load(pic).into(mPhotoImageView);

        profileSetting_modify = (ImageView)findViewById(R.id.profileSetting_modify);

        profileSetting_modify.setOnClickListener(this);

        profileSetting_hashTag01_editText =(EditText)findViewById(R.id.profileSetting_hashTag01_editText);
        profileSetting_hashTag01_editText.setText(hashTag01);
        profileSetting_hashTag02_editText =(EditText)findViewById(R.id.profileSetting_hashTag02_editText);
        profileSetting_hashTag02_editText.setText(hashTag02);
        profileSetting_hashTag03_editText =(EditText)findViewById(R.id.profileSetting_hashTag03_editText);
        profileSetting_hashTag03_editText.setText(hashTag03);

        profileSetting_ok = (ImageView)findViewById(R.id.profileSetting_ok);
        profileSetting_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new kbgokasync().execute();
                onBackPressed();
            }
        });
        profileSetting_cancel = (ImageView)findViewById(R.id.profileSetting_cancel);
        profileSetting_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

        bgfile = new File(strFilePath);
        OutputStream out = null;

        try
        {
            bgfile.createNewFile();
            out = new FileOutputStream(bgfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void doTakePhotoAction()
    {
    /*
     * 참고 해볼곳
     * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
     * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
     * http://www.damonkohler.com/2009/02/android-recipes.html
     * http://www.firstclown.us/tag/android/
     */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
        //intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction()
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK)
        {
            return;
        }

        switch(requestCode)
        {
            case CROP_FROM_CAMERA:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();


                if(extras != null)
                {

                    Bitmap photo = extras.getParcelable("data");
                    photo.setDensity(200);
                    SaveBitmapToFileCache(photo, Environment.getExternalStorageDirectory() + "/sampleimage.jpeg");
                    bgfile = new File(Environment.getExternalStorageDirectory() + "/sampleimage.jpeg");
                    mPhotoImageView.setImageBitmap(photo);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }

                break;
            }

            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();
            }
            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 160);
                intent.putExtra("outputY", 160);
                intent.putExtra("aspectX", 1);      //정사각형
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }


    public class kbgokasync extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        OkHttpClient client = new OkHttpClient();
        private final MediaType FormData = MediaType.parse("multipart/form-data");
        private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        private final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
        private final MediaType MEDIA_TYPE_jpg = MediaType.parse("img/jpg");

        private String msg;
        private String hashTag01;
        private String hashTag02;
        private String hashTag03;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ProfileSettingActivity.this, "서버전송중", "서버로 데이터 전송중", true);
            msg = profileSetting_statsMsg_editText.getText().toString();
            if(checkValue(profileSetting_hashTag01_editText.getText().toString()) || profileSetting_hashTag01_editText.getText().charAt(0)!='#' ) //X
            {
                hashTag03 = "";
                if (checkValue(profileSetting_hashTag02_editText.getText().toString()) || profileSetting_hashTag02_editText.getText().charAt(0)!='#') //XX
                {
                    hashTag02 = "";
                    if (checkValue(profileSetting_hashTag03_editText.getText().toString()) || profileSetting_hashTag03_editText.getText().charAt(0)!='#') //XXX
                    {
                        hashTag01 = "";
                    }
                    else if(profileSetting_hashTag03_editText.getText().length()>1 ||profileSetting_hashTag03_editText.getText().charAt(0)=='#')                                                         //XXO
                    {
                        hashTag01 = profileSetting_hashTag03_editText.getText().toString();
                    }
                }else if(profileSetting_hashTag02_editText.getText().length()>1 ||profileSetting_hashTag02_editText.getText().charAt(0)=='#')                                                         //XO
                {
                    hashTag01 = profileSetting_hashTag02_editText.getText().toString();

                    if (checkValue(profileSetting_hashTag03_editText.getText().toString()) || profileSetting_hashTag03_editText.getText().charAt(0)!='#')  //XOX
                    {
                        hashTag02 = "";
                    }
                    else if(profileSetting_hashTag03_editText.getText().length()>1 || profileSetting_hashTag03_editText.getText().charAt(0)=='#')                                                          //XOO
                    {
                        hashTag02 = profileSetting_hashTag03_editText.getText().toString();
                    }
                }
            }
            else if(profileSetting_hashTag01_editText.getText().length()>1 || profileSetting_hashTag01_editText.getText().charAt(0)=='#')                                                               //O
            {
                hashTag01 = profileSetting_hashTag01_editText.getText().toString();
                if (checkValue(profileSetting_hashTag02_editText.getText().toString()) || profileSetting_hashTag02_editText.getText().charAt(0)!='#')  //OX
                {
                    hashTag03 = "";
                    if (checkValue(profileSetting_hashTag03_editText.getText().toString()) || profileSetting_hashTag03_editText.getText().charAt(0)!='#') //OXX
                    {
                        hashTag02 = "";
                    }
                    else if(profileSetting_hashTag03_editText.getText().length()>1 ||profileSetting_hashTag03_editText.getText().charAt(0)=='#')                                                          //OXO
                    {
                        hashTag02 = profileSetting_hashTag03_editText.getText().toString();
                    }
                }
                else if(profileSetting_hashTag02_editText.getText().length()>1 ||profileSetting_hashTag02_editText.getText().charAt(0)=='#')                                                              //OO
                {
                    hashTag02 = profileSetting_hashTag02_editText.getText().toString();
                    if (checkValue(profileSetting_hashTag03_editText.getText().toString()) || profileSetting_hashTag03_editText.getText().charAt(0)!='#')  //OOX
                    {
                        hashTag03 = "";
                    }
                    else if(profileSetting_hashTag03_editText.getText().length()>1 || profileSetting_hashTag03_editText.getText().charAt(0)=='#')                                                          //OOO
                    {
                        hashTag03 = profileSetting_hashTag03_editText.getText().toString();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                RequestBody body;

                if(bgfile ==null) {

                    body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"userno\""),
                                    RequestBody.create(null, String.valueOf(OurMentorConstant.userNo)))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"h_tag1\""),
                                    RequestBody.create(null, hashTag01))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"h_tag2\""),
                                    RequestBody.create(null, hashTag02))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"h_tag3\""),
                                    RequestBody.create(null, hashTag03))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"message\""),
                                    RequestBody.create(null, msg))
                            .build();
                }else
                {
                   body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"userno\""),
                                    RequestBody.create(null, String.valueOf(OurMentorConstant.userNo)))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"h_tag1\""),
                                    RequestBody.create(null, hashTag01))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"h_tag2\""),
                                    RequestBody.create(null, hashTag02))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"h_tag3\""),
                                    RequestBody.create(null, hashTag03))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"message\""),
                                    RequestBody.create(null, msg))
                            .addFormDataPart("testPic", bgfile.getName(), RequestBody.create(MEDIA_TYPE_JPEG, bgfile))
                            .build();
                }




                Request request = new Request.Builder()
                        .url(OurMentorConstant.TARGET_URL + OurMentorConstant.PROFILE_MODIFY)
                        .post(body)
                        .build();

                Response response = toServer.newCall(request).execute();


            } catch (UnknownHostException une) {
                Log.e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                Log.e("fileUpLoad", e.toString());
            } finally {

            }
            return null;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s != null) {
                if (s.equalsIgnoreCase("ok")) {   //대소문자 구별
                    showDialog(1, null);
                } else {
                    showDialog(2, null);            //2번이 실패
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("message", "안드로이드는 잘못이 없습니다. 서버가 잘못됐습니다.");
                showDialog(2, bundle);
            }
        }

    }
    private boolean checkValue(String value){
        return (value==null || value.length()<=1);
    }

}
