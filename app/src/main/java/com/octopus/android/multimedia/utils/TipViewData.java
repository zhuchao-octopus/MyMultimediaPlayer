//package com.octopus.android.multimedia.utils;
//
//import android.annotation.SuppressLint;
//import android.app.Application;
//import android.graphics.Color;
//import android.graphics.PixelFormat;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//
//import com.car.core.kit.AppKit;
//import com.car.core.kit.HandlerKit;
//import com.car.core.kit.INotify;
//import com.car.core.kit.NotifyList;
//import com.octopus.android.multimedia.R;
//
//import java.util.Objects;
//
//@SuppressLint("DefaultLocale")
//public class TipViewData {
//
//	private static TextView sTipView;
//	private static boolean sIsShow;
//	private static Runnable sShowViewAction;
//
//	private static Application mApplication;
//
//	public static void init(Application application) {
//		mApplication = application;
//		setupPhoneWorkTipView();
//		addNotify();
//		fixTipView();
//	}
//
//	private static void addNotify() {
//		String []notifyIds = {AppData.ID_IS_TOP, BtData.ID_PHONE_STATE, BtData.ID_TALK_TIME};
//		NotifyList.add(notifyIds, new INotify() {
//			@Override
//			public void onNotify(String id, Object data) {
//				if (Objects.equals(id, AppData.ID_IS_TOP)) {
//					doFixTipView();
//				} else {
//					fixTipView();
//				}
//			}
//		});
//	}
//
//	private static WindowManager.LayoutParams buildParams(int x, int y, int width, int height) {
//		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//		params.format = PixelFormat.TRANSLUCENT;// 支持透明
//        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        params.gravity = Gravity.LEFT | Gravity.TOP;
//        params.width = width;
//        params.height = height;
//        params.x = x;
//        params.y = y;
//        return params;
//	}
//
//	private static void setupPhoneWorkTipView() {
//		WindowManager.LayoutParams params = buildParams(0, 0, AppKit.getRealWidth(), AppKit.getStatusBarHeight());
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        sTipView = new TextView(mApplication);
//        sTipView.setGravity(Gravity.CENTER);
//        sTipView.setBackgroundResource(R.drawable.bg_title);
//        sTipView.setLayoutParams(params);
//        sTipView.setTextSize(32);
//        sTipView.setTextColor(Color.WHITE);
//        sTipView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				MainActivity.bringToTop();
//			}
//		});
//	}
//
//	private static void fixTipView() {	// note. 主要修正闪提示view的问题
//		boolean isShow = BtData.isPhoneWork() && !AppData.isTop();
//		if (isShow) {
//			startShowViewAction();
//		} else {
//			cancelShowViewAction();
//			doFixTipView();
//		}
//	}
//
//	private static void startShowViewAction() {
//		if (sShowViewAction == null) {
//			HandlerKit.getUIHandler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					sShowViewAction = null;
//					doFixTipView();
//				}
//			}, 1000);
//		}
//	}
//
//	private static void cancelShowViewAction() {
//		if (sShowViewAction != null) {
//			HandlerKit.getUIHandler().removeCallbacks(sShowViewAction);
//			sShowViewAction = null;
//		}
//	}
//
//	private static void doFixTipView() {
//		boolean isShow = BtData.isPhoneWork() && !AppData.isTop();
//		showView(isShow);
//		if (isShow) {
//			fixTipViewText();
//		}
//	}
//
//	private static void fixTipViewText() {
//		if (BtData.isTalkState()) {
//			sTipView.setText(mApplication.getResources().getString(R.string.bt_str1)+String.format("  %s", formatTime(BtData.getTalkTime())));
//		} else {
//			sTipView.setText(mApplication.getResources().getString(R.string.bt_str1));
//		}
//	}
//
//	private static String formatTime(int time) {
//		time /= 1000;
//		return String.format("%02d:%02d", time/60, time%60);
//	}
//
//	private static void showView(boolean isShow) {
//		if (sIsShow != isShow) {
//			sIsShow = isShow;
//			if (isShow) {
//				AppKit.getWindowManager().addView(sTipView, sTipView.getLayoutParams());
//			} else {
//				AppKit.getWindowManager().removeView(sTipView);
//			}
//		}
//	}
//
//}
