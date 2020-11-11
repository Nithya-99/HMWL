package com.example.hmwl;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.hmwl.DBqueries.categoryModelList;
import static com.example.hmwl.DBqueries.firebaseFirestore;
import static com.example.hmwl.DBqueries.lists;
import static com.example.hmwl.DBqueries.loadCategories;
import static com.example.hmwl.DBqueries.loadFragmentData;
import static com.example.hmwl.DBqueries.loadedCategoriesName;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
//    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter adapter;
//    private List<CategoryModel> categoryModelList;
//    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);
//        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);



        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        if(categoryModelList.size() == 0){
            loadCategories(categoryAdapter,getContext());
        }else {
            categoryAdapter.notifyDataSetChanged();
        }

        //List<GridProductModel> gridProductModelList = new ArrayList<GridProductModel>();

        homePageRecyclerView = view.findViewById(R.id.hom_page_rv);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);

        if(lists.size() == 0){
            loadedCategoriesName.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            adapter = new HomePageAdapter(lists.get(0));
            loadFragmentData(adapter,getContext(),0, "HOME");
        }else {
            adapter = new HomePageAdapter(lists.get(0));
            adapter.notifyDataSetChanged();
        }

        homePageRecyclerView.setAdapter(adapter);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                categoryModelList.clear();
//                lists.clear();
//                loadedCategoriesName.clear();
//
//                loadCategories(categoryAdapter,getContext());
//
//                loadedCategoriesName.add("HOME");
//                lists.add(new ArrayList<HomePageModel>());
//                loadFragmentData(adapter,getContext(),0, "HOME");
//            }
//        });

        return view;
    }

}