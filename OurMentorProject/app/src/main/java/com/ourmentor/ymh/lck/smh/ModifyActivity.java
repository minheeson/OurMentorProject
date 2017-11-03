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


public class ModifyActivity extends AppCompatActivity {
    private ImageView modify_cancel , modify_ok;
    private CheckBox modify_checkBox_question, modify_checkBox_info;
    private Spinner modify_spinner_category , modify_spinner_detail_category;
    private EditText modify_editText_title, modify_editText_content, modify_editText_hashTag01, modify_editText_hashTag02, modify_editText_hashTag03;
    private ArrayAdapter<CharSequence> categoryAdapter;
    private ArrayAdapter<CharSequence> detailCategoryAdapter;
    private WritingObject modifyObject;
    private int b_no;
    private int main_dir;
    private int sub_dir;
    public static final int CATEGORY_QUESTION = 1;
    public static final int CATEGORY_INFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("modifyObject");

        modify_editText_title = (EditText)findViewById(R.id.modify_editText_title);
        modify_editText_content = (EditText)findViewById(R.id.modify_editText_content);
        modify_editText_hashTag01 = (EditText)findViewById(R.id.modify_editText_hashTag01);
        modify_editText_hashTag02 = (EditText)findViewById(R.id.modify_editText_hashTag02);
        modify_editText_hashTag03 = (EditText)findViewById(R.id.modify_editText_hashTag03);



        modify_editText_title.setText(bundle.getString("title"));
        modify_editText_content.setText(bundle.getString("content"));
        modify_editText_hashTag01.setText(bundle.getString("hashTag01"));
        modify_editText_hashTag02.setText(bundle.getString("hashTag02"));
        modify_editText_hashTag03.setText(bundle.getString("hashTag03"));

        if(bundle.getString("main_dir").equals("교육"))
        {
            main_dir = 1;
        }
        else if(bundle.getString("main_dir").equals("학문"))
        {
            main_dir =2;
        }
        else if(bundle.getString("main_dir").equals("취업"))
        {
            main_dir =3;
        }

        if(main_dir ==1)
        {
            if(bundle.getString("sub_dir").equals("중학교"))
            {
                sub_dir = 0;
            }
            else if(bundle.getString("sub_dir").equals("고등학교"))
            {
                sub_dir = 2;
            }
            else if(bundle.getString("sub_dir").equals("대학교"))
            {
                sub_dir = 3;
            }
            else if(bundle.getString("sub_dir").equals("평생"))
            {
                sub_dir = 4;
            }
            else if(bundle.getString("sub_dir").equals("영어"))
            {
                sub_dir = 5;
            }
        }
        else if(main_dir==2)
        {
            if(bundle.getString("sub_dir").equals("인문학"))
            {
                sub_dir = 0;
            }
            else if(bundle.getString("sub_dir").equals("사회학"))
            {
                sub_dir = 2;
            }
            else if(bundle.getString("sub_dir").equals("수학"))
            {
                sub_dir = 3;
            }
            else if(bundle.getString("sub_dir").equals("과학"))
            {
                sub_dir = 4;
            }
            else if(bundle.getString("sub_dir").equals("공학"))
            {
                sub_dir = 5;
            }
            else if(bundle.getString("sub_dir").equals("교육정책"))
            {
                sub_dir = 6;
            }
            else if(bundle.getString("sub_dir").equals("이슈"))
            {
                sub_dir = 7;
            }
        }else if(main_dir==3)
        {
            if(bundle.getString("sub_dir").equals("스펙"))
            {
                sub_dir = 0;
            }
            else if(bundle.getString("sub_dir").equals("자소서"))
            {
                sub_dir = 2;
            }
            else if(bundle.getString("sub_dir").equals("면접"))
            {
                sub_dir = 3;
            }
        }

        b_no=bundle.getInt("b_no");
        if(modifyObject == null)
        {
            modifyObject = new WritingObject();
        }

        //////////// 글 작성 취소
                    modify_cancel = (ImageView)findViewById(R.id.modify_cancel);
                    modify_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ModifyActivity.this);
                            dialog.setTitle("취소");
                            dialog.setMessage("글 수정을 취소하시겠습니까?");
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
        modify_ok = (ImageView)findViewById(R.id.modify_ok);
        modify_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ModifyActivity.this);
                dialog.setTitle("확인");
                dialog.setMessage("글 수정을 하시겠습니까?");
                dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modifyObject.id = "유명현";
                        modifyObject.title = modify_editText_title.getText().toString();
                        modifyObject.content = modify_editText_content.getText().toString();
                        if(checkValue(modify_editText_hashTag01.getText().toString()) || modify_editText_hashTag01.getText().charAt(0)!='#' ) //X
                        {
                            modifyObject.hashTag03 = "";
                            if (checkValue(modify_editText_hashTag02.getText().toString()) || modify_editText_hashTag02.getText().charAt(0)!='#') //XX
                            {
                                modifyObject.hashTag02 = "";
                                if (checkValue(modify_editText_hashTag03.getText().toString()) || modify_editText_hashTag03.getText().charAt(0)!='#') //XXX
                                {
                                    modifyObject.hashTag01 = "";
                                }
                                else if(modify_editText_hashTag03.getText().length()>1 || modify_editText_hashTag03.getText().charAt(0)=='#' )                                                         //XXO
                                {
                                    modifyObject.hashTag01 = modify_editText_hashTag03.getText().toString();
                                }
                            }else if(modify_editText_hashTag02.getText().length()>1 || modify_editText_hashTag02.getText().charAt(0)=='#')                                                         //XO
                            {
                                modifyObject.hashTag01 = modify_editText_hashTag02.getText().toString();

                                if (checkValue(modify_editText_hashTag03.getText().toString()) || modify_editText_hashTag03.getText().charAt(0)!='#')  //XOX
                                {
                                    modifyObject.hashTag02 = "";
                                }
                                else if(modify_editText_hashTag03.getText().length()>1 || modify_editText_hashTag03.getText().charAt(0)=='#')                                                          //XOO
                                {
                                    modifyObject.hashTag02 = modify_editText_hashTag03.getText().toString();
                                }
                            }
                        }
                        else if(modify_editText_hashTag01.getText().length()>1 || modify_editText_hashTag01.getText().charAt(0)=='#')                                                               //O
                        {
                            modifyObject.hashTag01 = modify_editText_hashTag01.getText().toString();
                            if (checkValue(modify_editText_hashTag02.getText().toString()) || modify_editText_hashTag02.getText().charAt(0)!='#')  //OX
                            {
                                modifyObject.hashTag03 = "";
                                if (checkValue(modify_editText_hashTag03.getText().toString()) || modify_editText_hashTag03.getText().charAt(0)!='#') //OXX
                                {
                                    modifyObject.hashTag02 = "";
                                }
                                else if(modify_editText_hashTag03.getText().length()>1 ||modify_editText_hashTag03.getText().charAt(0)=='#')                                                          //OXO
                                {
                                    modifyObject.hashTag02 = modify_editText_hashTag03.getText().toString();
                                }
                            }
                            else if(modify_editText_hashTag02.getText().length()>1 || modify_editText_hashTag02.getText().charAt(0)=='#')                                                              //OO
                            {
                                modifyObject.hashTag02 = modify_editText_hashTag02.getText().toString();
                                if (checkValue(modify_editText_hashTag03.getText().toString()) || modify_editText_hashTag03.getText().charAt(0)!='#')  //OOX
                                {
                                    modifyObject.hashTag03 = "";
                                }
                                else if(modify_editText_hashTag03.getText().length()>1 || modify_editText_hashTag03.getText().charAt(0)=='#')                                                          //OOO
                                {
                                    modifyObject.hashTag03 = modify_editText_hashTag03.getText().toString();
                                }
                            }
                        }
                        if(modifyObject.title == null || modifyObject.title.length() <= 0) {
                            Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                            modify_editText_title.requestFocus();
                        } else if(checkValue(modifyObject.content)){
                            Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                            modify_editText_content.requestFocus();
                        } else if(checkValue(modifyObject.directory)) {
                            Toast.makeText(getApplicationContext(), "1차 디렉토리를 선택해주세요", Toast.LENGTH_SHORT).show();
                            modify_spinner_category.findFocus();
                        } else if(checkValue(modifyObject.detail_directory)) {
                            Toast.makeText(getApplicationContext(), "2차 디렉토리를 선택해주세요", Toast.LENGTH_SHORT).show();
                            modify_spinner_detail_category.findFocus();
                        }
                        else{
                            new AsyncTaskModifyInsert().execute(modifyObject);
                            Intent intent = new Intent(ModifyActivity.this, BoardActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                dialog.setNegativeButton("아니오", null);
                dialog.show();
                return false;
            }
        });


        //////////// 글 카테고리
        modify_checkBox_info= (CheckBox)findViewById(R.id.modify_checkBox_info);
        modify_checkBox_question = (CheckBox)findViewById(R.id.modify_checkBox_question);
        modify_checkBox_question.setChecked(true);
        modifyObject.category = CATEGORY_QUESTION;

        if(bundle.getInt("category")==1)
        {
            modify_checkBox_question.setChecked(true);
            modify_checkBox_info.setChecked(false);
        }
        else if(bundle.getInt("category")==2)
        {
            modify_checkBox_info.setChecked(true);
            modify_checkBox_question.setChecked(false);
        }
        modify_checkBox_question.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    modifyObject.category = CATEGORY_QUESTION;                                                         //선택된 글 카테고리 저장
                    modify_checkBox_info.setChecked(false);
                }
                else
                {
                    modifyObject.category = CATEGORY_INFO;
                    modify_checkBox_info.setChecked(true);
                }
            }
        });

        modify_checkBox_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    modifyObject.category = CATEGORY_INFO;

                    modify_checkBox_question.setChecked(false);
                }
                else {
                    modifyObject.category = CATEGORY_QUESTION;
                    modify_checkBox_question.setChecked(true);
                }
            }
        });



        //////////// 글 디렉토리
        modify_spinner_category = (Spinner)findViewById(R.id.modify_spinner_category);
        modify_spinner_detail_category = (Spinner)findViewById(R.id.modify_spinner_detail_category);
        categoryAdapter = ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modify_spinner_category.setAdapter(categoryAdapter);
        modify_spinner_category.setSelection(main_dir);
        modify_spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    modifyObject.directory = categoryAdapter.getItem(position).toString();                 //선택된 1차 디렉토리 저장
                    detailCategoryAdapter = ArrayAdapter.createFromResource(ModifyActivity.this, R.array.detailEduCategory, android.R.layout.simple_spinner_item);
                    detailCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modify_spinner_detail_category.setAdapter(detailCategoryAdapter);
                    modify_spinner_detail_category.setSelection(sub_dir);
                    modify_spinner_detail_category.setVisibility(View.VISIBLE);

                } else if (position == 2) {
                    modifyObject.directory = categoryAdapter.getItem(position).toString();
                    detailCategoryAdapter = ArrayAdapter.createFromResource(ModifyActivity.this, R.array.detailStudyCategory, android.R.layout.simple_spinner_item);
                    detailCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modify_spinner_detail_category.setAdapter(detailCategoryAdapter);
                    modify_spinner_detail_category.setSelection(sub_dir);
                    modify_spinner_detail_category.setVisibility(View.VISIBLE);
                } else if (position == 3) {
                    modifyObject.directory = categoryAdapter.getItem(position).toString();
                    detailCategoryAdapter = ArrayAdapter.createFromResource(ModifyActivity.this, R.array.detailWorkCategory, android.R.layout.simple_spinner_item);
                    detailCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modify_spinner_detail_category.setAdapter(detailCategoryAdapter);
                    modify_spinner_detail_category.setSelection(sub_dir);
                    modify_spinner_detail_category.setVisibility(View.VISIBLE);
                } else if (position == 0) {
                    modify_spinner_detail_category.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modify_spinner_detail_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modifyObject.detail_directory = detailCategoryAdapter.getItem(position).toString();                //2차 디렉토리 저장
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean checkValue(String value){
        return (value==null || value.length()<=1);
    }

    public class AsyncTaskModifyInsert extends AsyncTask<WritingObject, Integer, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(WritingObject... params) {
            WritingObject param = params[0];
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;   //서버에서 넘어온 JSON을 받아내는 Stream
            StringBuilder queryBuf = new StringBuilder();

            String result = "";

            try {
                queryBuf.append("b_no=" + b_no)
                        .append("&userno=" + OurMentorConstant.userNo)
                        .append("&category="+ param.category)
                        .append("&title="+param.title)
                        .append("&content="+ param.content)
                        .append("&h_tag1=" + param.hashTag01)
                        .append("&h_tag2=" + param.hashTag02)
                        .append("&h_tag3=" + param.hashTag03)
                        .append("&main_dir=" + param.directory)
                        .append("&sub_dir="+param.detail_directory);

                Log.e("modifyLog",String.valueOf(queryBuf));
                URL targetURL = new URL(OurMentorConstant.TARGET_URL + OurMentorConstant.MODIFY);
                conn = (HttpURLConnection)targetURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //반드시 대분자
                conn.setRequestMethod("POST");

                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
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
                    result = responseData.getString("result");
                } else{
                    //200이 아닐 경우, 처리할 값(보통 다이얼로그를 이용하여 띄운다.
                    //절대 내 앱이 죽어서는 안됨...
                }
            } catch (Exception e) {
                Log.e("BloodInsertError", e.toString());
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ModifyActivity.this, "서버전송중", "서버로 데이터를 전송중입니다.", true);
        }
    }

    public Dialog onCreateDialog(int id, Bundle bundle){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("입력중 결과값");
        Dialog modifyDialog = null;

        switch (id){
            case 1:
                alertDialog.setMessage("해당정보를 입력하였습니다.");
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                modifyDialog = alertDialog.create();
                return modifyDialog;

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
                modifyDialog = alertDialog.create();
                return modifyDialog;
        }
        modifyDialog = alertDialog.create();
        return modifyDialog;
    }
}
