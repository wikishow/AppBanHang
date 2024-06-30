package huyvqph39239.fpoly.appbanhang.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import huyvqph39239.fpoly.appbanhang.Interface.ItemClickListener;
import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.activity.ChiTietActivity;
import huyvqph39239.fpoly.appbanhang.model.sanPhamMoi;

public class sanPhamMoiAdapter extends RecyclerView.Adapter<sanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<sanPhamMoi> array;

    public sanPhamMoiAdapter(Context context, List<sanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          sanPhamMoi spMoi = array.get(position);
          holder.txtten.setText(spMoi.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
          holder.txtgia.setText("Gia: " + decimalFormat.format(Double.parseDouble(spMoi.getGiasp()))+"D");
        Glide.with(context).load(spMoi.getHinhanh()).into(holder.imghinhanh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongCLick) {
                if(!isLongCLick){
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", spMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtgia, txtten;
        ImageView imghinhanh;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgia = itemView.findViewById(R.id.txtgia);
            txtten = itemView.findViewById(R.id.txtten);
            imghinhanh = itemView.findViewById(R.id.item_sp_img);
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
