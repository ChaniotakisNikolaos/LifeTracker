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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MyDrawerControllerInterface {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ActivityResultLauncher<Intent> addToDoItemActivityResultLauncher;
    private ActivityResultLauncher<Intent> addBudgetItemActivityResultLauncher;
    private ApplicationViewModel applicationViewModel;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView navView;
    public Menu m;

    SharedPreferences sharedPref;
    CircularImageView imageViewProfPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        //FOR THE NAVIGATION DRAWER
        //drawer layout instance to toggle the menu icon to open, drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        //pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navView = findViewById(R.id.menu_navigation);

        m = navView.getMenu();
        //add the All To Do, make it checked, it will always exist in the menu items
        m.add(Menu.NONE, 0, Menu.NONE, "All To Do").setIcon(R.drawable.ic_baseline_current_label_24).setChecked(true);

        //get the application view model
        applicationViewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);

        //for the tabs at the bottom of the screen
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        //execute when a tab is chosen
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

        //executes when whenever the page changes
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        //Handles the reply of the AddToDoItemActivity after add
        addToDoItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            applicationViewModel.insert(new ToDoItem(data.getStringExtra(AddToDoItemActivity.EXTRA_DESCRIPTION), data.getStringExtra(AddToDoItemActivity.EXTRA_LABEL), data.getStringExtra(AddToDoItemActivity.EXTRA_DUE_DATE), data.getStringExtra(AddToDoItemActivity.EXTRA_REMINDER), false), MainActivity.this);
                        }
                    }
                });

        //Handles the reply of the AddBudgetItemActivity after add
        addBudgetItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            applicationViewModel.insert(new BudgetItem(data.getStringExtra(AddBudgetItemActivity.EXTRA_LABEL), data.getIntExtra(AddBudgetItemActivity.EXTRA_SAVED, 0), data.getIntExtra(AddBudgetItemActivity.EXTRA_TOTAL, 0), data.getStringExtra(AddBudgetItemActivity.EXTRA_DUE_DATE)));
                        }
                    }
                });
    }

    // override the onOptionsItemSelected() function to implement the item click listener callback
    // to open and close the navigation drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user taps the plus button in to do tab
     */
    public void addToDoItem(View view) {
        Intent intent = new Intent(this, AddToDoItemActivity.class);
        addToDoItemActivityResultLauncher.launch(intent);
    }

    /**
     * Called when the user taps the plus button in budget tab
     */
    public void addBudgetItem(View view) {
        Intent intent = new Intent(this, AddBudgetItemActivity.class);
        addBudgetItemActivityResultLauncher.launch(intent);
    }

    /**
     * Called when the user taps the hello user text view in me fragment
     */
    public void changeUserName(View view) {
        EditText userNameEditText;
        Button cancelChangeNameBtn, saveChangeNameBtn;
        //build an alert dialog for the user, where the user can put their name
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View editUserNameView = getLayoutInflater().inflate(R.layout.dialog_edit_user_name, null);
        userNameEditText = editUserNameView.findViewById(R.id.userNameEditText);
        cancelChangeNameBtn = editUserNameView.findViewById(R.id.cancelChangeNameBtn);//button to cancel the dialog
        saveChangeNameBtn = editUserNameView.findViewById(R.id.saveChangeNameBtn);//button to save the name

        dialogBuilder.setView(editUserNameView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        cancelChangeNameBtn.setOnClickListener(v -> {
            dialog.dismiss();//close dialog
        });

        //when the user saves the name, changeStatus is called to save the name in SharedPreferences
        saveChangeNameBtn.setOnClickListener(v -> {
            String newName = userNameEditText.getText().toString();//change text
            changeStatus(newName);
            dialog.dismiss();//close dialog
        });
    }

    /**
     * save username in sharedPreferences concatenated with "Hello,"
     */
    public void changeStatus(String s) {
        TextView nameTextView = findViewById(R.id.userNameTextView);
        nameTextView.setText(MessageFormat.format("Hello, {0}", s));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_username_text_key), s);
        editor.apply();
    }

    /**
     * Launch Camera
     */
    ActivityResultLauncher<Intent> startActivityIntentCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();//get the intent data
                        if (data != null) {
                            imageViewProfPic = findViewById(R.id.imageView);
                            Bundle bundle = data.getExtras();
                            Bitmap bitmapImage = (Bitmap) bundle.get("data");
                            imageViewProfPic.setImageBitmap(bitmapImage);//set new image to circularImageView
                            savePhotoToGallery(null);//save image locally
                        }
                    }
                }
            });

    /**
     * Save image to Local Gallery(data->com.example.lifetracker)
     */
    private void savePhotoToGallery(Uri uri) {
        Bitmap bitmap;
        try {
            if(uri!=null){
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            }else{
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewProfPic.getDrawable();//get image drawable
                bitmap = bitmapDrawable.getBitmap();
            }
            //create image file
            FileOutputStream fileOutputStream;
            File directory = new File(this.getExternalFilesDir(null) + File.separator + "NISO");//get the directory that the photo will be saved
            //the directory will be named NISO
            //if does not exist, create a new one
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    Log.d("directory", "Directory has been created");
                } else {
                    Log.d("directory", "Directory not created");//in case it could not be created

                }
            } else
                Log.d("directory", "Directory exists");//if directory already exists

            //create the filepath of the photo, where it will be named profPic with the current time in millis
            //in order to not have a problem with the cached memory(otherwise the old cached photo will appear instead of the new one)
            final String filePath = directory + File.separator + "profPic" + System.currentTimeMillis() + ".png";
            File image = new File(filePath);
            fileOutputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            //get absolute path
            uri = Uri.fromFile(image);

            //put photo in shared preferences as a uri
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("imagepathURI", String.valueOf(uri));

            editor.apply();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Put photo from gallery
     */
    ActivityResultLauncher<Intent> startActivityIntentGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    CircularImageView imageViewProfPic = findViewById(R.id.imageView);
                    Uri selectedImageUri;

                    if (data != null && data.getData() != null) {
                        //save photo in circular image view with picasso
                        selectedImageUri = data.getData();
                        //have a place holder(which will look like loading and in case of error put the starting image
                        Picasso.with(getApplicationContext()).load(selectedImageUri).placeholder(R.drawable.ic_baseline_autorenew_24).error(R.drawable.cat_glasses).fit().centerInside().into(imageViewProfPic);
                        savePhotoToGallery(selectedImageUri);
                    }
                }
            });
    /**
     * Save image to Local Gallery(data->com.example.lifetracker)
     */
    private void savePhotoFromGallery(Uri uri) {
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            //create image file
            FileOutputStream fileOutputStream;
            try {
                File directory = new File(this.getExternalFilesDir(null) + File.separator + "NISO");//get the directory that the photo will be saved
                //the directory will be named NISO
                //if does not exist, create a new one
                if (!directory.exists()) {
                    if (directory.mkdirs()) {
                        Log.d("directory", "Directory has been created");
                    } else {
                        Log.d("directory", "Directory not created");//in case it could not be created

                    }
                } else
                    Log.d("directory", "Directory exists");//if directory already exists

                //create the filepath of the photo, where it will be named profPic with the current time in millis
                //in order to not have a problem with the cached memory(otherwise the old cached photo will appear instead of the new one)
                final String filePath = directory + File.separator + "profPic" + System.currentTimeMillis() + ".png";
                File image = new File(filePath);
                fileOutputStream = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                //get absolute path
                uri = Uri.fromFile(image);

                //put photo in shared preferences as a uri
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("imagepathURI", String.valueOf(uri));

                editor.apply();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Dialog to choose from where does he user want to load a photo(camera or gallery)
     */
    public void choosePicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        Button photoButton, galleryButton;

        final View dialogView = inflater.inflate(R.layout.dialog_picture, null);
        photoButton = dialogView.findViewById(R.id.buttonTakePhoto);
        galleryButton = dialogView.findViewById(R.id.buttonGalleryPhoto);

        builder.setView(dialogView);

        AlertDialog alertDialogProfPic = builder.create();
        alertDialogProfPic.show();

        //if user clicks on the photo button, then check permissions and take photo
        photoButton.setOnClickListener(v -> {
            alertDialogProfPic.dismiss();
            if (checkAndRequestPermission()) {
                takePictureFromCamera();
            }
        });

        //if user chooses gallery button -> open galley
        galleryButton.setOnClickListener(v -> {
            alertDialogProfPic.dismiss();
            takePictureFromGallery();
        });

    }

    /**
     * Launch Intent to take photo with camera
     */
    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            this.startActivityIntentCamera.launch(takePicture);
        }
    }

    /**
     * Request permissions from user for the camera
     */
    private boolean checkAndRequestPermission() {
        int cameraPermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (cameraPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 20);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePictureFromCamera();// in case there were no permissions beforehand, call take picture from camera
        } else {
            Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();//inform user he did not give permissions
        }
    }

    /**
     * Launch Intent to choose picture from gallery
     */
    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityIntentGallery.launch(pickPhoto);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setDrawerLocked() {
        //code to lock drawer
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void setDrawerUnlocked() {
        //code to unlock drawer
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}