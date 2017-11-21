package com.yuyh.library.imgsel;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.yuyh.library.imgsel.adapter.FolderListAdapter;
import com.yuyh.library.imgsel.adapter.ImageListAdapter;
import com.yuyh.library.imgsel.adapter.PreviewAdapter;
import com.yuyh.library.imgsel.bean.Folder;
import com.yuyh.library.imgsel.bean.Image;
import com.yuyh.library.imgsel.common.Callback;
import com.yuyh.library.imgsel.common.Constant;
import com.yuyh.library.imgsel.common.OnFolderChangeListener;
import com.yuyh.library.imgsel.common.OnItemClickListener;
import com.yuyh.library.imgsel.utils.FileUtils;
import com.yuyh.library.imgsel.utils.LogUtils;
import com.yuyh.library.imgsel.widget.CustomViewPager;
import com.yuyh.library.imgsel.widget.DividerGridItemDecoration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ImgSelFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private RecyclerView rvImageList;
    private Button btnAlbumSelected;
    private View rlBottom;
    private CustomViewPager viewPager;

    private ImgSelConfig config;
    private Callback callback;
    private List<Folder> folderList = new ArrayList<>();
    private List<Image> imageList = new ArrayList<>();

    private ListPopupWindow folderPopupWindow;
    private ImageListAdapter imageListAdapter;
    private FolderListAdapter folderListAdapter;
    private PreviewAdapter previewAdapter;

    private boolean hasFolderGened = false;
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private static final int REQUEST_CAMERA = 5;

    private File tempFile;

    public static ImgSelFragment instance() {
        ImgSelFragment fragment = new ImgSelFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img_sel, container, false);
        rvImageList = (RecyclerView) view.findViewById(R.id.rvImageList);
        btnAlbumSelected = (Button) view.findViewById(R.id.btnAlbumSelected);
        btnAlbumSelected.setOnClickListener(this);
        rlBottom = view.findViewById(R.id.rlBottom);
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        config = Constant.config;
        try {
            callback = (Callback) getActivity();
        } catch (Exception e) {

        }

        btnAlbumSelected.setText(config.allImagesText);

        rvImageList.setLayoutManager(new GridLayoutManager(rvImageList.getContext(), 3));
        rvImageList.addItemDecoration(new DividerGridItemDecoration(rvImageList.getContext()));
        if (config.needCamera)
            imageList.add(new Image());

        imageListAdapter = new ImageListAdapter(getActivity(), imageList, config);
        imageListAdapter.setShowCamera(config.needCamera);
        imageListAdapter.setMutiSelect(config.multiSelect);
        rvImageList.setAdapter(imageListAdapter);
        imageListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public int onCheckedClick(int position, Image image) {
                return checkedImage(position, image);
            }

            @Override
            public void onImageClick(int position, Image image) {
                if (config.needCamera && position == 0) {
                    showCameraAction();
                } else {
                    if (config.multiSelect) {
                        viewPager.setAdapter((previewAdapter = new PreviewAdapter(getActivity(), imageList, config)));
                        previewAdapter.setListener(new OnItemClickListener() {
                            @Override
                            public int onCheckedClick(int position, Image image) {
                                return checkedImage(position, image);
                            }

                            @Override
                            public void onImageClick(int position, Image image) {
                                hidePreview();
                            }
                        });
                        if (config.needCamera) {
                            callback.onPreviewChanged(position, imageList.size() - 1, true);
                        } else {
                            callback.onPreviewChanged(position + 1, imageList.size(), true);
                        }
                        viewPager.setCurrentItem(config.needCamera ? position - 1 : position);
                        viewPager.setVisibility(View.VISIBLE);
                    } else {
                        if (callback != null) {
                            callback.onSingleImageSelected(image.path);
                        }
                    }
                }
            }
        });

        folderListAdapter = new FolderListAdapter(getActivity(), folderList, config);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private int checkedImage(int position, Image image) {
        if (image != null) {
            if (Constant.imageList.contains(image.path)) {
                Constant.imageList.remove(image.path);
                if (callback != null) {
                    callback.onImageUnselected(image.path);
                }
            } else {
                if (config.maxNum <= Constant.imageList.size()) {
                    Toast.makeText(getActivity(), String.format(getString(R.string.maxnum), config.maxNum), Toast.LENGTH_SHORT).show();
                    return 0;
                }

                Constant.imageList.add(image.path);
                if (callback != null) {
                    callback.onImageSelected(image.path);
                }
            }
            return 1;
        }
        return 0;
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    List<Image> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        if (!image.path.endsWith("gif"))
                            tempImageList.add(image);
                        if (!hasFolderGened) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!folderList.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                folderList.add(folder);
                            } else {
                                Folder f = folderList.get(folderList.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(tempImageList);


                    imageListAdapter.notifyDataSetChanged();

                    if (Constant.imageList != null && Constant.imageList.size() > 0) {
                        //imageListAdapter.setDefaultSelected(Constant.imageList);
                    }

                    folderListAdapter.notifyDataSetChanged();

                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void createPopupFolderList(int width, int height) {
        folderPopupWindow = new ListPopupWindow(getActivity());
        folderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#aaaaaa")));
        folderPopupWindow.setAdapter(folderListAdapter);
        folderPopupWindow.setContentWidth(width);
        folderPopupWindow.setWidth(width);
        folderPopupWindow.setHeight(height);
        folderPopupWindow.setAnchorView(rlBottom);
        folderPopupWindow.setModal(true);
        folderListAdapter.setOnFloderChangeListener(new OnFolderChangeListener() {
            @Override
            public void onChange(int position, Folder folder) {
                folderPopupWindow.dismiss();
                if (position == 0) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    btnAlbumSelected.setText(config.allImagesText);
                } else {
                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(folder.images);
                    imageListAdapter.notifyDataSetChanged();

                    btnAlbumSelected.setText(folder.name);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAlbumSelected.getId()) {
            if (folderPopupWindow == null) {
                WindowManager wm = getActivity().getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                createPopupFolderList(width / 3 * 2, width / 3 * 2);
            }

            if (folderPopupWindow.isShowing()) {
                folderPopupWindow.dismiss();
            } else {
                folderPopupWindow.show();
                if (folderPopupWindow.getListView() != null) {
                    folderPopupWindow.getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.bottom_bg)));
                }
                int index = folderListAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                folderPopupWindow.getListView().setSelection(index);
            }
        }
    }

    private void showCameraAction() {
        if (config.maxNum <= Constant.imageList.size()) {
            Toast.makeText(getActivity(), String.format(getString(R.string.maxnum), config.maxNum), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            createFiels();
//            tempFile = new File(FileUtils.createRootPath(getActivity()) + "/" + System.currentTimeMillis() + ".jpg");
//            LogUtils.e(tempFile.getAbsolutePath());
//            FileUtils.createFile(tempFile);
            openCamera(cameraIntent);

        } else {
            Toast.makeText(getActivity(), getString(R.string.open_camera_failure), Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera(Intent cameraIntent) {
        File file1 = new File(cacheImg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            imageUri = FileProvider.getUriForFile(getActivity(), "com.ysj.fileprovider", file1);//通过FileProvider创建一个content类型的Uri
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    //    private File file1;
//    private File file;//拍照图片
    private String cacheImg;
    private Uri imageUri;//拍照保存图片路径

    private void createFiels() {
        String name = "image.jpg";
        //文件夹路径
        String ImgFile = Environment.getExternalStorageDirectory() + File.separator + getActivity().getPackageName() + File.separator + "Cache";//默认路径
//        String ImgFile = Environment.getExternalStorageDirectory() + File.separator + getActivity().getPackageName() + File.separator + "DCIM";

        File folderAddr = new File(ImgFile);
        if (!folderAddr.exists() || !folderAddr.isDirectory()) {
            folderAddr.mkdirs();
        }
        //图片路径
        cacheImg = ImgFile + File.separator + name;
        tempFile = new File(cacheImg);
        imageUri = Uri.fromFile(tempFile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempFile != null) {
                   // Log.i("test","tempFile:"+tempFile.getAbsolutePath());
                    if (callback != null) {
                        Bitmap smallBitmap = getSmallBitmap(tempFile.getAbsolutePath(), 720, 1080);
                        //  Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        String format = sdf.format(date);
                        Bitmap bitmap1 = createBitmap(smallBitmap, format, getActivity());
                        String path = saveFile(bitmap1);

                        File file = new File(path);
                        Log.i("test", "file=" + file);
                        callback.onCameraShot(file);
                    }
                }
            } else {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath  图片的路径
     * @param reqWidth  要求的图片的像素
     * @param reqHeight 要求的图片的像素
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        Log.i("test", "imgsiz:" + options.inSampleSize);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;

        final int width = options.outWidth;
        Log.i("test", "w:" + width + "**he:" + height);
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public  String saveFile(Bitmap pBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile",
                    "SD card is not avaiable/writeable right now.");
            return null;
        }
        String filePathName = Environment.getExternalStorageDirectory() + File.separator + getActivity().getPackageName() + File.separator + "DCIM";
//        File file = new File("/storage/emulated/0/ImgSelete/");
        File file = new File(filePathName);
        if (!file.exists()) {
            file.mkdirs();

        }
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd-hh-mm-ss");
//        String filePathName = file.getAbsolutePath() + "/" + sdf.format(new Date(System
//                .currentTimeMillis())) + ".jpg";
        String bitmapPath = filePathName + File.separator + sdf.format(new Date(System
                .currentTimeMillis())) + ".jpg";
        Log.i("test", "saveFile: " + bitmapPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bitmapPath);
            pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            return bitmapPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static TextPaint textPaint;
    private static String dx;
    private static String dy;

    public static Bitmap createBitmap(Bitmap src, String title3, Context context) {
        if (src == null) {
            return src;
        }
        // Bitmap bitmap = zoomImg(src, src.getWidth(), src.getHeight());
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        // 创建一个新的位图
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Log.i("Watertitle", "w=" + w + "\nh=" + h);
        Canvas mCanvas = new Canvas(newBitmap);
        // 在src的右下角添加水印
        // paint.setAlpha(100);
        // 开始加入文字
        if (null != title3) {
            textPaint = new TextPaint(Paint.DITHER_FLAG);
            // 往位图中开始画入src原始图片
            mCanvas.drawBitmap(src, 0, 0, null);
            float scale = context.getResources().getDisplayMetrics().density;
            Log.i("logl", "" + scale);
            textPaint.setColor(0xFCFF0000);
            textPaint.setTextSize((int) (h * 0.04 + 0.5f));
            // textPaint.setTextSize((int) (h *30*scale + 0.5f));
            String familyName = "宋体";
            Typeface typeface = Typeface.create(familyName,
                    Typeface.BOLD_ITALIC);
            textPaint.setTypeface(typeface);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            mCanvas.drawText(title3, (float) (w / 1.5), (float) ((h / 1.2) + h
                    * 0.1 + 0.5f), textPaint);
            // textPaint);
            // Float fDensity = (float) 0.7;
            // 文字自动换行，根据布局的宽度
            // StaticLayout layout = new StaticLayout(title, textPaint,
            // (int)(w*fDensity),
            // Alignment.ALIGN_NORMAL, 1.0F, 0, false);
            // CharSequence text = layout.getText();
            // System.out
            // .println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
            // + text);
            // if (h > 918) {
            // dx = String.valueOf(Math.round(w / 2.1));
            // dy = String.valueOf(Math.round((h / 1.4)) + 120);
            // } else {
            // dx = String.valueOf(Math.round(w / 2.1));
            // dy = String.valueOf(Math.round(h / 1.3));
            // // mCanvas.translate((float) (w/2.1), (float) (h/1.4));
            // }
            // //设置文字位置
            // mCanvas.translate(Float.parseFloat(dx), Float.parseFloat(dy));

            mCanvas.translate(0, 0);

            // layout.draw(mCanvas);
            // 保存
            mCanvas.save(Canvas.ALL_SAVE_FLAG);
            // 存储
            mCanvas.restore();
        }
        return newBitmap;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (config.needCamera) {
            callback.onPreviewChanged(position + 1, imageList.size() - 1, true);
        } else {
            callback.onPreviewChanged(position + 1, imageList.size(), true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public boolean hidePreview() {
        if (viewPager.getVisibility() == View.VISIBLE) {
            viewPager.setVisibility(View.GONE);
            callback.onPreviewChanged(0, 0, false);
            imageListAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }
}
