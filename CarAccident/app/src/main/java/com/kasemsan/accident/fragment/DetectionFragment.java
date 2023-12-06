package com.kasemsan.accident.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kasemsan.accident.databinding.FragmentDetectionBinding;
import com.kasemsan.accident.entity.DataEntity;
import com.kasemsan.accident.entity.DataInterface;
import com.kasemsan.accident.entity.DataRoomDatabase;
import com.kasemsan.accident.ml.CarModelKk;
import com.otaliastudios.cameraview.CameraView;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DetectionFragment extends Fragment {
    private FragmentDetectionBinding binding;
    private CameraView cameraView;
    private CarModelKk model;

    private DataRoomDatabase database;
    DataInterface dataInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        cameraView = binding.cameraView;

        database = DataRoomDatabase.getDatabase(getContext());
        dataInterface = database.dataInter();

        MLLoading();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (model != null) {
            model.close();
        }
    }

    private void MLLoading() {
        try {

            int[] colors = {Color.GREEN, Color.BLUE, Color.RED};
            model = CarModelKk.newInstance(requireContext());

            if (cameraView != null && getViewLifecycleOwner() != null) {
                cameraView.setLifecycleOwner(getViewLifecycleOwner());
                cameraView.addFrameProcessor(frame -> {

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    YuvImage yuvImage = new YuvImage(frame.getData(), ImageFormat.NV21, frame.getSize().getWidth(), frame.getSize().getHeight(), null);
                    yuvImage.compressToJpeg(new Rect(0, 0, frame.getSize().getWidth(), frame.getSize().getHeight()), 90, out);
                    byte[] imageBytes = out.toByteArray();
                    Bitmap originalBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    if (originalBitmap != null) {
                        int targetWidth = 255; // from model
                        int targetHeight = 255; // from model

                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);

                        TensorImage image = TensorImage.fromBitmap(resizedBitmap);
                        CarModelKk.Outputs outputs = model.process(image);

                        List<Category> probability = outputs.getProbabilityAsCategoryList();
                        Collections.sort(probability, (category1, category2) ->  Float.compare(category2.getScore(), category1.getScore()));
                        if (!probability.isEmpty()) {
                            Category highestProbabilityCategory = probability.get(0);

                            String categoryName = highestProbabilityCategory.getLabel();
                            float probabilityScore = highestProbabilityCategory.getScore();
                            DataEntity newData = new DataEntity();
                            newData.categoryName = categoryName;
                            newData.probabilityScore = 0.75;
                            newData.dateEvent = System.currentTimeMillis();
                            dataInterface.insert(newData);

                            requireActivity().runOnUiThread(() -> {
                                binding.txtLabel.setText(categoryName.toUpperCase());
                                binding.txtScore.setText(String.format("%.2f%%", probabilityScore * 100));

                                if (categoryName.equals("No accident")) {
                                    binding.idCard.setBackgroundColor(colors[0]);
                                } else if (categoryName.equals("Minor")) {
                                    binding.idCard.setBackgroundColor(colors[1]);
                                } else if (categoryName.equals("Totaled")) {
                                    binding.idCard.setBackgroundColor(colors[2]);
                                }
                            });
                        }

                    } else {
                        Log.e("MLData", "Failed to convert frame to Bitmap");
                    }
                });
                cameraView.open();
            } else {
                Log.e("MLData", "CameraView or LifecycleOwner is null");
            }

        } catch (IOException e) {
            Log.e("MLData", "Error creating CarModelKk model: " + e.getMessage());
        }
    }
}
