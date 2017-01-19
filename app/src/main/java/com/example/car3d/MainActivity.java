package com.example.car3d;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.car3d.photoview.PhotoViewAttacher;
import com.socks.library.KLog;

import java.io.IOException;

/**
 * @author zhaokaiqiang
 * @ClassName: com.example.car3d.MainActivity
 * @Description: 3D汽车模型
 * @date 2014-10-31 上午8:51:59
 */
public class MainActivity extends Activity {

    protected static final String TAG = "MainActivity";
    // 当前显示的bitmap对象
    private static Bitmap bitmap;
    // 图片容器
    private ImageView imageView;
    // 开始按下位置
    private int startX;
    private int startY;
    private int offsetX;
    private int offsetY;
    // 当前位置
    private int currentX;
    // 当前位置
    private int currentY;
    // 当前图片的编号
    private int scrNumX;
    private int scrNumY;
    // 图片的总数
    private static int maxNum = 71;
    private String url;
    private int size = 1;
    private int DIRECTION;
    private PhotoViewAttacher mAttacher;
    private Button btn_add, btn_sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = "http://192.168.2.24:8333/pansongbei/" + size + "_";

        imageView = (ImageView) findViewById(R.id.imageView);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        // 初始化当前显示图片编号
        scrNumX = 300;
        scrNumY = 80;
        imageView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        currentX = (int) event.getX();
                        currentY = (int) event.getY();
                        offsetX = Math.abs(currentX - startX);
                        offsetY = Math.abs(currentY - startY);

                        if (offsetX >= offsetY) {
                            // 判断手势滑动方向，并切换图片
                            if (currentX - startX > 15) {
                                modifySrcR();
                            } else if (currentX - startX < -15) {
                                modifySrcL();
                            }
                            // 重置起始位置
                            startX = (int) event.getX();
                            startY = (int) event.getY();
                        } else {
                            // 判断手势滑动方向，并切换图片
                            if (currentY - startY > 15) {
                                modifySrcT();
                            } else if (currentY - startY < -15) {
                                modifySrcB();
                            }
                            // 重置起始位置
                            startY = (int) event.getY();
                            startX = (int) event.getX();
                        }
                        break;
                }
                return true;
            }

        });
    }

    // 向右滑动修改资源
    private void modifySrcR() {
        KLog.e("right");
        DIRECTION = 3;
        if (scrNumX > 355) {
            scrNumX = 5;
        }

        if (scrNumX > 0) {
            makeUrlAndLoad();
            scrNumX = scrNumX + 5;
        }
    }

    // 向左滑动修改资源
    private void modifySrcL() {
        KLog.e("left");
        DIRECTION = 1;
        if (scrNumX <= 0) {
            scrNumX = maxNum * 5;
        }

        if (scrNumX <= 355) {
            makeUrlAndLoad();
            scrNumX = scrNumX - 5;
        }
    }

    // 向右滑动修改资源
    private void modifySrcT() {
        KLog.e("top");
        DIRECTION = 2;
        if (scrNumY > 355) {
            scrNumY = 5;
        }

        if (scrNumY > 0) {
            makeUrlAndLoad();
            scrNumY = scrNumY + 5;
        }
    }

    // 向左滑动修改资源
    private void modifySrcB() {
        KLog.e("bottom");
        DIRECTION = 4;
        if (scrNumY <= 0) {
            scrNumY = maxNum * 5;
        }

        if (scrNumY <= 355) {
            makeUrlAndLoad();
            scrNumY = scrNumY - 5;
        }
    }

    private void makeUrlAndLoad() {
        final String url_request = url + scrNumY + "_" + scrNumX + ".jpg";
        KLog.e(url_request);

        //同步加载一张图片, 注意只能在子线程中调用并且Bitmap不会被缓存到内存里.
        new Thread() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = YWDImage.Create(getApplicationContext(), url_request).get();

//                    final Bitmap bitmap = Picasso.with(getApplicationContext()).load(url_request).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            YWDImage.singleton.cancelTag("1");
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
//        Picasso.with(this).load(url_request).into(imageView);
    }

    private void add() {
        if (size <= 1) {
            size++;
            url = "http://192.168.2.24:8333/pansongbei/" + size + "_";
            makeUrlAndLoad();
        } else {
            Toast.makeText(getApplicationContext(), "已经最大了", Toast.LENGTH_SHORT).show();
        }
    }

    private void sub() {
        if (size >= 1) {
            size--;
            url = "http://192.168.2.24:8333/pansongbei/" + size + "_";
            makeUrlAndLoad();
        } else {
            Toast.makeText(getApplicationContext(), "已经最小了", Toast.LENGTH_SHORT).show();
        }
    }
}
