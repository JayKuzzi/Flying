package com.bb.offerapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;


public class cal extends BaseActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0,
            btn_plus, btn_minus, btn_mul, btn_divide, btn_point, btn_equal, btn_del, btn_c;
    private EditText editText;//显示输入的数字
    private String opt = "+";//操作符
    private double n1 = 0.0, n2 = 0.0;//两个操作数
    private TextView textView;//显示算式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);
        init();
        setFinishOnTouchOutside(false);

    }

    private void init() {
        textView = (TextView) findViewById(R.id.tv);
        editText = (EditText) findViewById(R.id.edit1);
        btn1 = (Button) findViewById(R.id.btn_1);
        btn2 = (Button) findViewById(R.id.btn_2);
        btn3 = (Button) findViewById(R.id.btn_3);
        btn4 = (Button) findViewById(R.id.btn_4);
        btn5 = (Button) findViewById(R.id.btn_5);
        btn6 = (Button) findViewById(R.id.btn_6);
        btn7 = (Button) findViewById(R.id.btn_7);
        btn8 = (Button) findViewById(R.id.btn_8);
        btn9 = (Button) findViewById(R.id.btn_9);
        btn0 = (Button) findViewById(R.id.btn_0);
        btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_minus = (Button) findViewById(R.id.btn_minus);
        btn_mul = (Button) findViewById(R.id.btn_mul);
        btn_divide = (Button) findViewById(R.id.btn_divide);
        btn_point = (Button) findViewById(R.id.btn_point);
        btn_equal = (Button) findViewById(R.id.btn_equal);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_c = (Button) findViewById(R.id.btn_c);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_mul.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_point.setOnClickListener(this);
        btn_equal.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_c.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_1: {
                String str = editText.getText().toString();
                editText.setText(str + 1);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_2: {
                String str = editText.getText().toString();
                editText.setText(str + 2);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_3: {
                String str = editText.getText().toString();
                editText.setText(str + 3);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_4: {
                String str = editText.getText().toString();
                editText.setText(str + 4);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_5: {
                String str = editText.getText().toString();
                editText.setText(str + 5);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_6: {
                String str = editText.getText().toString();
                editText.setText(str + 6);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_7: {
                String str = editText.getText().toString();
                editText.setText(str + 7);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_8: {
                String str = editText.getText().toString();
                editText.setText(str + 8);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_9: {
                String str = editText.getText().toString();
                editText.setText(str + 9);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_0: {
                String str = editText.getText().toString();
                editText.setText(str + 0);
                str = editText.getText().toString();
                textView.setText(str);
                break;
            }
            case R.id.btn_plus://+
            {
                String str = editText.getText().toString();
                n1 = Double.parseDouble(str);
                opt = "+";
                textView.setText(n1 + opt);
                editText.setText("");
                break;
            }
            case R.id.btn_minus://操作符-
            {
                String str = editText.getText().toString();
                n1 = Double.parseDouble(str);
                opt = "-";
                editText.setText("");
                textView.setText(n1 + opt);
                break;
            }
            case R.id.btn_mul://操作符*
            {
                String str = editText.getText().toString();
                n1 = Double.parseDouble(str);
                opt = "x";
                editText.setText("");
                textView.setText(n1 + opt);
                break;
            }
            case R.id.btn_divide://操作符 /
            {
                String str = editText.getText().toString();
                n1 = Double.parseDouble(str);
                opt = "÷";
                editText.setText("");
                textView.setText(n1 + opt);
                break;
            }
            case R.id.btn_c://CE
            {
                String str =editText.getText().toString();
                if(str.length() > 0)
                    editText.setText("");
                break;
            }
            case R.id.btn_del://<-
            {
                String str =editText.getText().toString();
                if(str.length() > 0)
                    editText.setText(str.substring(0, str.length() - 1));
                break;
            }
            case R.id.btn_point:
            {
                String str = editText.getText().toString();
                if(str.indexOf(".") != -1) //判断字符串中是否已经包含了小数点，如果有就什么也不做
                {

                }
                else //如果没有小数点
                {
                    if(str.equals("0"))//如果开始为0，
                        editText.setText(("0" + ".").toString());
                    else if(str.equals(""))//如果初时显示为空，就什么也不做
                    {

                    }
                    else
                        editText.setText(str + ".");
                }
                break;
            }
            case R.id.btn_equal://操作符=
            {
                if(opt == "+")
                {
                    String str = editText.getText().toString();
                    n2 = Double.parseDouble(str);
                    textView.setText(n1 + opt + n2 + "=");
                    editText.setText((n1 + n2) + "");
                }
                else if(opt == "-")
                {
                    String str = editText.getText().toString();
                    n2 = Double.parseDouble(str);
                    textView.setText(n1 + opt + n2 + "=");
                    editText.setText((n1 - n2) + "");
                }
                else if(opt == "x")
                {
                    String str = editText.getText().toString();
                    n2 = Double.parseDouble(str);
                    textView.setText(n1 + opt + n2 + "=");
                    editText.setText((n1 * n2) + "");
                }
                else if(opt == "÷")
                {
                    String str = editText.getText().toString();
                    n2 = Double.parseDouble(str);
                    if(n2 == 0)
                    {
                        editText.setText("");
                        textView.setText("除数不能为0");
                        break;
                    }
                    else
                    {
                        textView.setText(n1 + opt + n2 + "=");
                        editText.setText((n1 / n2) + "");
                    }
                }

                break;
            }


        }
    }
}