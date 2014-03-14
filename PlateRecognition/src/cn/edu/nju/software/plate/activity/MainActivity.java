package cn.edu.nju.software.plate.activity;

import java.util.List;
import java.util.Locale;

import cn.edu.nju.software.plate.R;
import cn.edu.nju.software.plate.asset.Messages;
import cn.edu.nju.software.plate.asset.ProgressDialogFactory;
import cn.edu.nju.software.plate.net.PlateRecognitionThread;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements Callback {

	SectionsPagerAdapter mSectionsPagerAdapter;
	static ImageView photo, searchPhoto;
	static View mPickPhotoButton, mButtonLayout;
	static TextView tvResult;
	String img_path;
	Handler handler;
	Dialog dialog;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		handler = new Handler(this);
		dialog = ProgressDialogFactory
				.create_progress_dialog(this, "正在努力加载。。。");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onPickPhotoClick(View view) {
		startActivityForResult(new Intent(this, SelectPicPopupWindow.class), 1);
	}

	public void onConfirmClick(View view) {
		mButtonLayout.setVisibility(View.GONE);
		mPickPhotoButton.setVisibility(View.VISIBLE);
		dialog.show();
		new PlateRecognitionThread(img_path, handler).start();
	}

	public void onCancelClick(View view) {
		mButtonLayout.setVisibility(View.GONE);
		mPickPhotoButton.setVisibility(View.VISIBLE);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			int position = getArguments().getInt(ARG_SECTION_NUMBER);
			if (position == 1) {
				rootView = inflater.inflate(R.layout.fragment_search,
						container, false);
				photo = (ImageView) rootView.findViewById(R.id.picked_photo);
				mPickPhotoButton = rootView.findViewById(R.id.btn_pick_photo);
				mButtonLayout = rootView.findViewById(R.id.layout_button);
			} else {
				rootView = inflater.inflate(R.layout.fragment_result,
						container, false);
				searchPhoto = (ImageView) rootView
						.findViewById(R.id.searched_photo);
				tvResult = (TextView) rootView.findViewById(R.id.tv_result);

			}
			return rootView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 1:
			if (data != null) {

				Uri mImageCaptureUri = data.getData();

				if (mImageCaptureUri != null) {
					Bitmap image;
					try {

						image = MediaStore.Images.Media.getBitmap(
								this.getContentResolver(), mImageCaptureUri);
						String[] proj = { MediaStore.Images.Media.DATA };
						Cursor actualimagecursor = managedQuery(
								mImageCaptureUri, proj, null, null, null);
						int actual_image_column_index = actualimagecursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						actualimagecursor.moveToFirst();
						img_path = actualimagecursor
								.getString(actual_image_column_index);
						if (image != null) {
							photo.setImageBitmap(image);
							searchPhoto.setImageBitmap(image);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null) {

						Bitmap image = extras.getParcelable("data");
						if (image != null) {
							photo.setImageBitmap(image);
							searchPhoto.setImageBitmap(image);
						}
					}
				}
				mPickPhotoButton.setVisibility(View.GONE);
				mButtonLayout.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;

		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case Messages.MSG_NET_CONNECTION_ERROR:
			Toast.makeText(this, msg.obj.toString(), 1000).show();
			break;
		case Messages.MSG_JSON_ERROR:
			Toast.makeText(this, msg.obj.toString(), 1000).show();
			break;
		case Messages.MSG_PHOTO_OK:
			tvResult.setText(msg.obj.toString());
			mViewPager.setCurrentItem(1);
			break;
		case Messages.MSG_GET_IMAGE_WITH_VIEW:
			if (msg.obj != null) {
				List<Object> objs = (List<Object>) msg.obj;
				Bitmap bitmap = (Bitmap) objs.get(0);
				ImageView iv = (ImageView) objs.get(1);
				iv.setImageBitmap(bitmap);
			}
			break;
		}
		if (dialog.isShowing())
			dialog.dismiss();
		return false;
	}
}
