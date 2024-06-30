package huyvqph39239.fpoly.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.stream.MalformedJsonException;

import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.retrofit.Apibanhang;
import huyvqph39239.fpoly.appbanhang.retrofit.RetrofitClient;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKy extends AppCompatActivity {
    TextInputEditText email, pass, repass, mobile, username;
    MaterialButton button;
    Apibanhang apibanhang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initCOntrol();
    }

    private void initCOntrol() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangki();
            }


        });
    }
    private void dangki() {
        String str_Email = email.getText().toString().trim();
        String str_Pass = pass.getText().toString().trim();
        String str_Repass = repass.getText().toString().trim();
        String str_Mobile = mobile.getText().toString().trim();
        String str_Username = username.getText().toString().trim();
        if(TextUtils.isEmpty(str_Email)){
            Toast.makeText(this, "Khong de trong Email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_Pass)) {
            Toast.makeText(this, "Khong de trong Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_Repass)) {
            Toast.makeText(this, "Vui long nhap lai mat khau moi", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_Mobile)) {
            Toast.makeText(this, "Khong de trong so dien thoai", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(str_Username)) {
            Toast.makeText(this, "Khong de trong so ten dang nhap", Toast.LENGTH_SHORT).show();
        }
        else {
            if(str_Pass.equals(str_Repass)){
                compositeDisposable.add(apibanhang.dangKi(str_Email, str_Pass, str_Username, str_Mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()) {
                                        Utils.user_curent.setEmail(str_Email);
                                        Utils.user_curent.setPass(str_Pass);
                                        Intent intent = new Intent(getApplicationContext(), DangNhap.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    if (throwable instanceof MalformedJsonException) {
                                        // Xử lý lỗi JSON không hợp lệ ở đây
                                        // Ví dụ: Hiển thị thông báo lỗi cho người dùng
                                        Log.e("TAG", "Lỗi JSON không hợp lệ: " + throwable.getMessage());
                                    } else {
                                        // Xử lý các loại lỗi khác ở đây
                                        // Ví dụ: Hiển thị thông báo lỗi cho người dùng
                                        Log.e("TAG", "Lỗi không xác định: " + throwable.getMessage());
                                    }
                                }
                        )
                );
            }else {
                Toast.makeText(this, "Pass chua khop", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initView() {
        apibanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(Apibanhang.class);
        email = findViewById(R.id.txtDKEmail);
        pass = findViewById(R.id.txtDKPass);
        repass = findViewById(R.id.txtDKrePass);
        button = findViewById(R.id.btndangky);
        mobile = findViewById(R.id.txtDKmobile);
        username = findViewById(R.id.txtDKUsername);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}