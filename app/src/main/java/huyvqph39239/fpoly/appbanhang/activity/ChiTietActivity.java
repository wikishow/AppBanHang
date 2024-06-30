package huyvqph39239.fpoly.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.model.GioHang;
import huyvqph39239.fpoly.appbanhang.model.sanPhamMoi;

public class ChiTietActivity extends AppCompatActivity {
   TextView tensp, giasp, mota;
   MaterialButton btnthem;
   ImageView imghinhanh;
   Spinner spinner;
   Toolbar toolbar;
   sanPhamMoi spmoi;
    NotificationBadge bage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolbar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGiohang();
            }
        });
    }

    private void themGiohang() {
        if (Utils.mangGiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.mangGiohang.size(); i++){
                if (Utils.mangGiohang.get(i).getIdsp() == spmoi.getId()){
                    Utils.mangGiohang.get(i).setSoLuong(soluong + Utils.mangGiohang.get(i).getSoLuong());
                    long gia = Long.parseLong(spmoi.getGiasp());
                    Utils.mangGiohang.get(i).setGiaSP(gia);
                    flag = true;

                }
            }
            if (!flag){
                long gia = Long.parseLong(spmoi.getGiasp()) ;
                GioHang gioHang = new GioHang();
                gioHang.setGiaSP(gia);
                gioHang.setSoLuong(soluong);
                gioHang.setIdsp(spmoi.getId());
                gioHang.setTensp(spmoi.getTensp());
                gioHang.setHinhSp(spmoi.getHinhanh());
                Utils.mangGiohang.add(gioHang);
            }
        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(spmoi.getGiasp()) ;
            GioHang gioHang = new GioHang();
            gioHang.setGiaSP(gia);
            gioHang.setSoLuong(soluong);
            gioHang.setIdsp(spmoi.getId());
            gioHang.setTensp(spmoi.getTensp());
            gioHang.setHinhSp(spmoi.getHinhanh());
            Utils.mangGiohang.add(gioHang);
        }
        int total = 0;
        for (int i = 0; i<Utils.mangGiohang.size(); i++){
            total = total + Utils.mangGiohang.get(i).getSoLuong();
        }
        bage.setText(String.valueOf(total));
    }

    private void initData() {
          spmoi = (sanPhamMoi) getIntent().getSerializableExtra("chitiet");
         tensp.setText(spmoi.getTensp());
         mota.setText(spmoi.getMota());
        Glide.with(getApplicationContext()).load(spmoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " +decimalFormat.format(Double.parseDouble(spmoi.getGiasp())) + " VND");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView() {
        tensp = findViewById(R.id.txttenSP);
        giasp = findViewById(R.id.txtgiaSP);
        mota = findViewById(R.id.txtmota);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        imghinhanh = findViewById(R.id.imgchitiet);
        spinner = findViewById(R.id.spiner);
        toolbar = findViewById( R.id.toolbar);
        bage = findViewById(R.id.menu_sl);

        FrameLayout framgiohang = findViewById(R.id.frameLayout);
        framgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if(Utils.mangGiohang != null){
            int total = 0;
            for(int i = 0; i < Utils.mangGiohang.size(); i++){
                total = total + Utils.mangGiohang.get(i).getSoLuong();
            }
            bage.setText(String.valueOf(total));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.mangGiohang != null){
            int total = 0;
            for(int i = 0; i < Utils.mangGiohang.size(); i++){
                total = total + Utils.mangGiohang.get(i).getSoLuong();
            }
            bage.setText(String.valueOf(total));
        }
    }
}