package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.zhihuitech.qtwsq.entity.CarBrand;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.path;

/**
 * Created by Administrator on 2016/7/30.
 */
public class AddCarActivity extends Activity {
    private RelativeLayout rlTitleBar;
    private LinearLayout llBack;
    private TextView tvAddDescription;
    private TextView tvChooseBrand;
    private EditText etCarPlateNo;
    private TextView tvRealName;
    private TextView tvAddress;
    private TextView tvTel;

    private TextView tvCarPic;
    private TextView tvDrivingLicensePic;
    private ImageView ivCarPic;
    private ImageView ivDrivingLicensePic;
    private Button btnConfirmAdd;

    private MyApplication myApp;

    private String selectedCarBrandId = "";
    private String carPlateNo = "";
    private File img1File;
    private File img2File;
    private String img1Path = "";
    private String img2Path = "";

    private final int GET_USER_INFO = 0;
    private final int SUBMIT_INFO = 1;

    private final int TAKE_PHOTO_FOR_IMG1 = 11;
    private final int SELECT_PIC_FOR_IMG1 = 12;
    private final int TAKE_PHOTO_FOR_IMG2 = 21;
    private final int SELECT_PIC_FOR_IMG2 = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car);

        myApp = (MyApplication) getApplication();

        findViews();
        addListeners();
        initStatusBar();

        new Thread() {
            @Override
            public void run() {
                String result = DataProvider.getUserInfo(myApp.getUser().getId());
                Message msg = handler.obtainMessage();
                msg.what = GET_USER_INFO;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void addListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCarActivity.this);
                View view = LayoutInflater.from(AddCarActivity.this).inflate(R.layout.add_description_dialog, null);
                Button btnConfirm = (Button)view.findViewById(R.id.btn_confirm_add_description);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        tvChooseBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCarActivity.this, ChooseBrandActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        tvCarPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectedDialog(1);
            }
        });
        ivCarPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, 2);
                showImageSelectedDialog(1);
            }
        });
        tvDrivingLicensePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, 3);
                showImageSelectedDialog(2);
            }
        });
        ivDrivingLicensePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, 3);
                showImageSelectedDialog(2);
            }
        });
        btnConfirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCarBrandId.equals("")) {
                    CustomViewUtil.createToast(AddCarActivity.this, getResources().getString(R.string.please_choose_your_car_brand));
                    return;
                }
                carPlateNo = etCarPlateNo.getText().toString();
                if(carPlateNo.equals("")) {
                    CustomViewUtil.createToast(AddCarActivity.this, getResources().getString(R.string.input_your_plate_number));
                    return;
                } else {
                    Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
                    Matcher matcher = pattern.matcher(carPlateNo);
                    if(!matcher.matches()) {
                        CustomViewUtil.createToast(AddCarActivity.this, getResources().getString(R.string.wrong_format_of_plate_number));
                        return;
                    }
                }
                if(img1Path.equals("") || img2Path.equals("")) {
                    CustomViewUtil.createToast(AddCarActivity.this, getResources().getString(R.string.add_pic_of_driving_license_for_car));
                    return;
                }
                CustomViewUtil.createProgressDialog(AddCarActivity.this, getResources().getString(R.string.submitting_information));
                new Thread() {
                    @Override
                    public void run() {
                        String result = DataProvider.carAdd(myApp.getUser().getId(), selectedCarBrandId, carPlateNo, img1Path, img2Path);
                        Message msg = handler.obtainMessage();
                        msg.what = SUBMIT_INFO;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    private void showImageSelectedDialog(final int index) {
        final CharSequence[] items = { getResources().getString(R.string.choose_from_system), getResources().getString(R.string.take_photo) };
        AlertDialog dlg = new AlertDialog.Builder(AddCarActivity.this)
                .setTitle(getResources().getString(R.string.choose_pic))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 1){
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                            Calendar c = Calendar.getInstance();
                            String fileName = format.format(c.getTime());
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            if(index == 1) {
                                img1File = new File(Environment.getExternalStorageDirectory()
                                        .getAbsolutePath()
                                        + File.separator
                                        + "carImage"
                                        + File.separator + fileName + ".png");
                                if (!img1File.getParentFile().exists()) {
                                    img1File.getParentFile().mkdirs();
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(img1File));
                            } else {
                                img2File = new File(Environment.getExternalStorageDirectory()
                                        .getAbsolutePath()
                                        + File.separator
                                        + "carImage"
                                        + File.separator + fileName + ".png");
                                if (!img2File.getParentFile().exists()) {
                                    img2File.getParentFile().mkdirs();
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(img2File));
                            }
                            startActivityForResult(intent, index == 1 ?  TAKE_PHOTO_FOR_IMG1 : TAKE_PHOTO_FOR_IMG2);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, index == 1 ? SELECT_PIC_FOR_IMG1 : SELECT_PIC_FOR_IMG2);
                        }
                    }
                }).create();
        dlg.show();
    }

    private void findViews() {
        rlTitleBar = (RelativeLayout) findViewById(R.id.rl_titlebar_add_car);
        llBack = (LinearLayout) findViewById(R.id.ll_back_add_car);
        tvAddDescription = (TextView) findViewById(R.id.tv_add_description_add_car);
        tvChooseBrand = (TextView) findViewById(R.id.tv_choose_brand_add_car);
        etCarPlateNo = (EditText) findViewById(R.id.et_car_plate_no_add_car);
        tvRealName = (TextView) findViewById(R.id.tv_realname_add_car);
        tvAddress = (TextView) findViewById(R.id.tv_address_add_car);
        tvTel = (TextView) findViewById(R.id.tv_tel_add_car);
        tvCarPic = (TextView) findViewById(R.id.tv_car_pic_add_car);
        tvDrivingLicensePic = (TextView) findViewById(R.id.tv_driving_license_pic_add_car);
        ivCarPic = (ImageView) findViewById(R.id.iv_car_pic_add_car);
        ivDrivingLicensePic = (ImageView) findViewById(R.id.iv_driving_license_pic_add_car);
        btnConfirmAdd = (Button) findViewById(R.id.btn_confirm_add_add_car);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                CarBrand cb = (CarBrand) data.getSerializableExtra("brand");
                selectedCarBrandId = cb.getId();
                tvChooseBrand.setText(cb.getName());
                break;
            case TAKE_PHOTO_FOR_IMG1:
                Bitmap bmCar = compressBySize(img1File.getAbsolutePath(), 480, 800);
                saveFile(img1File.getAbsolutePath(), bmCar, img1File);
                tvCarPic.setVisibility(View.GONE);
                ivCarPic.setVisibility(View.VISIBLE);
                ivCarPic.setImageBitmap(BitmapFactory.decodeFile(img1File
                        .getAbsolutePath()));
                img1Path = img1File.getAbsolutePath();
                break;
            case SELECT_PIC_FOR_IMG1:
                releaseImageViewResource(ivCarPic);
                String carPicPath = data.getData().getPath();
                if (carPicPath.startsWith("/external/images/media/")) {
                    Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    cursor.moveToFirst();
                    img1Path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    tvCarPic.setVisibility(View.GONE);
                    ivCarPic.setVisibility(View.VISIBLE);
                    ivCarPic.setImageBitmap(compressBySize(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)), 480, 800));
                    cursor.close();
                } else {
                    tvCarPic.setVisibility(View.GONE);
                    ivCarPic.setVisibility(View.VISIBLE);
                    ivCarPic.setImageBitmap(compressBySize(carPicPath, 480, 800));
                    img1Path = carPicPath;
                }
                break;
            case TAKE_PHOTO_FOR_IMG2:
                Bitmap bmDrivingLicense = compressBySize(img2File.getAbsolutePath(), 480, 800);
                saveFile(img2File.getAbsolutePath(), bmDrivingLicense, img2File);
                tvDrivingLicensePic.setVisibility(View.GONE);
                ivDrivingLicensePic.setVisibility(View.VISIBLE);
                ivDrivingLicensePic.setImageBitmap(BitmapFactory.decodeFile(img2File
                        .getAbsolutePath()));
                img2Path = img2File.getAbsolutePath();
                break;
            case SELECT_PIC_FOR_IMG2:
                releaseImageViewResource(ivDrivingLicensePic);
                String drivingLicensePicPath = data.getData().getPath();
                if (drivingLicensePicPath.startsWith("/external/images/media/")) {
                    Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    cursor.moveToFirst();
                    img2Path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    tvDrivingLicensePic.setVisibility(View.GONE);
                    ivDrivingLicensePic.setVisibility(View.VISIBLE);
                    ivDrivingLicensePic.setImageBitmap(compressBySize(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)), 480, 800));
                    cursor.close();
                } else {
                    tvDrivingLicensePic.setVisibility(View.GONE);
                    ivDrivingLicensePic.setVisibility(View.VISIBLE);
                    ivDrivingLicensePic.setImageBitmap(compressBySize(drivingLicensePicPath, 480, 800));
                    img2Path = drivingLicensePicPath;
                }
                break;
        }
    }

    public Bitmap compressBySize(String path, int targetWidth, int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        System.out.println("inSampleSize=" + opts.inSampleSize);
        //设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, opts);
        return bitmap;
    }

    private void saveFile(String fileName, Bitmap bitmap, File imgFile) {
        try {
            File dirFile = new File(fileName);
            //检测图片是否存在
            if(dirFile.exists()){
                dirFile.delete();  //删除原图片
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imgFile));
            //100表示不进行压缩，80表示压缩率为20%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void releaseImageViewResource(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            // 设置状态栏颜色
            tintManager.setTintColor(getResources().getColor(R.color.title_bar_bg));
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            rlTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CustomViewUtil.dismissDialog();
            switch (msg.what) {
                case GET_USER_INFO:
                    parseGetUserInfoResult((String) msg.obj);
                    break;
                case SUBMIT_INFO:
                    parseSubmitInfoResult((String) msg.obj);
                    break;
            }
        }
    };

    private void parseGetUserInfoResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    etCarPlateNo.setSelection(etCarPlateNo.getText().toString().length());
                    JSONObject dataObject = resultObject.getJSONObject("data");
                    tvRealName.setText(dataObject.getString("realname"));
                    tvAddress.setText(dataObject.getString("address"));
                    tvTel.setText(dataObject.getString("tel"));
                } else {
                    CustomViewUtil.createToast(AddCarActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseSubmitInfoResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    CustomViewUtil.createToast(AddCarActivity.this, resultObject.getString("sign"));
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    CustomViewUtil.createToast(AddCarActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
