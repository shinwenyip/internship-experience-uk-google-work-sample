package com.google;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video current;
  private boolean paused;
  private HashMap<String, VideoPlaylist>  playlists;
//  private HashSet<VideoPlaylist>  playlists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.playlists = new HashMap<>();
    this.current = null;
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.printf("Here's a list of all available videos:%n");
    ArrayList<String> vids = new ArrayList<>();
    videoLibrary.getVideos().forEach(
            video -> vids.add(
                    video.toString()
            ));
    vids.stream().sorted().forEach(video -> System.out.printf(video + "%n"));
//            video -> System.out.printf("%s (%s) [%s]%n",video.getTitle(),video.getVideoId(),String.join(" ",video.getTags()) ));
  }

  public void playVideo(String videoId) {
    Video playing = videoLibrary.getVideo(videoId);
    if (playing==null){
      System.out.printf("Cannot play video: Video does not exist%n");
      return;
    }
    if (this.current!=null){
      System.out.printf("Stopping video: %s%n",this.current.getTitle());
    }
    System.out.printf("Playing video: %s%n",playing.getTitle());
    this.current = playing;
    this.paused = false;
  }

  public void stopVideo() {
    if (this.current!=null){
      System.out.printf("Stopping video: %s%n",this.current.getTitle());
      this.current=null;
      return;
    }
    System.out.printf("Cannot stop video: No video is currently playing%n");
  }

  public void playRandomVideo() {
    Random r = new Random();
    Video random = videoLibrary.getVideos().get(r.nextInt(videoLibrary.getVideos().size()));
    if (this.current!=null){
      System.out.printf("Stopping video: %s%n",this.current.getTitle());
    }
    this.current = random;
    System.out.printf("Playing video: %s%n",random.getTitle());
  }

  public void pauseVideo() {
    if (current == null){
      System.out.printf("Cannot pause video: No video is currently playing%n");return;
    }
    if (paused){
      System.out.printf("Video already paused: %s%n",this.current.getTitle());
    }else{
      System.out.printf("Pausing video: %s%n",this.current.getTitle());
      this.paused = true;
    }
  }

  public void continueVideo() {
    if (this.current==null){
      System.out.printf("Cannot continue video: No video is currently playing%n");return;
    }
    if (!paused){
      System.out.printf("Cannot continue video: Video is not paused%n");
    }else{
      System.out.printf("Continuing video: %s%n",this.current.getTitle());
      this.paused=false;
    }
  }

  public void showPlaying() {
    if (this.current==null){
      System.out.printf("No video is currently playing%n");return;
    }
    System.out.printf("Currently playing: " + current.toString());

    if (paused){
      System.out.printf(" - PAUSED%n");
    }else{
      System.out.printf("%n");
    }
  }

  public void createPlaylist(String playlistName) {
    if (playlists.containsKey(playlistName.toUpperCase())){
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
      return;
    }
    playlists.put(playlistName.toUpperCase(),new VideoPlaylist(playlistName));
    System.out.printf("Successfully created new playlist: %s%n",playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = playlists.get(playlistName.toUpperCase());
    if (playlist == null){ System.out.printf("Cannot add video to %s: Playlist does not exist%n",playlistName);return;}
    Video v = videoLibrary.getVideo(videoId);
    if (v == null){ System.out.printf("Cannot add video to %s: Video does not exist%n",playlistName);return;}

    if (!playlist.contains(v)){
      playlist.addVideo(v);
      System.out.printf("Added video to %s: %s%n",playlistName,v.getTitle());
    }else{
      System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
    }
  }

  public void showAllPlaylists() {
    if (playlists.isEmpty()){
      System.out.println("No playlists exist yet");return;
    }
    System.out.println("Showing all playlists:");
    ArrayList<VideoPlaylist> l = new ArrayList<VideoPlaylist>(playlists.values());
    Collections.reverse(l);
    l.forEach(vp -> System.out.println(vp.getPlaylist_name()));
  }

  public void showPlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toUpperCase())){
      System.out.printf("Cannot show playlist %s: Playlist does not exist%n",playlistName); return;
    }
    VideoPlaylist p = playlists.get(playlistName.toUpperCase());
    System.out.printf("Showing playlist: %s%n", playlistName);
    if (p.isEmpty()){
      System.out.printf("No videos here yet%n");
    }else{
      p.getPlaylistVideos().forEach(v -> System.out.println(v.toString()));
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist p = playlists.get(playlistName.toUpperCase());
    if (p==null){ System.out.printf("Cannot remove video from %s: Playlist does not exist%n",playlistName); return; }
    Video v = videoLibrary.getVideo(videoId);
    if (v==null){ System.out.printf("Cannot remove video from %s: Video does not exist%n",playlistName); return;}
    if (p.contains(v)){
      p.remove(v);
      System.out.printf("Removed video from %s: %s%n",playlistName, v.getTitle());
    }else{
      System.out.printf("Cannot remove video from %s: Video is not in playlist%n",playlistName);
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist p = playlists.get(playlistName.toUpperCase());
    if (p==null){ System.out.printf("Cannot clear playlist %s: Playlist does not exist%n",playlistName); return; }
    p.clearPlaylist();
    System.out.printf("Successfully removed all videos from %s%n", playlistName);
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist p = playlists.get(playlistName.toUpperCase());
    if (p==null){ System.out.printf("Cannot delete playlist %s: Playlist does not exist%n",playlistName); return; }
    playlists.remove(playlistName.toUpperCase());
    System.out.printf("Deleted playlist: %s%n", playlistName);
  }

  public void searchVideos(String searchTerm) {
    Pattern p = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher("");

    ArrayList<Video> results = videoLibrary.getVideos().stream().filter(video ->
      {
        m.reset(video.getTitle());
        return m.find();
      }).sorted(new SortbyTitle()).collect(Collectors.toCollection(ArrayList::new));

    //If no results
    if (results.size()<1){
      System.out.printf("No search results for %s%n",searchTerm);
      return;
    }

    System.out.printf("Here are the results for %s:%n",searchTerm);
    for (int i =0; i < results.size(); i++) {
      System.out.printf("%d) %s%n",i+1,results.get(i).toString());
    }
    var scanner = new Scanner(System.in);
    System.out.printf("Would you like to play any of the above? If yes, specify the number of the video.%n " +
            "If your answer is not a valid number, we will assume it's a no.%n");
    var input = scanner.nextLine();
    try {
      int ans =  Integer.parseInt(input);
      if (ans <=results.size()){
        this.playVideo(results.get(ans-1).getVideoId());
      }
      System.out.println();
      return;
    }catch(Exception e){
      System.out.println();
      return;
    }
  }

  public void searchVideosWithTag(String videoTag) {
    Pattern p = Pattern.compile(videoTag, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher("");

    ArrayList<Video> results = videoLibrary.getVideos().stream().filter(video ->
    {
      m.reset(video.toString().split("\\[",2)[1]);
      return m.find();
    }).sorted(new SortbyTitle()).collect(Collectors.toCollection(ArrayList::new));

    //If no results
    if (results.size()<1){
      System.out.printf("No search results for %s%n",videoTag);
      return;
    }

    System.out.printf("Here are the results for %s:%n",videoTag);
    for (int i =0; i < results.size(); i++) {
      System.out.printf("%d) %s%n",i+1,results.get(i).toString());
    }
    var scanner = new Scanner(System.in);
    System.out.printf("Would you like to play any of the above? If yes, specify the number of the video.%n " +
            "If your answer is not a valid number, we will assume it's a no.%n");
    var input = scanner.nextLine();
    try {
      int ans =  Integer.parseInt(input);
      if (ans <=results.size()){
        this.playVideo(results.get(ans-1).getVideoId());
      }
      System.out.println();
      return;
    }catch(Exception e){
      System.out.println();
      return;
    }
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}