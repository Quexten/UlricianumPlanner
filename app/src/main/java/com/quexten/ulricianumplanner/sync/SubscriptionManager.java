package com.quexten.ulricianumplanner.sync;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;
import com.quexten.ulricianumplanner.courseplan.Course;
import com.quexten.ulricianumplanner.courseplan.CoursePlan;
import com.quexten.ulricianumplanner.courseplan.Day;
import com.quexten.ulricianumplanner.courseplan.Hour;

/**
 * Created by Quexten on 23-Dec-16.
 */

public class SubscriptionManager {

    private Context context;

    public SubscriptionManager(Context context) {
        this.context = context;
    }

    public void setCourse(Course oldCourse, Course course, Day day, Hour hour) {
        unsubscribe(oldCourse, day, hour);
        subscribe(course, day, hour);
    }

    private void subscribe(Course course, Day day, Hour hour) {
        if((course != null) && !course.getTeacher().isEmpty()) {
            FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(getShortDayName(day), getShortTimeName(hour), course.getTeacher(), course.getSubject()));

            if(course.hasDoubleWeekSchedule())
                FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(getShortDayName(day), getShortTimeName(hour), course.getTeacherB(), course.getSubjectB()));
        }
    }

    private void unsubscribe(Course course, Day day, Hour hour) {
        if(course != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(getShortDayName(day), getShortTimeName(hour), course.getTeacher(), course.getSubject()));

            if (course.hasDoubleWeekSchedule())
                FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(getShortDayName(day), getShortTimeName(hour), course.getTeacherB(), course.getSubjectB()));
        }
    }

    private String getTopicName(String day, String time, String teacher, String subject) {
        return ("substitution-updates-"+day + "-" + time + "-" + teacher + "-" + subject)
                .toLowerCase()
                .replace("ö", "o_")
                .replace("ä", "a_")
                .replace("ü", "u_");
    }

    private String getShortDayName(Day day) {
        switch(day) {
            case MON:
                return "mon";
            case TUE:
                return "tue";
            case WED:
                return "wed";
            case THU:
                return "thu";
            case FRI:
                return "fri";
            default:
                return "mon";
        }
    }

    private String getShortTimeName(Hour hour) {
        switch(hour) {
            case ONETWO:
                return "12";
            case THREFOUR:
                return "34";
            case FIVESIX:
                return "56";
            case EIGHTNINE:
                return "89";
            case TENELEVEN:
                return "1011";
            default:
                return "12";
        }
    }

    public void subscribeToPlan(CoursePlan plan) {
        Course[][] courses = plan.getCourses();
        for (int dayId = 0; dayId < courses.length; dayId++) {
            for (int hourId = 0; hourId < courses[0].length; hourId++) {
                subscribe(courses[dayId][hourId], Day.fromInt(dayId), Hour.fromInt(hourId));
            }
        }
    }

}
