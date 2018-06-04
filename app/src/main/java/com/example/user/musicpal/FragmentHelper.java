package com.example.user.musicpal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentHelper {

    public static final void cargarFragment(Fragment fragment, int container, FragmentManager fragmentManager) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container, fragment);
        fragmentTransaction.commit();
    }

    public static final void cargarFragmentConBackStack(Fragment fragment, int container, FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

}
}