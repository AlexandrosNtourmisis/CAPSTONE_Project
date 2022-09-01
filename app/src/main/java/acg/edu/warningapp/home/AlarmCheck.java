package acg.edu.warningapp.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import acg.edu.warningapp.login.Preferences;

public class AlarmCheck {
    private Context context;
    String time;

    public AlarmCheck(Context context) {
        this.context = context;
    }

    public void setAlarmManager() {

        //read values from session
        Preferences preferences = new Preferences(context);
        HashMap<String, String> userData = preferences.userInformation();
        //notification timer
        time = userData.get(Preferences.N_otifications);

        //Start alarm -- calls CheckNearLocations class every n(time)
        Intent intent = new Intent(context, CheckNearLocations.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {

            //alarm trigger timer
            switch (time) {
                case "15": {
                    // milliseconds --> minutes * seconds * milliseconds
                    long triggerAfter = 15 * 60 * 1000; //trigger after 1 hour

                    long triggerEvery = 15 * 60 * 1000; //trigger every hour

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);
                    break;
                }
                case "30 minutes (recommended)": {

                    long triggerAfter = 30 * 60 * 1000; //trigger after 1 hour

                    long triggerEvery = 30 * 60 * 1000; //trigger every hour

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);

                    break;
                }
                case "45": {

                    long triggerAfter = 45 * 60 * 1000; //trigger after 1 hour

                    long triggerEvery = 45 * 60 * 1000; //trigger every hour

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);

                    break;
                }
                case "1 hour": {

                    long triggerAfter = 60 * 60 * 1000; //trigger after 1 hour

                    long triggerEvery = 60 * 60 * 1000; //trigger every hour

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);

                    break;
                }
                case "2 hours": {

                    long triggerAfter = 120 * 60 * 1000; //trigger after 1 hour

                    long triggerEvery = 120 * 60 * 1000; //trigger every hour

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);

                    break;
                }
                case "Manual":
                    cancelAlarmManager();
                    break;
            }


        }

    }

    //Cancel Alarm
    public void cancelAlarmManager() {
        Intent intent = new Intent(context, CheckNearLocations.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }
    }


}
