package com.example.lifetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;

public class MeFragment extends Fragment {

    ApplicationViewModel applicationViewModel;
    int countTotalItems, countCheckedItems = 0;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_me, container, false);
        applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);

        //get username
        SharedPreferences sharedPref = this.requireActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultText = getResources().getString(R.string.default_username_text);
        String username = sharedPref.getString(getString(R.string.saved_username_text_key), defaultText);

        TextView textView = inflatedView.findViewById(R.id.userNameTextView);
        TextView finishedTasksTextView = inflatedView.findViewById(R.id.finishedTasksTextView);
        TextView pendingTasksTextView = inflatedView.findViewById(R.id.pendingTasksTextView);
        textView.setText(MessageFormat.format("Hello, {0}", username));

        //get user profile photo
        CircularImageView imageView = inflatedView.findViewById(R.id.imageView);
        if (sharedPref.contains("imagepathURI")) {
            String mFilePath = sharedPref.getString("imagepathURI", null);
            Picasso.with(getContext()).load(Uri.parse(mFilePath)).placeholder(R.drawable.ic_baseline_autorenew_24).error(R.drawable.cat_glasses).fit().centerInside().into(imageView);
        }
        //get to do list size and count how many are selected
        applicationViewModel.getToDoItemList().observe(getViewLifecycleOwner(), toDoItems -> {
            countCheckedItems = 0;
            countTotalItems = toDoItems.size();
            for (int i = 0; i < countTotalItems; i++) {
                if (toDoItems.get(i).isSelected()) {
                    countCheckedItems++;
                }
            }
            finishedTasksTextView.setText(MessageFormat.format("{0}\nFinished Tasks", countCheckedItems));
            pendingTasksTextView.setText(MessageFormat.format("{0}\nPending Tasks", (countTotalItems - countCheckedItems)));
        });
        // Inflate the layout for this fragment
        return inflatedView;
    }
}
