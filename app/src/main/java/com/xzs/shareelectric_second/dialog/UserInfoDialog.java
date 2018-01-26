package com.xzs.shareelectric_second.dialog;



import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xzs.shareelectric_second.R;


/**
 * Created by Lenovo on 2018/1/25.
 */

public class UserInfoDialog extends Dialog {

    private EditText userinfofix_et_nickname;
    private EditText userinfofix_et_birthday;
    private RadioGroup userinfofix_rg_sex;
    private Button userinfofix_btn;
    private Activity context;
    private String sex;
    private static final String TAG = "UserInfoDialog";


    public UserInfoDialog(Activity context) {
        super(context);
        this.context=context;
        setContentView(R.layout.activity_user_info_fix);
        init();


    }

    private void init(){
        userinfofix_et_nickname=(EditText)findViewById(R.id.userinfofix_et_nickname);
        userinfofix_et_birthday=(EditText)findViewById(R.id.userinfofix_et_birthday);
        userinfofix_rg_sex=(RadioGroup)findViewById(R.id.userinfofix_rg_sex);
        userinfofix_btn=(Button)findViewById(R.id.userinfofix_btn);
        userinfofix_btn.setOnClickListener(new MyOnClickListener());
        userinfofix_rg_sex.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }


    private class MyOnCheckedChangeListener  implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup,  int i) {
            switch (radioGroup.getId()){
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
            String nickname=userinfofix_et_nickname.getText().toString().trim();
            String birthday=userinfofix_et_birthday.getText().toString().trim();
            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onClick: "+nickname+" "+birthday+" "+sex);
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
