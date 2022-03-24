package com.homies.homies.services;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.homies.homies.user.LoginFragment;
import com.homies.homies.user.RegisterFragment;

public class Adaptador extends FragmentPagerAdapter {

    LoginFragment loginFragment;
    RegisterFragment registerFragment;

    public Adaptador(FragmentManager fm) {
        super(fm);
    }

    //Creo los distintos Fragment
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                loginFragment = new LoginFragment();
                fragment = loginFragment;
                break;
            case 1:
                registerFragment = new RegisterFragment();
                fragment = registerFragment;
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    //Se define el orden de las paginas
    @Override
    public CharSequence getPageTitle(int position) {
        String titulo;
        switch (position) {
            case 0:
                titulo = "Login";
                break;
            case 1:
                titulo = "Registro";
                break;
            default:
                titulo = "";
        }
        return titulo;
    }

}