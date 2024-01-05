package com.example.mediaplayer;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.File;

public class HelloController {
    private MediaPlayer mp;
    private Media me;

    @FXML
    private Button btnPlay;
    @FXML
    private Slider sldTime;
    @FXML
    private Label lblTime;

    private boolean isPlaying;

    @FXML
    private MediaView mediaView;

    @FXML
    void btnMediaClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String url = selectedFile.toURI().toString();
            me = new Media(url);
            mp = new MediaPlayer(me);
            mediaView.setMediaPlayer(mp);

            mp.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
                sldTime.setValue(newValue.toSeconds());
                lblTime.setText("Time: " + (int)sldTime.getValue() + "/" + (int)me.getDuration().toSeconds());
            }));

            mp.setOnReady(() ->{
                Duration totalDuration = me.getDuration();
                sldTime.setMax(totalDuration.toSeconds());
                lblTime.setText("Time: 00 /" + (int)me.getDuration().toSeconds());
            });

            Scene scene = mediaView.getScene();
            mediaView.fitWidthProperty().bind(scene.widthProperty());
            mediaView.fitHeightProperty().bind(scene.heightProperty());

            mp.setAutoPlay(false);
            isPlaying = false;
        }
    }

    @FXML
    void btnPlayClick() {
        if (isPlaying) {
            btnPlay.setText("Play");
            mp.pause();
            isPlaying = false;
        } else {
            btnPlay.setText("Pause");
            mp.play();
            isPlaying = true;
        }
    }

    @FXML
    void btnStopClick() {
        btnPlay.setText("Play");
        mp.stop();
        isPlaying = false;
    }

    @FXML
    void sldTimePress() {
        mp.seek(Duration.seconds(sldTime.getValue()));
    }
}