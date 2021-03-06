package com.quexten.ulricianumplanner;

import android.content.Context;
import android.os.AsyncTask;

import com.quexten.ulricianumplanner.courseplan.Course;
import com.quexten.ulricianumplanner.courseplan.CoursePlan;
import com.quexten.ulricianumplanner.courseplan.Day;
import com.quexten.ulricianumplanner.courseplan.Hour;
import com.quexten.ulricianumplanner.courseplan.TeacherManager;
import com.quexten.ulricianumplanner.notifications.NotificationPoster;
import com.quexten.ulricianumplanner.substitutions.SubstitutionHandler;
import com.quexten.ulricianumplanner.substitutions.Substitutions;
import com.quexten.ulricianumplanner.sync.iserv.TableEntry;

import java.util.Calendar;

/**
 * Created by Quexten on 01-Sep-16.
 */

class NextRoomTask extends AsyncTask<String, Boolean, Boolean> {

    private CoursePlan coursePlan;
    private NotificationPoster notificationPoster;
    private Substitutions substitutions;

    public NextRoomTask(Context context) {
        coursePlan = new CoursePlan(context, null);
        substitutions = new SubstitutionHandler(context).load();
        TeacherManager teacherManager = new TeacherManager(context);
        notificationPoster = new NotificationPoster(context, teacherManager);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        coursePlan.readClassName();
        coursePlan.read();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int currentDayIndex = Day.fromCalendarDay(day);

        Course nextCourse = coursePlan.getCourse(Day.fromInt(currentDayIndex), getNextHour());

        Course notificationCourse = new Course("Subject", "Room", "Teacher");
        notificationCourse.setSubject(nextCourse.getSubject());
        notificationCourse.setTeacher(nextCourse.getTeacher());
        notificationCourse.setRoom(nextCourse.getRoom());
        notificationCourse.setSubjectB(nextCourse.getSubjectB());
        notificationCourse.setTeacherB(nextCourse.getTeacherB());
        notificationCourse.setRoomB(nextCourse.getRoomB());

        if(notificationCourse.getTeacher().isEmpty())
            return true;

        TableEntry[] currentSubstitutions = new TableEntry[0];

        for(TableEntry entry : currentSubstitutions) {
            if(Hour.fromString(entry.getTime()).equals(getNextHour())) {
                if(entry.getType().equals("Entfall"))
                    return true;
                notificationCourse.setSubject(entry.getSubstituteSubject());
                notificationCourse.setTeacher(entry.getSubstituteTeacher());
                notificationCourse.setRoom(entry.getRoom());
                notificationCourse.setSubjectB(entry.getSubstituteSubject());
                notificationCourse.setTeacherB(entry.getSubstituteTeacher());
                notificationCourse.setRoomB(entry.getRoom());
            }
        }

        notificationPoster.postNextRoomNotification(notificationCourse);
        return true;
    }

    private static Hour getNextHour() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(hour < 8)
            return Hour.ONETWO;
        else if (hour < 10)
            return Hour.THREFOUR;
        else if (hour < 12)
            return Hour.FIVESIX;
        else if (hour < 15)
            return Hour.EIGHTNINE;
        else
            return Hour.TENELEVEN;
    }

}
