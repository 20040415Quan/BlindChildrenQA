package com.example.androidmvvmtest.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.bean.BaseResponse;
import com.example.androidmvvmtest.bean.VideoListBean;
import com.example.androidmvvmtest.manager.MusicManager;
import com.example.androidmvvmtest.network.api.NetworkApi;
import com.example.androidmvvmtest.network.observer.BaseObserver;
import com.example.androidmvvmtest.repository.GlobalRepository;
import com.example.androidmvvmtest.ui.adapter.MusicAllListAdapter;
import com.example.androidmvvmtest.utils.KLog;
import com.example.androidmvvmtest.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment {


    private int position;
    private RecyclerView rec;
    private MusicAllListAdapter adapter;
    private MusicManager.PlayListener playListener = new MusicManager.PlayListener() {
        @Override
        public void itemChange(int last, int now) {
            if (rec!=null&&rec.getAdapter()!=null){
                if (adapter!=null){
                    adapter.notifyPlaying(now,last);
                }
            }
        }

        @Override
        public void playChange(int id) {

        }
    };
    private MusicManager musicManager = MusicManager.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position", -1);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        initView(view);
        return view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        if (position==-1||GlobalRepository.videoList==null||GlobalRepository.videoList.data==null
            ||GlobalRepository.videoList.data.size()<position*4){
            KLog.i("TAGG","MusicFragment为空"+position);


            //再请求一次数据
            GlobalRepository.getVideoList(0,16).compose(
                    NetworkApi.applySchedulers(new BaseObserver<BaseResponse<VideoListBean>>() {
                        @Override
                        public void onSuccess(BaseResponse<VideoListBean> videoListBeanBaseResponse) {
                            if (videoListBeanBaseResponse!=null&&videoListBeanBaseResponse.code==200){
                                //保存到仓库
                                GlobalRepository.videoList = videoListBeanBaseResponse.data;
                                KLog.i("TAGG",videoListBeanBaseResponse.data.data.toString());

                                //初始化界面
                                int start = 4 * (position - 1);
                                int end = start + 4;

                                List<VideoListBean.Data> data = GlobalRepository.videoList.data.subList(start, end);

                                KLog.i("TAGG","MusicFragment不为空"+position);

                                initRec(data);

                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            e.printStackTrace();
                            KLog.i("再次获取失败error:"+e.toString());
                        }
                    })
            );
        }else {
            int start = 4 * (position - 1);
            int end = start + 4;

            List<VideoListBean.Data> data = GlobalRepository.videoList.data.subList(start, end);

            KLog.i("TAGG","MusicFragment不为空"+position);

            for (VideoListBean.Data bean:data) {
                KLog.i("TAGG",bean.aid+"");
            }

            initRec(data);
        }
    }

    private void initRec(List<VideoListBean.Data> data){

        adapter = new MusicAllListAdapter(R.layout.item_layout,data);
        rec.setAdapter(adapter);
        rec.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                musicManager.notifyPlay(data.get(position).aid-1);
            }
        });
        musicManager.setPlayListener(playListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicManager.removePlayListener(playListener);
    }

    public static MusicFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View view) {
        rec = (RecyclerView) view.findViewById(R.id.rec);
    }
}
