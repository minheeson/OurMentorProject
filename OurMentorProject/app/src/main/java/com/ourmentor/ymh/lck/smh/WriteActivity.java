package com.ourmentor.ymh.lck.smh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ourmentor.ymh.lck.smh.common.OurMentorConstant;
import com.ourmentor.ymh.lck.smh.common.WritingObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class WriteActivity extends AppCompatActivity {
    private ImageView write_cancel , write_ok;
    private CheckBox write_checkBox_question, write_checkBox_info;
    private Spinner write_spinner_category , write_spinner_detail_category;
    private EditText write_editText_title, write_editText_content, write_editText_hashTag01, write_editText_hashTag02, write_editText_hashTag03;
    private ArrayAdapter<CharSequence> categoryAdapter;
    private ArrayAdapter<CharSequence> detailCategoryAdapter;
    private WritingObject writingObject;
    private AsyncTaskWriteInsert asyncTaskWriteInsert;

    public static final int CATEGORY_QUESTION = 1;
    public static final int CATEGORY_INFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        write_editText_title = (EditText)findViewById(R.id.write_editText_title);
        write_editText_content = (EditText)findViewById(R.id.write_editText_content);
        write_editText_hashTag01 = (EditText)findViewById(R.id.write_editText_hashTag01);
        write_editText_hashTag02 = (EditText)findViewById(R.id.write_editText_hashTag02);
        write_editText_hashTag03 = (EditText)findViewById(R.id.write_editText_hashTag03);

        if(writingObject == null)
        {
            writingObject = new WritingObject();
        }

        //////////// 글 작성 취소
        write_cancel = (ImageView)findViewById(R.id.write_cancel);
        write_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteActivity.this);
                dialog.setTitle("취소");
                dialog.setMessage("글 작성을 취소하시겠습니까?");
                dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.setNegativeButton("아니오", null);
                dialog.show();
            }
        });

        //////////// 글 게시 AsyncTask
        write_ok = (ImageView)findViewById(R.id.write_ok);
        write_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteActivity.this);
                dialog.setTitle("확인");
                dialog.setMessage("글 게시를 하시겠습니까?");
                dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        writingObject.id = "유명현";
                        writingObject.title = write_editText_title.getText().toString();
                        writingObject.content = write_editText_content.getText().toString();
                        if(checkValue(write_editText_hashTag01.getText().toString()) || write_editText_hashTag01.getText().charAt(0)!='#' ) //X
                        {
                            writingObject.hashTag03 = "";
                            if (checkValue(write_editText_hashTag02.getText().toString()) || write_editText_hashTag02.getText().charAt(0)!='#') //XX
                            {
                                writingObject.hashTag02 = "";
                                if (checkValue(write_editText_hashTag03.getText().toString()) || write_editText_hashTag03.getText().charAt(0)!='#') //XXX
                                {
                                    writingObject.hashTag01 = "";
                                }
                                else if(write_editText_hashTag03.getText().length()>1 ||write_editText_hashTag03.getText().charAt(0)=='#')                                                         //XXO
                                {
                                    writingObject.hashTag01 = write_editText_hashTag03.getText().toString();
                                }
                            }else if(write_editText_hashTag02.getText().length()>1 ||write_editText_hashTag02.getText().charAt(0)=='#')                                                         //XO
                            {
                                writingObject.hashTag01 = write_editText_hashTag02.getText().toString();

                                if (checkValue(write_editText_hashTag03.getText().toString()) || write_editText_hashTag03.getText().charAt(0)!='#')  //XOX
                                {
                                    writingObject.hashTag02 = "";
                                }
                                else if(write_editText_hashTag03.getText().length()>1 || write_editText_hashTag03.getText().charAt(0)=='#')                                                          //XOO
                                {
                                    writingObject.hashTag02 = write_editText_hashTag03.getText().toString();
                                }
                            }
                        }
                        else if(write_editText_hashTag01.getText().length()>1 || write_editText_hashTag01.getText().charAt(0)=='#')                                                               //O
                        {
                            writingObject.hashTag01 = write_editText_hashTag01.getText().toString();
                            if (checkValue(write_editText_hashTag02.getText().toString()) || write_editText_hashTag02.getText().charAt(0)!='#')  //OX
                            {
                                writingObject.hashTag03 = "";
                                if (checkValue(write_editText_hashTag03.getText().toString()) || write_editText_hashTag03.getText().charAt(0)!='#') //OXX
                                {
                                    writingObject.hashTag02 = "";
                                }
                                else if(write_editText_hashTag03.getText().length()>1 ||write_editText_hashTag03.getText().charAt(0)=='#')                                                          //OXO
                                {
                                    writingObject.hashTag02 = write_editText_hashTag03.getText().toString();
                                }
                            }
                            else if(write_editText_hashTag02.getText().length()>1 ||write_editText_hashTag02.getText().charAt(0)=='#')                                                              //OO
                            {
                                writingObject.hashTag02 = write_editText_hashTag02.getText().toString();
                                if (checkValue(write_editText_hashTag03.getText().toString()) || write_editText_hashTag03.getText().charAt(0)!='#')  //OOX
                                {
                                    writingObject.hashTag03 = "";
                                }
                                else if(write_editText_hashTag03.getText().length()>1 || write_editText_hashTag03.getText().charAt(0)=='#')                                                          //OOO
                                {
                                    writingObject.hashTag03 = write_editText_hashTag03.getText().toString();
                                }
                            }
                        }

                        if(writingObject.title == null || writingObject.title.length() <= 0) {
                            Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                            write_editText_title.requestFocus();
                        } else if(writingObject.content == null || writingObject.content.length() <=0){
                            Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                            write_editText_content.requestFocus();
                        } else if(checkValue(writingObject.directory)) {
                            Toast.makeText(getApplicationContext(), "1차 디렉토리를 선택해주세요", Toast.LENGTH_SHORT).show();
                            write_spinner_category.findFocus();
                        } else if(checkValue(writingObject.detail_directory)) {
                            Toast.makeText(getApplicationContext(), "2차 디렉토리를 선택해주세요", Toast.LENGTH_SHORT).show();
                            write_spinner_detail_category.findFocus();
                        }
                        else{
                            asyncTaskWriteInsert = new AsyncTaskWriteInsert();
                            asyncTaskWriteInsert.execute(writingObject);
                        }
                    }
                });
                dialog.setNegativeButton("아니오", null);
                dialog.show();
                return false;
            }
        });


        //////////// 글 카테고리
        write_checkBox_info= (CheckBox)findViewById(R.id.write_checkBox_info);
        write_checkBox_question = (CheckBox)findViewById(R.id.write_checkBox_question);
        write_checkBox_question.setChecked(true);
        writingObject.category = CATEGORY_QUESTION;

        write_checkBox_question.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    writingObject.category = CATEGORY_QUESTION;                                                         //선택된 글 카테고리 저장
                    write_checkBox_info.setChecked(false);
                }
                else
                {
                    writingObject.category = CATEGORY_INFO;
                    write_checkBox_info.setChecked(true);
                }
            }
        });

        write_checkBox_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    writingObject.category = CATEGORY_INFO;

                    write_checkBox_question.setChecked(false);
                }
                else {
                    writingObject.category = CATEGORY_QUESTION;
                    write_checkBox_question.setChecked(true);
                }
            }
        });



        //////////// 글 디렉토리
        write_spinner_category = (Spinner)findViewById(R.id.write_spinner_category);
        write_spinner_detail_category = (Spinner)findViewById(R.id.write_spinner_detail_category);
        categoryAdapter = ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        write_spinner_category.setAdapter(categoryAdapter);
        write_spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    writingObject.directory = categoryAdapter.getItem(position).toString();                 //선택된 1차 디렉토리 저장
                    detailCategoryAdapter = ArrayAdapter.createFromResource(WriteActivity.this, R.array.detailEduCategory, android.R.layout.simple_spinner_item);
                    detailCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    write_spinner_detail_category.setAdapter(detailCategoryAdapter);
                    write_spinner_detail_category.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    writingObject.directory = categoryAdapter.getItem(position).toString();
                    detailCategoryAdapter = ArrayAdapter.createFromResource(WriteActivity.this, R.array.detailStudyCategory, android.R.layout.simple_spinner_item);
                    detailCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    write_spinner_detail_category.setAdapter(detailCategoryAdapter);
                    write_spinner_detail_category.setVisibility(View.VISIBLE);
                } else if (position == 3) {
                    writingObject.directory = categoryAdapter.getItem(position).toString();
                    detailCategoryAdapter = ArrayAdapter.createFromResource(WriteActivity.this, R.array.detailWorkCategory, android.R.layout.simple_spinner_item);
                    detailCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    write_spinner_detail_category.setAdapter(detailCategoryAdapter);
                    write_spinner_detail_category.setVisibility(View.VISIBLE);
                } else if (position == 0) {
                    write_spinner_detail_category.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        write_spinner_detail_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                writingObject.detail_directory = detailCategoryAdapter.getItem(position).toString();                //2차 디렉토리 저장
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean checkValue(String value){
        return (value==null || value.length()<=1);
    }

    public class AsyncTaskWriteInsert extends AsyncTask<WritingObject, Integer, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(WritingObject... params) {
            WritingObject param = params[0];
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;   //서버에서 넘어온 JSON을 받아내는 Stream
            StringBuilder queryBuf = new StringBuilder();

            String result = "";

            try {
                queryBuf.append("userno=" + OurMentorConstant.userNo)
                        .append("&name=" + OurMentorConstant.userName)
                        .append("&category="+ param.category)
                        .append("&title="+param.title)
                        .append("&content="+ param.content)
                        .append("&h_tag1=" + param.hashTag01)
                        .append("&h_tag2=" + param.hashTag02)
                        .append("&h_tag3=" + param.hashTag03)
                        .append("&main_dir=" + param.directory)
                        .append("&sub_dir=" + param.detail_directory);

                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.WRITE_INSERT_PATH);
                conn = (HttpURLConnection)targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //반드시 대분자
                conn.setRequestMethod("POST");

                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.flush();
                toServer.close();

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String oneLine = "";
                    StringBuilder jsonBuf = new StringBuilder();
                    while((oneLine = fromServer.readLine()) !=null ){
                        jsonBuf.append(oneLine);
                    }
                    JSONObject responseData = new JSONObject(jsonBuf.toString());
                    //OK or FAIL
                } else{
                    //200이 아닐 경우, 처리할 값(보통 다이얼로그를 이용하여 띄운다.
                    //절대 내 앱이 죽어서는 안됨...
                }
            } catch (Exception e) {
                Log.e("WriteError", e.toString());
            } finally {
                if(fromServer != null){
                    try{
                        fromServer.close();
                    } catch (IOException e){
                    }

                    if( conn != null){
                        conn.disconnect();
                    }

                }
            }
            if( conn != null) {
            }
            Log.e("conn", String.valueOf(conn));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Intent intent = new Intent(WriteActivity.this, BoardActivity.class);
            startActivity(intent);
            finish();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(WriteActivity.this, "서버전송중", "서버로 데이터를 전송중입니다.", true);
        }
    }

    public Dialog onCreateDialog(int id, Bundle bundle){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("입력중 결과값");
        Dialog writeDialog = null;

        switch (id){
            case 1:
                alertDialog.setMessage("해당정보를 입력하였습니다.");
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                writeDialog = alertDialog.create();
                return writeDialog;

            case 2:
                if(bundle != null){
                    alertDialog.setMessage(bundle.getString("message"));
                } else{
                    alertDialog.setMessage("서버때문에 입력실패...안드로이드는 잘못없음");
                }
                alertDialog.setMessage("해당정보를 입력하였습니다.");
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                writeDialog = alertDialog.create();
                return writeDialog;
        }
        writeDialog = alertDialog.create();
        return writeDialog;
    }
}
