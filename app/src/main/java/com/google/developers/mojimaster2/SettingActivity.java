package com.google.developers.mojimaster2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.developers.mojimaster2.service.NotificationJobService;

public class SettingActivity extends AppCompatActivity {

    public static final int ID=900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        public static final int JOB_ID = 28;

        @Override
        public void onCreatePreferences(Bundle bundle, String string) {
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            final String notifyKey = getString(R.string.pref_key_notification);
            if (preference.getKey().equals(notifyKey)) {
                // Check if notification setting is no then schedule notification
                boolean on = ((SwitchPreferenceCompat) preference).isChecked();

                if(on){
                    ComponentName componentName=new ComponentName(getActivity(), NotificationJobService.class);
                    JobScheduler jobScheduler = getContext().getSystemService(JobScheduler.class);
                    JobInfo jobInfo=new JobInfo.Builder(ID,componentName)
                            .setPeriodic(1000*60*60*24)
                            .build();
                    jobScheduler.schedule(jobInfo);

                }
                else {
                    JobScheduler jobScheduler = getContext().getSystemService(JobScheduler.class);
                    jobScheduler.cancel(ID);
                }


            }

            final String numberkey = getString(R.string.pref_key_answers);
            if (preference.getKey().equals(numberkey)) {
                String x=((ListPreference)preference).getValue();

            }


                return super.onPreferenceTreeClick(preference);
        }
    }

}
