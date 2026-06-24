package com.example.myapplication1111;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etSearchQuery;
    private Button btnSearch;
    private RecyclerView recyclerViewVideos;
    private ProgressBar progressBar;
    private VideoAdapter videoAdapter;
    private List<VideoModel> videoList;

    // ثوابت الرابط والـ API Key المرفق في الواجب
    private final String API_KEY = "AIzaSyAEk7F_bbhTFUWxwJXDn5fzxviwCJYk7EY";
    private final String MAX_RESULTS = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearchQuery = findViewById(R.id.etSearchQuery);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        progressBar = findViewById(R.id.progressBar);

        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));
        videoList = new ArrayList<>();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etSearchQuery.getText().toString().trim();

                // 5. معالجة الأخطاء: حقل البحث فارغ
                if (query.isEmpty()) {
                    Toast.makeText(MainActivity.this, "الرجاء إدخال كلمة للبحث!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // بدء جلب الفيديوهات عبر الآسينك تاسك Off the main thread
                new FetchYouTubeDataTask().execute(query);
            }
        });
    }

    // كلاس AsyncTask لجلب تفاصيل الـ API في الخلفية وتفكيك الـ JSON
    private class FetchYouTubeDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // إظهار الـ ProgressBar عند بدء التنزيل
            progressBar.setVisibility(View.VISIBLE);
            videoList.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            String searchQuery = params[0];
            try {
                // ترميز النص المدخل ليتوافق مع الروابط البرمجية URL
                String encodedQuery = URLEncoder.encode(searchQuery, "UTF-8");

                // بناء الرابط كما في نص سؤال الواجب تماماً
                String urlString = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q="
                        + encodedQuery + "&maxResults=" + MAX_RESULTS + "&key=" + API_KEY;

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // قراءة البيانات النصية القادمة من جوجل
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null; // سيرجع null في حال حدوث مشكلة في الشبكة
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // إخفاء مؤشر التحميل فور الانتهاء
            progressBar.setVisibility(View.GONE);

            // 5. معالجة الأخطاء: في حال فشل الاتصال بالشبكة
            if (result == null) {
                Toast.makeText(MainActivity.this, "فشل في جلب البيانات! تأكد من اتصال الإنترنت", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                // تحليل الـ JSON واستخراج المتغيرات الخمسة المطلوبة
                JSONObject jsonObject = new JSONObject(result);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                // 5. معالجة الأخطاء: عدم وجود أي نتائج
                if (itemsArray.length() == 0) {
                    Toast.makeText(MainActivity.this, "لم يتم العثور على أي فيديوهات مطابقة!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");

                    String title = snippet.getString("title");
                    String description = snippet.getString("description");
                    String publishTime = snippet.getString("publishedAt");
                    String channelTitle = snippet.getString("channelTitle");

                    // جلب مسار الصورة المصغرة عالية الجودة
                    String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                    // إضافة البيانات للكوليكشن
                    videoList.add(new VideoModel(title, description, publishTime, channelTitle, thumbnailUrl));
                }

                // عرض وتحديث البيانات داخل الـ RecyclerView
                videoAdapter = new VideoAdapter(videoList);
                recyclerViewVideos.setAdapter(videoAdapter);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "حدث خطأ أثناء معالجة بيانات الـ JSON!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}