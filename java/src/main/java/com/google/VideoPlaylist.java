package com.google;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {

//    private HashSet<Video> playlist;
    private List<Video> playlist;
    private String playlist_name;

    VideoPlaylist(String playlist_name){
        this.playlist_name = playlist_name;
        this.playlist = new ArrayList<>();
    }

//    public boolean equals(final Object obj){
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        final VideoPlaylist other = (VideoPlaylist) obj;
//        if (playlist_name == null) {
//            if (other.playlist_name != null)
//                return false;
//        } else if (!playlist_name.toUpperCase().equals(other.playlist_name.toUpperCase()))
//            return false;
//        return true;
//    }
//    public int hashCode(){
//        final int prime = 31;
//        int result = 1;
//        result = prime * result
//                + ((playlist_name == null) ? 0 : playlist_name.hashCode());
//        return result;
//    }

    String getPlaylist_name(){ return playlist_name;}

    void addVideo(Video v){
        playlist.add(v);
    }
    boolean contains(Video v){
        return playlist.contains(v);
    }
    List<Video> getPlaylistVideos(){
        return playlist;
    }
    boolean isEmpty(){ return playlist.isEmpty();}
    void remove(Video v){
        playlist.remove(v);
    }
    void clearPlaylist(){ playlist.clear();}
}
// ArrayList<VideoPlaylist>