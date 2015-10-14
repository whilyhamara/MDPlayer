package mdplayer.darvin.midhun.mdplayer;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mdplayer.darvin.midhun.mdplayer.Services.MusicService;

public class MusicFragment extends Fragment {

    Menu menu;
    public ArrayList<Song> songList;
    private ListView songListView;
    public MusicService musicSrv;
    private Intent playIntent;
    public boolean musicBound=false;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        songListView = (ListView) view.findViewById(R.id.song_list);
        songList = new ArrayList<Song>();

        songList = ((MainActivity) getActivity()).songList;

        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdt = new SongAdapter(getActivity(), songList);
        songListView.setAdapter(songAdt);

        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
                musicSrv.playSong();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.music_menu, menu);
        this.menu = menu;
    }

    @Override
    public void onStart() {
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
        super.onStart();
    }


    public ServiceConnection  musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            Log.d("check", "Service connected");
            //pass list
            musicSrv.setList(songList);
//            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            musicBound = false;
        }
    };
}
