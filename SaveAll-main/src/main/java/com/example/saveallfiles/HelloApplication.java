package com.example.saveallfiles;

import com.example.saveallfiles.org.json.simple.JSONObject;
import com.example.saveallfiles.org.json.simple.parser.JSONParser;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;


/*---------------back end---------------*/
class Search extends Thread{

    /*------------------------------------------------------------*/
    @Override
    public void run(){
        refactor();
        HelloApplication h1 = new HelloApplication();
        h1.threadend();
    }
    /*------------------------------------------------------------*/
    JSONObject hm = new JSONObject();
    int counter=0;
    int diractory=0;

    private void searchcore(File file){
        try{
            
        for(File fil : file.listFiles()) {
            if(fil.isDirectory()){
                diractory++;
                searchcore(fil);
            }else{
                counter++;
                if(fil.getName().endsWith(".txt")){
                if(hm.containsKey(fil.getName()) ){
                    System.out.println(fil.getName());
                    hm.put(fil.getName(), hm.get(fil.getName())+"\n"+fil.getPath());
                }else{
                    hm.put(fil.getName(),fil.getPath());
                }
                }
            }
        }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void refactor(){
        File f =  new File("C:\\");
        long start = System.nanoTime();
        searchcore(f);
        long end = System.nanoTime();
        long exec = end - start;
        double inSeconds = (double)exec / 1_000_000_000.0;
        System.out.println("***************results***************");
        System.out.println("The program takes "+exec+" nanoseconds that is "+inSeconds+" " +
                "seconds to execute that is "+(inSeconds/60)+" minutes");
        System.out.println("total no of folders    :"+diractory);
        System.out.println("total no of files      :"+counter);
        System.out.println("total no of text files :" + hm.size());
        try{
            FileWriter fr = new FileWriter("source1.json");
            fr.write(hm.toJSONString());
            fr.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

/*--------------------------------------*/

/*---------------front end---------------*/

public class HelloApplication extends Application {

    String saveall = "";
    boolean button = false;
    Boolean selectmode = false;
    VBox vb = new VBox();

    Scene scene = new Scene(vb, 700, 400);
    ProgressIndicator pi = new ProgressIndicator();
    MenuItem newbutton = new MenuItem("new");
    MenuItem openbutton = new MenuItem("open");
    static MenuItem savebutton = new MenuItem("save");
    static MenuItem saveallbutton = new MenuItem("saveall");
    static MenuItem searchbutton = new MenuItem("search");
    MenuItem mi6 = new MenuItem("duplicates");
    TextField tf1 = new TextField();
    BorderPane  bp1 = new BorderPane();
    Label l2 = new Label("file name: ");
    BorderPane bp3 = new BorderPane();
    Button b2 = new Button("enter");

    Menu m1 = new Menu();
    MenuBar mb1 = new MenuBar(m1);
    Menu m2 = new Menu();
    MenuItem me1 = new MenuItem("1.kasi");
    MenuItem me2 = new MenuItem("2.kishore");
    MenuBar mb2 = new MenuBar(m2);
    BorderPane b1 = new BorderPane();
    Label l1 = new Label("Select a file");
    TextArea ta1 = new TextArea();

    public Object  threadend(){
        saveallbutton.setDisable(false);
        savebutton.setDisable(false);
        searchbutton.setDisable(false);
        return null;
    }

    @Override
    public void start(Stage stage) throws IOException {
        //MenuBar -> Menu -> MenuItems//


        //++++++++++++++++++++++//

        l2.setTextFill(Color.web("GREY"));
        bp3.setCenter(l2);
        bp1.setLeft(bp3);
        bp1.setCenter(tf1);
        bp1.setRight(b2);
        //++++++++++++++++++++++//

        m1.getItems().addAll(newbutton,openbutton,savebutton,saveallbutton,searchbutton/*,mi6*/);
        m1.setText("file");
        m2.setText("credits");

        m2.getItems().add(me1);

        m2.getItems().add(me2);

        b1.setCenter(l1);
        b1.setLeft(mb1);
        b1.setRight(mb2);
        vb.getChildren().add(b1);

        ta1.setPrefSize(1000,1000);
        vb.getChildren().add(ta1);

        stage.setTitle("SaveAllFiles");
        stage.setScene(scene);
        stage.show();

        EventHandler newevent = new EventHandler() {
            @Override
            public void handle(Event event) {
                ta1.clear();
                l1.setText("Select a file");
            }
        };

        EventHandler openevevt = new EventHandler() {
            @Override
            public void handle(Event event) {
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(new File("C:\\"));
                File t = fc.showOpenDialog(stage);
                if(t!=null){
                    l1.setText(t.getPath());
                    try {
                        FileReader fr = new FileReader(t.getPath()+"");
                        String newString = "";
                        int temp = fr.read();
                        while(temp!=-1){
                            newString+=(char)temp;
                            temp = fr.read();
                        }
                        ta1.setText(newString);
                        fr.close();
                    }catch (Exception es){}

                }
            }
        };

        EventHandler<Event> temp_enter_button = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                l1.setText(tf1.getText());
                b1.setCenter(l1);
                File file = new File(l1.getText());
                try{
                    file.createNewFile();
                    FileWriter fw1 = new FileWriter(file);
                    fw1.write(ta1.getText());
                    fw1.close();
                    try{
                        JSONParser parser = new JSONParser();
                        FileReader fc = new FileReader("source1.json");
                        Object obj = parser.parse(fc);
                        JSONObject jsonObject = (JSONObject)obj;//like a hashmap
                        String cont = "";
                        int val = 0;
                        fc.close();
                        File tempfile = new File(l1.getText());
                        if(!jsonObject.containsKey(tempfile.getName())){
                            System.out.println("not contains");
                            jsonObject.put(tempfile.getName(), tempfile.getPath());
                            FileWriter fw = new FileWriter("source1.json");
                            fw.write(jsonObject.toJSONString());
                            fw.close();
                        }
                        String value=(String)jsonObject.get(tempfile.getName());
                        if(!value.contains(l1.getText())){
                            jsonObject.put(tempfile.getName(),jsonObject.get(tempfile.getName())+"\n"+tempfile.getPath());
                            value = (String)jsonObject.get(tempfile.getName());
                            FileWriter fww1 = new FileWriter("source1.json");
                            fww1.write(jsonObject.toJSONString());
                            fww1.close();
                        }
                        System.out.println(value);
                    }catch (Exception ep){
                        System.out.println(ep);
                    }
                }
                catch (Exception epe){
                    System.out.println(epe);
                    System.out.println(l1.getText());
                    l1.setText("Select a file");
                }finally {
                    selectmode=false;
                }
            }
        } ;

        EventHandler saveevent = new EventHandler() {
            @Override
            public void handle(Event event) {
                if(savebutton.isDisable())
                {
                    return;
                }
                String loc = l1.getText();
                if(loc!="Select a file") {
                    try {
                        FileWriter fr = new FileWriter(loc);
                        fr.write(ta1.getText());
                        fr.close();
                    } catch (Exception es) {
                    }
                }else{
                    try {
                        DirectoryChooser dr = new DirectoryChooser();
                        File dir = dr.showDialog(stage);
                        if (dir.getPath().toString().equals("C:\\")) {
                            l1.setText("Select a SubFolder");
                            return;
                        }
                        selectmode = true;
                        b1.setCenter(bp1);
                        tf1.setFocusTraversable(true);
                        tf1.requestFocus();
                        tf1.setText(dir.getPath() + "\\");
                        tf1.end();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    b2.setOnAction(em->{
                        temp_enter_button.handle(em);
                    });
                }
            }
        };

        EventHandler<Event> search = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if(searchbutton.isDisable())
                {
                    return;
                }
                if(l1.getText().equals("Select a file")){return;}
                String value = "";
                boolean flag1 = true;
                try{
                    JSONParser parser = new JSONParser();
                    FileReader fc = new FileReader("source1.json");
                    Object obj = parser.parse(fc);
                    JSONObject jsonObject = (JSONObject)obj;
                    String cont = "";
                    int val = 0;
                    fc.close();
                    if(!jsonObject.containsKey(new File(l1.getText()).getName())){
                        System.out.println("not contains");
                        jsonObject.put(new File(l1.getText()).getName(), new File(l1.getText()).getPath());
                        FileWriter fw = new FileWriter("source1.json");
                        fw.write(jsonObject.toJSONString());
                        fw.close();}
                    value=(String)jsonObject.get(new File(l1.getText()).getName());
                    System.out.println(value.contains(l1.getText()));
                    if(!value.contains(l1.getText())){
                        jsonObject.put(new File(l1.getText()).getName(),jsonObject.get(new File(l1.getText()).getName())+"\n"+new File(l1.getText()).getPath());
                        value = (String)jsonObject.get(new File(l1.getText()).getName());
                        System.out.println(2);
                        FileWriter fw1 = new FileWriter("source1.json");
                        fw1.write(jsonObject.toJSONString());
                        fw1.close();
                        System.out.println(3);
                    }
                }catch (Exception ep){
                    System.out.println(ep);
                }
                if(button){
                    Dialog<String> d1 = new Dialog<>();
                    d1.setTitle("SaveAll");
                    d1.getDialogPane().setContentText("do you need to overwrite?");
                    ButtonType bu1 = new ButtonType("YES");
                    ButtonType bu2 = new ButtonType("NO");
                    d1.getDialogPane().getButtonTypes().add(bu2);
                    d1.getDialogPane().getButtonTypes().add(bu1);
                    Optional op =  d1.showAndWait();
                    if(op.isPresent()){
                        if(op.toString().contains("YES")){
                            saveall=value;
                        }else{
                            saveall="";
                        }
                    }
                }else{
                    String files[] = value.split("\n");
                    ListView lv = new ListView<>();
                    ToggleGroup tg = new ToggleGroup();
                    Scene scene1 = new Scene(lv,scene.getWidth(), scene.getHeight(),Color.rgb(20,83,154));
                    for(String valu : files){
                        Button r1 = new Button(valu);
                        lv.getItems().add(r1);
                        r1.setBackground(Background.EMPTY);
                        r1.setTextFill(Color.BLACK);
                        r1.setPrefHeight(20);
                        r1.setOnAction(e->{
                            l1.setText(valu);
                            File t = new File(valu);
                            if(t!=null){
                                l1.setText(t.getPath());
                                try {
                                    FileReader fr = new FileReader(t.getPath()+"");
                                    String newString = "";
                                    int temp = fr.read();
                                    while(temp!=-1){
                                        newString+=(char)temp;
                                        temp = fr.read();
                                    }
                                    ta1.setText(newString);
                                    fr.close();
                                }catch (Exception es){}
                                stage.setScene(scene);
                            }
                        });
                    }
                    stage.setScene(scene1);
                }

            }
        };

        EventHandler saveallevent = new EventHandler() {
            @Override
            public void handle(Event event) {
                if(saveallbutton.isDisable())
                {
                    return;
                }
                button=true;
                search.handle(event);
                System.out.println(saveall);
                if(saveall==""){
                    return;
                }else{
                    String arr[] = saveall.split("\n");
                    for(String da:arr){
                        try{
                            FileWriter fr = new FileWriter(da);
                            fr.write(ta1.getText());
                            fr.close();
                        }catch (Exception ep){}
                    }
                }
                button=false;
            }
        };

        EventHandler<Event> preevent = new EventHandler<Event>() {
            @Override
            public void handle(Event event){
                saveallbutton.setDisable(true);
                savebutton.setDisable(true);
                searchbutton.setDisable(true);
                /*-------------------------------------*/
                Search se = new Search();  //instantiating the thread class
                se.start(); //invoking the run method
                /*-------------------------------------*/
            }
        };



        newbutton.setOnAction(e->{
            newevent.handle(e);
        });

        openbutton.setOnAction(e->{
            openevevt.handle(e);
        });

        savebutton.setOnAction(e->{
            saveevent.handle(e);
        });

        mi6.setOnAction(e->{
            preevent.handle(e);
        });

        saveallbutton.setOnAction(e->{
            button=true;
            saveallevent.handle(e);
        });

        searchbutton.setOnAction(e->{
            button=false;
            search.handle(e);
        });



        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("source1.json"));
            JSONObject jsonObject = (JSONObject)obj;
            if(jsonObject.size()==0){
                preevent.handle(new Event(Event.ANY));
            }
        }catch (Exception e){
            System.out.println(e);
            preevent.handle(new Event(Event.ANY));
        }

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.N,
                    KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.O,
                    KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb3 = new KeyCodeCombination(KeyCode.S,
                    KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb4 = new KeyCodeCombination(KeyCode.A,
                    KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb5 = new KeyCodeCombination(KeyCode.F,
                    KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb6 = new KeyCodeCombination(KeyCode.ENTER,
                    KeyCombination.CONTROL_ANY);
            public void handle(KeyEvent ke) {
                if (keyComb1.match(ke)) {
                      newevent.handle(new Event(Event.ANY));
                }
                else if (keyComb2.match(ke)) {
                    openevevt.handle(new Event(Event.ANY));
                }
                else if (keyComb3.match(ke)) {
                    saveevent.handle(new Event(Event.ANY));
                }
                else if (keyComb4.match(ke)) {
                    saveallevent.handle(new Event(Event.ANY));
                }
                else if (keyComb5.match(ke)) {
                    search.handle(new Event(Event.ANY));
                }
                else if (keyComb6.match(ke) && selectmode) {
                    temp_enter_button.handle(new Event(Event.ANY));
                }
            }
        });
    }

/*---------------------------------------*/


/*---------------Main Method-------------*/
    public static void main(String[] args) {
        launch();
    }
}