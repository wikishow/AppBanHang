package huyvqph39239.fpoly.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.adapter.DienThoaiAdapter;
import huyvqph39239.fpoly.appbanhang.model.sanPhamMoi;
import huyvqph39239.fpoly.appbanhang.retrofit.Apibanhang;
import huyvqph39239.fpoly.appbanhang.retrofit.RetrofitClient;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Apibanhang apibanhang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    int page = 1;
    int loai;
    DienThoaiAdapter adapterdt;
    List<sanPhamMoi> spmoilist;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        loai = getIntent().getIntExtra("loai",1);
        Anhxa();
        ActionToolbar();
      apibanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(Apibanhang.class);
       getData(page);
       addEventLoading();
    }

    private void addEventLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == spmoilist.size() -1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }
    private void loadMore(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                spmoilist.add(null);
                adapterdt.notifyItemInserted(spmoilist.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spmoilist.remove(spmoilist.size() -1 );
                adapterdt.notifyItemInserted(spmoilist.size());
                page = page + 1;
                getData(page);
                adapterdt.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apibanhang.getSanpham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sanPhamMoiModel -> {
                          if(sanPhamMoiModel.isSuccess()){
                              if(adapterdt == null){
                                  spmoilist = sanPhamMoiModel.getResult();
                                  adapterdt = new DienThoaiAdapter(getApplicationContext(),spmoilist);
                                  recyclerView.setAdapter(adapterdt);
                              }
                             else{
                                 int vitri = spmoilist.size();
                                 int soluong = sanPhamMoiModel.getResult().size();
                                 for (int i = 0; i < soluong; i++){
                                     spmoilist.add(sanPhamMoiModel.getResult().get(i));
                                 }
                                 adapterdt.notifyItemRangeChanged(vitri, soluong);
                              }
                          }else{
                              Toast.makeText(this, "Hết dữ liệu", Toast.LENGTH_SHORT).show();
                              isLoading = true;
                          }
                },
                  throwable -> {

                      Toast.makeText(this, "khong ket noi duoc voi Sever", Toast.LENGTH_SHORT).show();
                  }
                  ));

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

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleViewDT);
       linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        spmoilist = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}