package huyvqph39239.fpoly.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

import huyvqph39239.fpoly.appbanhang.Interface.ItemClickListener;
import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.activity.ChiTietActivity;
import huyvqph39239.fpoly.appbanhang.activity.DienThoaiActivity;
import huyvqph39239.fpoly.appbanhang.model.sanPhamMoi;

public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<sanPhamMoi> array;
    private static final int View_TYPE_DATA = 0;
    private static final int View_TYPE_LOADING = 1;


    public DienThoaiAdapter(Context context, List<sanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == View_TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      if(holder instanceof MyViewHolder){
          MyViewHolder myViewHolder = (MyViewHolder) holder;
          sanPhamMoi spmoi = array.get(position);
          myViewHolder.tenSp.setText(spmoi.getTensp().trim());
          DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
          myViewHolder.gia.setText("Giá: " +decimalFormat.format(Double.parseDouble(spmoi.getGiasp())) + " VND");
          myViewHolder.moTa.setText(spmoi.getMota());
          Glide.with(context).load(spmoi.getHinhanh()).into(myViewHolder.hinhanh);
          myViewHolder.setItemClickListener(new ItemClickListener() {
              @Override
              public void onClick(View view, int pos, boolean isLongCLick) {
                  if(!isLongCLick){
                      Intent intent = new Intent(context, ChiTietActivity.class);
                      intent.putExtra("chitiet", spmoi);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      context.startActivity(intent);
                  }
              }
          });
      }else{
          LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
          loadingViewHolder.progressBar.setIndeterminate(true);
      }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? View_TYPE_LOADING: View_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);

        }
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tenSp, gia, moTa;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenSp = itemView.findViewById(R.id.itemdt_ten);
            gia = itemView.findViewById(R.id.itemdt_gia);
            moTa = itemView.findViewById(R.id.itemdt_mota);

            hinhanh = itemView.findViewById(R.id.itemDT_img);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(),false);
        }
    }

}
