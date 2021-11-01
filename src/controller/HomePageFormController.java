package controller;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomePageFormController {

    public ImageView imageView;
    public AnchorPane context;
    public ProgressBar progressBar;
    public ProgressIndicator progressRange;
    public Label lblHeading;

    public void initialize(){
        Animation(imageView);
        new ShowSplashScreen().start();
    }

    public void Animation(ImageView imageView){
        ScaleTransition scaleTransition=new ScaleTransition();
        scaleTransition.setNode(imageView);
        scaleTransition.setDuration(Duration.millis(600));
        scaleTransition.setCycleCount(TranslateTransition.INDEFINITE);
        scaleTransition.setInterpolator(Interpolator.LINEAR);
        scaleTransition.setByX(2.0);
        scaleTransition.setByY(2.0);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

   /* public void goooooooooooooo(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/LoginForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) context.getScene().getWindow();
        window.setScene(new Scene(load));
    }*/

    class ShowSplashScreen extends Thread{
        public void run(){
            try{
                for (int i = 0 ; i <= 10 ; i++){
                    // prograssind.setVisible(false);
                    double x = i * (0.1);
                    //System.out.println(x);
                    progressBar.setProgress(x);
                    progressRange.setProgress(x);


                    if(i*10 == 100){
                        progressRange.setVisible(true);
                        progressRange.setProgress(1);

                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                    context.getScene().getWindow().hide();
                });
            } catch (Exception ex) {
                Logger.getLogger(HomePageFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
