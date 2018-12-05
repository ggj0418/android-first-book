package com.example.ggj04.sejongtalk.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ggj04.sejongtalk.R;

import java.util.ArrayList;

public class DepartmentFragment extends Fragment {

    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        spinner = (Spinner)view.findViewById(R.id.spinner);
        final WebView webView = (WebView)view.findViewById(R.id.htmlWebView);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String webViewString = "file:///android_asset/" + position + ".html";
                webView.loadUrl(webViewString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayList<String> departmentList = new ArrayList<>();
        departmentList.add("건설환경공학과");
        departmentList.add("건축공학부");
        departmentList.add("건축공학부 건축공학전공");
        departmentList.add("건축공학부 건축학전공");
        departmentList.add("경영학부");
        departmentList.add("경영학부 경영학 전공");
        departmentList.add("경제통상학과");
        departmentList.add("교육학과");
        departmentList.add("국방시스템공학과");
        departmentList.add("국어국문학과");
        departmentList.add("국제학부");
        departmentList.add("국제학부 영어영문학전공");
        departmentList.add("국제학부 일어일문학전공");
        departmentList.add("국제학부 중국통상학전공");
        departmentList.add("글로벌조리학과");
        departmentList.add("기계항공우주공학부");
        departmentList.add("기계항공우주공학부 기계공학전공");
        departmentList.add("기계항공우주공학부 항공우주공학전공");
        departmentList.add("나노신소재공학과");
        departmentList.add("대양휴머니티칼리지");
        departmentList.add("데이터사이언스학과");
        departmentList.add("디지털콘텐츠학과");
        departmentList.add("만화애니메이션학과");
        departmentList.add("무용과");
        departmentList.add("물리천문학과");
        departmentList.add("미디어커뮤니케이션학과");
        departmentList.add("법학부");
        departmentList.add("법학부 법학전공");
        departmentList.add("산업디자인학과");
        departmentList.add("생명시스템학부");
        departmentList.add("생명시스템학부 바이오산업자원공학전공");
        departmentList.add("생명시스템학부 바이오융합공학전공");
        departmentList.add("생명시스템학부 식품공학전공");
        departmentList.add("생명시스템학부 식품생명공학전공");
        departmentList.add("소프트웨어학과");
        departmentList.add("수학통계학부");
        departmentList.add("수학통계학부 수학전공");
        departmentList.add("수학통계학부 응용통계학전공");
        departmentList.add("에너지자원공학과");
        departmentList.add("역사학과");
        departmentList.add("영화예술학과");
        departmentList.add("원자력공학과");
        departmentList.add("융합창업전공");
        departmentList.add("음악과");
        departmentList.add("전자정보통신공학과");
        departmentList.add("정보보호학과");
        departmentList.add("지능기전공학부");
        departmentList.add("지능기전공학부 무인이동체공학전공");
        departmentList.add("지능기전공학부 스마트기기공학전공");
        departmentList.add("창의소프트학부");
        departmentList.add("창의소프트학부 디자인이노베이션전공");
        departmentList.add("창의소프트학부 만화애니메이션텍전공");
        departmentList.add("체육학과");
        departmentList.add("컴퓨터공학과");
        departmentList.add("패션디자인학과");
        departmentList.add("항공시스템공학과");
        departmentList.add("행정학과");
        departmentList.add("향장뷰티산업학과");
        departmentList.add("호텔관광외식경영학부");
        departmentList.add("호텔관광외식경영학부 외식경영학전공");
        departmentList.add("호텔관광외식경영학부 호텔관광경영학전공");
        departmentList.add("호텔외식관광프랜차이즈경영학과");
        departmentList.add("호텔외식비즈니스학과");
        departmentList.add("화학과");
        departmentList.add("환경에너지공간융합학과");
        departmentList.add("회화과");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item, departmentList);
        spinner.setAdapter(spinnerAdapter);

        return view;
    }
}
