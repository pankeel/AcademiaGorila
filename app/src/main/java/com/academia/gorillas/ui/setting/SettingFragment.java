package com.academia.gorillas.ui.setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academia.gorillas.R;
import com.academia.gorillas.adapter.CategoryAdapter;
import com.academia.gorillas.model.Category;

import java.util.List;

public class SettingFragment extends Fragment {


    private List<Category> mCategoryList;
    private CategoryAdapter categoryAdapter;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
//
//
//        RecyclerView mRecyclerView = root.findViewById(R.id.recyclerview);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mCategoryList = new ArrayList<>();
//        categoryAdapter = new CategoryAdapter(getContext(), mCategoryList);
//        mRecyclerView.setAdapter(categoryAdapter);
//
//        // ROT-BLAU. TV
//        Category category = new Category("TVVapp", "logo", "Mit derzeit über 1300 Mitgliedern ist der TV Vohburg der größte Verein im Stadtgebiet Vohburg und einer der größten Sportvereine im Landkreis Pfaffenhofen. Besonderen Wert legt der Verein auf seine Jugendarbeit, die sich an den 535 Kindern und Jugendlichen Mitgliedern unter 18 Jahren aufzeigen lässt.");
//
//
//        mCategoryList.add(category);
//
//        // fussball
//        category = new Category("Kalenderservice", "setting_calender", "Viele Nutzer entscheiden sich für die digitale Variante der Terminplanung. Mit dem Kalender des TV Vohburg lassen sich Termine synchronisieren.");
//        category.getLinks().add(new Link("Karate","https://calendar.google.com/calendar/ical/s9t8jlla4q0l60cjuo218p55e8%40group.calendar.google.com/public/basic.ics"));
//        category.getLinks().add(new Link("Tischtennis","https://calendar.google.com/calendar/ical/qlb5en5je8dh4qak1caoeus6cc%40group.calendar.google.com/public/basic.ics"));
//
//        mCategoryList.add(category);
//
//        // Tischtennis
//        category = new Category("Bayrischer Fussball-Verband", "category_bfv_logo", "Alle künftigen Spiele der angezeigten Mannschaft bequem in den eigenen Kalender");
//        category.getLinks().add(new Link("Fußball Training","https://calendar.google.com/calendar/ical/1fo02kkbismmtaidi9dk424jps%40group.calendar.google.com/public/basic.ics"));
//        category.getLinks().add(new Link("Herren","https://service-prod.bfv.de/rest/icsexport/Spielplan?staffel=027FDS20AK000002VS5489B3VUOI54T7-G&id=00ES8GNHPK000004VV0AG08LVUPGND5I"));
//
//        mCategoryList.add(category);
//
//        categoryAdapter.notifyDataSetChanged();

        return root;
    }
}