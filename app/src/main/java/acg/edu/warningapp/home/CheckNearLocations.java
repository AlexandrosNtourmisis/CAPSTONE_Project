package acg.edu.warningapp.home;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

import static acg.edu.warningapp.home.Channels.CHANNEL_1_ID;

public class CheckNearLocations extends BroadcastReceiver {

    String  time, country, messageCase;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<String> displayList, id_list;
    private NotificationManagerCompat notificationManager;

    String radius, phoneNo;
    List<String> id_match_list, id_all, checktypesize, checkidsize;


    @Override
    public void onReceive(Context context, Intent intent) {


        Preferences preferences = new Preferences(context);
        HashMap<String, String> userData = preferences.userInformation();


        country = userData.get(Preferences.C_country);
        time = userData.get(Preferences.N_otifications);
        radius = userData.get(Preferences.Safety_radius);
        phoneNo = userData.get(Preferences.Phone_No);


        notificationManager = NotificationManagerCompat.from(context);
        displayList = new ArrayList<>();
        id_list = new ArrayList<>();
        id_match_list = new ArrayList<>();
        id_all = new ArrayList<>();
        checkidsize = new ArrayList<>();
        checktypesize = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        //read all the cases that are in the user's account database
        Query user = FirebaseDatabase.getInstance().getReference("WarningApp_Users/" + phoneNo + "/").child("nearCasesIds");
        //clear list
        id_match_list.clear();
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if they exist
                if (snapshot.exists()) {

                    // put them on a list
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    id_match_list = snapshot.getValue(t);

                    // put all the cases that exist in users account into the stringBuilder
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < id_match_list.size(); i++) {
                        stringBuilder.append(id_match_list.get(i) + ", ");
                    }

                    //Testing Purposes
////                        textView.setText(stringBuilder);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Read from firebase all the cases that are in the user's account database
        Query userD = FirebaseDatabase.getInstance().getReference("WarningApp_Users/" + phoneNo + "/").child("nearCasestypes");
        //clear the list
        id_all.clear();
        userD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if they exist
                if (snapshot.exists()) {

                    //get their ids in a string
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    id_all = snapshot.getValue(t);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //get country from session values
        country = userData.get(Preferences.C_country);

        // Check if the timer from location settings is not equal to manual -- if it is, don't check the near cases
        if (!time.equals("Manual")) {

            //Check if the location services are allowed to be used by the app
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        //if the location is not empty
                        if (location != null) {

                            //Assign the user's location to Point A
                            Location locationA = new Location("point A");
                            locationA.setLatitude(location.getLatitude());
                            locationA.setLongitude(location.getLongitude());

                            //Read the cases from the database
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Cases");
                            Query query = myRef.child(country).orderByKey();

                            //convert radius to float
                            float f_radius = Float.parseFloat(radius);
                            //Clear the lists
                            displayList.clear();
                            id_list.clear();
                            checktypesize.clear();

                            //Check the cases to get their information from database
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot item : dataSnapshot.getChildren()) {

                                        if (!dataSnapshot.hasChildren()) {

                                            return;
                                        }

                                        //Create new location point for the cases
                                        Location locationB = new Location("point B");

                                        //Get their info such as location, type and id
                                        if (dataSnapshot.exists()) {
                                            String latituded = item.child("latitude").getValue(String.class);
                                            String longitued = item.child("longitude").getValue(String.class);
                                            String cit = item.child("city").getValue(String.class);
                                            String type = item.child("type").getValue(String.class);
                                            String id = item.child("post_id").getValue(String.class);
                                            //Convert them to float
                                            locationB.setLatitude(Float.parseFloat(latituded));
                                            locationB.setLongitude(Float.parseFloat(longitued));
                                            //Get all the cases to a string that contains the city and the type
                                            String city_type = cit + " (" + type + ")";


                                            //Get the distance of the two points (user's location and case's location)
                                            float distance = locationA.distanceTo(locationB);


                                            //If the distance is less than the safety radius, then add these cases to the lists.
                                            if (distance < f_radius) {
                                                displayList.add(city_type);
                                                id_list.add(id);
                                                displayList.removeAll(id_match_list);
                                                id_list.removeAll(id_all);

                                            }


                                            // If the distance is smaller than the safety distance
                                            if (distance < f_radius) {
//                                                    id_match_list.add(id);
//                                                    id_all.add(city_type);


//
//                                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("WarningApp_Users");
//                                                    myRef.child(phoneNo).child("nearCasesIds").setValue(displayList);
//                                                    myRef.child(phoneNo).child("nearCasestypes").setValue(id_list);


                                                // if the size is less than 2 display the appropriate message with the number for single or plural
                                                if (displayList.size() < 2) {
                                                    messageCase = "There is " + displayList.size() + " case near you";
                                                } else {
                                                    messageCase = "There are " + displayList.size() + " cases near you";
                                                }
//
                                                //Notification title
                                                String title = "Cases Near you";

                                                //Create a notification and send this to channel 1
                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                                                        .setSmallIcon(R.mipmap.logotriangle)
                                                        .setContentTitle(title)
                                                        .setContentText(messageCase)
                                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE);

                                                //If the user clicks on the notification, navigate the user to this class
                                                Intent intent = new Intent(context, CasesNearAlert.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                //Send these values to the next intent
                                                intent.putExtra("message", messageCase);
                                                intent.putExtra("country", country);
                                                ArrayList<String> myList = new ArrayList<String>(displayList);
                                                ArrayList<String> mySList = new ArrayList<String>(id_list);
                                                intent.putExtra("array", myList);
                                                intent.putExtra("id_list", mySList);

                                                //Send Notification
                                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                builder.setContentIntent(pendingIntent);
                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                notificationManager.notify(0, builder.build());

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //Set another request and get the last location
                        } else {
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    Location location1 = locationResult.getLastLocation();
                                }
                            };
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                });
            } else {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }


        // This handler is to store the cases, that the user was alerted, to the database in order to not view them in the next notification from this class
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {

                                //Save user's location to point A
                                Location locationA = new Location("point A");
                                locationA.setLatitude(location.getLatitude());
                                locationA.setLongitude(location.getLongitude());

                                //Read database
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Cases");
                                Query query = myRef.child(country).orderByKey();

                                //Convert radius to float
                                float f_radius = Float.parseFloat(radius);

                                //Clear lists
                                displayList.clear();
                                id_list.clear();
                                checktypesize.clear();
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot item : dataSnapshot.getChildren()) {

                                            if (!dataSnapshot.hasChildren()) {

                                                return;
                                            }

                                            //New location point for case's location
                                            Location locationB = new Location("point B");

                                            //Get case's information from database
                                            if (dataSnapshot.exists()) {
                                                String latituded = item.child("latitude").getValue(String.class);
                                                String longitued = item.child("longitude").getValue(String.class);
                                                String cit = item.child("city").getValue(String.class);
                                                String type = item.child("type").getValue(String.class);
                                                String id = item.child("post_id").getValue(String.class);

                                                locationB.setLatitude(Float.parseFloat(latituded));
                                                locationB.setLongitude(Float.parseFloat(longitued));
                                                String city_type = cit + " (" + type + ")";


                                                float distance = locationA.distanceTo(locationB);


                                                if (distance < f_radius) {
                                                    displayList.add(id);
                                                    id_list.add(city_type);

                                                }


                                                if (distance < f_radius) {

                                                    //Store the case's ids and types to the user's profile in Database
                                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("WarningApp_Users");
                                                    myRef.child(phoneNo).child("nearCasesIds").setValue(displayList);
                                                    myRef.child(phoneNo).child("nearCasestypes").setValue(id_list);

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                //Send new location request if location services are not allowed.
                            } else {
                                LocationRequest locationRequest = new LocationRequest()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(1000)
                                        .setNumUpdates(1);
                                LocationCallback locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(@NonNull LocationResult locationResult) {
                                        Location location1 = locationResult.getLastLocation();
                                    }
                                };
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                            }
                        }
                    });
                } else {
                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }

            //Handler delay timer
        }, 50000);


    }
}
