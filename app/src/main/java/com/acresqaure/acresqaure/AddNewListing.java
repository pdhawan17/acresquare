package com.acresqaure.acresqaure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.acresqaure.acresqaure.Adapters.ImagesViewAdapter;
import com.acresqaure.acresqaure.Models.ImageInfo;

import org.lucasr.twowayview.TwoWayView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddNewListing extends NavigationDrawerBaseActivity implements View.OnClickListener {

    TwoWayView twImages;
    ImageView ivAddButton,ivCoverImage,ivAddImage,ivNavigationDrawer;

    final int IMAGE_PICK_REQUEST_CODE =102;
    final int IMAGE_CAPTURE_REQUEST_CODE=103;

    ArrayList<ImageInfo> images=new ArrayList<>();

    ImagesViewAdapter imageViewAdapter=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_add_new_listing);
        getViewId();
        setOnClickListener();
    }

    public void getViewId(){
        twImages=(TwoWayView)findViewById(R.id.twImages);
        ivAddButton = (ImageView)findViewById(R.id.ivAdd);
        ivCoverImage = (ImageView)findViewById(R.id.ivCoverImage);
        ivAddImage = (ImageView)findViewById(R.id.ivAddImage);
        ivNavigationDrawer = (ImageView)findViewById(R.id.ivNavigationDrawer);
    }

    public void setOnClickListener(){
        ivAddButton.setOnClickListener(this);
        ivNavigationDrawer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivAdd:
                addImage();
                break;
            case R.id.ivNavigationDrawer:
                super.openNavigationDrawer();
                break;
        }

    }

    public void addImage(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.image_pick_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        TextView tvFromGalary=(TextView)dialogView.findViewById(R.id.tvSelectFromGallary);
        TextView tvFromCamera=(TextView) dialogView.findViewById(R.id.tvClickFromCamera);
        TextView tvCancel = (TextView)dialogView.findViewById(R.id.tvCancel);
        TextView tvRemove=(TextView)dialogView.findViewById(R.id.tvRemovePicture);
        tvRemove.setVisibility(View.GONE);

        tvFromGalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent1.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent1, "Select File"),
                        IMAGE_PICK_REQUEST_CODE);
                alertDialog.dismiss();
            }
        });

        tvFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_REQUEST_CODE);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            setImage(data);
        }else if(requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            setCameraCapturedImage(data);
        }
    }

    public void setCameraCapturedImage(Intent data){
        Bitmap photo = (Bitmap) data.getExtras().get("data");

        String path=getFilePath();
        saveBitmapToImage(photo, path);
        addToImagesArr(photo, path);
        setImageList();
    }

    public void setImage(Intent data){

        Uri selectedImage = data.getData();

        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path=getFilePath();
        if(bitmap!=null)
        saveBitmapToImage(bitmap,path);

        addToImagesArr(bitmap,path);
        setImageList();
    }

    public void addToImagesArr(Bitmap bitmap,String path){
        ImageInfo imageInfo=new ImageInfo();
        imageInfo.setBitmap(bitmap);
        imageInfo.setFilePath(path);
        images.add(imageInfo);
    }

    public void saveBitmapToImage(Bitmap bitmap,String path){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePath(){
        String direc= Environment.getExternalStorageDirectory().getAbsolutePath()+"/AcreSqaure";
        File file=new File(direc);
        if(!file.exists()) {
            file.mkdir();
        }
        String path=direc +"/"+ System.currentTimeMillis()+".png";
        return path;
    }

    public void setImageList(){
        if(imageViewAdapter==null){
            imageViewAdapter = new ImagesViewAdapter(images,this);
            twImages.setAdapter(imageViewAdapter);
        }else {
            imageViewAdapter.notifyDataSetChanged();
        }
    }
}
