package com.quexten.ulricianumplanner.ui;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.quexten.ulricianumplanner.R;
import com.quexten.ulricianumplanner.substitutions.SubstitutionHandler;
import com.quexten.ulricianumplanner.substitutions.Substitutions;
import com.quexten.ulricianumplanner.sync.iserv.TableEntry;
import com.quexten.ulricianumplanner.courseplan.Course;
import com.quexten.ulricianumplanner.courseplan.CoursePlan;
import com.quexten.ulricianumplanner.courseplan.Hour;

/**
 * Created by Quexten on 16-Dec-16.
 */

public class TimetableWidgetProvider extends AppWidgetProvider {

    private int[] LAYOUT_IDS = {R.layout.timetable_widget_1, R.layout.timetable_widget_2, R.layout.timetable_widget_3, R.layout.timetable_widget_4, R.layout.timetable_widget_5};
    private int[] ROOMVIEW_IDS_LEFT = {R.id.roomView_widget_1_left, R.id.roomView_widget_2_left, R.id.roomView_widget_3_left, R.id.roomView_widget_4_left, R.id.roomView_widget_5_left};
    private int[] ROOMVIEW_IDS_RIGHT = {R.id.roomView_widget_1_right, R.id.roomView_widget_2_right, R.id.roomView_widget_3_right, R.id.roomView_widget_4_right, R.id.roomView_widget_5_right};
    private int[] SUBJECTVIEW_IDS_LEFT = {R.id.subjectView_widget_1_left, R.id.subjectView_widget_2_left, R.id.subjectView_widget_3_left, R.id.subjectView_widget_4_left, R.id.subjectView_widget_5_left};
    private int[] SUBJECTVIEW_IDS_RIGHT = {R.id.subjectView_widget_1_right, R.id.subjectView_widget_2_right, R.id.subjectView_widget_3_right, R.id.subjectView_widget_4_right, R.id.subjectView_widget_5_right};

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            Bundle options = appWidgetManager.getAppWidgetOptions(widgetId);

            int minHeight = options
                    .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            RemoteViews remoteViews = getRemoteViews(context, minHeight);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

        int minHeight = options
                .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

        appWidgetManager.updateAppWidget(appWidgetId,
                getRemoteViews(context, minHeight));

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
                newOptions);
    }

    private RemoteViews getRemoteViews(Context context, int minHeight) {
        int rows = getCellsForSize(minHeight);

        CoursePlan coursePlan = new CoursePlan(context, null);
        coursePlan.read();
        coursePlan.readClassName();
        Substitutions substitutions = new SubstitutionHandler(context).load();

        RemoteViews views = new RemoteViews(context.getPackageName(),
                LAYOUT_IDS[rows - 1]);
        views.setInt(SUBJECTVIEW_IDS_LEFT[rows - 1], "setBackgroundColor",
                Color.parseColor("#e0e0e0"));

        for(int i = 0; i < rows; i++) {
            views.setInt(SUBJECTVIEW_IDS_LEFT[i], "setBackgroundColor",
                    Color.parseColor("#e0e0e0"));
            views.setInt(SUBJECTVIEW_IDS_RIGHT[i], "setBackgroundColor",
                    Color.parseColor("#e0e0e0"));

            /*String todayRoom = coursePlan.getCourse(substitutions.getTodayDay(), Hour.fromInt(i)).getCurrentRoom();
            String todaySubject = coursePlan.getCourse(substitutions.getTodayDay(), Hour.fromInt(i)).getCurrentSubject();
            String tomorrowRoom = coursePlan.getCourse(substitutions.getTomorrowDay(), Hour.fromInt(i)).getCurrentRoom();
            String tomorrowSubject = coursePlan.getCourse(substitutions.getTomorrowDay(), Hour.fromInt(i)).getCurrentSubject();

            todayRoom = todayRoom.isEmpty() ? " " : todayRoom;
            todaySubject = Course.getLongSubjectName(context, todaySubject.isEmpty() ? " " : todaySubject);
            tomorrowRoom = tomorrowRoom.isEmpty() ? " " : tomorrowRoom;
            tomorrowSubject = Course.getLongSubjectName(context, tomorrowSubject.isEmpty() ? " " : tomorrowSubject);

            for(TableEntry entry : substitutions.getTodaySubstitutions()) {
                if(Hour.fromString(entry.getTime()).equals(Hour.fromInt(i))) {
                    todaySubject = Course.getLongSubjectName(context, entry.getSubstituteSubject());
                    todayRoom = entry.getRoom();
                    views.setInt(SUBJECTVIEW_IDS_LEFT[i], "setBackgroundColor",
                            TimetableManager.getColorForSubstitution(context, entry.getType()));
                }
            }
            for(TableEntry entry : substitutions.getTomorrowSubstitutions()) {
                if(Hour.fromString(entry.getTime()).equals(Hour.fromInt(i))) {
                    tomorrowSubject = Course.getLongSubjectName(context, entry.getSubstituteSubject());
                    tomorrowRoom = entry.getRoom();
                    views.setInt(SUBJECTVIEW_IDS_RIGHT[i], "setBackgroundColor",
                            TimetableManager.getColorForSubstitution(context, entry.getType()));
                }
            }

            views.setTextViewText(ROOMVIEW_IDS_LEFT[i], todayRoom);
            views.setTextViewText(SUBJECTVIEW_IDS_LEFT[i], todaySubject);
            views.setTextViewText(ROOMVIEW_IDS_RIGHT[i], tomorrowRoom);
            views.setTextViewText(SUBJECTVIEW_IDS_RIGHT[i], tomorrowSubject);*/
        }

        return views;
    }

    private static int getCellsForSize(int size) {
        int n = 2;
        while (60 * n - 30 < size) {
            ++n;
        }
        int num = n - 1;
        return num <= 4 ? num : 4;
    }

}