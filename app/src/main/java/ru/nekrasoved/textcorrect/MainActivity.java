package ru.nekrasoved.textcorrect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    public static String inpText;
    public static String outText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Сожми свой текст");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4799E8")));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //скрыть панель навигации начало

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(flags);

        final View decorView = getWindow().getDecorView();
        decorView
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                {

                    @Override
                    public void onSystemUiVisibilityChange(int visibility)
                    {
                        if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                        {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });

        //скрыть панель навигации конец

        Button btSubmit = (Button) findViewById(R.id.btSubmit);
        final EditText etInputText = (EditText) findViewById(R.id.etInputText);
        final EditText etProc = (EditText) findViewById(R.id.etProc);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    inpText = etInputText.getText().toString();
                    String check = etProc.getText().toString();
                    if ((check.length() > 0)&&(inpText.length() > 0)){
                        Text refer = new Text(inpText);
                        refer.correctText(Integer.valueOf(etProc.getText().toString()));
                        outText = refer.getText();

                        Intent intent = new Intent(MainActivity.this, TextActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Для начала заполните поля!", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){

                }

            }
        });
    }

    public static class Text{
        ArrayList<Sentence> data = new ArrayList<>();
        int count;
        HashSet<Character> punctuation = new HashSet<>();
        HashSet<String> punctuationCheck = new HashSet<>();

        HashSet<String> trashWord = new HashSet<>();
        ArrayList<String> word = new ArrayList<>();
        ArrayList<Integer> size = new ArrayList<>();

        int[][] sortSentence;

        int sizeWord;
        int countWord;

        private void setPunctuation() {
//            punctuation.add(',');
//            punctuation.add(';');
//            punctuation.add('"');
//            punctuation.add(':');
//            punctuation.add('-');
            punctuation.add('\n');
//            punctuation.add('«');
//            punctuation.add('»');
            punctuation.add('.');
            punctuation.add('?');
            punctuation.add('!');

            punctuationCheck.add(",");
            punctuationCheck.add(";");
            punctuationCheck.add("" + '"');
            punctuationCheck.add(":");
            punctuationCheck.add("-");
            punctuationCheck.add("\n");
            punctuationCheck.add("«");
            punctuationCheck.add("»");
            punctuationCheck.add(".");
            punctuationCheck.add("?");
            punctuationCheck.add("!");
        }

        private void setTrashWord() {
            trashWord.add("а");
            trashWord.add("б");
            trashWord.add("в");
            trashWord.add("ж");
            trashWord.add("и");
            trashWord.add("к");
            trashWord.add("о");
            trashWord.add("с");
            trashWord.add("у");
            trashWord.add("э");
            trashWord.add("я");
            trashWord.add("ага");
            trashWord.add("да");
            trashWord.add("нет");
            trashWord.add("не");
            trashWord.add("ты");
            trashWord.add("он");
            trashWord.add("она");
            trashWord.add("они");
            trashWord.add("мы");
            trashWord.add("вы");
            trashWord.add("тебя");
            trashWord.add("его");
            trashWord.add("её");
            trashWord.add("ее");
            trashWord.add("их");
            trashWord.add("нас");
            trashWord.add("вас");
            trashWord.add("меня");
            trashWord.add("мой");
            trashWord.add("твой");
            trashWord.add("наш");
            trashWord.add("ваш");
            trashWord.add("но");
            trashWord.add("или");
            trashWord.add("что");
            trashWord.add("где");
            trashWord.add("когда");
            trashWord.add("куда");
            trashWord.add("откуда");
            trashWord.add("почему");
            trashWord.add("зачем");
            trashWord.add("как");
            trashWord.add("словно");
            trashWord.add("бы");
            trashWord.add("чтобы");
        }

        Text(String str){
            String a = "";
            int j = 0;
            setPunctuation();
            setTrashWord();
            for (int i = 0; i < str.length(); i++){
                if (!(punctuation.contains(str.charAt(i)))){
                    a += str.charAt(i);
                }
                else {
                    a += str.charAt(i);
                    data.add(new Sentence(a));
                    a = "";
                    j++;
                }
            }
            if (a.length() > 0){
                data.add(new Sentence(a));
                a = "";
                j++;
            }
            count = j;
            setSizeWord();
            sizeWord = 0;
            countWord = 0;
            for (int i = 0; i < data.size(); i++){
                data.get(i).setSizeSentence();
                sizeWord += data.get(i).sizeSentence;
            }
            for (int i = 0; i < data.size(); i++){
                for (int k = 0; k < data.get(i).data.size(); k++){
                    if (!punctuationCheck.contains(data.get(i).data.get(k).getWord())){
                        countWord++;
                    }
                }
            }

        }

        private String getSentence(int ind){
            return data.get(ind).getSentence();
        }

        public String getText(){
            String a = "";
            for(int j = 0; j < count; j++){
                a += getSentence(j) + " ";
            }
            return a;
        }

        private void setSizeWord(){
            int k = 0;
            String checkWord;
            for (int i = 0; i < data.size(); i++){
                for (int j = 0; j < data.get(i).data.size(); j++){
                    checkWord = data.get(i).data.get(j).data;

                    if ((!(trashWord.contains(checkWord)))&&(!(punctuationCheck.contains(checkWord)))){
                        if (!(word.contains(checkWord))){
                            word.add(k, data.get(i).data.get(j).data);
                            size.add(k, 1);
                            k ++;
                        }
                        else {
                            size.add(word.indexOf(checkWord),size.get(word.indexOf(checkWord))+1);
                            size.remove(word.indexOf(checkWord) + 1);
                        }
                    }
                    else {
                        if (!(word.contains(checkWord))){
                            word.add(k, data.get(i).data.get(j).data);
                            size.add(k, 0);
                            k ++;
                        }
                    }
                }
            }
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).data.size(); j++) {
                    String check = data.get(i).data.get(j).data;
                    k = word.indexOf(check);
                    data.get(i).data.get(j).sizeWord = size.get(k);
                }
            }
        }

        private void getSizeWord(){
            for (int i = 0; i < word.size(); i++){
                System.out.println(word.get(i) + " = " + size.get(i));
            }
        }

        public void correctText(int N){
            sortSentence = new int[data.size()][2];
            for (int i = 0; i < data.size(); i++){
                sortSentence[i][0] = i;
                sortSentence[i][1] = data.get(i).sizeSentence;
            }

            for (int i = 0; i < sortSentence.length - 1; i++){
                for (int j = i + 1; j < sortSentence.length; j++){
                    int a, b;
                    if (sortSentence[i][1] > sortSentence[j][1]){
                        a = sortSentence[i][1];
                        b = sortSentence[i][0];
                        sortSentence[i][1] = sortSentence[j][1];
                        sortSentence[i][0] = sortSentence[j][0];
                        sortSentence[j][1] = a;
                        sortSentence[j][0] = b;
                    }
                }
            }

            int countDel = (data.size()*N)/100;
            for (int i = 0; i < countDel; i++){
                sortSentence[i][0] = 0;
                sortSentence[i][1] = 0;
            }

            for (int i = 0; i < sortSentence.length - 1; i++){
                for (int j = i + 1; j < sortSentence.length; j++){
                    int a, b;
                    if (sortSentence[i][0] > sortSentence[j][0]){
                        a = sortSentence[i][1];
                        b = sortSentence[i][0];
                        sortSentence[i][1] = sortSentence[j][1];
                        sortSentence[i][0] = sortSentence[j][0];
                        sortSentence[j][1] = a;
                        sortSentence[j][0] = b;
                    }
                }
            }

            Text newText = new Text("");
            for (int i = countDel; i < sortSentence.length; i++){
                newText.data.add(data.get(sortSentence[i][0]));
            }
            int l = data.size();
            for (int i = 0; i < l; i++){
                data.remove(0);
            }
            for (int i = 0; i < newText.data.size(); i++){
                data.add(newText.data.get(i));
            }
            count = data.size();
        }
    }


    public static class Sentence{
        int count;
        int sizeSentence;
        ArrayList<Word> data = new ArrayList<>() ;
        HashSet<Character> punctuation = new HashSet<>();

        private void setPunctuation() {
            punctuation.add(',');
            punctuation.add(';');
            punctuation.add('"');
            punctuation.add(':');
//            punctuation.add('-');
            punctuation.add('\n');
            punctuation.add('«');
            punctuation.add('»');
            punctuation.add('.');
            punctuation.add('?');
            punctuation.add('!');
        }

        Sentence(String string){
            setPunctuation();
            String a = "";
            int j = 0;
            for (int i = 0; i < string.length(); i++){

                if ((string.charAt(i) != ' ')&&(!punctuation.contains(string.charAt(i)))){
                    a += string.charAt(i);
                }
                else {
                    if ((a.length() > 0)&&(!punctuation.contains(a.charAt(0)))){
                        data.add(new Word(a));
                        a = "";
                        j++;
                    }
                }
                if (punctuation.contains(string.charAt(i))){
                    a = "" + string.charAt(i);
                    data.add(new Word(a));
                    a = "";
                    j++;
                }
            }
            if (a.length() > 0){
                data.add(new Word(a));
                a = "";
                j++;
            }
            count = j;
        }

        public String getWord(int ind){
            return data.get(ind).getWord();
        }

        public String getSentence(){
            String a = "";
            for(int j = 0; j < count; j++){
                a += getWord(j) + " ";
            }
            return a;
        }

        private void setSizeSentence(){
            sizeSentence = 0;
            for (int i = 0; i < data.size(); i++){
                sizeSentence += data.get(i).sizeWord;
            }
        }
    }

    public static class Word{
        String data;
        int sizeWord;

        Word(String str){
            data = str;
            sizeWord = 0;
        }

        public String getWord(){
            return data;
        }
    }

    //отслеживание нажатий на экран

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
