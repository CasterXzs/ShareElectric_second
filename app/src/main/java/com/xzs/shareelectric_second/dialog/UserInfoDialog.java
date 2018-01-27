package com.xzs.shareelectric_second.dialog;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.application.MyApplication;
import com.xzs.shareelectric_second.entity.UserEntity;
import com.xzs.shareelectric_second.utils.Config;
import com.xzs.shareelectric_second.utils.GsonUtil;

/**
 * Created by Lenovo on 2018/1/25.
 */
//废弃 不用
public class UserInfoDialog extends Dialog {

    private EditText userinfofix_et_nickname;
    private Button userinfofix_btn_birthday;
    private RadioGroup userinfofix_rg_sex;
    private Button userinfofix_btn;
    private Activity context;
    private String sex;
    private static final String TAG = "UserInfoDialog";
    private String birthday;
    private String nickname;
    private UserEntity userEntity;


    public UserInfoDialog(Activity context) {
        super(context);
        this.context=context;
        setContentView(R.layout.activity_user_info_fix);
        init();


    }

    private void init(){
        userEntity= MyApplication.userEntity;
        userinfofix_et_nickname=(EditText)findViewById(R.id.userinfofix_et_nickname);
        userinfofix_btn_birthday=(Button)findViewById(R.id.userinfofix_btn_birthday);
        userinfofix_rg_sex=(RadioGroup)findViewById(R.id.userinfofix_rg_sex);
        userinfofix_btn=(Button)findViewById(R.id.userinfofix_btn);
        userinfofix_btn.setOnClickListener(new MyOnClickListener());
        userinfofix_rg_sex.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        userinfofix_btn_birthday.setOnClickListener(new MyOnClickListener());
    }


    private class MyOnCheckedChangeListener  implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup,  int i) {
            switch (i){
                case R.id.userinfofix_rg_sex_male:
                    sex="男";
                    break;
                case R.id.userinfofix_rg_sex_female:
                    sex="女";
                    break;
                default:
                    break;
            }
        }
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            nickname=userinfofix_et_nickname.getText().toString().trim();

            if(view.getId()==R.id.userinfofix_btn){
                if(TextUtils.isEmpty(nickname)||TextUtils.isEmpty(birthday)||TextUtils.isEmpty(sex)){
                    Log.d(TAG, "nickname: "+nickname+" birthday="+birthday+" sex="+sex);
                    Toast.makeText(context, "请输入相关参数", Toast.LENGTH_SHORT).show();
                }else{
                    final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                            context);
                    customProgressDialog.setMessage("正在修改...");
                    customProgressDialog.show();
                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: "+nickname+" "+birthday+" "+sex);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils httpUtils = new HttpUtils();
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("username", userEntity.getUsername());
                            params.addBodyParameter("nickname", nickname);
                            params.addBodyParameter("sex", sex);
                            params.addBodyParameter("birthday", birthday);
                            httpUtils.send(HttpMethod.POST, Config.UPDATA, params, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {

                                    String json = responseInfo.result.trim();
                                    Log.d(TAG, "json: "+json);
                                    final UserEntity userEntity = GsonUtil.fromJson(json, UserEntity.class);
                                    MyApplication.userEntity=userEntity;
                                    if (userEntity.errcode == 0) {

                                        Log.i(TAG, "onSuccess: "+json);
                                        if (customProgressDialog != null
                                                && customProgressDialog.isShowing()) {
                                            customProgressDialog.dismiss();
                                        }
                                        dismiss();
                                    }else {
                                        Toast.makeText(context,
                                                "登录失败:" + userEntity.errmsg,
                                                Toast.LENGTH_LONG).show();
                                        if (customProgressDialog != null
                                                && customProgressDialog.isShowing()) {
                                            customProgressDialog.dismiss();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(HttpException exception, String msg) {
                                    Toast.makeText(context,
                                            exception.getMessage() + " = " + msg,
                                            Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "onFailure: "+exception.getMessage());
                                    if (customProgressDialog != null
                                            && customProgressDialog.isShowing()) {
                                        customProgressDialog.dismiss();
                                    }
                                    return;
                                }
                            });

                        }
                    }).start();

                }
            }
            if(view.getId()==R.id.userinfofix_btn_birthday){
                Toast.makeText(context, "点击了生日", Toast.LENGTH_SHORT).show();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        birthday=String.format("%d-%d-%d",year,month,day);
                        Toast.makeText(context, "year,month,day="+year+" "+month+" "+day, Toast.LENGTH_SHORT).show();
                        userinfofix_btn_birthday.setText(year+"-"+month+"-"+day);
                    }
                },2000,1,1).show();
            }
        }
    }


    public  void change(){
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
    }

}
