package com.example.androidmvvmtest.network.Interface;

import com.example.androidmvvmtest.bean.AudioDetailsResponse;
import com.example.androidmvvmtest.bean.BaseResponse;
import com.example.androidmvvmtest.bean.FavoriteResponse;
import com.example.androidmvvmtest.bean.ThumbResponse;
import com.example.androidmvvmtest.bean.VideoListBean;
import com.example.androidmvvmtest.network.bean.response.ChatRecordsResponse;
import com.example.androidmvvmtest.network.bean.response.LoginResponse;
import com.example.androidmvvmtest.network.bean.response.QuestionResponse;
import com.example.androidmvvmtest.network.bean.response.Result;
import com.example.androidmvvmtest.network.bean.response.Token;
import com.example.androidmvvmtest.network.bean.response.VoiceToTextResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    @POST("api/user/login")
    Observable<LoginResponse> login(@Body RequestBody requestBody);


    @POST("api/user/sendCode")
    Observable<Result<Token>> sendCode(@Body RequestBody requestBody);

    @POST("api/qa/voice2words")
    Observable<VoiceToTextResponse> voiceToText(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );


    @POST("api/qa/chat")
    Observable<QuestionResponse> postQuestion(@Part("question") RequestBody question);

    @GET("api/qa/chat_history_list")
    Observable<ChatRecordsResponse> getChatRecords(
            @Header("Authorization") String token,
            @Query("current") int current,
            @Query("size") int size
    );

    @GET("api/audio/add/play/amount")
    Observable<Void> incrementPlayCount(@Header("Authorization") String authToken, @Query("audioId") int audioId);

    @POST("api//audio/list")
    Observable<BaseResponse<VideoListBean>> getVideList(@Header("Authorization") String token, @Body RequestBody body);

    @POST("api/audio/thumb")
    Observable<ThumbResponse> likeAudio(@Header("Authorization") String authToken, @Query("audioId") int audioId);

    @POST("api/audio/favorite")
    Observable<FavoriteResponse> toggleFavorite(@Header("Authorization") String authToken, @Query("audioId") int audioId);


    @GET("api/audio/one")
    Observable<AudioDetailsResponse> getAudioDetails(@Query("audioId") int audioId, @Header("Authorization") String authToken);


    @POST("audio/xiaobao")
    Observable<AudioDetailsResponse> uploadAudio(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part file
    );

}
