package com.aveeboy.studyflow;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int forest = Color.rgb(46,107,79), cream = Color.rgb(245,241,230), ink = Color.rgb(30,41,35), muted = Color.rgb(117,128,120);
    private boolean dark = false, sinhala = false, running = false;
    private String screen = "Home";
    private long startedAt = 0, elapsedBefore = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView timerText;
    private SharedPreferences prefs;
    private final Runnable ticker = new Runnable() { public void run() { if (running && timerText != null) { long ms = elapsedBefore + System.currentTimeMillis() - startedAt; timerText.setText(format(ms)); handler.postDelayed(this, 500); } } };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("studyflow", MODE_PRIVATE);
        dark = prefs.getBoolean("dark", false); sinhala = prefs.getBoolean("sinhala", false);
        showScreen("Home");
    }
    private String format(long ms) { long s=ms/1000; return String.format(Locale.getDefault(), "%02d:%02d:%02d", s/3600,(s/60)%60,s%60); }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); t.setPadding(0, dp(6), 0, dp(6)); return t;
    }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
    private LinearLayout column() { LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(dp(20),dp(12),dp(20),dp(18)); l.setBackgroundColor(dark?Color.rgb(21,29,25):cream); return l; }
    private Button button(String label, Runnable action) { Button b=new Button(this); b.setText(label); b.setAllCaps(false); b.setTextColor(dark?cream:forest); b.setOnClickListener(v->action.run()); return b; }
    private void showScreen(String target) {
        screen=target; LinearLayout root=column(); root.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
        LinearLayout header=new LinearLayout(this); header.setGravity(Gravity.CENTER_VERTICAL); header.setOrientation(LinearLayout.HORIZONTAL);
        TextView brand=text("StudyFlow  🌱",23,dark?cream:forest,true); header.addView(brand,new LinearLayout.LayoutParams(0,-2,1));
        Button settings=button("⚙",()->showSettings()); header.addView(settings); root.addView(header);
        ScrollView scroll=new ScrollView(this); LinearLayout body=column(); body.setPadding(0,dp(10),0,dp(12)); scroll.addView(body);
        body.addView(text(target,27,dark?cream:ink,true));
        if (target.equals("Home")) {
            body.addView(text(sinhala?"අද දවසේ කුඩා පියවරක්, හෙට ලොකු වර්ධනයක්.":"Small steps today, big growth tomorrow.",16,dark?cream:muted,false));
            card(body,"Today's study time",format(elapsedBefore + (running?System.currentTimeMillis()-startedAt:0)));
            card(body,"Today's tasks","Your task list will appear here in the next development stage.");
            card(body,"Daily goal","2 hours · Change this in Settings");
        } else if (target.equals("Tasks")) {
            body.addView(text("Daily tasks",18,dark?cream:ink,true)); body.addView(text("Task creation, priorities and saved history are planned for the next stage.",15,dark?cream:muted,false));
            body.addView(button("＋  Add task (coming next)",()->toast("Task saving is not implemented in this starter yet.")));
        } else if (target.equals("Focus")) {
            body.addView(text("Stopwatch",18,dark?cream:ink,true)); timerText=text(format(elapsedBefore),38,dark?cream:forest,true); timerText.setGravity(Gravity.CENTER); timerText.setPadding(0,dp(25),0,dp(25)); body.addView(timerText);
            body.addView(button(running?"Pause study timer":"Start study timer",()->toggleTimer()));
            body.addView(button("Reset display",()->{if(running){elapsedBefore+=System.currentTimeMillis()-startedAt;running=false;handler.removeCallbacks(ticker);getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);} elapsedBefore=0;showScreen("Focus");}));
            body.addView(text("Countdown and Pomodoro modes will be added after the starter build is confirmed.",14,dark?cream:muted,false));
            if(running){getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);handler.post(ticker);}
        } else if (target.equals("My Plant")) {
            body.addView(text("🌱",70,forest,false)); body.addView(text("Your study plant",20,dark?cream:ink,true)); body.addView(text("Plant growth is connected to recorded study time in a later stage.",15,dark?cream:muted,false));
            body.addView(text("Sakura · Lavender · Sunflower · Cherry Blossom · Tulip · Lotus",14,dark?cream:muted,false));
        } else if (target.equals("Progress")) {
            card(body,"Daily progress","Study time and task completion summaries"); card(body,"Weekly progress","Your week at a glance"); card(body,"Achievements","Your milestones will appear here");
        } else if (target.equals("Settings")) {
            body.addView(button("Language: "+(sinhala?"සිංහල":"English"),()->{sinhala=!sinhala;prefs.edit().putBoolean("sinhala",sinhala).apply();showScreen("Settings");}));
            body.addView(button("Theme: "+(dark?"Dark":"Light"),()->{dark=!dark;prefs.edit().putBoolean("dark",dark).apply();showScreen("Settings");}));
            body.addView(text("Daily target: 2 hours (editable settings will be implemented next).",15,dark?cream:muted,false));
            body.addView(text("Created by Aveesha Shahindu",13,muted,false));
        }
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=new LinearLayout(this); nav.setOrientation(LinearLayout.HORIZONTAL); nav.setGravity(Gravity.CENTER); nav.setBackgroundColor(dark?Color.rgb(34,45,39):Color.WHITE);
        String[] items={"Home","Tasks","Focus","My Plant","Progress"};
        for(String item:items){Button b=button(item,()->showScreen(item));b.setTextSize(10);b.setPadding(dp(2),dp(4),dp(2),dp(4));nav.addView(b,new LinearLayout.LayoutParams(0,dp(56),1));}
        root.addView(nav); setContentView(root);
    }
    private void card(LinearLayout parent,String title,String value){ LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(dp(15),dp(10),dp(15),dp(10));box.setBackgroundColor(dark?Color.rgb(34,45,39):Color.WHITE); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(7),0,dp(7));box.setLayoutParams(p);box.addView(text(title,15,forest,true));box.addView(text(value,17,dark?cream:ink,false));parent.addView(box); }
    private void toggleTimer(){ if(!running){running=true;startedAt=System.currentTimeMillis();getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);handler.post(ticker);}else{elapsedBefore+=System.currentTimeMillis()-startedAt;running=false;handler.removeCallbacks(ticker);getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);} showScreen("Focus"); }
    private void showSettings(){showScreen("Settings");}
    private void toast(String message){android.widget.Toast.makeText(this,message,android.widget.Toast.LENGTH_SHORT).show();}
    @Override protected void onPause(){super.onPause();prefs.edit().putLong("elapsed",elapsedBefore).apply();}
    @Override protected void onDestroy(){super.onDestroy();handler.removeCallbacks(ticker);}
}
