package com.acresqaure.acresqaure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.acresqaure.acresqaure.Constants.TextConstants;
import com.acresqaure.acresqaure.HttpUtility.HttpUtility;
import com.acresqaure.acresqaure.HttpUtility.URLs;
import com.acresqaure.acresqaure.Listeners.ErrorDialogActionListener;
import com.acresqaure.acresqaure.Utility.Utility;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by parasdhawan on 19/02/16.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText etName,etCompany,etEmail,etContact,etAddress,etPassword;
    Button btSignUp,btSignUpWithFb;
    Spinner spExperience;
    CircleImageView profile_image;

    final String VALIDATION_MSG="Please fill all the required fields before signup";

    String image_path="";

    final String API_TYPE_IMAGE="image";
    final String API_TYPE_FORM="form";

    final String SELECT_EXPERIENCE="Select years of experience";

    public static final SignUpFragment newInstance(String message)
    {
        SignUpFragment f = new SignUpFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        getViewId(v);
        setClickListeners();
        setSpinner();
        return v;
    }

    public void setClickListeners(){
        btSignUp.setOnClickListener(this);
        profile_image.setOnClickListener(this);
    }

    public void setSpinner(){
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Less than 1 year experience");
        arrayList.add("1-3 years experience");
        arrayList.add("3-5 years experience");
        arrayList.add("More than 5 years experience");

        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExperience.setAdapter(arrayAdapter);
    }

    public void getViewId(View view){
        etName = (EditText)view.findViewById(R.id.etName);
        etCompany = (EditText)view.findViewById(R.id.etCompany);
        etEmail = (EditText)view.findViewById(R.id.etEmail);
        etContact = (EditText)view.findViewById(R.id.etContact);
        spExperience = (Spinner)view.findViewById(R.id.spExperience);
        etAddress = (EditText)view.findViewById(R.id.etAddress);
        btSignUp = (Button)view.findViewById(R.id.btSignUp);
        etPassword = (EditText)view.findViewById(R.id.etPassword);
        profile_image= (CircleImageView)view.findViewById(R.id.profile_image);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btSignUp:
                if(isInputValid())
                {
                    if(Utility.isNetworkConnected(getActivity())) {
                        signUp();
                    }else {
                        Utility.showMessageDialog(getActivity(), TextConstants.NO_INTERNET_MSG);
                    }
                }else {
                    Utility.showToast(getActivity(),VALIDATION_MSG);
                }
                break;
            case R.id.profile_image:
                addImage();
                break;
        }
    }


    public void signUp(){
            if(!image_path.equals("")) {
                if(Utility.isNetworkConnected(getActivity())) {
                    Utility.showProgressDialog(getActivity());
                    HttpUtility.uploadImageRequest(URLs.PROFILE_PIC_URL, new File(image_path), new HttpUtility.ApiResponseListener() {
                        @Override
                        public void onResponse(String json) {
                            Utility.dismissProgressDialog();
                            if (Utility.isApiResponseSuccess(json)) {
//                                sendFormData();
                            } else {
                                showRetryDialog(API_TYPE_IMAGE);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Utility.dismissProgressDialog();
                            showRetryDialog(API_TYPE_IMAGE);
                        }
                    });
                }else {
                    Utility.showMessageDialog(getActivity(),TextConstants.NO_INTERNET_MSG);
                }
            }else {
//                sendFormData();
            }

    }

    public void sendFormData(){
        if(Utility.isNetworkConnected(getActivity())) {
            Utility.showProgressDialog(getActivity());
            HttpUtility.PostRequest(URLs.SIGNUP_URL, getJson(), new HttpUtility.ApiResponseListener() {
                @Override
                public void onResponse(String json) {
                    Utility.dismissProgressDialog();
                    if (Utility.isApiResponseSuccess(json)) {
                        Intent intent = new Intent(getActivity(), AddNewListing.class);
                        getActivity().startActivity(intent);
                    } else {
                        showRetryDialog(API_TYPE_FORM);
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    Utility.dismissProgressDialog();
                    showRetryDialog(API_TYPE_FORM);
                }
            });
        }else {
            Utility.showMessageDialog(getActivity(), TextConstants.NO_INTERNET_MSG);
        }
    }

    public void showRetryDialog(final String API_TYPE){
        Utility.showRetryDialog(getActivity(), new ErrorDialogActionListener() {
            @Override
            public void onRetry() {
                if(API_TYPE.equals(API_TYPE_FORM)){
                    sendFormData();
                }else {
                    signUp();
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }


    public String getJson(){
        String name=etName.getText().toString().trim();
        String company= etCompany.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String contact =  etContact.getText().toString().trim();
        String experience = spExperience.getSelectedItem().toString().trim();
        String address=  etAddress.getText().toString().trim();
        String password=etPassword.getText().toString();

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("userEmail",email);
            jsonObject.put("userPassword",password);
            jsonObject.put("companyName",company);
            jsonObject.put("companyAdd",address);
            jsonObject.put("phoneNo",contact);
            jsonObject.put("totalExperience",experience);
            jsonObject.put("agentAccountType","");
            jsonObject.put("accountPrivilegeType","");
            jsonObject.put("agentRating","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject.toString();
    }

    public boolean isInputValid(){
        boolean isValid=true;
        String name=etName.getText().toString().trim();
        String company= etCompany.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String contact =  etContact.getText().toString().trim();
        String experience = spExperience.getSelectedItem().toString().trim();
        String address=  etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(name.equals("")){
            Utility.setEditTextWarning(etName,getActivity(),"Name is required");
            isValid=false;
        }else {
            Utility.removeEditTextWarning(etName);
        }

        if(password.equals("")){
            Utility.setEditTextWarning(etPassword,getActivity(),"Enter the password");
            isValid=false;
        }else {
           Utility.removeEditTextWarning(etPassword);
        }

        if(contact.equals("") || contact.length()!=10){
            Utility.setEditTextWarning(etContact,getActivity(),"Invalid phone number");
            isValid=false;
        }else {
            Utility.removeEditTextWarning(etContact);
        }

        if(!email.equals("")){
            if(Utility.isValidEmail(email)){
                Utility.removeEditTextWarning(etEmail);
            }else {
                Utility.setEditTextWarning(etEmail, getActivity(), "No a valid email");
                isValid=false;
            }
        }

//        return isValid;
        return true;
    }

    // Add image to Profile

    public void addImage(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.image_pick_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        TextView tvFromGalary=(TextView)dialogView.findViewById(R.id.tvSelectFromGallary);
        TextView tvFromCamera=(TextView) dialogView.findViewById(R.id.tvClickFromCamera);
        TextView tvCancel = (TextView)dialogView.findViewById(R.id.tvCancel);
        TextView tvRemove=(TextView)dialogView.findViewById(R.id.tvRemovePicture);

        tvFromGalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent1.setType("image/*");
                getActivity().startActivityForResult(
                        Intent.createChooser(intent1, "Select File"),
                        RegisterScreen.IMAGE_PICK_REQUEST_CODE);
                alertDialog.dismiss();
            }
        });

        tvFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(cameraIntent, RegisterScreen.IMAGE_CAPTURE_REQUEST_CODE);
                alertDialog.dismiss();
            }
        });
        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProfilePic();
                alertDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public void removeProfilePic(){
        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        image_path="";
        profile_image.setImageBitmap(defaultImage);
    }

    public void setCameraCapturedImage(Intent data){
        Bitmap photo = (Bitmap) data.getExtras().get("data");

        saveBitmapToImage(photo);
        setImageView(photo);
    }

    public void setImagePickedFromGalary(Intent data){

        Uri selectedImage = data.getData();

        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap!=null)
            saveBitmapToImage(bitmap);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 4;
        bmOptions.inPurgeable = true;

        image_path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/image_profile"+".png";
        Bitmap picBitmap = BitmapFactory.decodeFile(image_path, bmOptions);
        setImageView(picBitmap);

    }

    public void setImageView(Bitmap bitmap){
        profile_image.setImageBitmap(bitmap);
    }

    public void saveBitmapToImage(Bitmap bitmap){
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/image_profile"+".png";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        image_path=path;
    }
}
