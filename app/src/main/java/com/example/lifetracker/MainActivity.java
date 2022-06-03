package com.example.lifetracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter fragmentAdapter;
    private ArrayList<ToDoItem> toDoItemArrayList;
    private ApplicationViewModel applicationViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationViewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        applicationViewModel.getToDoItemList().observe(this, new Observer<List<ToDoItem>>() {
            @Override
            public void onChanged(List<ToDoItem> toDoItems) {

            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    /**
     * Called when the user taps the plus button
     */
    public void addToDoItem(View view) {
        Intent intent = new Intent(this, AddToDoItemActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    /**
     * Called when the user taps the plus button
     */
    public void addBudgetItem(View view) {
        Intent intent = new Intent(this, AddBudgetItemActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void addMoneyDialog(View view) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText addMoneyEditText;
        TextView addMoneyTextView;
        Button addMoneyCancelButton, addMoneySaveButton;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addMoneyView = getLayoutInflater().inflate(R.layout.dialog_add_money, null);
        addMoneyEditText = (EditText) addMoneyView.findViewById(R.id.editTextAddAmountToBudget);
        addMoneyTextView = (TextView) addMoneyView.findViewById(R.id.textViewAddMoney);
        addMoneyCancelButton = (Button) addMoneyView.findViewById(R.id.buttonCancelAddMoney);
        addMoneySaveButton = (Button) addMoneyView.findViewById(R.id.buttonSaveAddMoney);

        dialogBuilder.setView(addMoneyView);
        dialog = dialogBuilder.create();
        dialog.show();

        addMoneyCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        addMoneySaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    public void subtractMoneyDialog(View view) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText subtractMoneyEditText;
        TextView subtractMoneyTextView;
        Button subtractMoneyCancelButton, subtractMoneySaveButton;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View subtractMoneyView = getLayoutInflater().inflate(R.layout.dialog_subtract_money, null);
        subtractMoneyEditText = (EditText) subtractMoneyView.findViewById(R.id.editTextSubtractAmountToBudget);
        subtractMoneyTextView = (TextView) subtractMoneyView.findViewById(R.id.textViewSubtractMoney);
        subtractMoneyCancelButton = (Button) subtractMoneyView.findViewById(R.id.buttonCancelSubtractMoney);
        subtractMoneySaveButton = (Button) subtractMoneyView.findViewById(R.id.buttonSaveSubtractMoney);

        dialogBuilder.setView(subtractMoneyView);
        dialog = dialogBuilder.create();
        dialog.show();

        subtractMoneyCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        subtractMoneySaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    public void deleteBudgetItemDialog(View view) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        TextView deleteBudgetItemTextView;
        Button deleteBudgetItemCancelButton, deleteBudgetItemButton;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View deleteBudgetItemView = getLayoutInflater().inflate(R.layout.dialog_delete_budget_item, null);
        deleteBudgetItemTextView = (TextView) deleteBudgetItemView.findViewById(R.id.textViewDeleteBudgetItem);
        deleteBudgetItemCancelButton = (Button) deleteBudgetItemView.findViewById(R.id.buttonCancelDeleteBudgetItem);
        deleteBudgetItemButton = (Button) deleteBudgetItemView.findViewById(R.id.buttonDeleteBudgetItem);

        dialogBuilder.setView(deleteBudgetItemView);
        dialog = dialogBuilder.create();
        dialog.show();

        deleteBudgetItemCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        deleteBudgetItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
    public static int getCameraPhotoOrientation(String imagePath) {
        int rotate = 0;
        try {
            ExifInterface exif  = null;
            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            assert exif != null;
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        ImageView imageViewProfPic = findViewById(R.id.imageView);
                        Uri selectedImageUri;
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(selectedImageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            imageViewProfPic.setRotation(getCameraPhotoOrientation(selectedImageUri.getPath()));

                            /*Bitmap selectedImageBitmap = null;
                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                                imageViewProfPic.setRotation(getCameraPhotoOrientation(selectedImageUri.getPath()));
                                Log.d("kkk",selectedImageUri.getPath());
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            imageViewProfPic.setImageBitmap(selectedImage);
                        }
                    }
                }
            });

    public void choosePicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        TextView profPicTextView, galleryPicTextView;
        Button photoButton, galleryButton;
        final View dialogView = inflater.inflate(R.layout.dialog_picture, null);
        profPicTextView = dialogView.findViewById(R.id.textViewTakePhoto);
        galleryPicTextView = dialogView.findViewById(R.id.textViewGalleryPhoto);
        photoButton = dialogView.findViewById(R.id.buttonTakePhoto);
        galleryButton = dialogView.findViewById(R.id.buttonGalleryPhoto);

        builder.setView(dialogView);

        AlertDialog alertDialogProfPic = builder.create();
        alertDialogProfPic.show();

        photoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialogProfPic.dismiss();
                takePictureFromGallery();
            }
        });

    }
    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityIntent.launch(pickPhoto);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}