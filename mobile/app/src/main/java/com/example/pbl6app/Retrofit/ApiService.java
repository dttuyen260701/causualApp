package com.example.pbl6app.Retrofit;

import com.example.pbl6app.Models.AddressTemp;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.Models.User;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.HTTPMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    Gson gson = new GsonBuilder().
            setDateFormat("yyyy-MM-dd HH:mm:ss").
            create();

    ApiService apiService = new Retrofit.Builder().
            baseUrl(Constant.BASE_URL).
            addConverterFactory(GsonConverterFactory.create(gson)).
            build().
            create(ApiService.class);
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @POST("/api/app/account/login")
    Call<ResponseRetrofit<User>> login(@Body Map<String, String> options);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @POST("/api/app/account/register-as-customer")
    Call<ResponseRetrofit<Object>> register(@Body Map<String, String> options);

    @Multipart
//    @PUT("/api/app/customer-info/{id}")
    @HTTP(method = HTTPMethod.PUT, path = "/api/app/customer-info/{Uid}", hasBody = true)
    Call<ResponseRetrofit<User>> updateCustomer(
            @Path("Uid") String id,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("id") RequestBody userId,
            @Part("gender") RequestBody gender,
            @Part("address") RequestBody address,
            @Part("addressPoint") RequestBody addressPoint,
            @Part("dateOfBirth") RequestBody dateOfBirth,
            @Part("provinceId") RequestBody provinceId,
            @Part("provinceName") RequestBody provinceName,
            @Part("districtId") RequestBody districtId,
            @Part("districtName") RequestBody districtName,
            @Part("wardId") RequestBody wardId,
            @Part("wardName") RequestBody wardName,
            @Part MultipartBody.Part avatar
    );

    @GET("/api/app/address/provinces")
    Call<ArrayList<AddressTemp>> getProvince();

    @GET("/api/app/address/{id}/districts")
    Call<ArrayList<AddressTemp>> getDistrict(@Path("id") String id);

    @GET("/api/app/address/{id}/wards")
    Call<ArrayList<AddressTemp>> getWard (@Path("id") String id);

    @GET("/api/app/type-of-job/type-job")
    Call<ResponseRetrofit<ArrayList<TypeOfJob>>> getTypeOfJob();

    @GET("/api/app/worker-info/{id}/worker-vm")
    Call<ResponseRetrofit<ArrayList<WorkerDetail>>> getListWorkerByIDUser(@Path("id") String id);

    @GET("/api/app/worker-info/worker-by-type-of-job/")
    Call<ResponseRetrofit<ArrayList<WorkerDetail>>> getListWorkerByIDTypeOfJob(@Query("idUser") String idUser,
                                                                               @Query("idJobInfo") String idJobInfo);

    @GET("/api/app/job-info/{id}/job-info-vm-belong-to-type-of-job")
    Call<ResponseRetrofit<ArrayList<JobInfo>>> getListJobInfo(@Path("id") String idTypeOfJob);

    @GET("/api/app/post-of-demand/{id}")
    Call<ResponseRetrofit<ArrayList<PostOfDemand>>> getListPostOfDemand(@Path("id") String idUser);
//    @POST("matrix/insertMatrix.php")
//    Call<ResponseRetrofit<>> insert_Matrix(@Body );

//    @Headers({
//            "Accept: application/json",
//            "Content-Type: application/json; charset=utf-8"
//    })
////    @GET("matrix/getAll.php")
//    @HTTP(method = "GET", path = "matrix/getAll.php")
//    Call<Respone_Retrofit<ArrayList<Matrix>>> getData_Options(@QueryMap Map<String, String> options);
//
//    @GET("feature/getAll.php")
//    Call<Respone_Retrofit> getData(@Query("page") int page,
//                                   @Query("step") int step,
//                                   @Query("search_txt") String  search_txt);
//
//    //feature/getAll.php/1/USER
//    @GET("feature/getAll.php/{id}/USER")// 1 dấu "/" nếu đường dẫn k cso path
//    Call<Respone_Retrofit> getData_Path(@Path("id") int id);
//
//    //feature/getAll.php/1/USER?text= ???
//    @GET("feature/getAll.php/{id}/USER")
//    Call<Respone_Retrofit> getData_Path_Params(@Path("id") int id,
//                                               @Query("search_txt") String  search_txt);
//
//    @POST("matrix/insertMatrix.php")
//    Call<Respone_Retrofit<Retrofit_Respone_Post<Integer>>> insert_Matrix(@Body Matrix matrix);
//
//    @PUT("matrix/updateMatrix.php")
//    Call<Respone_Retrofit<Retrofit_Respone_Post<Boolean>>> update(@Body Matrix matrix);
//
//    @HTTP(method = temp, path = "matrix/deleteMatrix.php", hasBody = true)
//    Call<Respone_Retrofit<Retrofit_Respone_Post<Boolean>>> delete(@Body Map<String, String> options);
}
