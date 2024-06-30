package huyvqph39239.fpoly.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.text.DecimalFormat;

import huyvqph39239.fpoly.appbanhang.MainActivity;
import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.retrofit.Apibanhang;
import huyvqph39239.fpoly.appbanhang.retrofit.RetrofitClient;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToan extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTongtien, txtsdt, txtemail;
    EditText diachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Apibanhang apibanhang;
    long tongtien;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();

    }

    private void countItem() {
         total = 0;
        for (int i = 0; i<Utils.mangGiohang.size(); i++){
            total = total + Utils.mangGiohang.get(i).getSoLuong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txtTongtien.setText( decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_curent.getEmail());
        txtsdt.setText(Utils.user_curent.getMobile());
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = diachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(ThanhToan.this, "Khong de trong dia chi", Toast.LENGTH_SHORT).show();
                }else {
                    // post data
                    String str_email = Utils.user_curent.getEmail();
                    String str_sdt = Utils.user_curent.getMobile();
                    int id = Utils.user_curent.getId();
                   compositeDisposable.add( apibanhang.createDer(str_email,str_sdt,String.valueOf(tongtien),id,str_diachi,total,new Gson().toJson(Utils.mangGiohang))
                           .subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread())
                           .subscribe(
                                   userModel -> {
                                       Toast.makeText(ThanhToan.this, "Them don hang thanh cong", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                       startActivity(intent);
                                   },
                                   throwable -> {
                                       Log.e("TAG", "Lỗi JSON không hợp lệ: " + throwable.getMessage());
                                   }
                           ));
                }
            }
        });
    }

    private void initView() {
        apibanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(Apibanhang.class);
        toolbar = findViewById(R.id.actionThanhtoan);
        txtemail = findViewById(R.id.txtemail);
        txtTongtien = findViewById(R.id.txtTongtien);
        txtsdt = findViewById(R.id.txtSDT);
        btndathang = findViewById(R.id.btndathang);
        diachi = findViewById(R.id.edtdiachi);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
//[{"giaSP":12200000,"hinhSp":"https://laptop88.vn/media/product/8393_laptop_acer_predator_triton_300_se_pt314_52s_747p.jpg","idsp":16,"soLuong":3,"tensp":"Laptop Acer Predator Triton 300 SE PT314-52s-747P"},{"giaSP":20000000,"hinhSp":"https://www.techone.vn/wp-content/uploads/2021/09/trang-1.jpg","idsp":15,"soLuong":1,"tensp":"IPHONE 12 PRO"}]