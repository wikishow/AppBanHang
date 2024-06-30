package huyvqph39239.fpoly.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import huyvqph39239.fpoly.appbanhang.Interface.ItemImageClick;
import huyvqph39239.fpoly.appbanhang.R;
import huyvqph39239.fpoly.appbanhang.Utils.Utils;
import huyvqph39239.fpoly.appbanhang.model.EventBus.TinhTongEventBus;
import huyvqph39239.fpoly.appbanhang.model.GioHang;

public class GiohangAdapter extends RecyclerView.Adapter<GiohangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> giohangList;

    public GiohangAdapter(Context context, List<GioHang> giohangList) {
        this.context = context;
        this.giohangList = giohangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = giohangList.get(position);
        holder.txtTengiohang.setText(gioHang.getTensp());
        holder.txtSoluonggiohang.setText(String.valueOf(gioHang.getSoLuong()));
        Glide.with(context).load(gioHang.getHinhSp()).into(holder.imggiohang);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText("Gia: " + decimalFormat.format(gioHang.getGiaSP()));
        long gia = gioHang.getSoLuong() * gioHang.getGiaSP();
        holder.txtgia2.setText(decimalFormat.format(gia) + " VND");

        holder.setItemImageClick(new ItemImageClick() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1) { // Giảm số lượng
                    if (giohangList.get(pos).getSoLuong() > 1) {
                        giohangList.get(pos).setSoLuong(giohangList.get(pos).getSoLuong() - 1);
                        updateItem(holder, giohangList.get(pos));
                    } else {
                        showRemoveItemDialog(view, pos);
                    }
                } else if (giatri == 2) { // Tăng số lượng
                    if (giohangList.get(pos).getSoLuong() < 11) {
                        giohangList.get(pos).setSoLuong(giohangList.get(pos).getSoLuong() + 1);
                        updateItem(holder, giohangList.get(pos));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return giohangList.size();
    }

    private void updateItem(MyViewHolder holder, GioHang gioHang) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtSoluonggiohang.setText(String.valueOf(gioHang.getSoLuong()));
        long gia = gioHang.getSoLuong() * gioHang.getGiaSP();
        holder.txtgia2.setText(decimalFormat.format(gia) + " VND");
        EventBus.getDefault().postSticky(new TinhTongEventBus());
    }

    private void showRemoveItemDialog(View view, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Utils.mangGiohang.remove(pos);
                notifyDataSetChanged();
                EventBus.getDefault().postSticky(new TinhTongEventBus());
                Toast.makeText(context, "Bạn đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imggiohang, imgtru, imgcong;
        TextView txtTengiohang, item_giohang_gia, txtSoluonggiohang, txtgia2;
        ItemImageClick itemImageClick;

        public void setItemImageClick(ItemImageClick itemImageClick) {
            this.itemImageClick = itemImageClick;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imggiohang = itemView.findViewById(R.id.item_giohang_img);
            txtTengiohang = itemView.findViewById(R.id.item_giohang_tensp);
            txtgia2 = itemView.findViewById(R.id.item_giohang_giasp2);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            txtSoluonggiohang = itemView.findViewById(R.id.item_giohang_soluog);
            imgcong = itemView.findViewById(R.id.item_gioang_cong);
            imgtru = itemView.findViewById(R.id.item_gioang_tru);
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == imgtru) {
                itemImageClick.onImageClick(view, getAdapterPosition(), 1);
            } else if (view == imgcong) {
                itemImageClick.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}

