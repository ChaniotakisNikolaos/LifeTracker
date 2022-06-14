package com.example.lifetracker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyDrawerControllerInterface {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter fragmentAdapter;
    //private ArrayList<ToDoItem> toDoItemArrayList;
    //private ApplicationViewModel applicationViewModel;
    public static final int ADD_TOOO_ITEM_ACTIVITY_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> addToDoItemActivityResultLauncher;
    private ActivityResultLauncher<Intent> addBudgetItemActivityResultLauncher;
    private ApplicationViewModel applicationViewModel;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView navView;
    public Menu m;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 20;

    SharedPreferences sharedPref;
    CircularImageView imageViewProfPic;

    Boolean toShowAll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navView = findViewById(R.id.menu_navigation);
        m = navView.getMenu();
        m.add(Menu.NONE, 0,Menu.NONE,"All To Do").setIcon(R.drawable.ic_baseline_current_label_24).setChecked(true);


        applicationViewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);

        NavigationView navigationView = findViewById(R.id.menu_navigation);
        Menu m = navigationView.getMenu();
        m.add(Menu.NONE, 0,Menu.NONE,"All To Do").setIcon(R.drawable.ic_baseline_current_label_24).setChecked(true);
         /*navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if(id != 0){
                Log.d("ccccc", String.valueOf(id));
                toShowAll = false;
                /*ToDoRecyclerViewAdapter toDoRecyclerViewAdapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.ToDoDiff());
                applicationViewModel.getAllToDoItemsWithLabel(menuItem.getTitle().toString()).observe(this, toDoItems -> {
                    applicationViewModel.getAllToDoItemsWithLabel(menuItem.getTitle().toString());
                    Log.d("cccccctest","changeeeeeee observed");

                });
                toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);
            }
            return true;
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
                            applicationViewModel.insert(new ToDoItem(data.getStringExtra(AddToDoItemActivity.EXTRA_DESCRIPTION),data.getStringExtra(AddToDoItemActivity.EXTRA_LABEL),data.getStringExtra(AddToDoItemActivity.EXTRA_DUE_DATE),data.getStringExtra(AddToDoItemActivity.EXTRA_REMINDER), false), MainActivity.this);
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
                            applicationViewModel.insert(new BudgetItem(data.getStringExtra(AddBudgetItemActivity.EXTRA_LABEL),data.getIntExtra(AddBudgetItemActivity.EXTRA_SAVED,0),data.getIntExtra(AddBudgetItemActivity.EXTRA_TOTAL,0),data.getStringExtra(AddBudgetItemActivity.EXTRA_DUE_DATE)));
                            Log.d("test","Budget inserted");
                        }
                    }
                });
    }
    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user taps the plus button
     */
    public void addToDoItem(View view) {
        Intent intent = new Intent(this, AddToDoItemActivity.class);
        addToDoItemActivityResultLauncher.launch(intent);
    }

    public void checkLabels(View view) {
        NavigationView navView = (NavigationView) MainActivity.this.findViewById(R.id.menu_navigation);
        Menu m = navView.getMenu();
        MenuItem foo_menu_item=m.add("foo");
        Log.d("ccccc", String.valueOf(navView.getMenu()));
    }

    /**
     * Called when the user taps the plus button
     */
    public void addBudgetItem(View view) {
        Intent intent = new Intent(this, AddBudgetItemActivity.class);
        addBudgetItemActivityResultLauncher.launch(intent);
    }

    /**
     * Called when the user taps the hello user text view in me fragment
     */
    public void changeUserName(View view) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText userNameEditText;
        Button cancelChangeNameBtn, saveChangeNameBtn;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View editUserNameView = getLayoutInflater().inflate(R.layout.dialog_edit_user_name, null);
        userNameEditText = (EditText) editUserNameView.findViewById(R.id.userNameEditText);
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
        nameTextView.setText("Hello, "+s);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_username_text_key), s);
        editor.apply();
    }
    public void addMoneyDialog(View view) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText addMoneyEditText;
        TextView addMoneyTextView;
        Button addMoneyCancelButton, addMoneySaveButton;
        dialogBuilder = new AlertDialog.Builder(this);
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

    ActivityResultLauncher<Intent> startActivityIntentCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if (data != null) {
                            imageViewProfPic = findViewById(R.id.imageView);
                            Bundle bundle = data.getExtras();
                            Bitmap bitmapImage = (Bitmap) bundle.get("data");
                            imageViewProfPic.setImageBitmap(bitmapImage);
                            savePhotoToGallery();
                        }
                    }
                }
            });

    private void savePhotoToGallery(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewProfPic.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Uri uri;
        //create image file
        FileOutputStream fileOutputStream = null;
        try{
            File directory = new File(this.getExternalFilesDir(null) + File.separator +"NISO");
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    Log.d("directory", "Directory has been created");
                }else{
                    Log.d("directory", "Directory not created");

                }
            }
            else
                Log.d("directory", "Directory exists");
            //Toast.makeText(MainActivity.this, "Directory exists", Toast.LENGTH_SHORT).show();
            //final String filePath=directory+ File.separator + ""+ System.currentTimeMillis() + ".png";
            final String filePath=directory+ File.separator + "profPic.png";
            File image = new File(filePath);
            fileOutputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            //get absolute path
            uri = Uri.fromFile(image);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("imagepathURI", String.valueOf(uri));

            editor.apply();
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
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
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("imagepathURI", String.valueOf(selectedImageUri));
                            editor.apply();
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
                alertDialogProfPic.dismiss();
                if(checkAndRequestPermission()) {
                    takePictureFromCamera();
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialogProfPic.dismiss();
                takePictureFromGallery();
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
            int writePermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            List<String> listPermissions = new ArrayList<>();
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                listPermissions.add(Manifest.permission.CAMERA);
            }
            if(writePermission == PackageManager.PERMISSION_DENIED){
                listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if(readPermission == PackageManager.PERMISSION_DENIED){
                listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!listPermissions.isEmpty()) {
                ActivityCompat.requestPermissions(MainActivity.this, listPermissions.toArray(new String[listPermissions.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("lllllllll", String.valueOf(requestCode));
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"LifeTracker Requires Access to Camara.", Toast.LENGTH_SHORT).show();
                //finish();
            } /*else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(getApplicationContext(),"LifeTracker Requires Access to write Your Storage.",Toast.LENGTH_SHORT).show();
                //finish();
            } */else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"LifeTracker Requires Access to read Your Storage.",Toast.LENGTH_SHORT).show();
                //finish();
            }  else {
                takePictureFromCamera();
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
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
    @Override
    public void setDrawerLocked() {
        //code to lock drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //toolbar.setNavigationIcon(null);
    }

    @Override
    public void setDrawerUnlocked() {
        //code to unlock drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}