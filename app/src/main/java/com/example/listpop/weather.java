
package com.example.listpop;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class weather extends AppCompatActivity
{
    String TAG = MainActivity.class.getSimpleName()+"My";
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        catchData();
    }

    private void catchData()
    {
        String catchData="https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-9EDA9DBB-0064-4EC1-B252-54A9AF849C6D&format=JSON&elementName=PoP";
        ProgressDialog dialog = ProgressDialog.show(this,"讀取中"
                ,"請稍候",true);
        new Thread(()->{
            try {
                //網路連接
                URL url = new URL(catchData);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                //可以增加的字串
                StringBuffer js = new StringBuffer();
                while (line != null) {
                    js.append(line);
                    line = in.readLine();
                }

                String json=new String(js);

                JSONObject weather=new JSONObject(json);
                JSONArray jsonOb = weather.getJSONObject("records").getJSONArray("location");//進入資料層


                for(int i=0;i<jsonOb.length();i++)
                {
                    JSONObject jsonObject = jsonOb.getJSONObject(i);//location list
                    String loca=jsonObject.getString("locationName");//取得locationname

                    JSONArray w = jsonObject.getJSONArray("weatherElement");//進入weatherElement
                    JSONObject jO2 = w.getJSONObject(0);

                    JSONArray t = jO2.getJSONArray("time");
                    JSONObject jO3 = t.getJSONObject(0);
                    String star=jO3.getString("startTime");
                    String end=jO3.getString("endTime");
                    JSONObject p=jO3.getJSONObject("parameter");
                    String pop=p.getString("parameterName");

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("locationName",loca);
                    hashMap.put("startTime",star);
                    hashMap.put("endTime",end);
                    hashMap.put("parameterName",pop);

                    arrayList.add(hashMap);
                }
                Log.d(TAG, "catchData: "+arrayList);

                runOnUiThread(()->{
                    dialog.dismiss();
                    RecyclerView recyclerView;
                    MyAdapter myAdapter;
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                    myAdapter = new MyAdapter();
                    recyclerView.setAdapter(myAdapter);
                });



            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView s, e, p, loc;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                s = itemView.findViewById(R.id.textView_begin);
                e = itemView.findViewById(R.id.textView_end);
                p = itemView.findViewById(R.id.textView_pop);
                loc = itemView.findViewById(R.id.textView_location);
            }
        }

        //連接剛才寫的layout檔案，return一個View
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_item, parent, false);
            return new ViewHolder(view);
        }

        //在這裡取得元件的控制(每個item內的控制)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.loc.setText(arrayList.get(position).get("locationName"));
            holder.s.setText("起始時間："+arrayList.get(position).get("startTime"));
            holder.e.setText("結束時間："+arrayList.get(position).get("endTime"));
            holder.p.setText("降雨機率：" + arrayList.get(position).get("parameterName")+"%");
        }

        //取得顯示數量，return一個int，通常都會return陣列長度(arrayList.size)
        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }


}

































