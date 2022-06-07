package com.example.lifetracker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter fragmentAdapter;
    //private ArrayList<ToDoItem> toDoItemArrayList;
    //private ApplicationViewModel applicationViewModel;
    public static final int ADD_TOOO_ITEM_ACTIVITY_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> addToDoItemActivityResultLauncher;
    private ActivityResultLauncher<Intent> addBudgetItemActivityResultLauncher;
    private ApplicationViewModel applicationViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationViewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        /*applicationViewModel.getToDoItemList().observe(this, new Observer<List<ToDoItem>>() {
            @Override
            public void onChanged(List<ToDoItem> toDoItems) {

            }
        });*/

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

        addToDoItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            applicationViewModel.insert(new ToDoItem(data.getStringExtra(AddToDoItemActivity.EXTRA_DESCRIPTION),data.getStringExtra(AddToDoItemActivity.EXTRA_LABEL),data.getStringExtra(AddToDoItemActivity.EXTRA_DUE_DATE),data.getStringExtra(AddToDoItemActivity.EXTRA_REMINDER)));
                            Log.d("test","ToDo inserted");
                        }
                    }
                });
        addBudgetItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            applicationViewModel.insert(new BudgetItem(data.getStringExtra(AddBudgetItemActivity.EXTRA_LABEL),data.getStringExtra(AddBudgetItemActivity.EXTRA_SAVED),data.getStringExtra(AddBudgetItemActivity.EXTRA_TOTAL),data.getStringExtra(AddBudgetItemActivity.EXTRA_DUE_DATE)));
                            Log.d("test","Budget inserted");
                        }
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
        addToDoItemActivityResultLauncher.launch(intent);
    }


    /**
     * Called when the user taps the plus button
     */
    public void addBudgetItem(View view) {
        Intent intent = new Intent(this, AddBudgetItemActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        addBudgetItemActivityResultLauncher.launch(intent);
    }

    /**
     * Called when the user taps the hello user text view in me fragment
     */
    public void changeUserName(View view) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText userNameEditText;
        TextView addNameTextView;
        Button cancelChangeNameBtn, saveChangeNameBtn;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View editUserNameView = getLayoutInflater().inflate(R.layout.dialog_edit_user_name, null);
        userNameEditText = (EditText) editUserNameView.findViewById(R.id.userNameEditText);
        addNameTextView = (TextView) editUserNameView.findViewById(R.id.addNameTextView);
        cancelChangeNameBtn = (Button) editUserNameView.findViewById(R.id.cancelChangeNameBtn);
        saveChangeNameBtn = (Button) editUserNameView.findViewById(R.id.saveChangeNameBtn);

        dialogBuilder.setView(editUserNameView);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelChangeNameBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        saveChangeNameBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newName = userNameEditText.getText().toString();
                Log.d("nnn",newName);
                changeStatus(newName);
                dialog.dismiss();
            }
        });
    }
    public void changeStatus(String s){
        TextView nameTextView = findViewById(R.id.userNameTextView);
        nameTextView.setText(s);
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
    ActivityResultLauncher<Intent> startActivityIntentCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        CircularImageView imageViewProfPic = findViewById(R.id.imageView);
                        if (data != null) {
                            Bundle bundle = data.getExtras();
                            Bitmap bitmapImage = (Bitmap) bundle.get("data");
                            imageViewProfPic.setImageBitmap(bitmapImage);
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> startActivityIntentGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        CircularImageView imageViewProfPic = findViewById(R.id.imageView);
                        Uri selectedImageUri;
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            /*Bitmap bitmapImage = null;
                            try {
                                bitmapImage = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), selectedImageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            imageViewProfPic.setImageBitmap(bitmapImage);*/
                            Picasso.with(getApplicationContext()).load(selectedImageUri).placeholder(R.drawable.ic_baseline_autorenew_24).error(R.drawable.cat_glasses).fit().centerInside().into(imageViewProfPic);
                            //Picasso.with(getApplicationContext()).load(selectedImageUri).fit().placeholder(R.drawable.ic_baseline_autorenew_24).error(R.drawable.cat_glasses).into(imageViewProfPic);
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
                if(checkAndRequestPermission()) {
                    takePictureFromCamera();
                    alertDialogProfPic.dismiss();
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePictureFromGallery();
                alertDialogProfPic.dismiss();
            }
        });

    }

    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            this.startActivityIntentCamera.launch(takePicture);
        }
    }

    private boolean checkAndRequestPermission(){
        if(Build.VERSION.SDK_INT>=23){
            int cameraPermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();
            Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityIntentGallery.launch(pickPhoto);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_to_do_item, popup.getMenu());
        popup.show();
    }
}