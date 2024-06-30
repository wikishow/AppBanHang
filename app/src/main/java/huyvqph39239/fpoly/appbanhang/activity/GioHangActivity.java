package huyvqph39239.fpoly.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.util.UniversalTimeScale;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.adapter.GiohangAdapter;
import huyvqph39239.fpoly.appbanhang.model.EventBus.TinhTongEventBus;
import huyvqph39239.fpoly.appbanhang.model.GioHang;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangTrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    MaterialButton btngiohang;
    GiohangAdapter adapter;
    long tongtiensp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        intiControl();
        tinhtongtien();
    }

    private void tinhtongtien() {
        tongtiensp = 0;
        for (int i = 0; i<Utils.mangGiohang.size(); i++){
            tongtiensp = tongtiensp +(Utils.mangGiohang.get(i).getGiaSP() * Utils.mangGiohang.get(i).getSoLuong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void intiControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.mangGiohang.size() == 0){
            giohangTrong.setVisibility(View.VISIBLE);
        }else
        if(Utils.mangGiohang.size() >0){
            adapter = new GiohangAdapter(getApplicationContext(), Utils.mangGiohang);
            recyclerView.setAdapter(adapter);
        }
        btngiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThanhToan.class);
                intent.putExtra("tongtien",tongtiensp);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        giohangTrong = findViewById(R.id.txtgiohangtrong);
        toolbar = findViewById(R.id.actionBarsize);
        recyclerView = findViewById(R.id.recycleView);
        tongtien = findViewById(R.id.txtTongtien);
        btngiohang = findViewById(R.id.btnMuahang);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public  void eventTinhTien(TinhTongEventBus event){
        if(event != null){
            tinhtongtien();
        }
    }
}