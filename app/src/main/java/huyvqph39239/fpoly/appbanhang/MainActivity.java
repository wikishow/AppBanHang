package huyvqph39239.fpoly.appbanhang;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.activity.DienThoaiActivity;
import huyvqph39239.fpoly.appbanhang.activity.GioHangActivity;
import huyvqph39239.fpoly.appbanhang.activity.LapTopActivity;
import huyvqph39239.fpoly.appbanhang.adapter.LoaiSPAdapter;
import huyvqph39239.fpoly.appbanhang.adapter.sanPhamMoiAdapter;
import huyvqph39239.fpoly.appbanhang.model.LoaiSp;
import huyvqph39239.fpoly.appbanhang.model.sanPhamMoi;
import huyvqph39239.fpoly.appbanhang.model.sanPhamMoiModel;
import huyvqph39239.fpoly.appbanhang.retrofit.Apibanhang;
import huyvqph39239.fpoly.appbanhang.retrofit.RetrofitClient;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewHome;
    DrawerLayout drawerLayout;
    LoaiSPAdapter loaiSPAdapter;
    List<LoaiSp> mangLoaisp;
   CompositeDisposable compositeDisposable = new CompositeDisposable();
    Apibanhang apibanhang;
    List<sanPhamMoi> mangSpMoi;
    sanPhamMoiAdapter spAdapter;
    NotificationBadge bage;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apibanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(Apibanhang.class);
        Anhxa();
        ActionBar();

        if(isConnected(this)){

            ActionViewFlipper();
            getLoaisp();
            getSpMoi();
            getEventClick();
        }else {
            Toast.makeText(this, "Khong co internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventClick() {
        listViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        intent1.putExtra("loai", 1);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        intent2.putExtra("loai", 2);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apibanhang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new sanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerView.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong ket noi duoc voi sever"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getLoaisp() {
        compositeDisposable
                .add(apibanhang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpmodel -> {
                            if(loaiSpmodel.isSuccess()){
                                mangLoaisp = loaiSpmodel.getResult();
                                loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(), mangLoaisp);
                                listViewHome.setAdapter(loaiSPAdapter);
                            }
                        }
                ));

    }

    private void ActionViewFlipper(){
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i = 0; i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void Anhxa(){
        toolbar = findViewById(R.id.toolbarHome);
        viewFlipper = findViewById(R.id.viewFliper);
        recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        listViewHome = findViewById(R.id.lvHome);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawLayout);
        bage = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.frameMain);
        //khoi tao list
        mangLoaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if (Utils.mangGiohang == null){
            Utils.mangGiohang = new ArrayList<>();
        }else{
            int total = 0;
            for (int i = 0; i<Utils.mangGiohang.size(); i++){
                total = total + Utils.mangGiohang.get(i).getSoLuong();
            }
            bage.setText(String.valueOf(total));
        }
        //khoi tao adapter
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(MainActivity.this, GioHangActivity.class);
                 startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int total = 0;
        for (int i = 0; i<Utils.mangGiohang.size(); i++){
            total = total + Utils.mangGiohang.get(i).getSoLuong();
        }
        bage.setText(String.valueOf(total));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// them quyen vao Manifesh
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }else {
            return  false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}