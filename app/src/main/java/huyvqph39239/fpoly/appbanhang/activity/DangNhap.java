package huyvqph39239.fpoly.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import huyvqph39239.fpoly.appbanhang.MainActivity;
import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.retrofit.Apibanhang;
import huyvqph39239.fpoly.appbanhang.retrofit.RetrofitClient;
import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhap extends AppCompatActivity {
    TextView txtdangky;
    TextInputEditText email, pass;
    MaterialButton btndangnhap;
    Apibanhang apibanhang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {

        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKy.class);
                startActivity(intent);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Khong de trong Email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Khong de trong Password", Toast.LENGTH_SHORT).show();
                }else {
                    Paper.book().write("email", str_email);
                    Paper.book().write("pass", str_pass);
                    compositeDisposable.add(apibanhang.dangnhap(str_email,str_pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Utils.user_curent = userModel.getResult().get(0);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {

                                        }
                                    },throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apibanhang= RetrofitClient.getInstance(Utils.BASE_URL).create(Apibanhang.class);
        email = findViewById(R.id.txtDNEmail);
        pass = findViewById(R.id.txtDNPass);
        txtdangky = findViewById(R.id.txtdangky);
        btndangnhap = findViewById(R.id.btndangnhap);
        if (Paper.book().read("email") != null && Paper.book().read("pass") != null){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_curent.getEmail() != null && Utils.user_curent.getPass() != null){
            email.setText(Utils.user_curent.getEmail());
            pass.setText(Utils.user_curent.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}