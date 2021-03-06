package toutiao.fake.com.faketoutiao.mvp.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import toutiao.fake.com.faketoutiao.mvp.contract.MicroContract;
import toutiao.fake.com.faketoutiao.mvp.model.Bean.MicroContentBean;
import toutiao.fake.com.faketoutiao.mvp.model.Bean.MicroHotBean;
import toutiao.fake.com.faketoutiao.mvp.model.MicroModel;
import toutiao.fake.com.faketoutiao.utils.Constants;

/**
 * Created by lihaitao on 2018/6/13.
 */
public class MicroPresenter implements MicroContract.IPresenter {
    private MicroContract.IView mView;
    private final MicroModel mMicroModel;
    private final String defaul_imaurl[] = {"https://timgsa.baidu" +
        ".com/timg?image&quality=80&size=b9999_10000&sec=1528894844079&di=2acb2ea382dbc7358ef2acbcbcb1c0e4&imgtype=0" +
        "&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0b55b319ebc4b745a19d9333c5fc1e178a82158d.jpg"
        , "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1528884759" +
        "&di=dca9614f16e38f45d997b4c4470d3193&src=http://imgsrc.baidu" +
        ".com/imgad/pic/item/bf096b63f6246b60553a62a0e1f81a4c510fa22a.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528894844081&di" +
            "=904437f467ba707d66d96e4c712bba0f&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu" +
            ".com%2Fimgad%2Fpic%2Fitem%2Ff703738da97739122ae8d547f2198618377ae2ce.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528894844079&di" +
            "=81bf8085faaae0385239b72f3c414343&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu" +
            ".com%2Fimgad%2Fpic%2Fitem%2F2cf5e0fe9925bc3165d663bc54df8db1cb13700e.jpg"};

    public MicroPresenter(MicroContract.IView view) {
        mView = view;
        mMicroModel = new MicroModel();
    }

    @Override
    public void loadContentData() {
        //假数据
        List<MicroContentBean> mLists = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            MicroContentBean bean = new MicroContentBean();
            bean.title = makeRandomData(Constants.title, random);
            bean.label = makeRandomData(Constants.label, random);
            bean.title_pic_url = makeRandomData(Constants.title_pic_url, random);
            bean.isV = makeRandomData(Constants.isV, random);
            bean.alais = makeRandomData(Constants.alias, random);
            bean.content = makeRandomData(Constants.content_text, random);
            List<String> mlists = new ArrayList<>();
            int i1 = random.nextInt(9) ;
            for (int j = 0; j < i1; j++) {
                mlists.add(makeRandomData(Constants.content_pic_url, random));
            }
            bean.content_pic_url = mlists;
            mLists.add(bean);
        }
        mView.setContentData(mLists);

    }

    private String makeRandomData(String[] strings, Random random) {
        return strings[random.nextInt(strings.length)];
    }

    @Override
    public void loadHotData() {
        //假数据
        List<MicroHotBean> list = new ArrayList<>();
        MicroHotBean bean1 = new MicroHotBean();
        bean1.title = "棕情端午";
        bean1.des = "发文赢奖";
        bean1.ima_utl = defaul_imaurl[0];

        MicroHotBean bean2 = new MicroHotBean();
        bean2.title = "失联女护士已遇害";
        bean2.des = "嫌犯被刑拘";
        bean2.ima_utl = defaul_imaurl[2];

        MicroHotBean bean3 = new MicroHotBean();
        bean3.title = "电竞国家队名单公布";
        bean3.des = "UZI老帅领衔";
        bean3.ima_utl = defaul_imaurl[3];

        MicroHotBean bean4 = new MicroHotBean();
        bean4.title = "厉害了我的国";
        bean4.des = "超燃造句大赛";
        bean4.ima_utl = defaul_imaurl[1];
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        mView.setHotData(list);
    }
}
