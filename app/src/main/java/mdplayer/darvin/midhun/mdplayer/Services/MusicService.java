package mdplayer.darvin.midhun.mdplayer.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

import mdplayer.darvin.midhun.mdplayer.MainActivity;
import mdplayer.darvin.midhun.mdplayer.R;
import mdplayer.darvin.midhun.mdplayer.Song;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener{

    //media player
    public MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private String songTitle="";
    private static final int NOTIFY_ID=1;

    private final IBinder musicBind = new MusicBinder();

    public MusicService() {
    }

    @Override
    public void onCreate() {
        Log.e("check", "service has started");
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        /*Set the class as listener for (1) when the MediaPlayer instance is prepared, (2) when a song has completed playback, and when(3)
        an error is thrown*/
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    public void playSong(){
        //play a song
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);
        //get song title
        songTitle = playSong.getTitle();
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(player.getCurrentPosition()>0){
            player.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        player.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        //start playback
        player.start();

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
        .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);

    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public int getPosition(){
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void startPlayer(){
        player.start();
    }

    public void playPrev(){
        songPosn--;
        if(songPosn < 0) songPosn=songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        songPosn++;
        if(songPosn >= songs.size()) songPosn=0;
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }



}
