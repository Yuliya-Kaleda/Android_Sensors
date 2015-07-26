package android.com.sensors;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


public class MainActivity extends Activity {


    MediaRecorder mediaRecorder;
    Button startBTN;
    Button stopBTN;
    Camera mCamera;
    int counter = 0;
    ListView lv;
    ArrayList<String> audioSources;
    URI currentAudioSourcePath;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioSources = new ArrayList<>();

        startBTN = (Button) findViewById(R.id.main_start_btn);
        stopBTN = (Button) findViewById(R.id.main_stop_btn);
        lv = (ListView) findViewById(R.id.list_audio);

        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                File rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                File soundFile = new File(rootPath, "video" + counter + ".mp4");
                currentAudioSourcePath = soundFile.toURI();
                mediaRecorder.setOutputFile(soundFile.getAbsolutePath());
                try {
                    mediaRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaRecorder.start();
            }
        });

        stopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSources.add(currentAudioSourcePath.toString());
                adapter.notifyDataSetChanged();
                counter++;
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,audioSources);
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, Uri.parse(audioSources.get(position)));
                mediaPlayer.setScreenOnWhilePlaying(true);
                mediaPlayer.start();
            }
        });

    }
}
