package huyvqph39239.fpoly.appbanhang.retrofit;
import huyvqph39239.fpoly.appbanhang.model.LoaiSpmodel;


import huyvqph39239.fpoly.appbanhang.model.UserModel;
import huyvqph39239.fpoly.appbanhang.model.sanPhamMoiModel;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Apibanhang {
    @GET("getloaisp.php")
    Observable<LoaiSpmodel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<sanPhamMoiModel> getSpMoi();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<sanPhamMoiModel> getSanpham(
      @Field("page") int page,
      @Field("loai") int loai
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangKi(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass

    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createDer (
            @Field("email") String email,
            @Field("mobile") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
}
