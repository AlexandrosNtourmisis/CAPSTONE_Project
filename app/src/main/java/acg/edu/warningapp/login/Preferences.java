package acg.edu.warningapp.login;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {

    SharedPreferences session;
    SharedPreferences.Editor editor;
    //which java class will be called
    Context context;


    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String F_name = "fname";
    public static final String L_name = "lname";
    public static final String E_mail = "email";
    public static final String C_country = "country";
    public static final String T_password = "password";
    public static final String Phone_No = "phoneNo";
    public static final String N_otifications = "notifications";
    public static final String Safety_radius = "500";


    //Create Session
    public Preferences(Context context_p) {
        context = context_p;
        session = context_p.getSharedPreferences("LoginSes", Context.MODE_PRIVATE);
        editor = session.edit();
    }

    //Put values
    public void LoginS(String fname, String lname, String email, String phoneNo, String password, String country, String notifications, String safety_radius) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(F_name, fname);
        editor.putString(L_name, lname);
        editor.putString(E_mail, email);
        editor.putString(C_country, country);
        editor.putString(T_password, password);
        editor.putString(Phone_No, phoneNo);
        editor.putString(N_otifications, notifications);
        editor.putString(Safety_radius, safety_radius);

        editor.commit();
    }

    //Create HashMap
    public HashMap<String, String> userInformation() {
        HashMap<String, String> userDataset = new HashMap<String, String>();

        userDataset.put(F_name, session.getString(F_name, null));
        userDataset.put(L_name, session.getString(L_name, null));
        userDataset.put(E_mail, session.getString(E_mail, null));
        userDataset.put(C_country, session.getString(C_country, null));
        userDataset.put(T_password, session.getString(T_password, null));
        userDataset.put(Phone_No, session.getString(Phone_No, null));
        userDataset.put(N_otifications, session.getString(N_otifications, null));
        userDataset.put(Safety_radius, session.getString(Safety_radius, null));

        return userDataset;
    }

    public boolean isLoggedIn() {
        if (session.getBoolean(IS_LOGIN, true)) {
            return true;

        } else
            return false;
    }

    //Log out -> clear session
    public void logOUT() {
        editor.putBoolean(IS_LOGIN, false);
        editor.clear();
        editor.commit();
    }

}
