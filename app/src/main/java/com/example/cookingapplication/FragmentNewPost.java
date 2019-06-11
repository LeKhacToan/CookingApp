package com.example.cookingapplication;

;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentNewPost extends Fragment {
    Button logout;
    AccountHelper helper;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_new_post,container,false);
        logout =(Button) view. findViewById(R.id.logout);
        helper= new AccountHelper(getActivity());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = helper.getAccountById(1);
                helper.deleteAccount(account);
                getLoginFragment();
               // Toast.makeText(getContext(), account.getEmail(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private void getLoginFragment(){
        // load fragment login
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new FragmentMenu());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
