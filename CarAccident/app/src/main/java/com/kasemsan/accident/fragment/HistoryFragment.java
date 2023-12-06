package com.kasemsan.accident.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kasemsan.accident.adapter.HistoryAdapter;
import com.kasemsan.accident.databinding.FragmentHistoryBinding;
import com.kasemsan.accident.entity.DataEntity;
import com.kasemsan.accident.entity.DataInterface;
import com.kasemsan.accident.entity.DataRoomDatabase;

import java.util.List;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private DataRoomDatabase database;
    DataInterface dataInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        database = DataRoomDatabase.getDatabase(getContext());
        dataInterface = database.dataInter();

        new GetDataAsyncTask().execute();

        return view;
    };

    private class GetDataAsyncTask extends AsyncTask<Void, Void, List<DataEntity>> {
        @Override
        protected List<DataEntity> doInBackground(Void... voids) {
            dataInterface.keepLatest100Rows();
            return dataInterface.getAllData();
        }
        @Override
        protected void onPostExecute(List<DataEntity> allData) {
            super.onPostExecute(allData);
            RecyclerView recyclerView = binding.recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            HistoryAdapter historyAdapter = new HistoryAdapter(allData);
            recyclerView.setAdapter(historyAdapter);
        }
    }
}