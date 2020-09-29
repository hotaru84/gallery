/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.gridtopager.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.app.SharedElementCallback;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.samples.gridtopager.MainActivity;
import com.google.samples.gridtopager.adapter.ImagePagerAdapter;
import com.google.samples.gridtopager.R;
import com.google.samples.gridtopager.adapter.ZoomOutPageTransformer;

import java.util.List;
import java.util.Map;

/**
 * A fragment for displaying a pager of images.
 */
public class ImagePagerFragment extends Fragment {

  private ViewPager2 viewPager;
  private ImagePagerAdapter imagePagerAdapter;
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewPager = (ViewPager2) inflater.inflate(R.layout.fragment_pager, container, false);
    imagePagerAdapter = new ImagePagerAdapter(this);
    viewPager.setAdapter(imagePagerAdapter);
    viewPager.setOffscreenPageLimit(3);
    viewPager.setPageTransformer(new ZoomOutPageTransformer());
    // Set the current position and add a listener that will update the selection coordinator when
    // paging the images.
    viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);
        MainActivity.currentPosition = position;
      }
    });
    viewPager.setCurrentItem(MainActivity.currentPosition,false);

    prepareSharedElementTransition();

    // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
    if (savedInstanceState == null) {
      postponeEnterTransition();
    }

    return viewPager;
  }

  /**
   * Prepares the shared element transition from and back to the grid fragment.
   */
  private void prepareSharedElementTransition() {
    Transition transition =
        TransitionInflater.from(getContext())
            .inflateTransition(R.transition.image_shared_element_transition);
    setSharedElementEnterTransition(transition);

    // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
    setEnterSharedElementCallback(
        new SharedElementCallback() {

          @Override
          public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if(imagePagerAdapter.getItemCount() == 0) return;
            Fragment currentFragment = imagePagerAdapter.createFragment(MainActivity.currentPosition);
            View view = currentFragment.getView();
            if(view == null){
              Log.d("@@","view null");
              return;
            }
            View elementView = view.findViewById(R.id.image);
            if(elementView == null) return;
            sharedElements.put(names.get(0), elementView);
          }
        });
  }
}
