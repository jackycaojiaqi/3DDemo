package com.example.car3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.socks.library.KLog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.RequestHandler;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伟忠 on 2015-7-16.
 */
public class YWDImage {
    public static File CacheDir = new File(FileUtils.offlinedata);

    // public static RequestCreator Create(Context context, Uri uri){
    // RequestCreator r = Picasso.with(context)
    // .load(uri)
    // .placeholder(R.drawable.img_loading)
    // .error(R.drawable.no_pic)
    // .fit()
    // .centerCrop();
    // return r;
    // }
    static volatile Picasso singleton = null;
    static Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public static RequestCreator Create(Context context, Uri uri, boolean is) {

        if (singleton == null) {
            // Class var1 = Picasso.class;
            synchronized (Picasso.class) {
                if (singleton == null) {
                    Picasso.Builder builder = new Picasso.Builder(context);

                    builder.addRequestHandler(new RequestHandler() {
                        @Override
                        public boolean canHandleRequest(Request data) {
                            if (data.uri.getScheme().equals("http")
                                    ) {
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public Result load(Request request, int networkPolicy)
                                throws IOException {
                            String file_path = (CacheDir + request.uri
                                    .getPath()).replaceAll("image/", "");
//
                            File local_file = new File(file_path);

                            if (local_file.exists() == false) {
                                KLog.e("download:" + request.uri.toString());
                                if (!local_file.getParentFile().exists()) {
                                    local_file.getParentFile().mkdirs();
                                }

                                FileOutputStream out = null;
                                try {

                                    URL url = new URL(request.uri.toString());
                                    InputStream is = url.openConnection()
                                            .getInputStream();

                                    out = new FileOutputStream(local_file);
                                    byte buffer[] = new byte[4 * 1024];
                                    int len = 0;
                                    while ((len = is.read(buffer)) != -1) {
                                        out.write(buffer, 0, len);
                                    }
                                    is.close();
                                    out.flush();
                                    out.close();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (local_file.exists() == true) {
                                KLog.e(local_file.getAbsolutePath());
                                return new Result(new FileInputStream(
                                        local_file), Picasso.LoadedFrom.DISK);
                            }
                            return null;
                        }
                    });
                    singleton = builder.build();
                }
            }
        }
        RequestCreator r = null;

        if (is) {
            r = singleton.load(uri).noFade().tag("1");
        } else {
            r = singleton.load(uri).noFade().tag("1");
        }
        return r;
    }


    public static RequestCreator Create(Context context, Uri uri, boolean is, String type) {

        if (singleton == null) {
            // Class var1 = Picasso.class;
            synchronized (Picasso.class) {
                if (singleton == null) {
                    Picasso.Builder builder = new Picasso.Builder(context);
                    builder.addRequestHandler(new RequestHandler() {
                        @Override
                        public boolean canHandleRequest(Request data) {
                            if (!StringUtil.isEmptyandnull(data.uri.getScheme())) {
                                if (data.uri.getScheme().equals("http")
                                        && data.uri.getPath().startsWith("/image/")) {
                                    return true;
                                }
                            }
                            return false;
                        }

                        @Override
                        public Result load(Request request, int networkPolicy)
                                throws IOException {
                            String file_path = (CacheDir + request.uri
                                    .getPath()).replaceAll("image/", "");
//
                            File local_file = new File(file_path);

                            if (local_file.exists() == false) {
                                Log.i("ywdimage",
                                        "download:" + request.uri.toString());

                                if (!local_file.getParentFile().exists()) {
                                    local_file.getParentFile().mkdirs();
                                }

                                FileOutputStream out = null;
                                try {

                                    URL url = new URL(request.uri.toString());
                                    InputStream is = url.openConnection()
                                            .getInputStream();

                                    out = new FileOutputStream(local_file);
                                    byte buffer[] = new byte[4 * 1024];
                                    int len = 0;
                                    while ((len = is.read(buffer)) != -1) {
                                        out.write(buffer, 0, len);
                                    }
                                    is.close();
                                    out.flush();
                                    out.close();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (local_file.exists() == true) {
                                return new Result(new FileInputStream(
                                        local_file), Picasso.LoadedFrom.DISK);
                            }
                            return null;
                        }
                    });
                    singleton = builder.build();
                }
            }
        }
        RequestCreator r = null;
        if (type.equals("home")) {
            r = singleton.load(uri);
        } else if (type.equals("around")) {
            r = singleton.load(uri);
        } else {
            singleton.load(uri);
        }
        return r;
    }

    public static RequestCreator Create(Context context, String path,
                                        int width, int height, int retina, int quality, boolean is) {
//        String s = "";
//        if (width != 0) {
//            s += "_" + width + "w";
//        }
//        if (height != 0) {
//            s += "_" + height + "h";
//        }
//        if (retina > 1 && retina < 4) {
//            s += "_" + retina + "x";
//        }
//        if (quality > 20 && quality < 100) {
//            s += "_" + quality + "q";
//        } else if (quality == 0) {
//            s += "_30q";
//        } else {
//            s += "";
//        }
//        if (s.startsWith("_")) {
//            s = s.substring(1);
//        }
//        if (s.equals("") == false) {
//            if (path != null) {
//                if (path.length() > 0) {
//                    path = path + "@" + s + ".jpg";
//                } else {
//                    path = "http://www.test.youwandao.com/image/20140917/0937405418e5e4cf01b1.jpg@"
//                            + s + ".jpg";
//                }
//            } else {
//                path = "http://www.test.youwandao.com/image/20140917/0937405418e5e4cf01b1.jpg@"
//                        + s + ".jpg";
//            }
//        }
        return Create(context, Uri.parse(path), is);
    }

    public static RequestCreator Create(Context context, String path,
                                        int width, int height, int retina, int quality, boolean is, String type) {

        String s = "";
        if (width != 0) {
            s += "_" + width + "w";
        }
        if (height != 0) {
            s += "_" + height + "h";
        }
        if (retina > 1 && retina < 4) {
            s += "_" + retina + "x";
        }
        if (quality > 20 && quality < 100) {
            s += "_" + quality + "q";
        } else if (quality == 0) {
            s += "_30q";
        } else {
            s += "";
        }
        if (s.startsWith("_")) {
            s = s.substring(1);
        }
        if (s.equals("") == false) {
            if (path != null) {
                if (path.length() > 0) {
                    path = path + "@" + s + ".jpg";
                } else {
                    path = "http://www.test.youwandao.com/image/20140917/0937405418e5e4cf01b1.jpg@"
                            + s + ".jpg";
                }
            } else {
                path = "http://www.test.youwandao.com/image/20140917/0937405418e5e4cf01b1.jpg@"
                        + s + ".jpg";
            }
        }
        return Create(context, Uri.parse(path), is, type);
    }

    public static RequestCreator Create(Context context, String path) {
        return Create(context, path, 0, 0, 0, 0, false);
    }

    public static RequestCreator Create(Context context, String path, int width) {
        return Create(context, path, width, 0, 0, 0, false);
    }

    public static RequestCreator Create(Context context, String path,
                                        int height, String str) {
        return Create(context, path, 0, height, 0, 0, true);
    }

    public static RequestCreator Create(Context context, String path,
                                        int width, int height) {
        return Create(context, path, width, height, 0, 0, false);
    }

    public static RequestCreator Create(Context context, String path,
                                        int width, int height, int retina) {
        return Create(context, path, width, height, retina, 0, false);
    }

    public static RequestCreator Create(Context context, File file) {
        return Create(context, Uri.fromFile(file), false);
    }

    public static RequestCreator Create(Context context, int resourceId) {
        RequestCreator r = Picasso.with(context).load(resourceId)
                .fit().centerCrop();
        return r;
    }

    private List<List<Integer>> addlistToList(List<Integer>... list) {

        List<List<Integer>> lists = new ArrayList<List<Integer>>();
        {
            for (List<Integer> list1 : list) {
                lists.add(list1);
                return lists;
            }
        }
        return lists;
    }
}
