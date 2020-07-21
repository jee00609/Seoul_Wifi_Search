package com.example.seoul_wifi_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edit;
    Button button;
    TextView text;

    String key = "키값";
    String data;
    XmlPullParser xpp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit =findViewById(R.id.user_edit);
        button = findViewById(R.id.user_button);
        text = findViewById(R.id.user_text);

    }

    public void mOnClick(View v){

        System.out.println("start!!!!");
        switch( v.getId() ){

            case R.id.user_button:

                // 쓰레드를 생성하여 돌리는 구간
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String num;
                        //2020-06-30
                        int location_num;   //검색해야할 총 개수 1000개가 넘으면 api 한번 가져오는 양을 넘어서 뻑간다.
                        final int api_limit = 1000;

                        num = getXmlData_num();

                        System.out.println("num:"+num);

                        //2020-06-30
                        //오늘에서야 api 1000건을 넘기면 가져 오지 않는 걸 알게 된 민지는 슬픈 것이어요...
                        //해결해보자

                        //ParsesInt 만 해선 쓰레드에서 에러난다.
                        // 문자열 뒤에 \n 같이 온전한 정수형이 들어가지 않기 때문이다.
                        //해결법 substring으로 나눠서 \n 을 뺀다.
                        //https://m.blog.naver.com/PostView.nhn?blogId=sseyoung513&logNo=221079088209&proxyReferer=https:%2F%2Fwww.google.com%2F

                        System.out.println("ERROR THIS IS LOCATION NUM"+num);

                        location_num = Integer.parseInt(num.substring(0, num.length()-1));
                        System.out.println("num.length : "+ num.length());
                        System.out.println("location_num : "+location_num);

                        if(location_num < api_limit){ //1000개 이하일 때
                            System.out.println("while no");
                            String start_num_String = "1";
                            data= getXmlData(start_num_String,num); // 하단의 getData 메소드를 통해 데이터를 파싱

                            //검색 시작!
                            runOnUiThread(new Runnable() {

                                //2020-07-21 카카오톡 맵과 병합할 때 맵포인트를 사용하기 위해 텍스트 split 가능 여부 확인 및 변경
                                @Override
                                public void run() {
                                    text.setText("");

                                    final String save_data = data.toString();
                                    String[] split = split = save_data.split("\n");

                                    for(int i=0;i<split.length;i++){
                                        String[] splitresult = split[i].split(",");
                                        for(int j=0; j<splitresult.length;j++) {
//                                            System.out.println("splits" + j + " = " + splitresult[j]);
                                            text.append(splitresult[j]);
                                        }
                                        text.append("\n");
                                    }
                                }
                            });

                        }
                        else{ //1000개 이상일 때
                            int index_num= 1000;
                            int start_num = 1;  //url에서 api 검색 시작 주소를 나타낸다.
                            String start_num_string;
                            int last_num = 1000;   //url에서 api 마지막 검색 주소를 나타낼 수 도 있다...?
                            String last_num_String;  //url에서 api 검색 시작 주소를 나타낸다.

                            final StringBuffer input_data = new StringBuffer();

                            do{
                                start_num_string = String.valueOf(start_num);
                                last_num_String = String.valueOf(last_num);

                                data = getXmlData(start_num_string,last_num_String);
                                input_data.append(data);
                                System.out.println("while data + "+data);


                                start_num+=1000;
                                last_num+=1000;
                                index_num+=1000;

                            }while(index_num<location_num);

                            List<Wifi> wifiList = new ArrayList<>();
                            Wifi wifiItem = new Wifi();

                            //검색 시작!
                            runOnUiThread(new Runnable() {
                                //2020-07-21 카카오톡 맵과 병합할 때 맵포인트를 사용하기 위해 텍스트 split 가능 여부 확인 및 변경
                                @Override
                                public void run() {

                                    text.setText("");

                                    final String save_data = input_data.toString();
                                    String[] split = split = save_data.split("\n");

                                    for(int i=0;i<split.length;i++){
                                        String[] splitresult = split[i].split(",");
                                        for(int j=0; j<splitresult.length;j++) {
                                            System.out.println("splits" + j + " = " + splitresult[j]);
                                            text.append(splitresult[j]);
                                        }
                                        text.append("\n");
                                    }

                                }
                            });

                        }

                        //data= getXmlData(num); // 하단의 getData 메소드를 통해 데이터를 파싱


                        System.out.println("data"+data);

                    }

                }).start();

                break;
        }
    }

    String getXmlData_num(){
        StringBuffer buffer_num = new StringBuffer();

        String str =  edit.getText().toString();

        String queryUrl="http://openapi.seoul.go.kr:8088/"
                +key
                +"/xml/PublicWiFiPlaceInfo/1/1/"
                +str;

        System.out.println("url : "+queryUrl);



        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
//                    case XmlPullParser.START_DOCUMENT:
//                        //파싱 시작
//                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("PublicWiFiPlaceInfo"))
                            System.out.println("ok"); // 첫번째 검색결과


                        else if(tag.equals("list_total_count")){
                            xpp.next();

                            buffer_num.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            System.out.println("buffer_num"+buffer_num.toString());
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("PublicWiFiPlaceInfo")) buffer_num.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        System.out.println("list_total_count : "+buffer_num.toString());
        System.out.println("list_total_buffer.length"+buffer_num.length());

        if(buffer_num.length() == 0){
            System.out.println("list total Here is if");
            buffer_num.delete(0,buffer_num.length());
            buffer_num.append("0");
            buffer_num.append("\n");
        }

        return buffer_num.toString(); // 파싱 다 종료 후 StringBuffer 문자열 객체 반환

    }

    String getXmlData(String start_num,String num_str){

        StringBuffer buffer = new StringBuffer();

        String str =  edit.getText().toString();
        System.out.println("str :"+str);


        StringBuffer queryUrl_buffer = new StringBuffer();
        queryUrl_buffer.append("http://openapi.seoul.go.kr:8088/");
        queryUrl_buffer.append(key);
        queryUrl_buffer.append("/xml/PublicWiFiPlaceInfo/"+start_num+"/");
        queryUrl_buffer.append(num_str);
        queryUrl_buffer.append("/");
        queryUrl_buffer.append(str);

        queryUrl_buffer.append("\n");//????이거 나오면 화면에 xml 데이터 나온다 왜지왜지????????

        String queryUrl = queryUrl_buffer.toString();

        try {

            System.out.println("url_try : "+queryUrl); //str 안나온다 근데 url 잘 찾아가서 데이터도 가져옴 왜지????

            //현재 queryUrl 에서 str을 제대로 출력하지 못하는 문제 발생
            //근데 url에서 데이터를 잘 가져옴 왜지?
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        //파싱 시작
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("row")) ;// 첫번째 검색결과

                        else if(tag.equals("PLACE_ADDR")){
                            //buffer.append("설치 주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(",");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("INSTL_X")){
                            //buffer.append("X좌표 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(",");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("INSTL_Y")){
                            //buffer.append("Y좌표 :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(",");//줄바꿈 여기가 하나의 row 마지막이라 보기 쉽게 하기 위해 \n 추가
                        }
                        else if(tag.equals("OBJECTID")){
                            //buffer.append("와이파이 이름 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            //buffer.append(",");//줄바꿈 문자 추가
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("row")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        return buffer.toString(); // 파싱 다 종료 후 StringBuffer 문자열 객체 반환

    }
}